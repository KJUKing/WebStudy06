<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>   
<table class="table table-bordered">
	<thead>
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${not empty boardList }">
			<c:forEach items="${boardList }" var="board">
				<tr>
					<td>${board.rnum }</td>
					<td>
						<a href="<c:url value='/board/${board.boNo }'/>">${board.boTitle }</a>
					</td>
					<td>${board.boWriter }</td>
					<td>${board.boDate }</td>
					<td>${board.boHit }</td>
				</tr>
			</c:forEach>
		</c:if>
		<c:if test="${empty boardList }">
			<tr>
				<td colspan="5">글 없음.</td>
			</tr>
		</c:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="5">
				<div class="paging-area">
					${pagingHTML }
				</div>
				<div class="search-area" data-pg-target="#searchform" data-pg-fn-name="fnPaging">
					<form:select path="condition.searchType">
						<form:option value="" label="전체" />
						<form:option value="title" label="제목" />
						<form:option value="writer" label="작성자" />
					</form:select>
					<form:input path="condition.searchWord"/>
					<button class="search-btn">검색</button>
				</div>
			</td>
		</tr>
	</tfoot>
</table>
<form:form id="searchform" method="get" modelAttribute="condition">
	<form:input path="searchType"/>
	<form:input path="searchWord"/>
	<input type="hidden" name="page" />
</form:form>
<script src="${pageContext.request.contextPath }/resources/js/app/utils/paging.js"></script>