package com.jaoafa.VCChannelSpeaker.Event;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent;

public class Event_Join {
	@EventSubscriber
	public void onUserVoiceChannelJoinEvent(UserVoiceChannelJoinEvent event) {
		if (event.getUser().getLongID() == event.getClient().getOurUser().getLongID()) {
			return;
		}
	}
}
