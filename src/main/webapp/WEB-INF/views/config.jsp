<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
</html> --%>





<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1">
	<title></title>
	<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
	<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>">
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js" type="text/javascript"></script>
	<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.js" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/include.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/ajax.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/jquery-2.1.4.min.js"/>" type="text/javascript"></script>
	
	<title>Configurations</title>

</head>
<body>


<header class="mainhead cf">
	<a href="" class="logo">Roster Configuration</a>
	<c:if test="${empty loginUser}">
		<span class = "loginButton"><a href="" class="btn btn-color open_login_modal">Log in</a></span>
	</c:if>
	<c:if test="${not empty loginUser}">
		<span class="loginUserMessage">${loginUser.username} logged successfully</span>
	</c:if>
		<div id="loginUserSuccessMessage"></div>
</header>
<main class="maincontent" role="main">
	<div class="columns cf tabs-container maintabs">
		<aside class="sidebar">
			<ul class="aside-list tabs-nav">
				<li class="tab-item active"><a data-href="basic" href="#">Basic setup</a></li>
				<li class="tab-item"><a data-href="rules" href="#">Rules</a></li>
			</ul>
		</aside>
		<div class="content">
			<div data-id="basic" class="tab-content active">
				<div class="opt-cont-row">
					<div class="options-container">
						<div class="options-cont-content">
						
							<c:if test="${not empty selectedSetupOptions}">
								<ul>
									<c:forEach var="option" items="${selectedSetupOptions}">
										<li>${option}</li>		
									</c:forEach>
								</ul>
							</c:if>
						</div>
					</div>
				</div>
				<s:url value="/config/available-options" var="available_option_url"/>
				<s:url value="/config/selected-options" var="selected_option_url"/>
				<a id="modify" href="" data-avalurl="${available_option_url}" data-selecurl="${selected_option_url}" class="btn btn-color open_hidden_options">Modify</a>


				<div class="opt-cont-row hidden_options change-options-container">
					<div class="c-title">Basic setup modification</div>
					<div class="options-container">
						<div class="options-cont-header">
							Available options
						</div>
						<div class="options-cont-content">
							<ul id="sortable1" class="connectedSortable list-left">
								<!-- <li class="">Restricted Location</li>
								<li class="">Rank</li>
								<li class="">Status</li> -->
							</ul>
						</div>
					</div>

					<div class="opt-arrows">
						<span class="arr-btn arr-btn-right"></span>
						<span class="arr-btn arr-btn-left"></span>
					</div>

					<div class="options-container">
						<div class="options-cont-header">
							Selected options
						</div>
						<div class="options-cont-content">
							<ul id="sortable2" class="connectedSortable list-right">
								<!-- <li class="">Assigned Location</li>
								<li class="">Task Location</li> -->

							</ul>
						</div>
					</div>
					<div class="btns-holder">
						<s:url value="/config/save-available-options" var="save_available_url"/>
						<s:url value="/config/save-selected-options" var="save_selected_url"/>
						<a href="" id="save_setup" data-saveaval="${save_available_url}" data-saveselec="${save_selected_url}" class="btn btn-color ">ok</a>
						<a href="" class="btn btn-red ">cancel</a>
					</div>

				</div>


			</div>

			<div data-id="rules" class="tab-content">

				<div class="rules-sort cf">
					<table class="rules-table">
						<thead>
						<tr>
							<th>Base</th>
							<th>Criteria</th>
							<th>Reference</th>
							<th>Type</th>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td>
								<select name="" id="">
									<option value="">Assigned Location</option>
									<option value="">Designaion</option>
									<option value="">Age</option>
								</select>
							</td>
							<td>
								<select name="" id="">
									<option value="">Should be equal to</option>
									<option value="">Should be not equal to</option>
									<option value="">Should contains</option>
									<option value="">In between</option>
									<option value="">Atleast</option>
									<option value="">Atmost</option>
								</select>
							</td>
							<td>
								<select name="" id="">
									<option value="">Task Location</option>
									<option value="">Junior</option>
									<option value="">Age range</option>
								</select>
							</td>
							<td>
								<select name="" id="">
									<option value="">Hard</option>
									<option value="">Soft</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								<select name="" id="">
									<option value="">Designaion</option>

									<option value="">Assigned Location</option>
									<option value="">Age</option>
								</select>
							</td>
							<td>
								<select name="" id="">
									<option value="">Should be equal to</option>
									<option value="">Should be not equal to</option>
									<option value="">Should contains</option>
									<option value="">In between</option>
									<option value="">Atleast</option>
									<option value="">Atmost</option>
								</select>
							</td>
							<td>
								<select name="" id="">
									<option value="">Task Location</option>
									<option value="">Junior</option>
									<option value="">Age range</option>
								</select>
							</td>
							<td>
								<select name="" id="">
									<option value="">Hard</option>
									<option value="">Soft</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								<select name="" id="">
									<option value="">Age</option>

									<option value="">Assigned Location</option>
									<option value="">Designaion</option>
								</select>
							</td>
							<td>
								<select name="" id="">
									<option value="">Should be equal to</option>
									<option value="">Should be not equal to</option>
									<option value="">Should contains</option>
									<option value="">In between</option>
									<option value="">Atleast</option>
									<option value="">Atmost</option>
								</select>
							</td>
							<td>
								<select name="" id="">
									<option value="">Task Location</option>
									<option value="">Junior</option>
									<option value="">Age range</option>
								</select>
							</td>
							<td>
								<select name="" id="">
									<option value="">Hard</option>
									<option value="">Soft</option>
								</select>
							</td>
						</tr>
						</tbody>
					</table>


					<div class="opt-arrows">
						<span class="arr-btn arr-btn-top"></span>
						<span class="arr-btn arr-btn-bottom"></span>
					</div>
				</div>

				<div class="btns-holder">
					<a href="" class="btn btn-color ">ok</a>
					<a href="" class="btn btn-red ">cancel</a>
				</div>
			</div>
		</div>
	</div>
</main>


<!------------login modal-------------->
<c:if test="${empty loginUser}">
	<div class="modal-wrap modal-login open">
		<div class="login-block">
			<div class="login-form-block">
				 <div class="flipper">
					<div class="login-form forgot-pass">
						<div class="loader-box">
							<div class="loader-inner">
								<div></div>
							</div>
						</div>
						<!-- <i class="close-modal" role="close_form">&times</i> -->
	
					</div>
	
					<div class="login-form">
						<!-- <div class="loader-box">
							<div class="loader-inner">
								<div></div>
							</div>
						</div>
						<i class="close-modal" role="close_form">&times</i>  -->
	
						<h3>Login</h3>
	
	
						<form id="loginform" action="/roster/config/login" method="post" class="fields"
							  name="loginform" autocomplete="off">
	
							<div class="inputs-group">
								<div class="text-input">
									<input type="text" id="username" name="username" class="first"
										   placeholder="username" value="">
								</div>
								<div class="text-input">
									<input type="password" id="password" name="password" class="last"
										   placeholder="password" value="">
								</div>
							</div>
							<div id="msg"></div>
							<button data-hover="Login" type="submit" data-action="contactFormSbmt"
									class="btn btn btn-green" id="comment_auth_button" role="button">
								Login
							</button>
						</form>
						
						
	
						<!-- <p><a class="lost-pass-btn" href=""
							  role="toggle_forgot_password_form">Забыли пароль?</a>
						</p> -->
					 </div>
				</div>
			</div>
		</div>
	</div>
</c:if>
<!----------------end modal---------->



</body>
</html>
