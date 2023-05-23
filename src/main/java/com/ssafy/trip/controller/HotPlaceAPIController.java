package com.ssafy.trip.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ssafy.trip.hotplace.model.dto.RegisterHotPlaceRequest;
import com.ssafy.trip.plan.model.dto.RegisterPlanRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.trip.board.model.dto.BoardDto;
import com.ssafy.trip.hotplace.model.dto.HotplaceDto;
import com.ssafy.trip.hotplace.model.service.HotplaceService;
import com.ssafy.trip.user.model.service.JwtServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/hotplace")
@Api("핫플레이스 컨트롤러 API")
@CrossOrigin("*")
public class HotPlaceAPIController {
	
	private static final Logger logger = LoggerFactory.getLogger(HotPlaceAPIController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Value("${file.path}/hotplace/image")
	private String uploadPath;
	
	@Value("${file.imgPath}")
	private String uploadImgPath;
	
	@Autowired
	private HotplaceService hotplaceService;
	
	@Autowired
	private JwtServiceImpl jwtService;

	public HotPlaceAPIController(HotplaceService hotplaceService) {
		super();
		this.hotplaceService = hotplaceService;
	}
	
	@ApiOperation(value = "핫플레이스 목록", notes = "핫플레이스 글 목록을 반환한다.", response = List.class)
	@GetMapping("/list")
	public ResponseEntity<?> hotplaceList() throws Exception {
		logger.debug("hotplaceList call");
		List<HotplaceDto> list = hotplaceService.getHotplaceList();
		logger.debug("hotplace list : {}", list);
		return new ResponseEntity<List<HotplaceDto>>(list, HttpStatus.OK);
	}
	
	@ApiOperation(value = "핫플레이스 글 작성", notes = "사진과 함께 핫플레이스 글 내용을 데이터베이스에 넣는다.")
	@PostMapping
	@Transactional
	public ResponseEntity<?> hotplaceWrite(
			@RequestParam("userId") String userId,
			@RequestParam("title") String title,
			@RequestParam("joinDate") String joinDate,
			@RequestParam("description") String description,
			@RequestParam("tag1") String tag1,
			@RequestParam("tag2") String tag2,
			@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude,
			@RequestParam("mapUrl") String mapUrl,
			@RequestParam("image") MultipartFile file
			) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		HotplaceDto hotplaceDto = new HotplaceDto(userId, title, joinDate, description, tag1, tag2, latitude, longitude, mapUrl);
		logger.debug("post hotplace called: {} , {}, {}", hotplaceDto, file);
		
			
		// FileUpload
		if (!file.isEmpty()) {
			String saveFolder= uploadPath;
			logger.debug("저장 폴더 : {}", saveFolder);
			File folder = new File(saveFolder);

			if (!folder.exists()) {
				folder.mkdirs();
			}
			String originalFileName = file.getOriginalFilename();
			System.out.println(originalFileName);
			if (!originalFileName.isEmpty()) {
				String saveFileName = UUID.randomUUID().toString()
						+ originalFileName.substring(originalFileName.lastIndexOf('.'));
				// 파일 저장하기
				file.transferTo(new File(folder, saveFileName));
				hotplaceDto.setImage(saveFileName);
			}

			hotplaceService.registHotplace(hotplaceDto);
		}
		
		resultMap.put("message", SUCCESS);
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation(value = "핫플레이스 글 보여주기", notes = "id에 맞는 핫플레이스 글 내용을 반환한다.")
	@GetMapping(value = "/{hotplaceId}")
	public ResponseEntity<?> hotplaceView(
			@PathVariable @ApiParam(value = "핫플레이스 id") int hotplaceId
			) throws Exception {
		HotplaceDto hotplaceDto = hotplaceService.getHotplace(hotplaceId);
		return new ResponseEntity<HotplaceDto>(hotplaceDto, HttpStatus.OK);
	}
	
	// 이미지 받은 경우
	@ApiOperation(value = "핫플레이스 글 수정", notes = "작성자란을 제외한 수정되어 있는 핫플레이스 글 내용을 데이터베이스에 넣는다.")
	@PutMapping
	public ResponseEntity<?> hotplaceModify1(
			@RequestParam("userId") String userId,
			@RequestParam("title") String title,
			@RequestParam("num") int num,
			@RequestParam("joinDate") String joinDate,
			@RequestParam("description") String description,
			@RequestParam("tag1") String tag1,
			@RequestParam("tag2") String tag2,
			@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude,
			@RequestParam("mapUrl") String mapUrl,
			@RequestParam("image") MultipartFile file
			) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		HotplaceDto hotplaceDto = new HotplaceDto(userId, title, joinDate, description, tag1, tag2, latitude, longitude, mapUrl);
		logger.debug("post hotplace called: {} , {}", hotplaceDto, file);
		hotplaceDto.setNum(num);
		
		if (file != null && !file.isEmpty()) {
			String saveFolder = uploadPath;
			logger.debug("저장 폴더 : {}", saveFolder);
			File folder = new File(saveFolder);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			String originalFileName = file.getOriginalFilename();
			if (!originalFileName.isEmpty()) {
				String saveFileName = UUID.randomUUID().toString()
						+ originalFileName.substring(originalFileName.lastIndexOf('.'));
				file.transferTo(new File(folder, saveFileName));
				hotplaceDto.setImage(saveFileName);
			}
		}
		else {
			logger.debug("파일 없음");
		}
		hotplaceService.modifyHotplace1(hotplaceDto);
		resultMap.put("message", SUCCESS);
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	// 이미지 받지 않은 경우
	@PutMapping(value = "/{hotplaceId}")
	public ResponseEntity<?> hotplaceModify2(@RequestBody HotplaceDto hotplaceDto) throws Exception{
		logger.debug("modify hotplace : {}", hotplaceDto);
		
		Map<String, Object> resultMap = new HashMap<>();
		
		hotplaceService.modifyHotplace2(hotplaceDto);
		resultMap.put("message", SUCCESS);
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation(value = "핫플레이스 글 삭제", notes = "전달받은 핫플레이스 id로 게시글 삭제")
	@DeleteMapping("/{hotplaceId}")
	public ResponseEntity<?> hotplaceDelete(
			@PathVariable("hotplaceId") @ApiParam(value = "삭제해야할 핫플레이스 id.", required = true) int hotplaceId
			) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		hotplaceService.deleteHotplace(hotplaceId);
		resultMap.put("message", SUCCESS);
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
}
