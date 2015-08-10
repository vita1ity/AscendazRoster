
//login
$(document).ready(function() {
	$("#loginform").submit(function(e){
		var username = $(this).find('.first').val();
		console.log(username);
		var password = $(this).find('.last').val();
		console.log(password);
		$.ajax({
			   url: $(this).attr("action"),
               type: "POST",
               data: {username: username, password: password},
        }).done (function(response) {
        	console.log(response);
        	if (response == "success") {
        		$("#msg").html("<span class=\"loginFormSuccess\">Login was successful!</span>");
        		$(".modal-login").removeClass('open');
        		$(".loginButton").addClass("notDisplayLogin");
        		$("#loginUserSuccessMessage").html("<span class=\"loginFormSuccess\">User Login successfully</span>");
        		console.log("SUCCESS");
        	}
        	else {
        		$("#msg").html("<span class=\"loginFormError\">Your login attempt was not successful</br> " +
        				"Reason: Invalid username or password</span>");
        		console.log("FAIL");
        	}
        }).fail (function(err) {
        	
	    	   console.log(err);
	    	});
		 e.preventDefault();
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
		console.log(list);
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
		console.log(list);
		$("#sortable2").html(list);
	
	}).fail (function(err) {
	       console.error(err);
	});
	
});


//save values of selected and available options in the db and get selected options
$(document).on('click', '#save_setup', function (e) {
	
	e.preventDefault();

	$('.hidden_options').slideUp(200);
	
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
    
    //selected options
    
    var selecOptionsArray = new Array();
    var option = new Object();
    var selecurl = $(this).data("saveselec");
    
    console.log(JSON.stringify(selecOptionsArray));
    
    $('#sortable2').delay(1000);
    
    $('#sortable2').find('li').each(function(i){
    	var current = $(this);
    	option = current.text();
    	
    	selecOptionsArray[i] = option;
    	i++;
    });
    
    
    $.ajax({
        type: "POST",
        url: selecurl,
        data: JSON.stringify(selecOptionsArray),
        contentType: "application/json; charset=utf-8"
    }).done (function(data) {
    	console.log(data);
	
	}).fail (function(err) {
		console.error(err);
	});
    
    var selectedOptions = $('#sortable2').html();
    
    $("#options-list").html(selectedOptions);
    
});


