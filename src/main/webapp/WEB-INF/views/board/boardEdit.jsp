<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<form:form id="updateForm" method="post" enctype="multipart/form-data" modelAttribute="targetBoard">
	<table>
		<tr>
			<th>제목</th>
			<td><form:input type="text" path="boTitle" required="required"
					cssClass="form-control" />
				<form:errors path="boTitle" cssClass="text-danger" /></td>
		</tr>
		<tr>
			<th>제목</th>
			<td><form:input type="text" path="boWriter" required="required"
					cssClass="form-control" />
				<form:errors path="boWriter" cssClass="text-danger" /></td>
		</tr>
		<tr>
			<th>기존파일</th>
			<td>
			<c:forEach items="${targetBoard.atchFile.fileDetails }" var="fd" varStatus="vs">
				<span>
					${fd.orignlFileNm }[${fd.fileFancysize }]
					<a data-atch-file-id="${fd.atchFileId }" data-file-sn="${fd.fileSn }" class="btn btn-danger" href="javascript:;">
						삭제						
					</a>
					${not vs.last ? '|' : ''}
				</span>
			</c:forEach>
			</td>
		</tr>
		<tr>
			<th>파일</th>
			<td>
<!-- 				<input type="file" name="atchFile.fileDetails[0].uploadFile"  class="form-control"/> -->
<!-- 				<input type="file" name="atchFile.fileDetails[1].uploadFile"  class="form-control"/> -->
				<input type="file" name="uploadFiles" multiple  class="form-control"/>
			</td>
		</tr>
		<tr>
			<th>아이피</th>
			<td><form:input type="text" path="boIp" cssClass="form-control" readonly="true"/>
				<form:errors path="boIp" cssClass="text-danger" /></td>
		</tr>
		<tr>
			<th>이메일</th>
			<td><form:input type="text" path="boMail"
					cssClass="form-control" />
				<form:errors path="boMail" cssClass="text-danger" />
			</td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td>
				<input type="password" name="boPass" required class="form-control" />
				<form:errors path="boPass" cssClass="text-danger" />
			</td>
		</tr>
		<tr>
			<th>내용</th>
			<td>
				<form:textarea path="boContent" class="tinymce-editor"/>
				<form:errors path="boContent" cssClass="text-danger" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="전송" class="btn btn-primary" /> 
				<input type="button" value="삭제" class="btn btn-danger" id="delBtn"/> 
			</td>
		</tr>
	</table>
</form:form>
<form action="<c:url value='/board/${targetBoard.boNo }' />" method="post" id="deleteForm">
<!-- 	POST 요청에 포함된 hidden "_method" 파라미터로 브라우저가 지원하지 않는 put/delete 등의 요청 메소드를 대신 표현할 수 있음. -->
<!-- 	단, 서버측에서 해당 파라미터로 요청의 메소드를 변경할 수 있는 Filter 등이 필요함.(web.xml 참고) -->
	<input type="hidden" name="_method" required value="delete"/>
	<input type="hidden" name="password" required />
</form>
<script src="${pageContext.request.contextPath }/resources/js/app/board/boardEdit.js"></script>













