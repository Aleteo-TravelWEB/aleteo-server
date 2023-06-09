package com.ssafy.trip.board.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.trip.board.model.dto.BoardDto;
import com.ssafy.trip.board.model.dto.BoardParameterDto;
import com.ssafy.trip.util.PageNavigation;

public interface BoardService {
	
	List<BoardDto> listBoard(BoardParameterDto boardParameterDto) throws Exception;
	BoardDto viewBoard(int id) throws Exception;
	void updateHit(int id) throws Exception;
	boolean writeBoard(BoardDto boardDto) throws Exception;
	boolean modifyBoard1(BoardDto boardDto) throws Exception;
	boolean modifyBoard2(BoardDto boardDto) throws Exception;
	boolean deleteBoard(int id) throws Exception;
	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
}
