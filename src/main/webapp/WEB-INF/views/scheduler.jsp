<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
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
	<script src="<c:url value="/resources/js/jquery.fixedheadertable.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/ajax.js"/>" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/include.js"/>" type="text/javascript"></script>
	

</head>
<body>


<header class="mainhead cf">
	<a href="" class="logo">Roster</a>
</header>
<main class="maincontent" role="main">
	<div class="columns cf">
		
		<div class="content content-full">
			<div class="content-header">
				
				<c:if test="${not empty schedule}">
					<c:forEach var="s" items="${schedule}">
						<c:forEach var="task" items="${s.tasks}">
						<c:if test="${task.status eq 'Draft' or empty task.shift}">
							<c:set var="isDraft" scope="request" value="true"/>	
						</c:if>
						
						</c:forEach>
					</c:forEach>
				</c:if>
				<s:url value="/scheduler/run-engine" var="run_engine"/> 
				<s:url value="/scheduler/approve-schedule" var="approveSchedule"/>
				<c:if test="${isDraft eq 'true'}">
					
					<span id="runEngine" class="cl-icon icon-roster show_preload" data-url="${run_engine}"></span>
					<span id="runAdvanced" class="cl-icon icon-options open_adv_options"></span>
					
					<span id="approveSchedule" class="cl-icon icon-check show_approve_modal"></span>
					
				</c:if>
				<c:if test="${empty isDraft}">
					
					<span id="runEngine" class="cl-icon icon-roster show_preload disabled" data-url="${run_engine}"></span>
					<span id="runAdvanced" class="cl-icon icon-options open_adv_options disabled"></span>
					<span id="approveSchedule" class="cl-icon icon-check disabled show_approve_modal" ></span>
				</c:if>
				<!-- <div id="dialog-confirm" class="cl-icon"></div> -->
				<div class="date-filters">
					<s:url value="/scheduler/get-schedule?page=1" var="getSchedule"/>
					<span id="prev" class="cl-btn"><</span>
					<div id="dateWeek" data-url="${getSchedule}" type="text" class="cl-inp"><!-- <input class="week-picker" type="text"> -->
						<div id="weekDateValue">
							<span id="startDate"><joda:format value="${startDate}" pattern="dd/MM/yyyy"/></span>
							 - <span id="endDate"><joda:format value="${endDate}" pattern="dd/MM/yyyy"/></span>
						</div>
						<div id="monthDateValue" class="display-none">
							<span id="monthDate">Month</span>
						</div>
						<div id="dayValue" class="display-none">
							<span id="dayDate">Day</span>
						</div>
					</div>
					<span id="next" class="cl-btn">></span>
					<span id="today" class="cl-btn">Today</span>
					<span class="cl-btn"><input class="week-picker week-input" value="Week"/>Week</span>
					<input class="cl-btn pick-month" value="Month"  />

				</div>
				
				<div class="filters">
					<%-- <s:url value="/scheduler/staff-without-tasks" var="staffWithoutTasks"/>
					<s:url value="/scheduler/rules-violated-tasks" var="rulesViolatedTasks"/>
					<s:url value="/scheduler/leaves-tasks" var="onLeavesTasks"/> --%>
					<s:url value="/scheduler/apply-filters" var="applyFilters"/>
					
					<span class="cl-btn btn-filter staff-without-tasks" data-url="${applyFilters}">Not assigned</span>
					<span class="cl-btn location-btn open_locations_modal">Locations</span>
					<span class="cl-btn btn-filter rules-violated-tasks" data-url="${applyFilters}">Rules violated tasks</span>
					<span class="cl-btn btn-filter leaves-tasks" data-url="${applyFilters}">Leaves</span>
				</div>
				
			</div>
			
			<%--  <div class="content-inner ">
				<div class="tbl-scroll cf vert-scroll">
				<div class="fixed-part">
					<table class="simple-table">
						<thead>
							<tr>
									
								<th>Name</th>
								<th>Assigned location</th>
								<th>Reference</th>
							</tr>
						</thead>
						<tbody id="scheduleTableBody">
							<tr>
							<c:if test="${not empty schedule}">
								<c:forEach var="s" items="${schedule}">
									<tr>
										<td>${s.name}</td>
										<td>${s.location}</td>
										<td>${s.reference}</td>
									</tr>
								</c:forEach>
							</c:if>
							</tr>
						
						</tbody>
					</table>
				</div>
				<div class="table-scroll-x">
				<table class="simple-table">
					<thead id="scheduleTableHeader">
					<tr>
						<c:forEach var="day" items="${daysOfWeek}">
							
							<c:if test="${day == currentDay}">
								<th class="today">${day}</th>	
							</c:if>
							<c:if test="${day != currentDay}">
								<th>${day}</th>	
							</c:if>
						
						</c:forEach>
						
					</tr>
					</thead>
					<tbody id="scheduleTableBodyScrollable">
						<c:if test="${not empty schedule}">
								<c:forEach var="s" items="${schedule}">
									<tr>
										<c:forEach var="task" items="${s.tasks}">
											<c:set var="shift" scope="request" value="${task.shift}"/>
											<td>
												<c:if test="${task.status eq 'Draft'}">
													
													<c:set var="status" scope="request" value="D"/>	
												</c:if>
												<c:if test="${task.status eq 'Submitted'}">
													<c:set var="status" scope="request" value="S"/>	
												</c:if>
												<c:if test="${not empty shift}">
													<c:choose>
													    <c:when test="${shift.shiftLetter == 'M'}">
													    	<span data-tip class="table-tag yellow">D12 <span class="tag-mark">${status}</span>
													    	<c:if test="${task.violated}">
													    		<span class="left-tag">V</span>
													    	</c:if>
													    	
													    		<span class="custom-tooltip">
																	<strong>Location:</strong>${s.location}<br/>
																	<strong>Shift:</strong>${shift.shift}<br/>
																	<strong>Trainings:</strong> 
																	<c:forEach var="training" items="${task.trainings}" varStatus="status">
																		<c:if test="${not status.last}">
																			${training},
																		</c:if>
																		<c:if test="${status.last}">
																			${training}
																		</c:if>   
																	</c:forEach>
																</span>
															</span>
													    </c:when>
													    <c:when test="${shift.shiftLetter == 'A'}">
													        <span data-tip class="table-tag green">A8 <span class="tag-mark">${status}</span>
													        <c:if test="${task.violated}">
													    		<span class="left-tag">V</span>
													    	</c:if>
													        	<span class="custom-tooltip">
																	<strong>Location:</strong>${s.location}<br/>
																	<strong>Shift:</strong>${shift.shift}<br/>
																	<strong>Training:</strong> 
																	<c:forEach var="training" items="${task.trainings}" varStatus="status">
																		<c:if test="${not status.last}">
																			${training},
																		</c:if>
																		<c:if test="${status.last}">
																			${training}
																		</c:if>   
																	</c:forEach>
																</span>
													        </span>
													    </c:when>
													    <c:when test="${shift.shiftLetter == 'N'}">
													    	<span data-tip class="table-tag purple">N12 <span class="tag-mark">${status}</span>
														    	<c:if test="${task.violated}">
														    		<span class="left-tag">V</span>
														    	</c:if>
													    		<span class="custom-tooltip">
																	<strong>Location:</strong>${s.location}<br/>
																	<strong>Shift:</strong>${shift.shift}<br/>
																	<strong>Training:</strong> 
																	<c:forEach var="training" items="${task.trainings}" varStatus="status">
																		<c:if test="${not status.last}">
																			${training},
																		</c:if>
																		<c:if test="${status.last}">
																			${training}
																		</c:if>   
																	</c:forEach>
																</span>
													    	</span>
													    </c:when>
													    <c:when test="${shift.shiftLetter == 'L'}">
													        <span data-tip class="table-tag pink">${task.leave.type} <span class="tag-mark">${status}</span>
													        	<c:if test="${task.violated}">
														    		<span class="left-tag">V</span>
														    	</c:if>
													        	<span class="custom-tooltip">
																	<strong>Location:</strong>${s.location}<br/>
																	<strong>Shift:</strong>${shift.shift}<br/>
																	<strong>Training:</strong> 
																	<c:forEach var="training" items="${task.trainings}" varStatus="status">
																		<c:if test="${not status.last}">
																			${training},
																		</c:if>
																		<c:if test="${status.last}">
																			${training}
																		</c:if>   
																	</c:forEach>
																</span>
													        </span>
													    </c:when>
													    <c:when test="${shift.shiftLetter == 'O'}">
													        <span data-tip class="table-tag black">OFF <span class="tag-mark">${status}</span>
													        	<c:if test="${task.violated}">
														    		<span class="left-tag">V</span>
														    	</c:if>
													        	<span class="custom-tooltip">
																	<strong>Location:</strong>${s.location}<br/>
																	<strong>Shift:</strong>${shift.shift}<br/>
																	<strong>Training:</strong> 
																	<c:forEach var="training" items="${task.trainings}" varStatus="status">
																		<c:if test="${not status.last}">
																			${training},
																		</c:if>
																		<c:if test="${status.last}">
																			${training}
																		</c:if>   
																	</c:forEach>
																</span>
													        </span>
													    </c:when>
													    
													</c:choose>
													
													
												</c:if>
												<c:if test="${empty shift}">
													<span class="table-tag grey">NA</span>
												</c:if>
											</td>
										</c:forEach>
										
									</tr>
								</c:forEach>
							</c:if>
					
					</tbody>
				</table>
				</div>
				</div>
			</div>  --%>
			
			
			
			
			
			  <div class="content-inner">
				<!-- <div class="table-scroll-x"> -->
				<table class="simple-table scrolltable">
					<thead id="scheduleTableHeader">
					<tr>
						<th>Name</th>
						<th>Assigned location</th>
						<th>Reference</th>

						<c:forEach var="day" items="${daysOfWeek}">
							
							<c:if test="${day == currentDay}">
								<th class="today">${day}</th>	
							</c:if>
							<c:if test="${day != currentDay}">
								<th>${day}</th>	
							</c:if>
						
						</c:forEach>
						
					</tr>
					</thead>
						<tbody id="scheduleTableBody">
							<c:if test="${not empty schedule}">
								<c:forEach var="s" items="${schedule}">
									
									<tr>
										<td>${s.name}</td>
										<td>${s.location}</td>
										<td>${s.reference}</td>
										<c:forEach var="task" items="${s.tasks}">
											<c:set var="shift" scope="request" value="${task.shift}"/>
											<td>
												<c:if test="${task.status eq 'Draft'}">
													
													<c:set var="status" scope="request" value="D"/>	
												</c:if>
												<c:if test="${task.status eq 'Submitted'}">
													<c:set var="status" scope="request" value="S"/>	
												</c:if>
												<c:if test="${not empty shift}">
													<c:choose>
													    <c:when test="${shift.shiftLetter == 'M'}">
													    	<span data-tip class="table-tag yellow">D12 <span class="tag-mark">${status}</span>
													    	<c:if test="${task.violated}">
													    		<span class="left-tag">V</span>
													    	</c:if>
													    	
													    		<span class="custom-tooltip">
																	<strong>Location:</strong>${s.location}<br/>
																	<strong>Shift:</strong>${shift.shift}<br/>
																	<strong>Trainings:</strong> 
																	<c:forEach var="training" items="${task.trainings}" varStatus="status">
																		<c:if test="${not status.last}">
																			${training},
																		</c:if>
																		<c:if test="${status.last}">
																			${training}
																		</c:if>   
																	</c:forEach>
																</span>
															</span>
													    </c:when>
													    <c:when test="${shift.shiftLetter == 'A'}">
													        <span data-tip class="table-tag green">A8 <span class="tag-mark">${status}</span>
													        <c:if test="${task.violated}">
													    		<span class="left-tag">V</span>
													    	</c:if>
													        	<span class="custom-tooltip">
																	<strong>Location:</strong>${s.location}<br/>
																	<strong>Shift:</strong>${shift.shift}<br/>
																	<strong>Training:</strong> 
																	<c:forEach var="training" items="${task.trainings}" varStatus="status">
																		<c:if test="${not status.last}">
																			${training},
																		</c:if>
																		<c:if test="${status.last}">
																			${training}
																		</c:if>   
																	</c:forEach>
																</span>
													        </span>
													    </c:when>
													    <c:when test="${shift.shiftLetter == 'N'}">
													    	<span data-tip class="table-tag purple">N12 <span class="tag-mark">${status}</span>
														    	<c:if test="${task.violated}">
														    		<span class="left-tag">V</span>
														    	</c:if>
													    		<span class="custom-tooltip">
																	<strong>Location:</strong>${s.location}<br/>
																	<strong>Shift:</strong>${shift.shift}<br/>
																	<strong>Training:</strong> 
																	<c:forEach var="training" items="${task.trainings}" varStatus="status">
																		<c:if test="${not status.last}">
																			${training},
																		</c:if>
																		<c:if test="${status.last}">
																			${training}
																		</c:if>   
																	</c:forEach>
																</span>
													    	</span>
													    </c:when>
													    <c:when test="${shift.shiftLetter == 'L'}">
													        <span data-tip class="table-tag pink">AL <span class="tag-mark">${status}</span>
													        	<c:if test="${task.violated}">
														    		<span class="left-tag">V</span>
														    	</c:if>
													        	<span class="custom-tooltip">
																	<strong>Location:</strong>${s.location}<br/>
																	<strong>Shift:</strong>${shift.shift}<br/>
																	<strong>Training:</strong> 
																	<c:forEach var="training" items="${task.trainings}" varStatus="status">
																		<c:if test="${not status.last}">
																			${training},
																		</c:if>
																		<c:if test="${status.last}">
																			${training}
																		</c:if>   
																	</c:forEach>
																</span>
													        </span>
													    </c:when>
													    <c:when test="${shift.shiftLetter == 'O'}">
													        <span data-tip class="table-tag black">OFF <span class="tag-mark">${status}</span>
													        	<c:if test="${task.violated}">
														    		<span class="left-tag">V</span>
														    	</c:if>
													        	<span class="custom-tooltip">
																	<strong>Location:</strong>${s.location}<br/>
																	<strong>Shift:</strong>${shift.shift}<br/>
																	<strong>Training:</strong> 
																	<c:forEach var="training" items="${task.trainings}" varStatus="status">
																		<c:if test="${not status.last}">
																			${training},
																		</c:if>
																		<c:if test="${status.last}">
																			${training}
																		</c:if>   
																	</c:forEach>
																</span>
													        </span>
													    </c:when>
													    
													</c:choose>
													
													
												</c:if>
												<c:if test="${empty shift}">
													<span class="table-tag grey">NA</span>
												</c:if>
											</td>
										</c:forEach>
										
									</tr>
								</c:forEach>
							</c:if>
							
							
						</tbody>
					</table>
				<!-- </div> -->
			</div>  
			
			<input id="numOfPages" type="hidden" name="pages" value="${numberOfPages}">
			<input id="currentPage" type="hidden" name="currentPage" value="${currentPage}">
			<input id="startDisplayPage" type="hidden" name="startDisplayPage" value="${startDisplayPage}">
			<input id="pageUrl" type="hidden" name="pageUrl" value=<c:url value="/scheduler/get-schedule?page=${currentPage}"/>>
			
		    <ul id="pager" class="pager">
		   		 <c:if test="${(numberOfPages gt 10) && (currentPage > 10)}">
					<c:url value="/scheduler/get-schedule?page=${currentPage - currentPage % 10 - 9}" var="page"/>
					<li class="prev pager_nav pager_item" data-page="${page}"><a>Prev 10</a></li>
				</c:if>
				<c:if test="${currentPage != 1}">
					<c:url value="/scheduler/get-schedule?page=${currentPage - 1}" var="page"/>
					<li class="prev pager_nav pager_item" data-page="${page}"><a>Prev</a></li>
			    </c:if>
				
				<c:forEach begin="${startDisplayPage}" end="${startDisplayPage + 9}" var="i">
					
					<c:choose>
						<c:when test="${currentPage eq i}">
							<li class="current_page"><a>${i}</a></li>
						</c:when>
						<c:otherwise>
							<c:url value="/scheduler/get-schedule?page=${i}" var="page"/>
							<li class="pager_item" data-page="${page}"><a>${i}</a></li>
						</c:otherwise>
					</c:choose>
					
				</c:forEach>
				<c:if test="${currentPage lt numberOfPages}">
					<c:url value="/scheduler/get-schedule?page=${currentPage + 1}" var="page"/>
					<li class="next pager_nav pager_item" data-page="${page}"><a>Next</a></li>
				</c:if>
				<c:if test="${(currentPage / 10) lt (numberOfPages / 10)}">
					<c:url value="/scheduler/get-schedule?page=${currentPage + 1 + (10 - currentPage % 10)}" var="page"/>
					<li class="next pager_nav pager_item" data-page="${page}"><a>Next 10</a></li>
				</c:if>
			</ul>
			
			
		</div>
	</div>
