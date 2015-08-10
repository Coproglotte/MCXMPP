package tk.torche.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import tk.torche.plugin.util.Config;
import tk.torche.plugin.util.XMPP.XMPPHandler;


public class MCXMPP extends JavaPlugin {

	private Config config;
	private XMPPHandler XMPPh;

	@Override
	public void onEnable() {
	    saveDefaultConfig();
		this.config = new Config(this.getConfig());
		this.XMPPh = new XMPPHandler(this);
		XMPPh.connect();
		System.out.println("[MCXMPP v0.1] Plugin loaded.");
	}

	@Override
	public void onDisable() {
		XMPPh.disconnect();
		System.out.println("[MXCMPP v0.1] Plugin unloaded.");
	}

	public Config getLoadedConfig() {
		return this.config;
	}
}
