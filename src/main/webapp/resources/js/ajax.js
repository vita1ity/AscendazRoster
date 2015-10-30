
//login
$(document).ready(function() {
	var username = $(this).find('.first').val();
	//console.log(username);
	var password = $(this).find('.last').val();
	//console.log(password);
	$.ajax({
		   url: $(this).attr("action"),
           type: "POST",
           data: {username: username, password: password},
    }).done (function(response) {
    	//console.log(response);
    	if (response == "success") {
    		$("#msg").html("<span class=\"loginFormSuccess\">Login was successful!</span>");
    		$(".display-none").removeClass("display-none");
    		$(".modal-login").removeClass('open');
    		$(".loginButton").addClass("notDisplayLogin");
    		$("#loginUserSuccessMessage").html("<span class=\"loginFormSuccess\">User Login successfully</span>");
    		//console.log("SUCCESS");
    	}
    	else {
    		$("#msg").html("<span class=\"loginFormError\">Your login attempt was not successful</br> " +
    				"Reason: Invalid username or password</span>");
    		//console.log("FAIL");
    	}
    }).fail (function(err) {
    	
    	   console.log(err);
    	});
	 
});

//modify button (get available and selected options)
$(document).on('click', '.open_hidden_options', function (e) {
	e.preventDefault();
	
	$('.hidden_options').slideToggle(200);
	
	var avalurl = $(this).data("avalurl");
	console.log(avalurl);
	
	$.ajax({
        url:avalurl,
        type: "GET"
	}).done (function(data) {
		var list = "";
		var len = data.length;
		for (var i = 0; i < len; i++) {
			list += "<li>" + data[i].setupOption + "</li>"
		}
		//console.log(list);
		$("#sortable1").html(list);
	
	}).fail (function(err) {
	       console.error(err);
	});
	
	
	var selecurl = $(this).data("selecurl");
	console.log(selecurl);
	
	$.ajax({
        url:selecurl,
        type: "GET"
	}).done (function(data) {
		var list = "";
		var len = data.length;
		for (var i = 0; i < len; i++) {
			list += "<li>" + data[i].setupOption + "</li>"
		}
		//console.log(list);
		$("#sortable2").html(list);
	
	}).fail (function(err) {
	       console.error(err);
	});
	
});


//save values of selected and available options in the db and get selected options
$(document).on('click', '#save_setup', function (e) {
	
	e.preventDefault();

	$('.hidden_options').slideUp(200);
	
	//selected options
    
    var selecOptionsArray = new Array();
    var option = new Object();
    var selecurl = $(this).data("saveselec");
    
    console.log(JSON.stringify(selecOptionsArray));
    
    $('#sortable2').find('li').each(function(i){
    	var current = $(this);
    	option = current.text();
    	
    	selecOptionsArray[i] = option;
    	i++;
    });
	
    //write selected options to the list
    var selectedOptions = $('#sortable2').html();
    $("#options-list").html(selectedOptions);
    
	//available options
    var avalOptionsArray = new Array();
    var option = new Object();
    var avalurl = $(this).data("saveaval");
    
    $('#sortable1').find('li').each(function(i){
    	var current = $(this);
    	
    	option = current.text();
    	
    	avalOptionsArray[i] = option;
    	i++;
    });
    
    
    //save selected options
    $.ajax({
        type: "POST",
        url: selecurl,
        data: JSON.stringify(selecOptionsArray),
        contentType: "application/json; charset=utf-8"
    }).done (function(data) {
    	
    	console.log(JSON.stringify(avalOptionsArray));
        
        $.ajax({
            type: "POST",
            url: avalurl,
            data: JSON.stringify(avalOptionsArray),
            contentType: "application/json; charset=utf-8"
        }).done (function(data) {
        	console.log(data);
    	
    	}).fail (function(err) {
    		console.error(err);
    	});
	
	}).fail (function(err) {
		console.error(err);
	});
    
    
    
});


//save rules in the db
$(document).on('click', '#save_rules', function (e) {
	
	e.preventDefault();
	
    var rulesArray = new Array();
    
    var url = $(this).data("url");
    
    $('#rules-table-body').find('tr').each(function(i){
    	var rule = "";
    	$(this).find('td').each(function(j){
    		
    		var html = $(this).html();
    		
    		if ($(html).prop("tagName") == "SELECT") {
    			
    			var id = $(html).attr("id");
    			
    			var idIndex = id.slice(-1);
    			//console.log("index: " + idIndex);
    			if (id.indexOf("rule-type") == 0) {
    			
    				var ruleType = $("#rule-type" + idIndex + " option:selected").text();
    				rule += ruleType;
    				console.log("type: " + ruleType);
    			}
    			else {
    				var criteria = $("#criteria" + idIndex + " option:selected").text();
    				rule += criteria + ",";
    				console.log("criteria: " + criteria);
    			}
    			
    		}
    		else {
    			rule += $(this).text() + ",";
    		}
    		
    		//console.log("RULE: " + rule);
    	});
    	console.log(rule);
    	rulesArray[i] = rule;
    	
    });
    
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(rulesArray),
        contentType: "application/json; charset=utf-8"
    }).done (function(data) {
    	console.log(data);
    	alert("Rules were saved successfully");
	}).fail (function(err) {
		console.error(err);
	});
    
});


