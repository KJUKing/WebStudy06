<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>    

<h4>웰컴 페이지</h4>
<!-- 현재 사용자가 인증된 상태라면 신원 정보를 출력하고, -->
<!-- 인증되지 않은 사용자에게는 로그인 페이지 링크를 출력함. -->
<pre>
<security:authorize access="isAnonymous()">
	로그인하기 전.
</security:authorize>
<security:authorize url="/mypage">
	로그인된 사용자
	<security:authentication property="principal" var="principal"/>
	<security:authentication property="details"/>
	<security:authentication property="authorities"/>
</security:authorize>
</pre>

<hr />
