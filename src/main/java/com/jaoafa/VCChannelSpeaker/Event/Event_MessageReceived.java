package com.jaoafa.VCChannelSpeaker.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioInputStream;

import com.jaoafa.VCChannelSpeaker.Main;
import com.jaoafa.VCChannelSpeaker.MuteManager;

import am.ik.voicetext4j.Emotion;
import am.ik.voicetext4j.Emotion.Level;
import am.ik.voicetext4j.EmotionalSpeaker;
import am.ik.voicetext4j.EmotionalVoiceContext;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.IVoiceState;
import sx.blah.discord.util.RequestBuffer;
import sx.blah.discord.util.audio.AudioPlayer;

public class Event_MessageReceived {
	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event) {
		IChannel channel = event.getChannel();
		IUser user = event.getAuthor();
		IMessage message = event.getMessage();
		String text = event.getMessage().getContent();

		if (channel.getLongID() != 623153228267388958L) {
			return;
		}
		if (Main.channel == null) {
			return;
		}

		// Mute
		if (MuteManager.isMuted(user.getStringID())) {
			return;
		}

		// Command

		if (text.equalsIgnoreCase("/ChangeChannel")) {
			IVoiceState vs = user.getVoiceStateForGuild(event.getGuild());
			IVoiceChannel vc = vs.getChannel();
			if (vc == null) {
				RequestBuffer.request(() -> {
					message.addReaction(ReactionEmoji.of("❌")); // :x:
				});
				return;
			}
			vc.join();
		}
		if (text.equalsIgnoreCase("/Clear")) {
			AudioPlayer audioP = AudioPlayer.getAudioPlayerForGuild(event.getGuild());
			audioP.clear();
		}

		String[] args;
		if (text.contains(" ")) {
			args = Arrays.stream(text.split(" "))
					.filter(s -> (s != null && s.length() > 0))
					.toArray(String[]::new);
		} else {
			args = new String[] { text };
		}

		List<String> excludes = new ArrayList<>();

		EmotionalSpeaker speaker = EmotionalSpeaker.HIKARI;
		List<String> speakers = Arrays.stream(args).filter(
				arg -> arg != null && arg.startsWith("speaker:")).collect(Collectors.toList());
		excludes.addAll(speakers);
		if (speakers.size() != 0) {
			String speaker_str = speakers.get(0).substring("speaker:".length());
			for (EmotionalSpeaker one : EmotionalSpeaker.values()) {
				if (one.name().equalsIgnoreCase(speaker_str)) {
					speaker = one;
					break;
				}
			}
		}

		EmotionalVoiceContext context = speaker.ready();

		List<String> speeds = Arrays.stream(args).filter(
				arg -> arg != null && arg.startsWith("speed:")).collect(Collectors.toList());
		excludes.addAll(speeds);
		if (speeds.size() != 0) {
			String speed_str = speeds.get(0).substring("speed:".length());
			try {
				int speed = Integer.parseInt(speed_str);
				context = context.speed(speed);
			} catch (NumberFormatException e) {
				RequestBuffer.request(() -> {
					message.addReaction(ReactionEmoji.of("❌")); // :x:
				});
				return;
			}
		}
		List<String> pitchs = Arrays.stream(args).filter(
				arg -> arg != null && arg.startsWith("pitch:")).collect(Collectors.toList());
		excludes.addAll(pitchs);
		if (pitchs.size() != 0) {
			String pitch_str = pitchs.get(0).substring("pitch:".length());
			try {
				int pitch = Integer.parseInt(pitch_str);
				context = context.pitch(pitch);
			} catch (NumberFormatException e) {
				RequestBuffer.request(() -> {
					message.addReaction(ReactionEmoji.of("❌")); // :x:
				});
				return;
			}
		}
		List<String> emotions = Arrays.stream(args).filter(
				arg -> arg != null && arg.startsWith("emotion:")).collect(Collectors.toList());
		List<String> levels = Arrays.stream(args).filter(
				arg -> arg != null && arg.startsWith("level:")).collect(Collectors.toList());
		excludes.addAll(emotions);
		excludes.addAll(levels);
		if (emotions.size() != 0) {
			Level level = null;
			if (levels.size() != 0) {
				String level_str = levels.get(0).substring("level:".length());
				for (Level l : Level.values()) {
					if (l.name().equalsIgnoreCase(level_str)) {
						level = l;
						break;
					}
				}
			}
			String emotion_str = emotions.get(0).substring("emotion:".length());
			for (Emotion one : Emotion.values()) {
				if (one.name().equalsIgnoreCase(emotion_str)) {
					if (level == null) {
						context.emotion(one);
					} else {
						context.emotion(one, level);
					}
					break;
				}
			}
		}

		List<String> happys = Arrays.stream(args).filter(
				arg -> arg != null && arg.startsWith("happy:"))
				.collect(Collectors.toList());
		excludes.addAll(happys);
		if (happys.size() != 0) {
			String happy_str = happys.get(0).substring("happy:".length());
			if (happy_str.equalsIgnoreCase("true")) {
				context.happy();
			}
		}
		List<String> sads = Arrays.stream(args).filter(
				arg -> arg != null && arg.startsWith("sad:"))
				.collect(Collectors.toList());
		excludes.addAll(sads);
		if (sads.size() != 0) {
			String sad_str = sads.get(0).substring("sad:".length());
			if (sad_str.equalsIgnoreCase("true")) {
				context.sad();
			}
		}
		List<String> angrys = Arrays.stream(args).filter(
				arg -> arg != null && arg.startsWith("angry:"))
				.collect(Collectors.toList());
		excludes.addAll(angrys);
		if (angrys.size() != 0) {
			String angry_str = angrys.get(0).substring("angry:".length());
			if (angry_str.equalsIgnoreCase("true")) {
				context.angry();
			}
		}
		List<String> verys = Arrays.stream(args).filter(
				arg -> arg != null && arg.startsWith("very:"))
				.collect(Collectors.toList());
		excludes.addAll(verys);
		if (verys.size() != 0) {
			String very_str = verys.get(0).substring("very:".length());
			if (very_str.equalsIgnoreCase("true")) {
				context.very();
			}
		}
		AudioPlayer audioP = AudioPlayer.getAudioPlayerForGuild(event.getGuild());
		IVoiceChannel voice = event.getGuild().getConnectedVoiceChannel();
		if (voice == null) {
			Main.channel.join();
		}

		// 読み上げ
		List<String> texts = Arrays.stream(args).filter(
				arg -> arg != null && !excludes.contains(arg)).collect(Collectors.toList());
		AudioInputStream stream = context.getResponse(String.join(" ", texts)).audioInputStream();
		audioP.queue(stream);
	}
}
