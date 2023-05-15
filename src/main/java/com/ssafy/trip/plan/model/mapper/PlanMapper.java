package com.ssafy.trip.plan.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trip.plan.model.dto.PlaceDto;
import com.ssafy.trip.plan.model.dto.PlanDto;

@Mapper
public interface PlanMapper {
	
	/** 여행 계획 추가 */
	public int insertPlan(PlanDto planDto) throws SQLException;

	/** 여행지 추가 */
	public int insertPlace(PlaceDto placeDto) throws SQLException;

	/** 여행 계획 삭제 */
	public void deletePlan(int id) throws SQLException;

	/** 여행 계획 리스트 출력 */
	public List<PlanDto> selectPlan(Map<String, Object> param) throws SQLException;
	
	/** 여행 계획 리스트 출력 */
	public List<PlanDto> selectPlanList() throws SQLException;

	/** 글 번호에 맞는 여행 계획 출력 */
	public PlanDto selectPlanOne(int planId) throws SQLException;

	/** 여행 경로에 맞는 여행지 리스트 출력 */
	public List<PlaceDto> selectPlace(int planId) throws SQLException;
	
	/** 여행 계획 id 가져오기 */
	// String userId, String title
	public int selectPlanId(Map<String, Object> param) throws SQLException;
	
	/** 조회수 증가 */
	public void updateHit(int planId) throws SQLException;
	
	/** 여행 계획 수정 */
	public void updatePlan(PlanDto planDto) throws SQLException;
	
	/** 여행지 수정 */
	public void updatePlace(PlaceDto placeDto) throws SQLException;

	
	public int getTotalArticleCount(Map<String, Object> param) throws SQLException;
	
	public int getTotalPlaceCount(int planId) throws SQLException;
	
}
