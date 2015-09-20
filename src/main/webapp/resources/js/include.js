jQuery(document).ready(function ($) {


	if ($('.connectedSortable').length) {
		$("#sortable1, #sortable2").sortable({
			connectWith: ".connectedSortable"
		}).disableSelection();
	}

	$(document).on('click', '.connectedSortable li', function (e) {
		e.preventDefault();
		$(this).toggleClass('active');
	});

	$(document).on('click', '.rules-sort .rules-table tr', function (e) {
		e.preventDefault();
		if ($(this).hasClass('active')) {
			$(this).removeClass('active');
		}
		else {
			$(this).parents('table').find('.active').removeClass('active');
			$(this).addClass('active');
		}

	});
	
	
	$(window).on('load', function () {
		if ($('.rules-table select').length) {
			setTimeout(function(){
				$('.rules-table select').each(function () {
					$(this).attr('data-value', $(this).val());
				});
			},300);
		}
	});

	

	$(document).on('change','.rules-table select', function () {
		$(this).attr('data-value', $(this).val());
		});
	
	function setSelectValue(a) {
	   a.find('select').each(function () {
	      if ($(this).attr('data-value') != 0) {
	         var val = $(this).attr('data-value');
	      }
	      else {
	         var val = $(this).val();
	      }
	      $(this).val(val);
	   })

	}

		$(document).on('click', '.arr-btn', function (e) {
		   e.preventDefault();

		   var parent = $(this).parents('.change-options-container'),
		      parent2 = $(this).parents('.rules-sort'),
		      elements, itemIndex = 0, listlength;

		   if ($(this).hasClass('arr-btn-right') && parent.find('.list-left .active').length) {
		      elements = parent.find('.list-left .active').clone();
		      parent.find('.list-right').append(elements);
		      parent.find('.list-left .active').remove();
		      parent.find('.active').removeClass('active');

		   }
		   if ($(this).hasClass('arr-btn-left') && parent.find('.list-right .active').length) {
		      elements = parent.find('.list-right .active').clone();
		      parent.find('.list-left').append(elements);
		      parent.find('.list-right .active').remove();
		      parent.find('.active').removeClass('active');


		   }

		   if ($(this).hasClass('arr-btn-top') && parent2.find('.rules-table .active').length) {
		      itemIndex = parent2.find('.rules-table .active').index();
		      if (itemIndex > 0) {
		         elements = parent2.find('.rules-table .active').clone();
		         parent2.find('.rules-table .active').remove();
		         parent2.find('.rules-table tbody tr').eq(itemIndex - 1).before(elements);
		         setSelectValue(parent2.find('.rules-table .active'));
		      }

		   }
		   if ($(this).hasClass('arr-btn-bottom') && parent2.find('.rules-table .active').length) {
		      elements = parent2.find('.rules-table .active').clone();
		      listlength = parent2.find('.rules-table tbody tr').length;
		      itemIndex = parent2.find('.rules-table .active').index();
		      console.log(listlength);
		      if (itemIndex != listlength - 1) {
		         elements = parent2.find('.rules-table .active').clone();
		         parent2.find('.rules-table .active').remove();
		         parent2.find('.rules-table tbody tr').eq(itemIndex).after(elements);
		         setSelectValue(parent2.find('.rules-table .active'));
		      }


		   }

		});


	function tabs() {

		$(".tab-item").click(function () {
			
			if (this.id == "rulesTab") {		
				//rules tab (get and display rules from selected options)
					
				var url = $(this).data("url");
				console.log("URL:" + url);
				$.ajax({
			        type: "GET",
			        url: url
			    }).done (function(data) {
			    	
			    	var table = "";
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
						
						
						//console.log("criteria" + data[i].criteria.criteriaString);
						var criteriaVal = data[i].criteria.criteriaString;
						
						var criteriaHtml = "";
						$("#criteria0").find('option').each(function(i) {
						    
							if ($(this).text() == criteriaVal) {
								criteriaHtml += "<option value=\"" + $(this).val() + "\" selected>" + criteriaVal + "</option> \n"
							}
							else {
								
								criteriaHtml += "<option value=\"" + $(this).val() + "\">" + $(this).html() + "</option> \n";
							}
						     
						});
						
						//console.log(criteriaHtml);
						
						table += criteriaHtml;
						table += "</select> \n";
						table += "</td>";
						
						table += "<td> \n";
						var reference = data[i].reference.reference;
						//console.log("reference:" + reference);
						table += reference + "\n";
						table += "</td> \n";
						
						table += "<td> \n";
						table += "<select id=\"rule-type" + i + "\">";
						
						var ruleVal = data[i].type;
						var ruleHtml = "";
						$("#rule-type0").find('option').each(function(i) {
						    
							if ($(this).text() == ruleVal) {
								ruleHtml += "<option value=\"" + $(this).val() + "\" selected>" + ruleVal + "</option> \n"
							}
							else {
								
								ruleHtml += "<option value=\"" + $(this).val() + "\">" + $(this).html() + "</option> \n";
							}
						     
						});
						//console.log(ruleHtml);
						
						table += ruleHtml;
						table += "</select> \n";
						table += "</td>";
						
						table += "</tr>";
					}
					//console.log(table);
					$("#rules-table-body").html(table);

					if ($('#rules-table-body select').length) {
						$('#rules-table-body select').each(function () {
							$(this).attr('data-value', $(this).val());
						});
					}
				
				}).fail (function(err) {
					console.error(err);
				});
					
			}
			
			
			$(this).parents('.tabs-container').find(".tab-content").hide().css('opacity', '0');
			$(this).parents('.tabs-container').find(".tab-item").removeClass("active");
			$(this).addClass("active");

			var id = $(this).find('a').attr('data-href');
			$('.tab-content[data-id="' + id + '"]').show().css({"height": "100%"}).animate({
				'opacity': '1'
			}, 500);
			
			
			return false;

		});
	}

	tabs();


	function showHideModal(trig,targ){
		$(trig).on('click touchstart', function(e){
			e.preventDefault();
			$(targ).addClass('open');
		});

		$(this).keydown(function (eventObject) {
			if (eventObject.which == 27){
				$(targ).removeClass('open');
			}
		});



		$('.close-modal').click(function(){
			$(targ).removeClass('open');
		});


		$(targ).on('click touchstart', function (e) {
			if (!$(e.target).parents().hasClass('modal-wrap') && !$(e.target).hasClass('modal-wrap')) {
				$(targ).removeClass('open');
			}

		});
	}

	showHideModal('.open_login_modal','.modal-login');
	showHideModal('.open_adv_options', '.modal-advanced');
	
	showHideModal('.show_approve_modal', '.modal-approve');

	jQuery(document).on('click touchstart', '[role="toggle_forgot_password_form"]', function (e) {
		e.preventDefault();
		jQuery(this).parents('.login-form-block').find('.flipper').toggleClass('rotate');
	});

	
	
	//SCHEDULER PAGE
		

	$(".day-picker").datepicker({
		dateFormat : "dd/mm/yy",
		firstDay   : 1,
		onSelect   : function () {
			var date = new Date();
			//var date = $(this).datepicker('getDate');
			$("#runEngine").addClass("disabled");
			$("#runAdvanced").addClass("disabled");
			$("#approveSchedule").addClass("disabled");
			getSchedule(date, date);
		}
	});

	if($('.pick-month').length){
		$('.pick-month').datepicker({
			changeMonth: true,
			changeYear: true,
			showButtonPanel: true,
			dateFormat: "M yy",
			beforeShow: function (e, t) {
				$(this).datepicker("hide");
	
				
				$("#ui-datepicker-div").addClass("hide-calendar");
				$("#ui-datepicker-div").addClass('MonthDatePicker');
				$("#ui-datepicker-div").addClass('HideTodayButton');
			},
			onClose: function(dateText, inst){
				$("#runEngine").removeClass("disabled");
				$("#runAdvanced").removeClass("disabled");
				$("#approveSchedule").removeClass("disabled");
	
				var n = Math.abs($("#ui-datepicker-div .ui-datepicker-month :selected").val());
				var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
				var currentMonth = new Date(year, n, 1);
				
				var formatMonth = $.datepicker.formatDate('M yy', currentMonth);
				$("#dayValue").addClass("display-none");
				$("#weekDateValue").addClass("display-none");
				$("#monthDateValue").removeClass("display-none");
				$("#monthDate").text(formatMonth);
				
				
				//$(this).datepicker("setDate", currentMonth);
				$("#ui-datepicker-div").removeClass("hide-calendar");
				$("#ui-datepicker-div").removeClass('MonthDatePicker');
				var startDate = new Date(year, n, 1);
				var endDate = new Date(year, n + 1, 0);
				getSchedule(startDate, endDate);
			}
		});
	}

	$('.date-picker').datepicker( {
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true,
		dateFormat: 'MM yy',
		onClose: function(dateText, inst) {
			var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
			var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
			$(this).datepicker('setDate', new Date(year, month, 1));
		}
	});
	
	
	$(function () {
		var startDate;
		var endDate;

		var selectCurrentWeek = function () {
			window.setTimeout(function () {
				$('.week-picker').find('.ui-datepicker-current-day a').addClass('ui-state-active')
			}, 1);
		}

		$('.week-picker').datepicker({
			showOtherMonths  : true,
			selectOtherMonths: true,
			dateFormat       : "dd/mm/yy",
			firstDay		 : 1,
			onSelect         : function (dateText, inst) {
				
				var date = $(this).datepicker('getDate');
				startDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - date.getDay() + 1);
				endDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - date.getDay() + 7);
				var dateFormat = inst.settings.dateFormat || $.datepicker._defaults.dateFormat;
				$('#startDate').text($.datepicker.formatDate(dateFormat, startDate, inst.settings));
				$('#endDate').text($.datepicker.formatDate(dateFormat, endDate, inst.settings));
				
				$("#weekDateValue").removeClass("display-none");
				$("#monthDateValue").addClass("display-none");
				$("#dayValue").addClass("display-none");
				
				$("#runEngine").removeClass("disabled");
				$("#runAdvanced").removeClass("disabled");
				$("#approveSchedule").removeClass("disabled");
				
				getSchedule(startDate, endDate);
				selectCurrentWeek();
			},
			beforeShowDay    : function (date) {
				var cssClass = '';
				if (date >= startDate && date <= endDate)
					cssClass = 'ui-datepicker-current-day';
				return [true, cssClass];
			},
			onChangeMonthYear: function (year, month, inst) {
				selectCurrentWeek();
			}
		});

		$('.week-picker .ui-datepicker-calendar tr').on('mousemove', function () {
			$(this).find('td a').addClass('ui-state-hover');
		});
		$('.week-picker .ui-datepicker-calendar tr').on('mouseleave', function () {
			$(this).find('td a').removeClass('ui-state-hover');
		});
	});	

	$(document).on('click', '.cancel-modal', function (e) {
		$('.modal-wrap').removeClass('open');
	});
	
	
	
	/*$(document).on('click', '.btn-filter', function(){
		$(this).toggleClass('active-filter');
	});*/
	
	
	
	$(document).on('click', '.open_locations_modal', function(){
		$('.open_locations_modal').toggleClass('active-filter');
		if ($('.open_locations_modal').hasClass("active-filter")) {
			console.log("Locations is active: " + $('.open_locations_modal').hasClass("active-filter"));
			$('.modal-locations').addClass('open');
			//showHideModal('.open_locations_modal', '.modal-locations');
		}
		else {
			
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
			
			var url = $('#filter-locations').data("url");
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
			
			}).fail (function(err) {
			       console.error(err);
			});
		}
		
		
	});
	
	/*$(document).on('click', '#approveSchedule', function (e) {
		console.log("Approve schedule");
		$("#dialog-confirm").html("Do you want to approve roster for the selected period?");

	    // Define the Dialog and its properties.
	    $("#dialog-confirm").dialog({
	        resizable: false,
	        modal: true,
	        title: "Approval Confirmation",
	        height: 250,
	        width: 400,
	        buttons: {
	            "Yes": function () {
	                $(this).dialog('close');
	                
	            },
	                "No": function () {
	                $(this).dialog('close');
	                
	            }
	        }
	    });
	});*/
	if($('.scrolltable').length){
		$('.scrolltable').fixedHeaderTable({
			footer: false,
			cloneHeadToFoot: false,
			fixedColumns: 3,
			height:400
		});
	}
	
});