<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1">
	<title>Roster Scheduler</title>
	<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>">
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js" type="text/javascript"></script>
	<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.js" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/include.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/ajax.js"/>" type="text/javascript"></script>

</head>
<body>


<header class="mainhead cf">
	<a href="" class="logo">Roster</a>
	<a href="" class="btn btn-color">Log in</a>
</header>
<main class="maincontent" role="main">
	<div class="columns cf">
		<aside class="sidebar">
			<h5>Filters</h5>
			<ul class="aside-list">
				<li class=""><a href="#">Staff without any task assigned</a></li>
				<li class="open_locations_modal"><a href="#">Locations</a></li>
				<li class=""><a href="#">Rules violated tasks</a></li>
				<li class=""><a href="#">Leaves</a></li>
			</ul>
		</aside>
		<div class="content">
			<div class="content-header">
				<s:url value="/scheduler/run-engine" var="run_engine"/>
				<span id="runEngine" class="cl-icon icon-roster" data-url="${run_engine}"></span>
				<span class="cl-icon icon-options open_adv_options"></span>

				<div class="date-filters">

					<div type="text" class="cl-inp"><input class="week-picker" type="text">
						<span id="startDate">06/05/1991</span> - <span id="endDate">06/12/1991</span>
					</div>
					<span class="or">or</span>
					<input	type="text" value="06/14/2015" class="day-picker cl-inp"/>



					<!--<div class="week-picker cl-btn">Today</div>-->

				</div>

				<span class="cl-icon icon-check"></span>
			</div>

			<div class="content-inner">
				<table class="simple-table">
					<thead id="scheduleTableHeader">
					<tr>
						<th>Name</th>
						<th>Assigned location</th>
						<th>Reference</th>
						<th>Jul 27<br/>Monday</th>
						<th>Jul 28<br/>Monday</th>
						<th>Jul 28<br/>Monday</th>
						<th>Jul 30<br/>Monday</th>
						<th>Jul 31<br/>Monday</th>
						<th>Jul 32<br/>Monday</th>
						<th>Jul 33<br/>Monday</th>
					</tr>
					</thead>
					<tbody id="scheduleTableBody">
					<tr>
						<td>John Abraham</td>
						<td>Hotel ABC</td>
						<td>RN101</td>
						<td><span class="table-tag yellow">D12 <span class="tag-mark">S</span></span></td>
						<td><span class="table-tag green">A8</span></td>
						<td><span class="table-tag purple">N12</span></td>
						<td><span class="table-tag pink">AL</span></td>
						<td><span class="table-tag black">off</span></td>
						<td><span class="table-tag yellow">D12 <span class="tag-mark">D</span></span></td>
						<td><span class="table-tag yellow">D12</span></td>
					</tr>
					<tr>
						<td>John Abraham</td>
						<td>Hotel ABC</td>
						<td>RN101</td>
						<td><span class="table-tag yellow">D12 <span class="tag-mark">S</span></span></td>
						<td><span class="table-tag green">A8</span></td>
						<td><span class="table-tag purple">N12</span></td>
						<td><span class="table-tag pink">AL</span></td>
						<td><span class="table-tag black">off</span></td>
						<td><span class="table-tag yellow">D12 <span class="tag-mark">D</span></span></td>
						<td><span class="table-tag yellow">D12</span></td>
					</tr>
					<tr>
						<td>John Abraham</td>
						<td>Hotel ABC</td>
						<td>RN101</td>
						<td><span class="table-tag yellow">D12 <span class="tag-mark">S</span></span></td>
						<td><span class="table-tag green">A8</span></td>
						<td><span class="table-tag purple">N12</span></td>
						<td><span class="table-tag pink">AL</span></td>
						<td><span class="table-tag black">off</span></td>
						<td><span class="table-tag yellow">D12 <span class="tag-mark">D</span></span></td>
						<td><span class="table-tag yellow">D12</span></td>
					</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</main>

<div class="modal-wrap modal-orangehead modal-info modal-advanced">
	<div class="modal-inner">
		<div class="modal-header">
			Advanced options <i class="close-modal deco-icon icon-close" role="close_form">&times;</i>
		</div>
		<div class="motal-content">
			<form class="advanced-form" action="">
				<label class="lbl" for="sal"><input id="sal" type="checkbox">Consider salary</label>
				<label class="lbl" for="weekly"><input id="weekly" type="radio" name="r1">Weekly</label>
				<label class="lbl" for="monthly"><input id="monthly" type="radio" name="r1">Monthly</label>
				<button class="btn btn-color">ok</button>
				<span class="btn btn-red">Cancel</span>
			</form>
		</div>
	</div>
</div>

<div class="modal-wrap modal-orangehead modal-info modal-locations">
	<div class="modal-inner">
		<div class="modal-header">
			Select Locations <i class="close-modal deco-icon icon-close" role="close_form">&times;</i>
		</div>
		<div class="motal-content">
			<form class="advanced-form" action="">
				<label class="lbl" for="l1"><input id="l1" type="checkbox">Hotel ABC</label>
				<label class="lbl" for="l2"><input id="l2" type="checkbox">Shopping center</label>
				<label class="lbl" for="l3"><input id="l3" type="checkbox">Hotel CBA</label>
				<button class="btn btn-color">ok</button>
				<span class="btn btn-red">Cancel</span>
			</form>
		</div>
	</div>
</div>



</body>
</html>