</main>

<div class="modal-wrap modal-orangehead modal-info modal-advanced">
	<div class="modal-inner">
		<div class="modal-header">
			Advanced options <i class="cancel-modal close-modal deco-icon icon-close" role="close_form">&times;</i>
		</div>
		<div class="modal-content">
			<form class="advanced-form" action="">
				<div class="option-labels">
					<label class="lbl" for="sal"><input id="sal" type="checkbox" checked>Consider salary</label>
					<label class="lbl" for="weekly"><input id="weekly" type="radio" name="r1" checked>Weekly</label>
					<label class="lbl" for="monthly"><input id="monthly" type="radio" name="r1">Monthly</label>
				</div>
				<s:url value="/scheduler/run-engine" var="runAdvancedEngine"/>
				<button id="runAdvancedEngine" data-url="${runAdvancedEngine}" class="btn btn-color">ok</button>
				<span class="cancel-modal btn btn-red">Cancel</span>
			</form>
		</div>
		<div class="clean"></div>
	</div>
</div>

<div class="modal-wrap modal-orangehead modal-info modal-locations">
	<div class="modal-inner">
		<div class="modal-header">
			Select Locations <i class="cancel-modal close-modal deco-icon icon-close" role="close_form">&times;</i>
		</div>
		<div class="modal-content">
			<form id="locationsForm" class="advanced-form" action="">
				<div class="location-checkboxes">
					<c:forEach var="location" items="${locationList}">
						<label class="lbl" for="l1"><input id="l1" type="checkbox" value="${location}">${location}</label>
					</c:forEach>
				</div>
				<%-- <s:url value="/scheduler/filter-locations" var="filterLocations"/> --%>
				<button type="button" class="btn btn-color btn-filter" id="filter-locations" data-url="${applyFilters}">ok</button>
				<span class="cancel-modal btn btn-red">Cancel</span>
			</form>
		</div>
		<div class="clean"></div>
	</div>
</div>
<div class="modal-wrap modal-orangehead modal-info modal-approve">
	<div class="modal-inner">
		<div class="modal-header">
			Approve Confirmation <i class="close-modal deco-icon icon-close" role="close_form">&times;</i>
		</div>
		<div class="modal-content">
				<p>Do you want to approve schedule for selected period?</p>
				<a href="" class="btn btn-color" id="confirmApproveSchedule" data-url="${approveSchedule}">ok</a>
				<span class="cancel-modal btn btn-red close_modal">Cancel</span>

		</div>
	</div>
</div>
<!-- preloader -->
<div class="preload-wrap">
	<div class="preload">
		<div class="circle"></div>
		<div class="circle1"></div>
	</div>
</div>
<!-- end preloader -->

</body>
</html>