//save rules in the db
$(document).on('click', '#save_rules', function (e) {
	
	e.preventDefault();
	
    var rulesArray = new Array();
    
    var url = $(this).data("url");
    
    $('#rules-table-body').find('tr').each(function(i){
    	var rule = "";
    	$(this).find('td').each(function(j){
    		//console.log($(this).html());
    		var html = $(this).html();
    		//console.log("this: " + $(html).prop("tagName"));
    		
    		if ($(html).prop("tagName") == "SELECT") {
    			
    			var id = $(html).attr("id");
    			//console.log(id);
    			var idIndex = id.slice(-1);
    			console.log("index: " + idIndex);
    			if (id.indexOf("rule-type") == 0) {
    			//if (id == ("rule-type" + i)) {
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
    	/*var current = $(this);
    	
    	option = current.text();
    	
    	avalOptionsArray[i] = option;
    	i++;*/
    });
    
   // console.log(JSON.stringify(avalOptionsArray));
    
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(rulesArray),
        contentType: "application/json; charset=utf-8"
    }).done (function(data) {
    	console.log(data);
	
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
	
	$.ajax({
        type: "POST",
        url: url,
        data: {startDate: startDate, endDate: endDate},
        //contentType: "application/json; charset=utf-8"
    }).done (function(data) {
    	//TODO
    	//table header
    	var tableHeader = "";
    	tableHeader += "<tr> \n";;
    	tableHeader += "<th>Name</th> \n";
    	tableHeader += "<th>Assigned location</th> \n"
    	tableHeader += "<th>Reference</th> \n";
    	var startDateObj = $.datepicker.parseDate("dd/mm/yy", startDate);
    	var endDateObj = $.datepicker.parseDate("dd/mm/yy", endDate);
    	
    	var dateName;
    	var day;
    	var currentDate = new Date(startDateObj);
    	while (currentDate <= endDateObj) {
    		//console.log(startDateObj);
    		
    		dateName = $.datepicker.formatDate("M dd", currentDate);
    		day = $.datepicker.formatDate("D", currentDate);
    		tableHeader += "<th>" + dateName + "<br>" + day + "</th> \n";
    		currentDate.setDate(currentDate.getDate() + 1)
    	}
    	tableHeader += "</tr> \n";
    	$("#scheduleTableHeader").html(tableHeader);
    	
    	//table body
    	var tableBody = "";
    	var len = data.length;
    	
    	var staffName;
    	var location;
    	var reference = "1";
    	
		for (var i = 0; i < len; i++) {
			tableBody += "<tr> \n";
			staffName = data[i].staff.name;
			console.log(staffName);
			tableBody += "<td>" + staffName + "</td> \n";
			if (data[i].task != null) {
				location = data[i].task.location.location;
			}
			else {
				var locationSet = data[i].staff.staffLocationSet;
				var locationSetLength = locationSet.length;
				for (var j = 0; j < locationSetLength; j++) {
					var effectDate = new Date(locationSet[j].effectiveDate);
					var expireDate = new Date(locationSet[j].expireDate);
					var type = locationSet[j].type;
					console.log(effectDate);
					console.log(expireDate);
					console.log(type);
					if (type == "Assigned") {
						if (effectDate < startDateObj && expireDate > startDateObj) {
							
							location = locationSet[j].location.location;
							console.log("Valid:" + location);
							break;
						} 
					}
				}
			}
			
			tableBody += "<td>" + location + "</td> \n";
			tableBody += "<td>" + reference + "</td> \n";
			
			currentDate = new Date(startDateObj);
			//from monday to sunday
			daysOfWeek: for (var j = 0; j < 7; j++) {
				
				console.log("START DATE: " + currentDate);
				while (data[i].staff.name == staffName) {
					var scheduleDate = new Date(data[i].date);
					console.log(currentDate + ",   " + scheduleDate);
					if (currentDate.getTime() < scheduleDate.getTime()) {
						//TODO blank task
						tableBody += "<td><span class=\"table-tag not-assigned\">NA</span></td> \n";
						currentDate.setDate(currentDate.getDate() + 1);
						continue daysOfWeek;
					}
					//there is no task for current date
					else if (currentDate.getTime() > scheduleDate.getTime()) {
						i++;
						continue;
						
					}	
					//dates are equal
					else {
						console.log("EQUAL:" + currentDate + ",   " + scheduleDate);
						//assign attributes for the day
						console.log("SHIFT: " + data[i].shift.shift);
						var shiftLetter = data[i].shift.shiftLetter;
						if (shiftLetter == "M") {
							tableBody += "<td><span class=\"table-tag yellow\">D12 <span class=\"tag-mark\">D</span></span></td> \n";
						}
						else if (shiftLetter == "A") {
							tableBody += "<td><span class=\"table-tag green\">A8 <span class=\"tag-mark\">D</span></span></td> \n";
						}
						else if (shiftLetter == "N") {
							tableBody += "<td><span class=\"table-tag purple\">N12 <span class=\"tag-mark\">D</span></span></td> \n";
						}
						else if (shiftLetter == "L") {
							tableBody += "<td><span class=\"table-tag pink\">AL <span class=\"tag-mark\">D</span></span></td> \n";
						}
						else if (shiftLetter == "O") {
							tableBody += "<td><span class=\"table-tag black\">OFF <span class=\"tag-mark\">D</span></span></td> \n";
						}
						currentDate.setDate(currentDate.getDate() + 1);
						continue daysOfWeek;
					}
					
				}
				
			}
			
			tableBody += "</tr>";
			
		}
    	
		$("#scheduleTableBody").html(tableBody);
		
    	/*var table = "";
		var len = data.length;
		for (var i = 0; i < len; i++) {
			table += "<tr> \n";
			
			table += "<td> \n";
			var setupOption = data[i].setupOption.setupOption;
			//console.log("setup option:" + setupOption);
			table += setupOption + "\n";
			table += "</td> \n";
			
			table += "<td> \n";
			table += "<select id=\"criteria" + i + "\">";
			if (i == 0) {
				table += "<select id=\"criteria\">";
			}
			else {
				table += "<select>";
			}
			var criteria = $("#criteria0").html();
			//console.log("Criteria:" + criteria);
			table += criteria;
			table += "</select> \n";
			table += "</td>";
			
			table += "<td> \n";
			var reference = data[i].reference.reference;
			//console.log("reference:" + reference);
			table += reference + "\n";
			table += "</td> \n";
			
			table += "<td> \n";
			table += "<select id=\"rule-type" + i + "\">";
			if (i == 0) {
				table += "<select id=\"rule-type\">";
			}
			else {
				table += "<select>";
			}
			var type = $("#rule-type0").html();
			//console.log("Criteria:" + criteria);
			table += type;
			table += "</select> \n";
			table += "</td>";
			
			table += "</tr>";
		}
		//console.log(table);
		$("#rules-table-body").html(table);*/
	
	}).fail (function(err) {
		console.error(err);
	});
});
