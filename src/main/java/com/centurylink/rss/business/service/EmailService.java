package com.centurylink.rss.business.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.domain.entity.Email;
import com.centurylink.rss.domain.entity.Story;
import com.centurylink.rss.domain.entity.SupportContact;
import com.centurylink.rss.domain.entity.User;
import com.sun.mail.smtp.SMTPAddressFailedException;

@Service
public class EmailService {
	private static final Logger logger = Logger.getLogger(EmailService.class);
	@Autowired
	private JavaMailSenderImpl mailSender;
	@Autowired
	@Qualifier("xmlPathProperties")
	Properties filePaths;
	@Autowired
	private UserDS userService;
	@Autowired
	private DataService ds;
	public void sendEmail(Email email) throws MessagingException { 
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		// use the true flag to indicate you need a multipart message
		boolean hasAttachments = false;
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
				hasAttachments);
		helper.setTo(email.getTo());
		if (email.getCc() != null) {
			helper.setCc(email.getCc());
		}
		helper.setFrom(email.getFrom());
		try {
			helper.setSubject(MimeUtility.encodeText(email.getSubject()));
		} catch (UnsupportedEncodingException e) {
			logger.warn("MAIL WARNING: Could not universally encode the subject line. Please enter a subject in the Suject line.");
			logger.warn(e.getMessage());
		}
		helper.setText(email.getText(), true);
		String emailTo = "";
		for(String s: email.getTo())
		{
			emailTo += s + "; ";
		}
		logger.info("sending Email to " + emailTo + " from "
				+ email.getFrom() + " with subject " + email.getSubject()
				+ " and content : " + email.getText());
		if (mailSender != null) {
			mailSender.send(mimeMessage);
		}
	}

	// Sends an email for any kind of error
	 	
	public void errorEncounteredEmail (Exception e) throws MessagingException {
		Email email = new Email();
		email.setFrom("InfoLink@DoNotReply.com");
		// make a list of users to receive email 
		// and email them whenever the error occurs
		// and what the error is about
		List<SupportContact> supportContacts = ds.findAllSupportContacts();
		List<String> supportContactEmails = new ArrayList<String>();
		for (SupportContact s : supportContacts) {
			supportContactEmails.add(s.getEmail());
		}
		String[] toField = new String[supportContactEmails.size()];
		int i=0;
		for (String s : supportContactEmails) {
			toField[i++] = s;
		}
		email.setTo(toField);
		email.setSubject("RSS Server Error");
		email.setFrom("InfoLink@DoNotReply.com");
		setErrorTextBody(email, e);
		checkAndSendEmail(email);
	}
	
	
	/**
	 * Sends an email to each administrator individually informing them that
	 * there is an invalid email that must be corrected.
	 * 
	 * @param email
	 *            The email that failed.
	 * @throws MessagingException
	 */
	public void emailNotSentEmail(Email email, Boolean nil)
			throws MessagingException {

//		Role adminRole = roleDao.findRoleByExactName("Administrator");
//		List<User> admin = userService.findActiveUsersByRole(adminRole);
//
//		for (User user : admin) {
//			if (user.getEmail() != null) {
//				// If reusing current email doesn't work.
//				// Email email = new Email();
//				email.setTo(user.getEmail());
//				email.setFrom("ProjectManagement@DoNotReply.com");
//				if (project != null) {
//					if (nil) {
//						email.setSubject("ALERT : Email Failure - An email for the Transitional Device project "
//								+ (project.getProjectName() != null ? project
//										.getProjectName() : "No project name")
//								+ " failed to send due to a null email.");
//					} else {
//						email.setSubject("ALERT : Email Failure - An email for the Transitional Device project "
//								+ (project.getProjectName() != null ? project
//										.getProjectName() : "No project name")
//								+ " Transitional Device project failed to send due to a malformed email.");
//					}
//				} else {
//					email.setSubject("ALERT : Email Failure - Archive Transitional Device projects email");
//				}
//
//				email = setText(email, project, false);
//
//				sendEmail(email);
//
//			}
//		}

	}
	
	public void checkAndSendEmail(Email email)
			throws MessagingException {
		Boolean nullEmail = false;

		if (email.getTo() != null) {
			for (String to : email.getTo()) {
				if (to.equals("")) {
					nullEmail = true;
					break;
				}
			}
		} else {
			nullEmail = true;
		}

		if (nullEmail != true) {
			logger.debug("Attempting to send Email");
			try {
				sendEmail(email);
			} catch (MailSendException e) {
				logger.warn("MAIL ERROR: Could not connect to SMTP server");
				emailNotSentEmail(email, false);
				logger.warn(e.getMessage());
			} catch (SMTPAddressFailedException e) {
				logger.warn("MAIL WARNING: Wrong address");
				emailNotSentEmail(email, false);
			} catch (MessagingException e) {
				emailNotSentEmail(email, false);
				e.printStackTrace();
			}
		} else {
			emailNotSentEmail(email, true);
		}
	}
	
	public void seekingApprovalStatusEmail(Story story) throws MessagingException {
		if (story != null) {
			Email email = new Email();
			email.setFrom("InfoLink@DoNotReply.com");
			List<Channel> channels = new ArrayList<Channel>(story.getChannels());
			ChannelGroup group = channels.get(0).getChannelGroup();
			List<User> users = new ArrayList<User>(group.getContentGatekeepers());
			List<String> userEmails = new ArrayList<String>();
			for (User u : users) {
				userEmails.add(u.getEmail());
			}
			String[] toField = new String[userEmails.size()];
			int i=0;
			for (String s : userEmails) {
				toField[i++] = s;
			}
			email.setTo(toField);
			email.setSubject("A user is seeking approval to publish a story to channel " + channels.get(0).getTitle());
			String link = filePaths.getProperty("deployUrl") + "/RSS/secure/submissionReview?storyId=" + story.getId();
			email.setText("User " + story.getAuthor() + " is requesting to publish '" + story.getTitle() + "' to channel "
					+ channels.get(0).getTitle() + ". <br /><a href=\"" + link + "\"><u>Click Here to go to the "
							+ "RSS web application and review this story.</u></a>");
			checkAndSendEmail(email);
			
		} else {
			logger.error("Story seeking approval email not sent due to null Story object");
		}
	}

	public void sendApprovalStatusEmail(Story story) throws MessagingException {
		if (story != null) {
			String status = story.getApprovalStatus();
			String author = story.getAuthor();
			if (author != null && status != null && status != Story.PENDING_STATUS) {
				Email email = new Email();
				email.setFrom("InfoLink@DoNotReply.com");
				email.setTo(story.getAuthor());
				if(status == Story.REJECTED_STATUS) {
					setRejectedText(email, story);
				} else if (status == Story.APPROVED_STATUS) {
					setApprovedText(email, story.getTitle(), story.getPublishDate());
				}
				checkAndSendEmail(email);
			} else {
				logger.error("Status email not sent due to missing recipient or unsuitable approval status");
			}
		} else {
			logger.error("Status email not sent due to null Story object");
		}
	}
	private Email setRejectedText (Email email, Story story) {
		email.setSubject("Your request to publish '"+story.getTitle()+"' has been rejected");
		String text = "The reason for the rejection is:<br /> '"+ story.getRejectedReasoning()+"'";
		text += "<br /> <u> <a href=\"" + filePaths.getProperty("deployUrl") + "/RSS/secure/submissionReview?storyId=" + String.valueOf(story.getId()) + "\"> Click here to edit your submission for resubmission. </a> </u>";
		email.setText(text);
		return email;
	}
	
	private Email setApprovedText (Email email, String title, Date pubDate) {
		email.setSubject("Your request to publish '"+title+"' has been approved.");
		email.setText("This story will be available to view on or after: "+pubDate+".");
		return email;
	}
	
	private Email setErrorTextBody (Email email, Exception e) {
//		email.setText("Please review the error.");
		String emailBody = "An error has occured in the RSS server application at approximately" + new Date();
		emailBody += "<br />The stack trace below was generated by the error with error message \"" + e.getMessage() +"\".";
		emailBody += "<br />Please review the stack trace and the log files.";
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		emailBody += "<br /><br />" + pw.toString();
		email.setText(emailBody);
		return email;
	}

	public void seekingReapprovalStatusEmail(Story story) throws MessagingException {
		if (story != null) {
			Email email = new Email();
			email.setFrom("InfoLink@DoNotReply.com");
			List<Channel> channels = new ArrayList<Channel>(story.getChannels());
			ChannelGroup group = channels.get(0).getChannelGroup();
			List<User> users = new ArrayList<User>(group.getContentGatekeepers());
			List<String> userEmails = new ArrayList<String>();
			for (User u : users) {
				userEmails.add(u.getEmail());
			}
			String[] toField = new String[userEmails.size()];
			int i=0;
			for (String s : userEmails) {
				toField[i++] = s;
			}
			email.setTo(toField);
			email.setSubject("A user is seeking re-approval to publish a story to channel " + channels.get(0).getTitle());
			String link = filePaths.getProperty("deployUrl") + "/RSS/secure/submissionReview?storyId=" + story.getId();
			email.setText("User " + story.getAuthor() + " is requesting to publish '" + story.getTitle() + "' to channel "
					+ channels.get(0).getTitle() + ". <br /><a href=\"" + link + "\"><u>Click Here to go to the "
							+ "RSS web application and review this story.</u></a>");
			checkAndSendEmail(email);
			
		} else {
			logger.error("Story seeking approval email not sent due to null Story object");
		}
	}
	
}
