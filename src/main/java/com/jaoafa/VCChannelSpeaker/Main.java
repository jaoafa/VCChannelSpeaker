package com.jaoafa.VCChannelSpeaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jaoafa.VCChannelSpeaker.Event.Event_Join;
import com.jaoafa.VCChannelSpeaker.Event.Event_Leave;
import com.jaoafa.VCChannelSpeaker.Event.Event_MessageReceived;
import com.jaoafa.VCChannelSpeaker.Event.Event_Ready;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;

public class Main {
	public static IVoiceChannel channel = null;

	public static void main(String[] args) {
		File f = new File("conf.properties");
		Properties props;
		try {
			InputStream is = new FileInputStream(f);

			// プロパティファイルを読み込む
			props = new Properties();
			props.load(is);
		} catch (FileNotFoundException e) {
			// ファイル生成
			props = new Properties();
			props.setProperty("token", "PLEASETOKEN");
			props.setProperty("voicetext_apikey", "PLEASETOKEN");
			try {
				props.store(new FileOutputStream("conf.properties"), "Comments");
				System.out.println("Please Config Token!");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// キーを指定して値を取得する
		String token = props.getProperty("token");
		if (token.equalsIgnoreCase("PLEASETOKEN")) {
			System.out.println("Please Token!");
			return;
		}

		// キーを指定して値を取得する
		String voicetext_apikey = props.getProperty("voicetext_apikey");
		if (voicetext_apikey.equalsIgnoreCase("PLEASETOKEN")) {
			System.out.println("Please Token!");
			return;
		}
		System.setProperty("voicetext.apikey", voicetext_apikey);

		IDiscordClient client = createClient(token, true);
		EventDispatcher dispatcher = client.getDispatcher();
		dispatcher.registerListener(new Event_Ready());
		dispatcher.registerListener(new Event_Join());
		dispatcher.registerListener(new Event_Leave());
		dispatcher.registerListener(new Event_MessageReceived());

		Runtime.getRuntime().addShutdownHook(
				new Thread(
						() -> {
							IGuild guild = client.getGuildByID(597378876556967936L);
							if (guild.getConnectedVoiceChannel() != null) {
								guild.getConnectedVoiceChannel().leave();
							}
						}));
	}

	public static IDiscordClient createClient(String token, boolean login) { // Returns a new instance of the Discord client
		ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
		clientBuilder.withToken(token); // Adds the login info to the builder
		try {
			if (login) {
				return clientBuilder.login(); // Creates the client instance and logs the client in
			} else {
				return clientBuilder.build(); // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
			}
		} catch (DiscordException e) { // This is thrown if there was a problem building the client
			e.printStackTrace();
			return null;
		}
	}
}
