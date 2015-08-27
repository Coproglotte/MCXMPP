package tk.torche.plugin;

import java.util.logging.Level;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import tk.torche.plugin.XMPP.XMPPHandler;
import tk.torche.plugin.XMPP.XMPPMessageListener;
import tk.torche.plugin.players.PlayerChatListener;
import tk.torche.plugin.players.PlayerPresenceListener;


public class MCXMPP extends JavaPlugin {

	private static MCXMPP instance;
	private Config config;
	private XMPPHandler XMPPh;

	@Override
	public void onEnable() {
		MCXMPP.instance = this;
		saveDefaultConfig();
		this.config = new Config(this.getConfig());

		this.XMPPh = new XMPPHandler();
		try {
			XMPPh.connect();

			getServer().getPluginManager().registerEvents(new PlayerChatListener(XMPPh), this);
			getServer().getPluginManager().registerEvents(new PlayerPresenceListener(XMPPh), this);
			new XMPPMessageListener(getServer(), XMPPh.getMuc(), config.getMcChatFormat());
		} catch (Exception e) {
			e.printStackTrace();
			XMPPh.disconnect();
			setEnabled(false);
			getLogger().log(Level.INFO, "Plugin disabled");
		}
	}

	@Override
	public void onDisable() {
		XMPPh.disconnect();
		HandlerList.unregisterAll();
	}

	public Config getPluginConfig() {
		return this.config;
	}

	public static MCXMPP getInstance() {
		return MCXMPP.instance;
	}
}