//run engine
$(document).on('click', '#runEngine', function (e) {
	e.preventDefault();
	var url = $(this).data("url");
	var startDate = $('#startDate').text();
	var endDate = $('#endDate').text();
	console.log(startDate);
	console.log(endDate);
	console.log(url);
	if ($('#weekDateValue').hasClass("display-none")) {
		alert("You should pick the week first!");

		return;
	}
	$('.preload-wrap').show();
	
	$.ajax({
        type: "POST",
        url: url,
        data: {startDate: startDate, endDate: endDate, considerSalary: true}
        
    }).done (function(data) {
    	renderTable(startDate, endDate, data);
    	/*renderHeader(startDate, endDate);
    	renderSchedule(data);*/
    	$('.preload-wrap').hide();
		alert("Roster successfully completed for the given period: \n " + startDate + " - " + endDate);
	
	}).fail (function(err) {
		$('.preload-wrap').hide();
		$("#scheduleTableBody").html("");
		if (err.responseJSON != null) {
			alert("Error occured during processing: \n " + err.responseJSON.ex);
		}
		
		console.error(err.responseText);
	});
});

//run advanced engine
$(document).on('click', '#runAdvancedEngine', function (e) {
	e.preventDefault();
	var url = $(this).data("url");
	
	$('.modal-wrap').removeClass('open');
	console.log(url);
	var considerSalary = $('#sal:checkbox:checked').val();
	var monthly = $('#monthly:radio:checked').val();
	if (considerSalary != null) {
		considerSalary = true;
	}
	else{
		considerSalary = false;
	}
	if (monthly != null) {
		var monthString = $('#monthDate').text();
		//var monthString = $('.pick-month').val();
		
		if ($('#monthDateValue').hasClass("display-none")) {
			alert("You should pick the month first!");

			return;
		}
		
		monthString = monthString + " 01";
		console.log(monthString);
		var startDateObj = $.datepicker.parseDate("M yy dd", monthString);
		
		var year = startDateObj.getFullYear();
		var month = startDateObj.getMonth();
		
		var endDateObj = new Date(year, month + 1, 0)
		var startDate = $.datepicker.formatDate('dd/mm/yy', startDateObj);
		var endDate = $.datepicker.formatDate('dd/mm/yy', endDateObj);
		console.log(startDate);
		console.log(endDate);
		
	}
	else {
		if ($('#weekDateValue').hasClass("display-none")) {
			alert("You should pick the week first!");

			return;
		}
		var startDate = $('#startDate').text();
		var endDate = $('#endDate').text();
	}
	//console.log(considerSalary);
	
	$('.preload-wrap').show();
	
	
	$.ajax({
        type: "POST",
        url: url,
        data: {startDate: startDate, endDate: endDate, considerSalary: considerSalary}
        //contentType: "application/json; charset=utf-8"
    }).done (function(data) {
    	renderTable(startDate, endDate, data);
    	/*renderHeader(startDate, endDate);
    	renderSchedule(data);*/
    	$('.preload-wrap').hide();
		alert("Advanced Roster successfully completed for the given period: \n " + startDate + " - " + endDate);
	
	}).fail (function(err) {
		$('.preload-wrap').hide();
		$("#scheduleTableBody").html("");
		if (err.responseJSON != null) {
			alert("Error occured during processing: \n " + err.responseJSON.ex);
		}
		
		console.error(err.responseText);
	});
});

//function approveSchedule() {
$(document).on('click', '#confirmApproveSchedule', function (e) {
	e.preventDefault();
	
	var startDate;
	var endDate;
	//get current date range
	if (!$('#weekDateValue').hasClass("display-none")) {
		var startStr = $('#startDate').text();
		var endStr = $('#endDate').text();
		
		var startDateObj = new Date($.datepicker.parseDate("dd/mm/yy", startStr));
		var endDateObj = new Date($.datepicker.parseDate("dd/mm/yy", endStr));
		
		startDate = $.datepicker.formatDate('dd/mm/yy', startDateObj);
		endDate = $.datepicker.formatDate('dd/mm/yy', endDateObj);
		
	}
	else if (!$('#monthDateValue').hasClass("display-none")) {
		var monthStr = $('#monthDate').text();
		monthStr = monthStr + " 01";
		console.log(monthStr);
		
		var startDateObj = $.datepicker.parseDate("M yy dd", monthStr);
		
		var year = startDateObj.getFullYear();
		var month = startDateObj.getMonth();
		var endDateObj = new Date(year, month + 1, 0)
		startDate = $.datepicker.formatDate('dd/mm/yy', startDateObj);
		endDate = $.datepicker.formatDate('dd/mm/yy', endDateObj);
		//console.log(startDate);
		//console.log(endDate);
		
	}
	else if (!$('#dayValue').hasClass("display-none")) {
		var dayStr = $('#dayDate').text();
		var dayDate = new Date($.datepicker.parseDate("dd/mm/yy", dayStr));
		
		startDate = $.datepicker.formatDate('dd/mm/yy', dayDate);
		endDate = startDate;
		
	}
	
	$('.modal-wrap').removeClass('open');
	var url = $(this).data("url");
	console.log(url);
	
	$.ajax({
        url:url,
        type: "GET"
	}).done (function(data) {
		if (data) {
			renderTable(startDate, endDate, data);
			//renderSchedule(data);
		
		}
	
	}).fail (function(err) {
	       console.error(err);
	});
	
});

