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
						
						var type = $("#rule-type0").html();
						//console.log("Criteria:" + criteria);
						table += type;
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
	showHideModal('.open_locations_modal', '.modal-locations');

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
			dateFormat: "mm/yy",
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
				$(this).datepicker("setDate", new Date(year, n,1));
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

	$(document).on('click', '#cancel-filter-locations', function (e) {
		$('.modal-wrap').removeClass('open');
	});
	
	$(document).on('click', '#cancelAdvancedEngine', function (e) {
		$('.modal-wrap').removeClass('open');
	});
	
	
	
});