package com.centurylink.rss.business.service;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.Link;
import com.centurylink.rss.domain.entity.Story;
import com.centurylink.rss.domain.entity.util.HibernateUtil;
import com.centurylink.rss.web.form.StoryForm;

@Service
public class RSSWritingService {
	
	@Autowired
	StoryDS storyDS;
	
	private static final Logger logger = Logger.getLogger(RSSWritingService.class);

	@Autowired
	@Qualifier("xmlPathProperties")
	Properties filePaths;

	private String generateDestinationFilePath(Channel c) {
		logger.debug("Entered generateDestinationFilePath method of RSSWritingService");
		
		String path = filePaths.getProperty("appBaseDir");
		path += "/";
		path += filePaths.getProperty("xmlPath");
		path += c.getId();
		path += ".xml";
		logger.debug("Path for xml for Channel " + c.getTitle() + ": " + path);
		return path;
	}

	@Transactional
	public void scheduledWriteChannel(Channel c) throws Exception {
		// c doesn't have it's stories initialized because lazy collection at the time of grabbing c.
		// so we have to re grab our channel here, so that it's collection will work.
		Channel d = HibernateUtil.findById(c.getClass(), c.getId());
		writeChannel(d);
	}
	
	public void writeChannel(Channel c) throws Exception {
		
		
		logger.debug("Entered writeChannel method of RSSWritingService.");
		String destinationFile = generateDestinationFilePath(c);
		logger.info("Writing channel " + c.getTitle() + " to file path: " + destinationFile);

		// create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

		// create XMLEventWriter
		XMLEventWriter w = outputFactory.createXMLEventWriter(new FileOutputStream(destinationFile));

		// create an eventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent nl = eventFactory.createCharacters("\n");

		// create a date format for rss compliant dates
		DateFormat rssDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

		// create Start and End Document Tags, and version attribute
		StartDocument startDoc = eventFactory.createStartDocument();
		EndDocument endDoc = eventFactory.createEndDocument();
		Attribute version = eventFactory.createAttribute("version", "2.0");

		// create open and close rss tags
		StartElement rssStart = eventFactory.createStartElement("", "", "rss");
		EndElement rssEnd = eventFactory.createEndElement("", "", "rss");

		// create open and close channel tags for rss
		StartElement channelStart = eventFactory.createStartElement("", "", "channel");
		EndElement channelEnd = eventFactory.createEndElement("", "", "channel");

		w.add(startDoc);
		w.add(nl);
		w.add(rssStart);
		w.add(version);
		w.add(nl);
		w.add(channelStart);
		w.add(nl);
		writeElement(w, "title", c.getTitle());
		writeElement(w, "link", c.getLink());
		writeElement(w, "description", c.getDescription());
		writeElement(w, "language", c.getLanguage());
		writeElement(w, "rating", c.getRating());
		writeElement(w, "copyright", c.getCopyright());
		if (c.getPublishDate() != null) {
			writeElement(w, "pubDate", rssDateFormat.format(c.getPublishDate()));
		}
		if (c.getLastBuildDate() != null) {
			writeElement(w, "lastBuildDate", rssDateFormat.format(c.getLastBuildDate()).toString());
		}
		writeElement(w, "generator", c.getGenerator());
		writeElement(w, "docs", c.getDocs());
		writeElement(w, "cloud", c.getCloud());
		writeElement(w, "ttl", c.getTtl());
		writeElement(w, "managingEditor", c.getManagingEditor());
		writeElement(w, "webMaster", c.getWebMaster());
		Set<Story> channelStories = c.getStories();
		if (channelStories != null) {
			Date today = new Date();
			writeStories(channelStories, today, w);
		} else {
			logger.warn("Channel '" + c.getTitle() + "' has no stories, skipping story writing...");
		}
		w.add(channelEnd);
		w.add(nl);
		w.add(rssEnd);
		w.add(nl);
		w.add(endDoc);
		logger.info("All elements of xml written without error.");
	}

