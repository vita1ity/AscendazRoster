
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
			<s:url value="/config/selected-rules" var="selected_rules_url"/>
			<ul class="aside-list tabs-nav">
				<li class="tab-item active"><a data-href="basic" href="#">Basic setup</a></li>
				<li class="tab-item" id="rulesTab"  data-url="${selected_rules_url}"><a data-href="rules" href="#">Rules</a></li>
			</ul>
		</aside>
		<div class="content">
			<div data-id="basic" class="tab-content active">
				<div class="opt-cont-row">
					<div class="options-container">
						<div class="options-cont-content">
						
							<c:if test="${not empty selectedSetupOptions}">
								<ul id="options-list">
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
						<tbody id="rules-table-body">
						<tr>
							<td>
								<select name="" id="base">
									<option value="">Assigned Location</option>
									<option value="">Designaion</option>
									<option value="">Age</option>
								</select>
							</td>
							<td>
								<select name="" id="criteria0">
									<c:forEach var="criteria" items="${criteriaList}">
										<option value="${criteria.criteriaString}">${criteria.criteriaString}</option>
									</c:forEach>
									
								</select>
							</td>
							<td>
								<select name="" id="reference">
									<option value="">Task Location</option>
									<option value="">Junior</option>
									<option value="">Age range</option>
								</select>
							</td>
							<td>
								<select name="" id="rule-type0">
									<c:forEach var="type" items="${typeList}">
										<option value="${type}">${type}</option>
									</c:forEach>
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

				<s:url value="/config/save-rules" var="save_rules_url"/>
				<div class="btns-holder">
					<a  id="save_rules" data-url="${save_rules_url}" href="" class="btn btn-color">ok</a>
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
						
					 </div>
				</div>
			</div>
		</div>
	</div>
</c:if>
<!----------------end modal---------->

</body>
</html>
