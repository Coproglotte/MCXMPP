package tk.torche.plugin;

import org.bukkit.configuration.file.FileConfiguration;


public class Config {

	private final String service;
	private final String username;
	private final String jid;
	private final String password;
	private final String nickname;
	private final String channel;
	private final String channelPassword;
	private final String mcChatFormat;

	public Config(FileConfiguration fileConf) {
		this.service = fileConf.getString("service");
		this.username = fileConf.getString("username");
		this.jid = this.username + "@" + this.service;
		this.password = fileConf.getString("password");
		this.nickname = fileConf.getString("nickname");
		this.channel = fileConf.getString("channel");
		this.channelPassword = fileConf.getString("channel-password");
		this.mcChatFormat = fileConf.getString("mc-chat-format");
	}

	public String getService() {
		return service;
	}

	public String getJid() {
		return jid;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getNickname() {
		return nickname;
	}

	public String getChannel() {
		return channel;
	}

	public String getChannelPassword() {
		return channelPassword;
	}

	public String getMcChatFormat() {
		return mcChatFormat;
	}
}
