$(document).ready(function() {
	
	
	var payload = {};


$(".removeIntegrationBtn").click(function(event){

    event.preventDefault();
    
    if (confirm('Are you sure you want to  delete this integration ?')) {
    	  
    	  console.log('Will be Removed');
    	} else {

    	 return;
    	}
  var id = this.id.split("-")[1];
 

  
   $.ajax({
       url: '/ui/RemoveIntegration/'+id,
       type: 'post',
       contentType: 'application/json',
       success: function (data) {
           console.log(' success ');
           window.location.href = "/ui/integrations";
       },
       error: function(data) {
           
    	   console.log(' error		 ');
       },
       data: JSON.stringify(payload)
   });
	
  	event.stopPropagation();
});




$('button.navbar-toggler').click(function(event){

    event.preventDefault();
    
    var presentVal = ($('#sideNav').val()=='true');	

	$('#sideNav').val(!presentVal);
    
   
    $.ajax({
       url: '/ui/sideNavToggle?enabled='+!presentVal,
       type: 'GET',
       contentType: 'application/json',
       success: function () {
           console.log(' success ');
       },
       error: function() {
           
    	   console.log(' error		 ');
       }
   });
	
  	event.stopPropagation();
});

});

