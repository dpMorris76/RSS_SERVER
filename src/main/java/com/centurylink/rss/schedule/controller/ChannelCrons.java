
package com.centurylink.rss.schedule.controller;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.centurylink.rss.business.service.ChannelGroupDS;
import com.centurylink.rss.business.service.DataService;
import com.centurylink.rss.business.service.RSSWritingService;
import com.centurylink.rss.business.service.StoryDS;
import com.centurylink.rss.domain.entity.Channel;
import com.centurylink.rss.domain.entity.ChannelGroup;
import com.centurylink.rss.domain.entity.ExpiredStory;
import com.centurylink.rss.domain.entity.StoryToPublish;
import com.centurylink.rss.domain.entity.Story;
import com.centurylink.rss.domain.entity.util.HibernateUtil;

public class ChannelCrons {

	@Autowired
	RSSWritingService rws;

	@Autowired
	Trigger rewriteChannelsTrigger;
	
	@Autowired
	DataService ds;

	@Autowired
	StoryDS storyS;

	@Autowired
	ChannelGroupDS channelGroupS;

	@Autowired
	org.springframework.scheduling.quartz.SchedulerFactoryBean scheduler;

	private static final Logger logger = Logger.getLogger(ChannelCrons.class);

	public ChannelCrons() {
	}

	/**
	 * A method used to deal with weird timings & server lag
	 * 
	 * @return
	 */
	private Date rightNowPlusFifteenSeconds() {
		Date d = new Date();
		d.setTime(d.getTime() + 15000);
		return d;
	}
	
	/**
	 * We rewrite all the channels that are before right now in the table then
	 * we set the next trigger time to the earliest thing that needs to be
	 * re-written. Basically this needs to be triggered a bunch.
	 */
	@Transactional
	public void rewriteThingsAndSetNextTrigger() {
		try {
			StoryToPublish sp = ds.getEarliestStoryToPublish();
			while (sp != null && sp.getDate().before(new Date())) {
				Story s = storyS.findById(sp.getStoryId());
				// if these two dates are not equal then someone has changed the
				// publish date of the story.
				// this means that we don't want to write out right now because
				// it will have created a different cr
				// so all we need to do is cycle.
				if (s != null && s.getApprovalStatus().equals(Story.APPROVED_STATUS)
						&& s.getPublishDate().equals(sp.getDate())) {
					for (Channel c : s.getChannels()) {
						try {
							logger.info("rewriting channel in job execution id: " + c.getId());
							rws.writeChannel(c);
						} catch (Exception e) {
							logger.error("Error writing xml to channel id: " + c.getId());
							logger.error("Error is: " + e.getLocalizedMessage());
						} finally {
							logger.info("StoryToPublish " + sp.getId() + " was deleted");
							HibernateUtil.delete(sp); // because we've done the
														// rewrite for it. & we
														// don't want to
														// infinitly loop
							HibernateUtil.getSessionFactory().getCurrentSession().flush();
						}
					}
				} else {
					logger.info("story was not actually approved, skipping story publish for: " + s.getId());
					HibernateUtil.delete(sp);
					HibernateUtil.getSessionFactory().getCurrentSession().flush();
				}
				sp = ds.getEarliestStoryToPublish();
			}
			if (sp == null) {
				logger.info(
						"rescheduling story publish job for 1 hour from now because nothing needed to be scheduled.");
				// set the nextScheduledTime for one hour from now, just to be
				// certain that it's working.
				setNextScheduledTime(new Date(new Date().getTime() + 60 * 1000 * 360));
			} else {
				logger.info("rescheduling story publish job for " + sp.getDate() + " because publish needed.");
				setNextScheduledTime(sp.getDate());
			}
		} catch (Exception e) {
			logger.error("Something went wrong with the rewriting of things! Potentially a database conn error. ");
			logger.error(e);
			// only jump five minutes if we're in super 
			setNextScheduledTime(new Date(new Date().getTime() + 60 * 1000 * 5)); 
		}
	}

	/**
	 * for things that are immediatly published because hibernate lag
	 * 
	 * @param date
	 *            other potential minumum time.
	 */
	@Transactional
	@Deprecated
	public void rewriteThingsAndSetNextTrigger(Date t) {
		StoryToPublish minCR = ds.getEarliestStoryToPublish();
		while (minCR != null && minCR.getDate().before(rightNowPlusFifteenSeconds())) {
			Story s = storyS.findById(minCR.getStoryId());
			// if these two dates are not equal then someone has changed the
			// publish date of the story.
			// this means that we don't want to write out right now because it
			// will have created a different cr
			// so all we need to do is cycle.
			if (s != null && s.getApprovalStatus().equals(Story.APPROVED_STATUS)
					&& s.getPublishDate().equals(minCR.getDate())) // as opposed
																	// to
																	// deleting
																	// crs, we
																	// can add
																	// some
																	// robustness
																	// by just
																	// not
																	// writing
																	// out
																	// things.
			{
				for (Channel c : storyS.findById(minCR.getStoryId()).getChannels()) {
					try {
						logger.info("rewriting channel in job execution id: " + c.getId());
						rws.writeChannel(c);
					} catch (Exception e) {
						logger.error("Error writing xml to channel id: " + c.getId());
						logger.error("Error is: " + e.getLocalizedMessage());
					} finally {
						logger.info("ChannelRewrite " + minCR.getId() + " was deleted");
						HibernateUtil.delete(minCR); // because we've done the
														// rewrite for it. & we
														// don't want to
														// infinitly loop
						HibernateUtil.getSessionFactory().getCurrentSession().flush();
					}
				}
			} else {
				logger.info("story was not actually approved, skipping channel rewrite" + s.getId());
				HibernateUtil.delete(minCR);
				HibernateUtil.getSessionFactory().getCurrentSession().flush();
			}
			minCR = ds.getEarliestStoryToPublish();
		}
		if (minCR != null) {
			if (t.before(minCR.getDate())) {
				logger.info(
						"rescheduling channel write job for " + t + " because recently approved story publish needed.");
				setNextScheduledTime(t);
			} else {
				logger.info("rescheduling channel write job for " + minCR.getDate()
						+ " because recomputation for next publish time was needed.");
				setNextScheduledTime(minCR.getDate());
			}
		} else {
			logger.info("rescheduling channel write job for " + t + " because recently approved story publish needed.");
			setNextScheduledTime(t);
		}
	}

