package kr.or.ddit.paging.renderer;

import kr.or.ddit.paging.PaginationInfo;

public interface PaginationRenderer {
	/**
	 * PaginationInfo 내의 startPage 와 endPage로 페이지 이동 링크 생성
	 * @param paging
	 * @param fnName
	 * @return
	 */
	public String renderPagination(PaginationInfo paging, String fnName);
}
