package com.ssafy.trip.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.user.model.dto.UserDto;
import com.ssafy.trip.user.model.service.JwtServiceImpl;
import com.ssafy.trip.user.model.service.UserService;
import com.ssafy.trip.util.TempKey;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
@Api("사용자 컨트롤러 API V1")
public class UserAPIController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserAPIController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	private JwtServiceImpl jwtService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	public JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String from;
	
	@ApiOperation(value = "아이디 저장", response = Map.class)
	@GetMapping("/{userId}")
	public ResponseEntity<String> idCheck(
			@PathVariable("userId") @ApiParam(value = "사용자 아이디") String userId
			) throws Exception {
		int cnt = userService.idCheck(userId);
		return new ResponseEntity<String>(cnt + "", HttpStatus.OK);
	}
	
	@ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환한다.", response = Map.class)
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(
			@RequestBody @ApiParam(value="로그인 시 필요한 회원정보(아이디, 비밀번호).", required=true) UserDto userDto) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			UserDto loginUser = userService.login(userDto);
			if (loginUser != null) {
				String token = jwtService.create("userid", loginUser.getUserId(), "access-token");
				logger.debug("로그인 토큰정보 : {}", token);
				resultMap.put("access-token", token);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} else {
				resultMap.put("message", FAIL);
				status = HttpStatus.ACCEPTED;
			}			
		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String,Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
	@GetMapping(value = "info/{userId}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("userId") @ApiParam(value="인증할 회원의 아이디.", required = true) String userId,
			HttpServletRequest request) {
		logger.debug("userid: {}", userId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (jwtService.isUsable(request.getHeader("access-token"))) {
			logger.info("사용 가능한 토큰");
			try {
				// 로그인 사용자 정보
				UserDto userDto = userService.userInfo(userId);
				resultMap.put("userInfo", userDto);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} catch (Exception e) {
				logger.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			logger.error("사용 불가능 토큰");
			resultMap.put("message", FAIL);
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String,Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원정보 수정", notes = "회원 정보 수정 후 결과 메세지를 반환한다.", response = Map.class)
	@PutMapping("/info/{userId}")
	public ResponseEntity<?> modifyUser(
			@RequestBody @ApiParam(value = "수정할 회원 정보", required = true) UserDto userDto
			) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		
		userService.modifyUser(userDto);
		
		resultMap.put("message", SUCCESS);
		status = HttpStatus.ACCEPTED;
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원가입", notes = "회원 정보 등록 후 결과 메세지를 반환한다.", response = Map.class)
	@PostMapping("/join")
	public ResponseEntity<Map<String, Object>> join(
			@RequestBody @ApiParam(value = "가입할 회원 정보", required = true) UserDto userDto
			) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		
		userService.joinUser(userDto);
		
		resultMap.put("message", SUCCESS);
		status = HttpStatus.ACCEPTED;
		return new ResponseEntity<Map<String,Object>>(resultMap, status);
	}
	
	
	@ApiOperation(value = "비밀번호 찾기", notes = "아이디에 해당되는 이메일로 임시 비밀번호를 발급하고, 결과 메세지를 반환한다.", response = Map.class)
	@PostMapping("/find")
	@Transactional
	public ResponseEntity<Map<String, Object>> sendMail(
			@RequestBody @ApiParam(value = "찾을 아이디와 이메일", required = true) Map<String, String> map
			) throws Exception {
		logger.debug("sendmail parameter : {}", map);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		
		String userId = map.get("search-id");
		String emailId = map.get("search-email-id");
		String emailDomain = map.get("search-email-domain");
		String to = emailId + "@" + emailDomain;
		
		String password = userService.getPassword(userId);
		
		if(password != null) {
			String tempPw = new TempKey().getKey(10, false); // 임시 비밀번호 발급
			Map<String, String> userMap = new HashMap<>();
			userMap.put("id", userId);
			userMap.put("password", tempPw);
			userService.modifyUserPw(userMap);
			
			SimpleMailMessage simpleMessage = new SimpleMailMessage();
			simpleMessage.setFrom(from);
			simpleMessage.setTo(to);
			simpleMessage.setSubject(" [TTT] 비밀번호 발급 ");			
			simpleMessage.setText(" 임시 비밀번호 : " + tempPw);
			javaMailSender.send(simpleMessage);
			
			resultMap.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} else {
			resultMap.put("message", FAIL);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<Map<String,Object>>(resultMap, status);
	}
	
}
