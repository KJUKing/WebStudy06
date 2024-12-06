package kr.or.ddit.board.mapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.annotation.RootContextWebConfig;
import kr.or.ddit.board.vo.FreeBoardVO;
import kr.or.ddit.paging.PaginationInfo;
import kr.or.ddit.paging.SimpleCondition;

@RootContextWebConfig
@Transactional
class BoardMapperTest {
	@Autowired
	BoardMapper mapper;

	FreeBoardVO board;

	@BeforeEach
	void beforeEach() {
		board = new FreeBoardVO();
		board.setBoTitle("새글 제목");
		board.setBoWriter("작성자");
		board.setBoPass("java");
		board.setBoIp("192.168.0.1");
		board.setBoMail("aa@gmail.com");
		board.setBoContent("새글의 내용");
		assertEquals(1, mapper.insertBoard(board));
	}

	@Test
	void testSelectBoard() {
		assertNotNull(mapper.selectBoard(board.getBoNo()));
	}

	@Test
	void testIncrementHit() {
		assertEquals(1, mapper.incrementHit(board.getBoNo()));
	}

	@Test
	void testSelectBoardList() {
		PaginationInfo paging = new PaginationInfo();
		paging.setCurrentPage(1);
		SimpleCondition simpleCondition = new SimpleCondition();
		simpleCondition.setSearchWord("은대");
		paging.setSimpleCondition(simpleCondition);
		assertDoesNotThrow(() -> mapper.selectBoardList(paging));
	}

	@Test
	void testSelectBoardCount() {
		PaginationInfo paging = new PaginationInfo();
		paging.setCurrentPage(1);
		SimpleCondition simpleCondition = new SimpleCondition();
		simpleCondition.setSearchWord("은대");
		paging.setSimpleCondition(simpleCondition);
		assertDoesNotThrow(() -> mapper.selectTotalRecord(paging));
	}

	@Test
	void testUpdateBoard() {
		board.setBoNo(board.getBoNo());
		board.setBoTitle("새글 제목2222");
		board.setBoMail("aa@gmail.com");
		board.setBoContent("수정한 글의 내용");
		assertEquals(1, mapper.updateBoard(board));
	}

	@Test
	void testDeleteBoard() {
		assertEquals(1, mapper.deleteBoard(board.getBoNo()));
	}
}
