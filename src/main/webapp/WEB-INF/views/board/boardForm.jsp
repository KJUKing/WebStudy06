<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<form:form method="post" enctype="multipart/form-data" modelAttribute="newBoard">
	<table>
		<tr>
			<th>제목</th>
			<td><form:input type="text" path="boTitle" 
					cssClass="form-control" />
				<form:errors path="boTitle" cssClass="text-danger" /></td>
		</tr>
		<tr>
			<th>작성자</th>
			<td><form:input type="text" path="boWriter" 
					cssClass="form-control" />
				<form:errors path="boWriter" cssClass="text-danger" /></td>
		</tr>
		<tr>
			<th>파일</th>
			<td>
<!-- 				<input type="file" name="atchFile.fileDetails[0].uploadFile"  class="form-control"/> -->
<!-- 				<input type="file" name="atchFile.fileDetails[1].uploadFile"  class="form-control"/> -->
<!-- 				<input type="file" name="atchFile.fileDetails[2].uploadFile"  class="form-control"/> -->	
				<input type="file" name="uploadFiles" multiple class="form-control"/>	
					
			</td>
		</tr>
		<tr>
			<th>아이피</th>
			<security:authentication property="details" var="details" />
			<td><form:input type="text" path="boIp"  value="${details.remoteAddress }"
					cssClass="form-control" readonly="true"/>
				<form:errors path="boIp" cssClass="text-danger" /></td>
		</tr>
		<tr>
			<th>이메일</th>
			<td><form:input type="text" path="boMail"
					cssClass="form-control" />
				<form:errors path="boMail" cssClass="text-danger" /></td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td><input type="password" name="boPass" class="form-control" />
				<form:errors path="boPass" cssClass="text-danger" /></td>
		</tr>
		<tr>
			<th>내용</th>
			<td>
				<form:textarea path="boContent"/>
				<form:errors path="boContent" cssClass="text-danger" /></td>
				<script>
					document.addEventListener("DOMContentLoaded", () => {
						tinymce.init({
							selector: '#boContent',  // change this value according to your HTML
							plugins: 'image',
							toolbar: 'image',
							images_upload_url: '${pageContext.request.contextPath}/board/upload'
						});
					});
				</script>
		</tr>
		<tr>
			<td>
				<input type="submit" value="전송" />
			</td>
		</tr>
	</table>
</form:form>












