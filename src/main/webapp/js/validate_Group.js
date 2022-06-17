$(document).ready(function() {
	$("#channelGroupForm").validate({
		errorElement: 'p',
		rules: {
			groupTitle: {
				maxlength:50,
				required: true
			}
		},
		messages: {
			groupTitle: {
				maxlength: "Group Title is limited to 50 characters",
				required: "Group Title required"
			}
		}
	});// end of validation rules for request form
}); // end of ready function