	private void setNextScheduledTime(Date d) {
		// use set start time
		// don't care about the
		// repeateInterval nonsense.
		rewriteChannelsTrigger = rewriteChannelsTrigger.getTriggerBuilder().startAt(d).build();
		logger.debug("setting next start time to : " + d);
		// scheduler.schedule(new writeChannelLaterClass(), d);
		// scheduler.getScheduler().getTrigger(arg0, arg1)
		try {
			scheduler.getScheduler().rescheduleJob(rewriteChannelsTrigger.getKey(), rewriteChannelsTrigger);
		} catch (Exception e) {
			logger.error("trigger not set correctly error message is: " + e.getLocalizedMessage());
		}
		return;
	}

	/**
	 * @param s
	 *            the channel to schedule the rewrite for
	 * @param t
	 *            the time to schedule the channel rewrite
	 * @return if it succeeded (not working.)
	 */
	@Transactional
	public boolean scheduleStoryRewrite(Story s) {
		logger.info("scheduling story write-out for: " + s.getTitle() + "(" + s.getId() + ") at " + s.getPublishDate());
		StoryToPublish cr = new StoryToPublish();
		cr.setStoryId(s.getId());
		cr.setDate(s.getPublishDate());
		ds.saveSP(cr);
		// hopefully it'll be flushed by the time that this is running, but no
		// promises
		// this is mostly so that we can get the next min value
		// we could grab the date and keep running with it, but we probably
		// aren't going to.
		rewriteThingsAndSetNextTrigger();
		// inside of ^ s.getPublishDate() probably unneeded method because of
		// locaiton of flushes.

		// old stuff.
		// logger.info("scheduling channel rewrite for channel titled: " +
		// c.getTitle() + " at: " + t.toString());
		// scheduler.schedule(new writeChannelLaterClass(c), t);
		return false;
	}

	@Deprecated
	class writeChannelLaterClass implements Runnable {
		@Override
		public void run() {
			try {
				// channelCrons.rewriteThingsAndSetNextTrigger();
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
		}

	}

	@Deprecated
	@Transactional
	public void rewriteChannels() {
		System.err.println("\nw\nf\ns\nn");
		// TODO will probably need some more logic to
		// optimize the writing of channels.
		logger.info("Cron job to rewrite channel commencing. ");
		for (ChannelGroup g : channelGroupS.findAll()) {
			for (Channel c : g.getChannels()) {
				// atm can only do this.
				try {
					rws.writeChannel(c);
				} catch (Exception e) {
					logger.error("Error writing to channel Title: " + c.getTitle());
					logger.error("error was: " + e.getLocalizedMessage());
				}
			}
		}
		try {
			rws.writeChannel(Channel.findById(Channel.ADMIN_CHANNEL_ID));
		} catch (Exception e) {
			logger.error("Error writing to Admin Feed");
			logger.error("error was: " + e.getLocalizedMessage());
		}
	}

	/**
	 * logic for writing out an approved story including the potential need to
	 * schedule a xml write in the future
	 * 
	 * @param s
	 *            story to write.
	 */
	public void writeApprovedStory(Story s) {
		// redundant code here.
		logger.debug("Write logic for " + s.getTitle() + " with current ID of: " + s.getId());
		if (s.getPublishDate().after(rightNowPlusFifteenSeconds())) // if our
																	// publish
																	// date is
																	// after
																	// today we
																	// need to
																	// schedule
																	// it to be
																	// written
		{
			scheduleStoryRewrite(s);
		} else {
			Set<Channel> channelsToWrite = new HashSet<Channel>(s.getChannels());// otherwise
																					// we
																					// can
																					// go
																					// ahead
																					// and
																					// write
																					// right
																					// now
			// s.setPublishDate(new Date()); // IDK if they want this...
			// Story.saveStory(s);
			for (Channel c : channelsToWrite) {
				try {
					rws.writeChannel(c);
				} catch (Exception e) {
					logger.error("Exception occured while writing the channel: " + c.getTitle() + "\n"
							+ e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void moveExpiredStories() {
		logger.info("Checking for expired stories");
		List<Story> expiredStoryList = storyS.findExpiredStories();
		// collect the channels that'll need to be rewritten
		Set<Channel> rewriteThese = new HashSet<Channel>();
		for(Story s: expiredStoryList){
			rewriteThese.addAll(s.getChannels());
		}
		logger.info("Expiring stories list size: " + expiredStoryList.size());
		// expire the stories that we collected
		for(Story s : expiredStoryList){
			try {
				storyS.expireStory(s);
			} catch (Exception e) {
				logger.error("Exception occured trying to expireStory with id: " + s.getId());
				e.printStackTrace();
			}
		}
		// rewrite the channels 
		for(Channel c: rewriteThese){
			logger.debug("Channel: " + c);
			try{
				rws.writeChannel(c);
			}catch(Exception e){
				logger.error("Error writing channel after expiring stories: " + c.getId());
				logger.error(e);
				e.printStackTrace();
			}
		}
	}
}