function renderTable(startDate, endDate, data) {
	
	//remove scroll
	$(".fht-table-wrapper").remove();
	
	//header
	
	var table = "";
	table += "<table class=\"simple-table scrolltable\"> \n";
	table += "<thead id=\"scheduleTableHeader\"> \n";
	
	
	table += "<tr> \n";;
	
	table += "<th>Name</th> \n";
	table += "<th>Assigned location</th> \n"
	table += "<th>Reference</th> \n";
	
	var startDateObj = $.datepicker.parseDate("dd/mm/yy", startDate);
	var endDateObj = $.datepicker.parseDate("dd/mm/yy", endDate);
	
	var dateName;
	var day;
	var currentDate = new Date(startDateObj);
	var currentDateFormatted = $.datepicker.formatDate('dd/M/yy D', new Date(startDateObj));
	var today = $.datepicker.formatDate('dd/M/yy D', new Date());
	
	while (currentDate <= endDateObj) {
		
		currentDateFormatted = $.datepicker.formatDate('dd/M/yy D', currentDate);
		
		dateName = $.datepicker.formatDate("M dd", currentDate);
		
		day = $.datepicker.formatDate("D", currentDate);
		if (currentDateFormatted == today) {
			table += "<th class=\"today\">" + dateName + " " + day + "</th> \n";
		}
		else {
			table += "<th>" + dateName + " " + day + "</th> \n";
		}
		currentDate.setDate(currentDate.getDate() + 1)
	}
	table += "</tr> \n";
	//$("#scheduleTableHeader").html(table);
	table += "</thead> \n";
	
	//console.log(table);
	
	//body
	
	//var table = "";
	table += "<tbody id=\"scheduleTableBody\"> \n";
	
	var len = data.length;
	if (len == 0) {
		table += "</tbody> \n";
		table += "</table> \n";
		$(".content-inner").html(table);
		return;
	}
	var staffName;
	var location;
	var reference = "1";
	
	var isDraft = false;
	
	for (var i = 0; i < len; i++) {
		table += "<tr> \n";
		staffName = data[i].name;
		table += "<td>" + staffName + "</td> \n";
		location = data[i].location;
		table += "<td>" + location + "</td> \n";
		reference = data[i].reference;
		table += "<td>" + reference + "</td> \n";
		
		var tasks = data[i].tasks;
		if (tasks != null) {
			for (var j = 0; j < tasks.length; j++) {
				var shift = tasks[j].shift;
				var NA = false;
				if (shift != null) {
					
					var status = tasks[j].status;
					if (status == "Submitted") {
						
						status = "S";
					}
					else {
						isDraft = true;
						status = "D";
					}
					
					var shiftLetter = shift.shiftLetter;
					if (shiftLetter == "M") {
						table += "<td><span data-tip class=\"table-tag yellow\">D12 <span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "A") {
						table += "<td><span data-tip class=\"table-tag green\">A8 <span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "N") {
						table += "<td><span data-tip class=\"table-tag purple\">N12 <span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "L") {
						var leave = tasks[j].leave.type;
						table += "<td><span data-tip class=\"table-tag pink\">" + leave + "<span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "O") {
						table += "<td><span data-tip class=\"table-tag black\">OFF <span class=\"tag-mark\">" + status + "</span> \n";
					}
					//unknown shift. Mark as not assigned.
					else {
						
						table += "<td><span class=\"table-tag grey\">NA";
						NA = true;
						isDraft = true;
					}
				}
				else {
					
					table += "<td><span class=\"table-tag grey\">NA";
					NA = true;
					isDraft = true;
				}
				
				if (!NA) {
					if (tasks[j].violated) {
						table += "<span class=\"left-tag\">V</span> \n";
					}
					table += "<span class=\"custom-tooltip\"> \n";
					table += "<strong>Location:</strong>" + location + " <br/> \n";
					table += "<strong>Shift:</strong>" + shift.shift + " <br/> \n"; 
					table += "<strong>Training:</strong> ";
					var trainings = tasks[j].trainings;
					if (trainings != null) {
						var trainingsLen = trainings.length;
						for (var k = 0; k < trainingsLen; k++) {
							if (k != trainingsLen - 1) {
								table += trainings[k].name + ", ";
							}
							else {
								table += trainings[k].name;
							}
						}
					}
				}
				table += "</span></td> \n";
			}
		}
		table += "</tr> \n";
	}
	
	if (!isDraft) {
		
		$("#runEngine").addClass("disabled");
		$("#runAdvanced").addClass("disabled");
		$("#approveSchedule").addClass("disabled");
		
	}
	table += "</tbody> \n";
	table += "</table> \n";
	
	//console.log(table);
	
	//$(".content-inner").append(table);
	//$("#scheduleTableBody").html(table);
	
	//console.log($(".content-inner").html());
	
	$(".content-inner").html(table);
	
	setTimeout(function(){
		if ($('.scrolltable').length){
			$('.scrolltable').fixedHeaderTable({
				footer: false,
				cloneHeadToFoot: false,
				fixedColumns: 3,
				height:400
			});
		}
	}, 1);
	
	
	//pager
	
	$('#currentPage').val(data[0].page);
	var page = data[0].page;
	var startDisplayPage = parseInt($('#startDisplayPage').val());
	console.log("startDisplayPage: " + startDisplayPage);
	//console.log(startDisplayPage / 10);
	
	if ((Math.floor((page - 1) / 10))  != (Math.floor(startDisplayPage / 10))) {
		
		startDisplayPage = Math.floor((page - 1) / 10) * 10 + 1;
		$('#startDisplayPage').val(startDisplayPage);
		console.log("start display page changed: " + startDisplayPage);
	}
	
	$('#pageUrl').val("scheduler/get-schedule?page=" + data[0].page);
	
	console.log("Page url: " + $('#pageUrl').val());
	
	var pager = "";
	
	
	console.log(page);
	var numberOfPages = $("#numOfPages").val();
	if ((numberOfPages > 10) && (page > 10)) {
		//var pageUrl = "/roster/scheduler/get-schedule?page=" + (page - page % 10 - 9) + "";
		var pageUrl = "/roster/scheduler/get-schedule?page=" + (startDisplayPage - 10) + "";
		pager += "<li class=\"prev pager_nav pager_item\" data-page=\"" + pageUrl + "\"><a>Prev 10</a></li>\n";
	}
	if (page != 1) {
		var pageUrl = "/roster/scheduler/get-schedule?page=" + (page - 1) + "";
		pager += "<li class=\"prev pager_nav pager_item\" data-page=\"" + pageUrl + "\"><a>Prev</a></li>\n";
	}
	
	console.log("pages: " + numberOfPages);
	console.log("last display page: " + (startDisplayPage + 9));
	var end;
	if (startDisplayPage + 9 >= numberOfPages) {
		end = numberOfPages;
	}
	else {
		end = startDisplayPage + 9;
	}
	for (var i = startDisplayPage; i <= end; i++) {
		if (i == page) {
			pager += "<li class=\"current_page\"><a>" + i + "</a></li>";
		}
		else {
			var pageUrl = "/roster/scheduler/get-schedule?page=" + i + "";
			pager += "<li class=\"pager_item\" data-page=\"" + pageUrl + "\"><a>" + i + "</a></li>\n";
		}
	}
	if (page != numberOfPages) {
		var pageUrl = "/roster/scheduler/get-schedule?page=" + (page + 1) + "";
		pager += "<li class=\"next pager_nav pager_item\" data-page=\"" + pageUrl + "\"><a>Next</a></li>\n";
	}
	
	if (Math.floor(page / 10) < Math.floor(numberOfPages / 10)) {
		//var pageUrl = "/roster/scheduler/get-schedule?page=" + (page + 1 + (10 - (page - 1) % 10 )) + "";
		var pageUrl = "/roster/scheduler/get-schedule?page=" + (startDisplayPage + 10) + "";
		pager += "<li class=\"next pager_nav pager_item\" data-page=\"" + pageUrl + "\"><a>Next 10</a></li>\n";
	}
	//console.log(pager);
	$("#pager").html(pager);
	
}
/*function renderHeader(startDate, endDate) {
	
	//$('.scrolltable').fixedHeaderTable('destroy');
	$(".fht-table-wrapper").remove();
	
	
	var tableHeader = "";
	tableHeader += "<table class=\"simple-table scrolltable\"> \n";
	tableHeader += "<thead id=\"scheduleTableHeader\"> \n";
	
	
	tableHeader += "<tr> \n";;
	
	tableHeader += "<th>Name</th> \n";
	tableHeader += "<th>Assigned location</th> \n"
	tableHeader += "<th>Reference</th> \n";
	
	var startDateObj = $.datepicker.parseDate("dd/mm/yy", startDate);
	var endDateObj = $.datepicker.parseDate("dd/mm/yy", endDate);
	
	var dateName;
	var day;
	var currentDate = new Date(startDateObj);
	var currentDateFormatted = $.datepicker.formatDate('dd/M/yy D', new Date(startDateObj));
	var today = $.datepicker.formatDate('dd/M/yy D', new Date());
	
	while (currentDate <= endDateObj) {
		
		currentDateFormatted = $.datepicker.formatDate('dd/M/yy D', currentDate);
		
		dateName = $.datepicker.formatDate("M dd", currentDate);
		
		day = $.datepicker.formatDate("D", currentDate);
		if (currentDateFormatted == today) {
			tableHeader += "<th class=\"today\">" + dateName + " " + day + "</th> \n";
		}
		else {
			tableHeader += "<th>" + dateName + " " + day + "</th> \n";
		}
		currentDate.setDate(currentDate.getDate() + 1)
	}
	tableHeader += "</tr> \n";
	//$("#scheduleTableHeader").html(tableHeader);
	tableHeader += "</thead> \n";
	
	//console.log(tableHeader);
	
	$(".content-inner").html(tableHeader);
	
	console.log($(".content-inner").html());
}

function renderSchedule(data) {
	
	//$('.scrolltable').fixedHeaderTable('destroy');
	//table body
	var tableBody = "";
	tableBody += "<tbody id=\"scheduleTableBody\"> \n";
	
	var len = data.length;
	if (len == 0) {
		tableBody += "</tbody> \n";
		tableBody += "</table> \n";
		$("#scheduleTableBody").append(tableBody);
		return;
	}
	var staffName;
	var location;
	var reference = "1";
	
	var isDraft = false;
	
	for (var i = 0; i < len; i++) {
		tableBody += "<tr> \n";
		staffName = data[i].name;
		tableBody += "<td>" + staffName + "</td> \n";
		location = data[i].location;
		tableBody += "<td>" + location + "</td> \n";
		reference = data[i].reference;
		tableBody += "<td>" + reference + "</td> \n";
		
		var tasks = data[i].tasks;
		if (tasks != null) {
			for (var j = 0; j < tasks.length; j++) {
				var shift = tasks[j].shift;
				var NA = false;
				if (shift != null) {
					
					var status = tasks[j].status;
					if (status == "Submitted") {
						
						status = "S";
					}
					else {
						isDraft = true;
						status = "D";
					}
					
					var shiftLetter = shift.shiftLetter;
					if (shiftLetter == "M") {
						tableBody += "<td><span data-tip class=\"table-tag yellow\">D12 <span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "A") {
						tableBody += "<td><span data-tip class=\"table-tag green\">A8 <span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "N") {
						tableBody += "<td><span data-tip class=\"table-tag purple\">N12 <span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "L") {
						var leave = tasks[j].leave.type;
						tableBody += "<td><span data-tip class=\"table-tag pink\">" + leave + "<span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "O") {
						tableBody += "<td><span data-tip class=\"table-tag black\">OFF <span class=\"tag-mark\">" + status + "</span> \n";
					}
					//unknown shift. Mark as not assigned.
					else {
						
						tableBody += "<td><span class=\"table-tag grey\">NA";
						NA = true;
						isDraft = true;
					}
				}
				else {
					
					tableBody += "<td><span class=\"table-tag grey\">NA";
					NA = true;
					isDraft = true;
				}
				
				if (!NA) {
					if (tasks[j].violated) {
						tableBody += "<span class=\"left-tag\">V</span> \n";
					}
					tableBody += "<span class=\"custom-tooltip\"> \n";
					tableBody += "<strong>Location:</strong>" + location + " <br/> \n";
					tableBody += "<strong>Shift:</strong>" + shift.shift + " <br/> \n"; 
					tableBody += "<strong>Training:</strong> ";
					var trainings = tasks[j].trainings;
					if (trainings != null) {
						var trainingsLen = trainings.length;
						for (var k = 0; k < trainingsLen; k++) {
							if (k != trainingsLen - 1) {
								tableBody += trainings[k].name + ", ";
							}
							else {
								tableBody += trainings[k].name;
							}
						}
					}
				}
				tableBody += "</span></td> \n";
			}
		}
		tableBody += "</tr> \n";
	}
	
	if (!isDraft) {
		
		$("#runEngine").addClass("disabled");
		$("#runAdvanced").addClass("disabled");
		$("#approveSchedule").addClass("disabled");
		
	}
	tableBody += "</tbody> \n";
	tableBody += "</table> \n";
	
	console.log(tableBody);
	
	//$(".content-inner").append(tableBody);
	//$("#scheduleTableBody").html(tableBody);
	
	console.log($(".content-inner").html());
	
	setTimeout(function(){
		if ($('.scrolltable').length){
			$('.scrolltable').fixedHeaderTable({
				footer: false,
				cloneHeadToFoot: false,
				fixedColumns: 3,
				height:400
			});
		}
	}, 300);

	
	
	$('.scrolltable').fixedHeaderTable({
		footer: false,
		cloneHeadToFoot: false,
		fixedColumns: 3,
		height:400
	});
	
	
	//table body
	var tableBody = "";
	var len = data.length;
	if (len == 0) {
		$("#scheduleTableBody").html(tableBody);
		$("#scheduleTableBodyScrollable").html(tableBody);
		return;
	}
	var staffName;
	var location;
	var reference = "1";
	
	var isDraft = false;
	
	for (var i = 0; i < len; i++) {
		tableBody += "<tr> \n";
		staffName = data[i].name;
		tableBody += "<td>" + staffName + "</td> \n";
		location = data[i].location;
		tableBody += "<td>" + location + "</td> \n";
		reference = data[i].reference;
		tableBody += "<td>" + reference + "</td> \n";
		tableBody += "</tr> \n";
	}
	$("#scheduleTableBody").html(tableBody);
	
	tableBody = "";
	for (var i = 0; i < len; i++) {	
		var tasks = data[i].tasks;
		tableBody += "<tr> \n";
		if (tasks != null) {
			for (var j = 0; j < tasks.length; j++) {
				var shift = tasks[j].shift;
				var NA = false;
				if (shift != null) {
					
					var status = tasks[j].status;
					if (status == "Submitted") {
						
						status = "S";
					}
					else {
						isDraft = true;
						status = "D";
					}
					
					var shiftLetter = shift.shiftLetter;
					if (shiftLetter == "M") {
						tableBody += "<td><span data-tip class=\"table-tag yellow\">D12 <span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "A") {
						tableBody += "<td><span data-tip class=\"table-tag green\">A8 <span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "N") {
						tableBody += "<td><span data-tip class=\"table-tag purple\">N12 <span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "L") {
						var leave = tasks[j].leave.type;
						tableBody += "<td><span data-tip class=\"table-tag pink\">" + leave + "<span class=\"tag-mark\">" + status + "</span> \n";
					}
					else if (shiftLetter == "O") {
						tableBody += "<td><span data-tip class=\"table-tag black\">OFF <span class=\"tag-mark\">" + status + "</span> \n";
					}
					//unknown shift. Mark as not assigned.
					else {
						
						tableBody += "<td><span class=\"table-tag grey\">NA";
						NA = true;
						isDraft = true;
					}
				}
				else {
					
					tableBody += "<td><span class=\"table-tag grey\">NA";
					NA = true;
					isDraft = true;
				}
				
				if (!NA) {
					if (tasks[j].violated) {
						tableBody += "<span class=\"left-tag\">V</span> \n";
					}
					tableBody += "<span class=\"custom-tooltip\"> \n";
					tableBody += "<strong>Location:</strong>" + location + " <br/> \n";
					tableBody += "<strong>Shift:</strong>" + shift.shift + " <br/> \n"; 
					tableBody += "<strong>Training:</strong> ";
					var trainings = tasks[j].trainings;
					if (trainings != null) {
						var trainingsLen = trainings.length;
						for (var k = 0; k < trainingsLen; k++) {
							if (k != trainingsLen - 1) {
								tableBody += trainings[k].name + ", ";
							}
							else {
								tableBody += trainings[k].name;
							}
						}
					}
				}
				tableBody += "</span></td> \n";
			}
		}
		tableBody += "</tr> \n";
	}
	
	if (!isDraft) {
		
		$("#runEngine").addClass("disabled");
		$("#runAdvanced").addClass("disabled");
		$("#approveSchedule").addClass("disabled");
		
	}
	$("#scheduleTableBodyScrollable").html(tableBody);
}*/

