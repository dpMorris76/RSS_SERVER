## Expiring stories: (does not run at application startup.) 

* Stories could expire at any point after their expiration date has passed
* Stories will be expired based on the cron trigger in app.properties @CRON\_EXPIRE@, every time this function is called it expires things, and moves expired stories to the expired\_stories table.
* Stories can also expire if a new story is written out to the channel after their expiration date, but before they have been cleaned up by the cron trigger, however this type of expiration will not move them to the expired\_stories table.

## Publishing Stories

Published stories go through this logic (approved stories):

1. Published stories are checked to see if their publish date is in the future 
2. If their publish date is right now or before right now, it will immediatly trigger a channel re-write which will rewrite all the channels that the story is in. This proccess will not right out stories that have an expiration date before right now, but not move them into the expired\_stories table. 
3. If their publish date is in the future then it will create an entry in the stories\_to\_publish table. This table exists for two reasons, one is in case the server needs to be restarted, and the other is to help manage rewriteChannelsTrigger. 
4. Then, if the publish date was in the future, it will manually trigger the cron method "rewriteChannelsTrigger".

## The cron method "rewriteChannelsTrigger": (also runs ~10 seconds after application startup) 

1. It starts by finding the minimum publish time in story\_to\_publish, and if the found publish time is in the future, then set the cron to trigger at that publish time. If the cron is not in the future then it will trigger a channel rewrite on that story's channels. 
2. It will then remove the story\_to\_publish entry, that it just wrote out for, and only that entry. 
3. Repeat step 1 until the time is in the future, or the table story\_to\_publish is empty. 
4. If we made it past step 3, then set a heartbeat of 1 hour to call this method to make sure we didn't miss anything. 

>[ INFO] 04/24/17 12:30:42 <com.centurylink.rss.schedule.controller.ChannelCrons:rewriteThingsAndSetNextTrigger():120>: 

>    rescheduling story publish job for 1 hour from now because nothing needed to be scheduled.
