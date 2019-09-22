package com.jaoafa.VCChannelSpeaker.Event;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.audio.AudioPlayer;

public class Event_Leave {
	@EventSubscriber
	public void onUserVoiceChannelLeaveEvent(UserVoiceChannelLeaveEvent event) {
		if (event.getGuild().getLongID() != 597378876556967936L) {
			return;
		}
		System.out.println("VoiceLeave: " + event.getUser().getName() + " " + event.getVoiceChannel().getName());

		AudioPlayer audioP = AudioPlayer.getAudioPlayerForGuild(event.getGuild());

		List<IUser> noBots = new ArrayList<>();
		for (IUser user : event.getVoiceChannel().getConnectedUsers()) {
			if (user.isBot()) {
				continue;
			}
			noBots.add(user);
		}
		System.out.println("VCLeft: " + noBots.size());
		if (noBots.size() == 0) { // 自分含め
			audioP.clear();
			event.getVoiceChannel().leave();
		}
	}

	@EventSubscriber
	public void onUserVoiceChannelMoveEvent(UserVoiceChannelMoveEvent event) {
		if (event.getGuild().getLongID() != 597378876556967936L) {
			return;
		}
		System.out.println("VoiceLeave: " + event.getUser().getName() + " " + event.getOldChannel().getName());

		AudioPlayer audioP = AudioPlayer.getAudioPlayerForGuild(event.getGuild());

		List<IUser> noBots = new ArrayList<>();
		for (IUser user : event.getOldChannel().getConnectedUsers()) {
			if (user.isBot()) {
				continue;
			}
			noBots.add(user);
		}
		System.out.println("VCLeft: " + noBots.size());
		if (noBots.size() == 0) { // 自分含め
			audioP.clear();
			event.getOldChannel().leave();
		}
	}
}
