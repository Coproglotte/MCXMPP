package tk.torche.plugin.util;

import org.bukkit.configuration.file.FileConfiguration;


public class Config {

	private String host;
	private int port;
	private String username;
	private String password;

	public Config(FileConfiguration fileConf) {
		this.host = fileConf.getString("host");
		this.port = fileConf.getInt("port");
		this.username = fileConf.getString("username");
		this.password = fileConf.getString("password");
	}

}