function disactivateFilters() {
	
	$('.open_locations_modal').removeClass('active-filter');
	$('.staff-without-tasks').removeClass('active-filter');
	$('.rules-violated-tasks').removeClass('active-filter');
	$('.leaves-tasks').removeClass('active-filter');
	
}

function getSchedule(startDate, endDate, url) {
	disactivateFilters();
	if (url == null) {
		url = $('#dateWeek').data("url");
	}
	
	console.log(url);
	startDate = $.datepicker.formatDate('dd/mm/yy', startDate);
	endDate = $.datepicker.formatDate('dd/mm/yy', endDate);
	console.log(startDate);
	console.log(endDate);
	
	$.ajax({
        url:url,
        type: "POST",
        data: {startDate: startDate, endDate: endDate}
        	
	}).done (function(data) {
		renderTable(startDate, endDate, data)
		/*renderHeader(startDate, endDate);
		renderSchedule(data);*/
	
	}).fail (function(err) {
	       console.error(err);
	       
	});
}

/*$(document).on('click', '.staff-without-tasks', function (e) {
	e.preventDefault();
	$(this).toggleClass('active-filter');
	if ($('.staff-without-tasks').hasClass("active-filter")) {
		$("#runEngine").addClass("disabled");
		$("#runAdvanced").addClass("disabled");
		$("#approveSchedule").addClass("disabled");
		
		var url = $(this).data("url");
		console.log(url);
		
		$.ajax({
	        url:url,
	        type: "GET"
		}).done (function(data) {
			renderSchedule(data);
		
		}).fail (function(err) {
		       console.error(err);
		});
	}
	else {
		var schedule = $(this).data("schedule");
		console.log(schedule);
		renderSchedule(schedule);
	}
	
});

$(document).on('click', '#filter-locations', function (e) {
	e.preventDefault();
	//$('.open_locations_modal').toggleClass('active-filter');
	//if ($('.open_locations_modal').hasClass("active-filter")) {
		$("#runEngine").addClass("disabled");
		$("#runAdvanced").addClass("disabled");
		$("#approveSchedule").addClass("disabled");
		
		
		var url = $(this).data("url");
		console.log(url);
		
		
		var checkedValues = $('#locationsForm input:checkbox:checked').map(function() {
		    return this.value;
		}).get();
		console.log(checkedValues);
		
		$.ajax({
	        url:url,
	        type: "POST",
	        data: JSON.stringify(checkedValues),
	        contentType: "application/json; charset=utf-8"
		}).done (function(data) {
			renderSchedule(data);
	
			$('.modal-wrap').removeClass('open');
		
		}).fail (function(err) {
		       console.error(err);
		});
	//}
});

$(document).on('click', '.rules-violated-tasks', function (e) {
	e.preventDefault();
	$(this).toggleClass('active-filter');
	if ($('.rules-violated-tasks').hasClass("active-filter")) {
		$("#runEngine").addClass("disabled");
		$("#runAdvanced").addClass("disabled");
		$("#approveSchedule").addClass("disabled");
		
		var url = $(this).data("url");
		console.log(url);
		
		$.ajax({
	        url:url,
	        type: "GET"
		}).done (function(data) {
			renderSchedule(data);
		
		}).fail (function(err) {
		       console.error(err);
		});
	}
});
$(document).on('click', '.leaves-tasks', function (e) {
	e.preventDefault();
	$(this).toggleClass('active-filter');
	if ($('.leaves-tasks').hasClass("active-filter")) {
		$("#runEngine").addClass("disabled");
		$("#runAdvanced").addClass("disabled");
		$("#approveSchedule").addClass("disabled");
		
		
		var url = $(this).data("url");
		console.log(url);
		
		$.ajax({
	        url:url,
	        type: "GET"
		}).done (function(data) {
			renderSchedule(data);
		
		}).fail (function(err) {
		       console.error(err);
		});
	}
});*/


