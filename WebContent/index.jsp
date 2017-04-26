<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OAuth Login</title>
</head>
<body>
	<h1>OAuth Login</h1>
	<table>
		<tr>
			<th><s:form action="GoTwitterAction">
					<input type="image" src="./img/twitter.png" />
				</s:form></th>
			<th><s:form action="GoFacebookAction">
					<input type="image" src="./img/facebook.png" />
				</s:form></th>
		</tr>
	</table>
</body>
</html>