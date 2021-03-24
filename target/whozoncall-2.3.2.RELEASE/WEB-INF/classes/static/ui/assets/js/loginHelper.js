var payload = {};
$("#loginerror").hide();

$("#signinbtn").click(function(event){

      event.preventDefault();

  
  payload.username = $("#username").val();
  payload.password = $("#password").val();
  payload.company = $("#company").val();

  
   $.ajax({
       url: '/v1/Login',
       type: 'post',
       contentType: 'application/json',
       success: function (data) {
           console.log(' success ');
           window.location.href = "/ui/index.html";
       },
       error: function(data) {
           
    	   $("#loginerror").show();
    	   console.log(' error		 ');
       },
       data: JSON.stringify(payload)
   });
	
  	
});