$(document).on('click', '.btn-filter', function(){
	$(this).toggleClass('active-filter');
	
	var startDate;
	var endDate;
	//get current date range
	if (!$('#weekDateValue').hasClass("display-none")) {
		var startStr = $('#startDate').text();
		var endStr = $('#endDate').text();
		
		var startDateObj = new Date($.datepicker.parseDate("dd/mm/yy", startStr));
		var endDateObj = new Date($.datepicker.parseDate("dd/mm/yy", endStr));
		
		startDate = $.datepicker.formatDate('dd/mm/yy', startDateObj);
		endDate = $.datepicker.formatDate('dd/mm/yy', endDateObj);
		
	}
	else if (!$('#monthDateValue').hasClass("display-none")) {
		var monthStr = $('#monthDate').text();
		monthStr = monthStr + " 01";
		console.log(monthStr);
		
		var startDateObj = $.datepicker.parseDate("M yy dd", monthStr);
		
		var year = startDateObj.getFullYear();
		var month = startDateObj.getMonth();
		var endDateObj = new Date(year, month + 1, 0)
		startDate = $.datepicker.formatDate('dd/mm/yy', startDateObj);
		endDate = $.datepicker.formatDate('dd/mm/yy', endDateObj);
		//console.log(startDate);
		//console.log(endDate);
		
	}
	else if (!$('#dayValue').hasClass("display-none")) {
		var dayStr = $('#dayDate').text();
		var dayDate = new Date($.datepicker.parseDate("dd/mm/yy", dayStr));
		
		startDate = $.datepicker.formatDate('dd/mm/yy', dayDate);
		endDate = startDate;
		
	}
	
	var leaveTasks = $('.leaves-tasks').hasClass("active-filter");
	var locationTasks = $('.open_locations_modal').hasClass("active-filter");
	var withoutTasks = $('.staff-without-tasks').hasClass("active-filter");
	var rulesViolated = $('.rules-violated-tasks').hasClass("active-filter");
	
	console.log("leaveTasks: " + leaveTasks);
	console.log("locationTasks: " + locationTasks);
	console.log("withoutTasks: " + withoutTasks);
	console.log("rulesViolated: " + rulesViolated);
	
	var locations = [];
	if (locationTasks) {
		locations = $('#locationsForm input:checkbox:checked').map(function() {
		    return this.value;
		}).get();
		console.log(locations);
		$('.modal-wrap').removeClass('open');
	}
	if (leaveTasks || locationTasks || withoutTasks || rulesViolated) {
		$("#runEngine").addClass("disabled");
		$("#runAdvanced").addClass("disabled");
		$("#approveSchedule").addClass("disabled");
		
	}
	else {
		$("#runEngine").removeClass("disabled");
		$("#runAdvanced").removeClass("disabled");
		$("#approveSchedule").removeClass("disabled");
	}
	
	var url = $(this).data("url");
	console.log(url);
	
	$.ajax({
        url:url,
        type: "POST",   
        dataType: "json",
        data: JSON.stringify({leaveTasks: leaveTasks, locationTasks: locationTasks, withoutTasks: withoutTasks, 
        	rulesViolated: rulesViolated, locations: locations}),
        contentType : 'application/json; charset=utf-8'
     
	}).done (function(data) {
		renderTable(startDate, endDate, data);
		//renderSchedule(data);
	
	}).fail (function(err) {
	       console.error(err);
	});
	
});


