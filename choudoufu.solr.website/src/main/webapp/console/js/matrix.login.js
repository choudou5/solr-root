
$(document).ready(function(){

	var login = $('#loginform');
	var recover = $('#recoverform');
	var speed = 400;

    $("#loginform").validate({
		rules:{
			required:{
				required:true
			},
			pwd:{
				required: true,
				minlength:6,
				maxlength:20
			},
		},
		errorClass: "help-inline",
		errorElement: "span",
		highlight:function(element, errorClass, validClass) {
			$(element).parents('.control-group').addClass('error');
		},
		unhighlight: function(element, errorClass, validClass) {
			$(element).parents('.control-group').removeClass('error');
			$(element).parents('.control-group').addClass('success');
		}
	});
		
	$('#to-recover').click(function(){
		
		$("#loginform").slideUp();
		$("#recoverform").fadeIn();
	});
	$('#to-login').click(function(){
		
		$("#recoverform").hide();
		$("#loginform").fadeIn();
	});
	
	
	$('#to-login').click(function(){
	
	});
    
    if($.browser.msie == true && $.browser.version.slice(0,3) < 10) {
        $('input[placeholder]').each(function(){
	        var input = $(this);       
	        $(input).val(input.attr('placeholder'));
	               
	        $(input).focus(function(){
	             if (input.val() == input.attr('placeholder')) {
	                 input.val('');
	             }
	        });
	       
	        $(input).blur(function(){
	            if (input.val() == '' || input.val() == input.attr('placeholder')) {
	                input.val(input.attr('placeholder'));
	            }
	        });
	    });
    }
        
});