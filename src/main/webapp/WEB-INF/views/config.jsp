<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Configurations</title>
</head>
<body>
	<c:if test="${empty loginUser}">
		<div class="login-form">
		    <h2>Login</h2>
		    <div>
		    	<c:url value="/config/login" var="loginUrl" />
		   	<sf:form method="POST" modelAttribute="user" action="${loginUrl}">
		   	
				<label for="user_screen_name">Username:</label>
				<sf:input path="username" size="30" maxlength="50" id="user_screen_name"/>			
				<sf:errors path="username" cssClass="error"/>
				
				<label for="user_password">Password:</label>
				<sf:password path="password" size="30" showPassword="true" id="user_password"/>
				<sf:errors path="password" cssClass="error"/>
				
				<c:if test="${loginError == 'true'}">
					<font color='red'><span>Your login attempt was not successful, try again.<br /> 
					Reason: Invalid name or password</span></font>
				</c:if>
				
				<input class="btn" name="commit" type="submit" value="Log In"/>
			</sf:form>
			    
		    </div>
		</div>
	</c:if>
	<c:if test="${not empty loginUser}">
		<h1>User login was successfull</h1>
	</c:if>
</body>
</html>