package com.jaoafa.VCChannelSpeaker.Event;

import com.jaoafa.VCChannelSpeaker.Main;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class Event_Ready {
	@EventSubscriber
	public void onReadyEvent(ReadyEvent event) {
		System.out.println("Ready: " + event.getClient().getOurUser().getName());

		IGuild guild = event.getClient().getGuildByID(597378876556967936L);
		if (guild == null) {
			return;
		}
		IVoiceChannel channel = guild.getVoiceChannelByID(616987346901925908L);
		if (channel == null) {
			return;
		}
		Main.channel = channel;
	}
}
