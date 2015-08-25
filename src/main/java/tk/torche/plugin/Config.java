package tk.torche.plugin;

import org.bukkit.configuration.file.FileConfiguration;


public class Config {

	private final String host;
	private final String service;
	private final int port;
	private final String username;
	private final String jid;
	private final String password;
	private final String channel;
	private final String channelPassword;
	private final String mcChatFormat;

	public Config(FileConfiguration fileConf) {
		this.host = fileConf.getString("host");
		this.service = fileConf.getString("service");
		this.port = fileConf.getInt("port");
		this.username = fileConf.getString("username");
		this.jid = this.username + "@" + this.service;
		this.password = fileConf.getString("password");
		this.channel = fileConf.getString("channel");
		this.channelPassword = fileConf.getString("channel-password");
		this.mcChatFormat = fileConf.getString("mc-chat-format");
	}

	public String getHost() {
		return host;
	}

	public String getService() {
		return service;
	}

	public int getPort() {
		return port;
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
