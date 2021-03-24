var payload = {};

$("#sigupbtn").click(function(event){

      event.preventDefault();

  
  payload.username = $("#username").val();
  payload.password = $("#password").val();
  payload.company = $("#company").val();
  payload.email = $("#email").val();

  
   $.ajax({
       url: '/v1/Register',
       type: 'post',
       contentType: 'application/json',
       success: function (data) {
           console.log(' success ');
           window.location.href = "/ui/index.html";
       },
       error: function(data) {
           
    	   console.log(' error		 ');
       },
       data: JSON.stringify(payload)
   });
	
  	event.stopPropagation();
});