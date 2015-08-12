package tk.torche.plugin;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import tk.torche.plugin.util.Config;
import tk.torche.plugin.util.XMPP.XMPPHandler;
import tk.torche.plugin.util.XMPP.XMPPMessageListener;
import tk.torche.plugin.util.players.PlayerChatListener;
import tk.torche.plugin.util.players.PlayerPresenceListener;


public class MCXMPP extends JavaPlugin {

	private Config config;
	private XMPPHandler XMPPh;

	@Override
	public void onEnable() {
	    saveDefaultConfig();
		this.config = new Config(this.getConfig());

		this.XMPPh = new XMPPHandler(this);
		XMPPh.connect();

		getServer().getPluginManager().registerEvents(new PlayerChatListener(XMPPh), this);
		getServer().getPluginManager().registerEvents(new PlayerPresenceListener(XMPPh), this);
		new XMPPMessageListener(getServer(), XMPPh.getMuc());

		System.out.println("[MCXMPP v0.1] Plugin loaded.");
	}

	@Override
	public void onDisable() {
		XMPPh.disconnect();
		HandlerList.unregisterAll();
		System.out.println("[MXCMPP v0.1] Plugin unloaded.");
	}

	public Config getLoadedConfig() {
		return this.config;
	}
}
