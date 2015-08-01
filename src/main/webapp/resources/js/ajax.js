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


//save values of selected and available options in the db
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
    
    $('#sortable2').find('li').each(function(i){
    	var current = $(this);
    	option = current.text();
    	
    	selecOptionsArray[i] = option;
    	i++;
    });
    
    console.log(JSON.stringify(selecOptionsArray));
    
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
    
});
