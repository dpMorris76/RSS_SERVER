$(document).ready(function() {
	$("#storyForm").validate({
		errorPlacement: function(error, element) {
		    error.appendTo( element.parent().next("div.errorHolder") );
		  },
		errorElement: 'span',
		rules: {
			channelIds: {
				required: true
			},
			storyPublishDate: {
				required: true,
				dateTime: true
			},
			expirationDate: {
				dateTime: true
			},
			storyTitle: {
				maxlength: 100,
				required: true
			},
			storyLink: {
				maxlength: 500,
			},
			storyLinkName: {
				maxlength: 70,
			},
			storyDescription: {
				required: true
			},
			storyPocPhNbr: {
				maxlength: 25,
			},
			storyPocName:{
				maxlength:100,
			}
		},
		messages: {
			channelIds: {
				required: "You must pick at least one channel to post to"
			},
			storyPublishDate: {
				required: "Publish Date required",
				dateTime: "Must match format: yyyy/mm/dd hh:mm"
			},
			expirationDate: {
				dateTime: "Must match format: yyyy/mm/dd hh:mm"
			},
			storyTitle: {
				maxlength: "Story Title is limited to 100 characters",
				required: "Headline required"
			},
			storyLink: {
				maxlength: "Headline Link to Full Story is limited to 500 characters",
			},
			storyLink: {
				maxlength: "Name of Headline Link is limited to 70 characters",
			},
			storyPocPhNbr: {
				maxlength: "Point of contact phone number is too long.",
			},
			storyPocName:{
				maxlength: "Point of contact name is too long.",
			},
			storyDescription: {
				required: "Story description required"
			}
		}
	});// end of validation rules for request form
}); // end of ready function