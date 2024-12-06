package kr.or.ddit.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.board.vo.FreeBoardVO;
import kr.or.ddit.paging.PaginationInfo;

@Mapper
public interface BoardMapper {
	/**
	 * 새글 등록
	 * 
	 * @param board
	 * @return
	 */
	public Integer insertBoard(FreeBoardVO board);

	/**
	 * 특정 글 조회
	 * 
	 * @param boNo 조회할 글 번호
	 * @return
	 */
	public FreeBoardVO selectBoard(int boNo);

	/**
	 * 글 조회수 카운트
	 * 
	 * @param boNo
	 * @return
	 */
	public int incrementHit(int boNo);

	/**
	 * 글 목록 조회
	 * 
	 * @param paginationInfo
	 * @return
	 */
	public List<FreeBoardVO> selectBoardList(PaginationInfo<FreeBoardVO> paging);

	/**
	 * 글 목록 조회
	 * 
	 * @param paginationInfo
	 * @return
	 */
	public List<FreeBoardVO> selectBoardListNonPaging();

	/**
	 * 게시글 목록 수 조회
	 * 
	 * @param paginationInfo
	 * @return
	 */
	public int selectTotalRecord(PaginationInfo<FreeBoardVO> paging);

	/**
	 * 글 수정
	 * 
	 * @param board
	 * @return
	 */
	public int updateBoard(FreeBoardVO board);

	/**
	 * 글 삭제
	 * 
	 * @param boNo
	 * @return
	 */
	public int deleteBoard(int boNo);
}
