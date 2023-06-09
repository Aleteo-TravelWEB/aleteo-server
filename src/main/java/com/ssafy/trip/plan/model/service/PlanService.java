package com.ssafy.trip.plan.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.trip.board.model.dto.BoardParameterDto;
import com.ssafy.trip.plan.model.dto.GoodPlanDto;
import com.ssafy.trip.plan.model.dto.PlaceDto;
import com.ssafy.trip.plan.model.dto.PlanDto;
import com.ssafy.trip.plan.model.dto.PlanJoinGoodDto;
import com.ssafy.trip.util.PageNavigation;

public interface PlanService {

	/** 최단 경로 여행지 리스트 출력 */
	public List<PlaceDto> selectFastDistancePlace(int planId) throws SQLException;

	/** 여행 글 목록 네비게이션 */
	public PageNavigation makePageNavigation(Map<String, String> map) throws Exception;


	// api
	/** 여행 경로 리스트 출력 */
	List<PlanDto> getPlanList(BoardParameterDto boardParameterDto) throws Exception;
	/** 여행 계획 추가 */
	void registPlan(PlanDto planDto) throws Exception;

	/** 여행지 추가 */
	void registPlace(PlaceDto placeDto) throws Exception;

	/** 글 번호에 맞는 여행 계획 출력 */
	PlanDto getPlanOne(int planId) throws Exception;

	/** 여행 경로에 맞는 여행지 리스트 출력 */
	List<PlaceDto> getPlaceList(int planId) throws Exception;

	/** 최단 경로 여행지 리스트 출력 */
	List<PlaceDto> getFastDistancePlace(int planId) throws Exception;

	/** 여행 계획 수정 */
	void modifyPlan(PlanDto planDto) throws Exception;

	/** 여행 경로 삭제 */
	void deletePlan(int id) throws Exception;

	/** 여행지 삭제 */
	void deletePlace(int planId) throws Exception;

	/** 여행 경로에 맞는 여행지 리스트 출력 */
	public List<PlaceDto> getPlace(int planId) throws SQLException;

	/** 여행 경로 id 가져오기 */
	public int getPlanId(Map<String, Object> param) throws SQLException;

	/** 조회수 증가 */
	public void updateHit(int planId) throws SQLException;

	/** 여행 계획 좋아요 수 가져오기 */
	public int getGoodPlanNum(Map<String, Object> map) throws SQLException;

	/** 여행 계획 좋아요 가져오기 */
	public List<PlanJoinGoodDto> getGoodPlan(String userId) throws SQLException;

	/** 여행 계획 좋아요 넣기 */
	public void registGoodPlan(GoodPlanDto goodPlanDto) throws SQLException;

	/** 여행 계획 좋아요 삭제 */
	public void deleteGoodPlan(Map<String, Object> map) throws SQLException;

	/** 제일 많이 저장된 장소 TOP TEN */
	public List<PlaceDto> getTopTenPlace() throws SQLException;
}
