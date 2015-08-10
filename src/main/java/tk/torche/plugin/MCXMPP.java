package tk.torche.plugin;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import tk.torche.plugin.util.Config;
import tk.torche.plugin.util.XMPP.XMPPHandler;
import tk.torche.plugin.util.playerchat.PlayerChatListener;


public class MCXMPP extends JavaPlugin {

	private Config config;
	private XMPPHandler XMPPh;
	private PlayerChatListener pcl;

	@Override
	public void onEnable() {
	    saveDefaultConfig();
		this.config = new Config(this.getConfig());
		this.XMPPh = new XMPPHandler(this);
		XMPPh.connect();
		this.pcl = new PlayerChatListener(XMPPh);
		getServer().getPluginManager().registerEvents(pcl, this);
		System.out.println("[MCXMPP v0.1] Plugin loaded.");
	}

	@Override
	public void onDisable() {
		XMPPh.disconnect();
		HandlerList.unregisterAll(pcl);
		System.out.println("[MXCMPP v0.1] Plugin unloaded.");
	}

	public Config getLoadedConfig() {
		return this.config;
	}
}
