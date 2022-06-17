$(document).ready(function() {
	$("#channelForm").validate({
		errorElement: 'p',
		rules: {
			channelTitle: {
				maxlength: 50,
				required: true
			},
			channelDescription: {
				required: true
			}
		},
		messages: {
			channelTitle: {
				maxlength: "Group Title is limited to 50 characters",
				required: "Channel Title required"
			},
			channelDescription: {
				required: "Channel description required"
			}
		}
	});// end of validation rules for request form
}); // end of ready function