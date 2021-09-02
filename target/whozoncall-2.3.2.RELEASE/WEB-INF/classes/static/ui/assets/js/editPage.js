$(document).ready(function() {
	$('#PDSchedulesList').select2({
		  placeholder: "Select onCalls",
		  templateResult: formatStatePD,
		  templateSelection:formatStatePD,
		  dropdownCssClass : 'bigdrop'
	});
	
	$('#slackChannels').select2({
		  placeholder: "Pick a Channel to publish to",
		  templateResult: formatStateSlack,
		  templateSelection:formatStateSlack,
		  dropdownCssClass : 'bigdrop'
	});
	
	
	$('#slackChannels').val($('#selectedChannel').val());
	$('#slackChannels').trigger('change');

	
	$('#PDSchedulesList').val($('#selectedOncalls').val().split(","));
	$('#PDSchedulesList').trigger('change');
	
});



function formatStatePD (state) {
	  if (!state.id) {
	    return state.text;
	  }
	  var baseUrl = "assets/images/PD/PD_logo.png";
	  var $state = $(
	    '<span style="font-size:18px;"><img width="20" height="20"  src="'+baseUrl+'" class="img-flag" /> ' + state.text + '</span>'
	  );
	  return $state;
	};

	
	function formatStateSlack (state) {
		  if (!state.id) {
		    return state.text;
		  }
		  var baseUrl = "assets/images/slack/Slack_small_logo.png";
		  var $state = $(
		    '<span style="font-size:18px;"><img width="20" height="20"  src="'+baseUrl+'" class="img-flag" /> ' + state.text + '</span>'
		  );
		  return $state;
		};
	

$("#saveIntegration").click(function(event){

		      event.preventDefault();

		  
		      
		      var channelIdsArr = $('#slackChannels').select2('data').map(a => a.id).join();
		  	var onCallIdsArr = $('#PDSchedulesList').select2('data').map(a => a.id).join();
		  	
		  	var data = {};
		  	data.id = $("#integrationId").val();
		  	data.channelId = channelIdsArr;
		  	data.onCallIds = onCallIdsArr;
		  	
		  	$.ajax({
		  	       url: '/v1/editIntegration',
		  	       type: 'post',
		  	       contentType: 'application/json',
		  	       success: function (data) {
		  	           console.log(' success ');
		  	           window.location.href = "/ui/integrations";
		  	       },
		  	       error: function(data) {
		  	           
		  	    	   console.log(' error		 ');
		  	    	 window.location.href = "/ui/integrations";
		  	       },
		  	       data: JSON.stringify(data)
		  	   });
			
		  	
});