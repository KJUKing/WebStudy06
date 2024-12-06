<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>	
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>	
<table class="table table-bordered">
	<tr>
		<th>글번호</th>
		<td>${board.boNo}</td>
	</tr>
	<tr>
		<th>제목</th>
		<td>${board.boTitle}</td>
	</tr>
	<tr>
		<th>파일</th>
		<td>
			<c:forEach items="${board.atchFile.fileDetails }" var="fd" varStatus="vs">
				<c:url value='/board/${board.boNo}/atch/${fd.atchFileId }/${fd.fileSn }' var="downUrl"/>
				<a href="${downUrl }">${fd.orignlFileNm }(${fd.fileFancysize })</a>
				${not vs.last ? '|' : ''}
			</c:forEach>
		</td>
	</tr>
	<tr>
		<th>작성자</th>
		<td>${board.boWriter}</td>
	</tr>
	<tr>
		<th>아이피</th>
		<td>${board.boIp}</td>
	</tr>
	<tr>
		<th>이메일</th>
		<td>${board.boMail}</td>
	</tr>
	<tr>
		<th>내용</th>
		<td>${board.boContent}</td>
	</tr>
	<tr>
		<th>작성일</th>
		<td>${board.boDate}</td>
	</tr>
	<tr>
		<th>조회수</th>
		<td>${board.boHit}</td>
	</tr>
</table>
<a href="<c:url value='/board/${boNo}/edit' />" class="btn btn-primary">수정하기</a>