$(document).on('click', '#today', function (e) {
	e.preventDefault();
	var date = new Date();
	
	$("#runEngine").addClass("disabled");
	$("#runAdvanced").addClass("disabled");
	$("#approveSchedule").addClass("disabled");
	console.log(date);
	var startDate = $.datepicker.formatDate("dd/mm/yy", date);
	$("#dayDate").text(startDate);
	$("#weekDateValue").addClass("display-none");
	$("#monthDateValue").addClass("display-none");
	$("#dayValue").removeClass("display-none");
	getSchedule(date, date, null);
});

$(document).on('click', '#prev', function (e) {
	e.preventDefault();
	$("#runEngine").removeClass("disabled");
	$("#runAdvanced").removeClass("disabled");
	$("#approveSchedule").removeClass("disabled");
	if (!$('#weekDateValue').hasClass("display-none")) {
		var startStr = $('#startDate').text();
		var endStr = $('#endDate').text();
		/*console.log(startStr);
		console.log(endStr);*/
		var startDate = new Date($.datepicker.parseDate("dd/mm/yy", startStr));
		var endDate = new Date($.datepicker.parseDate("dd/mm/yy", endStr));
		/*console.log(startDate);
		console.log(endDate);*/
		startDate.setDate(startDate.getDate() - 7);
		endDate.setDate(endDate.getDate() - 7);
		/*console.log(startDate);
		console.log(endDate);*/
		var startDateFinal = $.datepicker.formatDate('dd/mm/yy', startDate);
		var endDateFinal = $.datepicker.formatDate('dd/mm/yy', endDate);
		$('#startDate').text(startDateFinal);
		$('#endDate').text(endDateFinal);
		getSchedule(startDate, endDate, null);
		
	}
	else if (!$('#monthDateValue').hasClass("display-none")) {
		var monthStr = $('#monthDate').text();
		monthStr = monthStr + " 01";
		console.log(monthStr);
		
		var startDateObj = $.datepicker.parseDate("M yy dd", monthStr);
		
		var year = startDateObj.getFullYear();
		var month = startDateObj.getMonth();
		startDateObj = new Date(year, month - 1, 1)
		var endDateObj = new Date(year, month, 0)
		var startDate = $.datepicker.formatDate('dd/mm/yy', startDateObj);
		var endDate = $.datepicker.formatDate('dd/mm/yy', endDateObj);
		console.log(startDate);
		console.log(endDate);
		var monthDateFinal = $.datepicker.formatDate('M yy', startDateObj);
		$('#monthDate').text(monthDateFinal);
		getSchedule(startDateObj, endDateObj, null);
	}
	else if (!$('#dayValue').hasClass("display-none")) {
		var dayStr = $('#dayDate').text();
		var dayDate = new Date($.datepicker.parseDate("dd/mm/yy", dayStr));
		dayDate.setDate(dayDate.getDate() - 1);
		var dayDateFinal = $.datepicker.formatDate('dd/mm/yy', dayDate);
		$('#dayDate').text(dayDateFinal);
		getSchedule(dayDate, dayDate, null);
	}
	
});

