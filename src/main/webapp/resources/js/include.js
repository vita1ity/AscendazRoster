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

/*	$(document).on('click', '.open_hidden_options', function (e) {
		e.preventDefault();
		$('.hidden_options').slideToggle(200);
	});*/

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
			if(itemIndex > 0){
				elements = parent2.find('.rules-table .active').clone();
				parent2.find('.rules-table .active').remove();
				parent2.find('.rules-table tbody tr').eq(itemIndex - 1 ).before(elements);

			}

		}
		if ($(this).hasClass('arr-btn-bottom') && parent2.find('.rules-table .active').length) {
			elements = parent2.find('.rules-table .active').clone();
			listlength = parent2.find('.rules-table tbody tr').length;
			itemIndex = parent2.find('.rules-table .active').index();
			console.log(listlength);
			if(itemIndex != listlength - 1){
				elements = parent2.find('.rules-table .active').clone();
				parent2.find('.rules-table .active').remove();
				parent2.find('.rules-table tbody tr').eq(itemIndex ).after(elements);

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
						/*if (i == 0) {
							table += "<select id=\"criteria\">";
						}
						else {
							table += "<select>";
						}*/
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
						/*if (i == 0) {
							table += "<select id=\"rule-type\">";
						}
						else {
							table += "<select>";
						}*/
						var type = $("#rule-type0").html();
						//console.log("Criteria:" + criteria);
						table += type;
						table += "</select> \n";
						table += "</td>";
						
						table += "</tr>";
					}
					//console.log(table);
					$("#rules-table-body").html(table);
				
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

	jQuery(document).on('click touchstart', '[role="toggle_forgot_password_form"]', function (e) {
		e.preventDefault();
		jQuery(this).parents('.login-form-block').find('.flipper').toggleClass('rotate');
	});




});