package com.jaoafa.VCChannelSpeaker;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class MuteManager {
	private static HashSet<String> mutes = null;

	public static HashSet<String> refreshMuteList() {
		if (mutes != null) {
			mutes.clear();
		} else {
			mutes = new HashSet<>();
		}

		File sqliteFile = new File("mutes.db");
		if (!sqliteFile.exists()) {
			// nasa
			return null;
		}
		Connection conn;
		try {
			SQLiteDBManager sqlite = new SQLiteDBManager(sqliteFile);
			conn = sqlite.getConnection();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		try {
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM users;");
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				try {
					Long.valueOf(res.getString("userid"));
				} catch (NumberFormatException e) {
					continue;
				}
				mutes.add(res.getString("userid"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return mutes;
	}

	public static void saveMuteList() {
		if (mutes == null)
			return;
		File sqliteFile = new File("mutes.db");
		Connection conn;
		try {
			SQLiteDBManager sqlite = new SQLiteDBManager(sqliteFile);
			conn = sqlite.getConnection();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		try {
			PreparedStatement statement_delete = conn.prepareStatement("delete from users;");
			statement_delete.executeUpdate();
			for (String userid : mutes) {
				PreparedStatement statement_insert = conn.prepareStatement("insert into users values(?);");
				statement_insert.setString(1, userid);
				statement_insert.executeUpdate();
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	public static boolean isMuted(String userid) {
		if (mutes == null)
			refreshMuteList();
		return mutes.contains(userid);
	}

	public static void addMuteList(String userid) {
		if (mutes == null)
			refreshMuteList();
		if (mutes.contains(userid))
			return;
		mutes.add(userid);
		saveMuteList();
	}

	public static void removeMuteList(String userid) {
		if (mutes == null)
			refreshMuteList();
		if (!mutes.contains(userid))
			return;
		mutes.remove(userid);
		saveMuteList();
	}
}