$(document).on('click', '#next', function (e) {
	e.preventDefault();
	$("#runEngine").removeClass("disabled");
	$("#runAdvanced").removeClass("disabled");
	$("#approveSchedule").removeClass("disabled");
	if (!$('#weekDateValue').hasClass("display-none")) {
		var startStr = $('#startDate').text();
		var endStr = $('#endDate').text();
		
		var startDate = new Date($.datepicker.parseDate("dd/mm/yy", startStr));
		var endDate = new Date($.datepicker.parseDate("dd/mm/yy", endStr));
		startDate.setDate(startDate.getDate() + 7);
		endDate.setDate(endDate.getDate() + 7);
		var startDateFinal = $.datepicker.formatDate('dd/mm/yy', startDate);
		var endDateFinal = $.datepicker.formatDate('dd/mm/yy', endDate);
		$('#startDate').text(startDateFinal);
		$('#endDate').text(endDateFinal);
		getSchedule(startDate, endDate, null);
		
	}
	else if (!$('#monthDateValue').hasClass("display-none")) {
		var monthStr = $('#monthDate').text();
		monthStr = monthStr + " 01";
		console.log(monthStr);
		
		var startDateObj = $.datepicker.parseDate("M yy dd", monthStr);
		
		var year = startDateObj.getFullYear();
		var month = startDateObj.getMonth();
		startDateObj = new Date(year, month + 1, 1)
		var endDateObj = new Date(year, month + 2, 0)
		var startDate = $.datepicker.formatDate('dd/mm/yy', startDateObj);
		var endDate = $.datepicker.formatDate('dd/mm/yy', endDateObj);
		console.log(startDate);
		console.log(endDate);
		var monthDateFinal = $.datepicker.formatDate('M yy', startDateObj);
		$('#monthDate').text(monthDateFinal);
		getSchedule(startDateObj, endDateObj, null);
	}
	else if (!$('#dayValue').hasClass("display-none")) {
		var dayStr = $('#dayDate').text();
		var dayDate = new Date($.datepicker.parseDate("dd/mm/yy", dayStr));
		dayDate.setDate(dayDate.getDate() + 1);
		var dayDateFinal = $.datepicker.formatDate('dd/mm/yy', dayDate);
		$('#dayDate').text(dayDateFinal);
		getSchedule(dayDate, dayDate, null);
	}
	
});

$(document).on('click', '.pager_item', function (e) {
	e.preventDefault();
	var url = $(this).data("page");
	console.log(url);
	
	var startDate;
	var endDate;
	//get current date range
	if (!$('#weekDateValue').hasClass("display-none")) {
		var startStr = $('#startDate').text();
		var endStr = $('#endDate').text();
		
		startDate = new Date($.datepicker.parseDate("dd/mm/yy", startStr));
		endDate = new Date($.datepicker.parseDate("dd/mm/yy", endStr));
		
	}
	else if (!$('#monthDateValue').hasClass("display-none")) {
		var monthStr = $('#monthDate').text();
		monthStr = monthStr + " 01";
		console.log(monthStr);
		
		startDate = $.datepicker.parseDate("M yy dd", monthStr);
		
		var year = startDate.getFullYear();
		var month = startDate.getMonth();
		endDate = new Date(year, month + 1, 0)
		
		
	}
	else if (!$('#dayValue').hasClass("display-none")) {
		var dayStr = $('#dayDate').text();
		var startDate = new Date($.datepicker.parseDate("dd/mm/yy", dayStr));
		
		endDate = startDate;
		
	}
	
	getSchedule(startDate, endDate, url);
	
});

