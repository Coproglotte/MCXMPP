package tk.torche.plugin;

import org.bukkit.configuration.file.FileConfiguration;


/**
 * This class stores every values used by the plugin.
 * 
 * @author Coproglotte
 *
 */
public class Config {

	private final String service;
	private final String username;
	private final String jid;
	private final String password;
	private final String nickname;
	private final String room;
	private final String roomPassword;
	private final String mcChatFormat;

	/**
	 * Instanciates a new Config.
	 * @param fileConf The file configuration corresponding to the plugin's
	 * config file
	 */
	public Config(FileConfiguration fileConf) {
		this.service = fileConf.getString("service");
		this.username = fileConf.getString("username");
		this.jid = this.username + "@" + this.service;
		this.password = fileConf.getString("password");
		this.nickname = fileConf.getString("nickname");
		this.room = fileConf.getString("room");
		this.roomPassword = fileConf.getString("room-password");
		this.mcChatFormat = fileConf.getString("mc-chat-format");
	}

	/**
	 * Gets the service name of the XMPP server.
	 * @return The service name of the XMPP server
	 */
	public String getService() {
		return service;
	}

	/**
	 * Gets the JID to be used by the plugin.
	 * @return The JID used by the plugin
	 */
	public String getJid() {
		return jid;
	}

	/**
	 * Gets the username to be used to connect to the XMPP server.
	 * @return The username used by the plugin
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the password to be used to connect to the XMPP server.
	 * @return The password used by the plugin
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the nickname to be used to be identified in MUC rooms.
	 * @return The nickname used on MUC rooms
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Gets the room JID of the MUC room to join.
	 * @return The room JID of the MUC room to join
	 */
	public String getRoom() {
		return room;
	}

	/**
	 * Gets the optional MUC room password
	 * @return The MUC room password
	 */
	public String getRoomPassword() {
		return roomPassword;
	}

	/**
	 * Gets the representation of the formatting of MC chat messages sent
	 * by the plugin.
	 * @return String representing the formatting of MC chat messages
	 */
	public String getMcChatFormat() {
		return mcChatFormat;
	}
}
