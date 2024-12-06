package kr.or.ddit.board.service;

import java.util.List;

import kr.or.ddit.atch.vo.AtchFileDetailVO;
import kr.or.ddit.board.exception.WriterAuthenticationException;
import kr.or.ddit.board.vo.FreeBoardVO;
import kr.or.ddit.paging.PaginationInfo;

public interface BoardService {
	/**
	 * 신규 글 생성
	 * 
	 * @param board
	 */
	public void createBoard(FreeBoardVO board);

	/**
	 * 특정 글 조회
	 * 
	 * @param boNo
	 */
	public FreeBoardVO readBoard(int boNo);

	/**
	 * 페이징 처리 없는 목록 조회
	 * 
	 * @return
	 */
	public List<FreeBoardVO> readBoardList();

	/**
	 * @param paginationInfo
	 * @return
	 */
	public List<FreeBoardVO> readBoardList(PaginationInfo<FreeBoardVO> paginationInfo);

	/**
	 * 작성자 인증
	 * 
	 * @param boNo
	 * @param rawPassword
	 * @return
	 * @throws WriterAuthenticationException 인증 실패시 던져질 예외
	 */
	public FreeBoardVO writerAuthenticate(int boNo, String rawPassword) throws WriterAuthenticationException;

	/**
	 * 게시글 수정
	 * 
	 * @param board
	 */
	public void modifyBoard(FreeBoardVO board);

	/**
	 * 게시글 삭제
	 * 
	 * @param boNo
	 * @param rawPassword
	 */
	public void removeBoard(int boNo, String rawPassword);

	/**
	 * 파일 다운로드
	 * 
	 * @param atchFileId
	 * @param fileSn
	 * @return
	 */
	public AtchFileDetailVO download(int atchFileId, int fileSn);

	/**
	 * 파일 한건 삭제
	 * 
	 * @param atchFileId
	 * @param fileSn
	 */
	public void removeFile(int atchFileId, int fileSn);
}