	private static void writeStories(Set<Story> storiesToWrite, Date today, XMLEventWriter writer) throws XMLStreamException {
		for (Story s : storiesToWrite) {
			// if we're after the publish date, before the expiration date, and we're approved: then write the story.
			if (s.getPublishDate().before(today) && s.getExpirationDate().after(today) && s.getApprovalStatus().equalsIgnoreCase(Story.APPROVED_STATUS)) {
				writeStory(s, writer);
			}
		}
	}

	// could be duplicating stories into same element because eventFactory internals use a thread for writing or something. 
	// or java thread could be changing or smth... 
	private static void writeStory(Story s, XMLEventWriter w) throws XMLStreamException {
		logger.debug("Entered writeStory method of RSSWritingService. Writing story with title: " + s.getTitle());
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent nl = eventFactory.createCharacters("\n");
		DateFormat rssDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

		StartElement start = eventFactory.createStartElement("", "", "item");
		EndElement end = eventFactory.createEndElement("", "", "item");
		w.add(start);
		w.add(nl);
		writeElement(w, "title", s.getTitle());
		
	
		// i know this is gross, but all these tags have to be there, or the client will show links from the previous story on the current one - Brandon
		List<Link> linkList = Link.findAllLinksByStory(s);
		int counter = 0;
		for(; counter < linkList.size(); counter++){
			if(counter == 0){
				if(linkList.get(counter) == null){
					writeElement(w, "link", "");
					writeElement(w, "linkName", "");
				}else{
					writeElement(w, "link", linkList.get(counter).getLink());
					writeElement(w, "linkName", linkList.get(counter).getLinkName());
				}
				
			}else{
				if(linkList.get(counter) == null){
					writeElement(w, "link"+String.valueOf(counter), "");
					writeElement(w, "linkName"+String.valueOf(counter), "");
				}else{
					writeElement(w, "link"+ String.valueOf(counter), linkList.get(counter).getLink());
					writeElement(w, "linkName"+ String.valueOf(counter), linkList.get(counter).getLinkName());
				}
			}
		}
		while (counter < 3){
			if(counter == 0){
				writeElement(w, "link", "");
				writeElement(w, "linkName", "");
			}else{
				writeElement(w, "link"+String.valueOf(counter), "");
				writeElement(w, "linkName"+String.valueOf(counter), "");
			}
			counter++;
		}
		
		writeElement(w, "isHighPriority", s.getIsHighPriority().toString());
		writeElement(w, "description", s.getDescription());
		writeElement(w, "author", s.getAuthor());
		writeElement(w, "category", s.getCategory());
		writeElement(w, "comments", s.getComments());
		writeElement(w, "enclosure", s.getEnclosure());
		writeElement(w, "guid", String.valueOf(s.getId()));
		writeElement(w, "pubDate", rssDateFormat.format(s.getPublishDate()).toString());
		writeElement(w, "source", s.getSource());
		logger.info("pocName: " + s.getPocName());
		logger.info("pocPhNbr: " + s.getPocPhNbr());
		writeElement(w, "pocName", s.getPocName());
		writeElement(w, "pocPhNbr", s.getPocPhNbr());
		w.add(end);
		w.add(nl);
	}

	private static void writeElement(XMLEventWriter writer, String tagName, String tagContents)
			throws XMLStreamException {
		logger.debug("Entered writeElement method of RSSWritingService. Writing element: " + tagName
				+ " with contents: " + tagContents);
		if (tagContents != null) {
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent nl = eventFactory.createCharacters("\n");
			XMLEvent tab = eventFactory.createCharacters("\t");

			// create and write open tag
			StartElement start = eventFactory.createStartElement("", "", tagName);
			writer.add(tab);
			writer.add(start);

			// create and write tag contents
			if (tagName.equals("description")) {
				Characters content = eventFactory.createCData(tagContents);
				writer.add(content);
			} else {
				Characters content = eventFactory.createCharacters(tagContents);
				writer.add(content);
			}
			// create and write close tag
			EndElement end = eventFactory.createEndElement("", "", tagName);
			writer.add(end);
			writer.add(nl);
		}
	}

}
