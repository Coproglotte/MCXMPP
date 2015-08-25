package tk.torche.plugin;

import java.util.logging.Level;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import tk.torche.plugin.XMPP.XMPPHandler;
import tk.torche.plugin.XMPP.XMPPMessageListener;
import tk.torche.plugin.players.PlayerChatListener;
import tk.torche.plugin.players.PlayerPresenceListener;


public class MCXMPP extends JavaPlugin {

	private Config config;
	private XMPPHandler XMPPh;

	@Override
	public void onEnable() {
	    saveDefaultConfig();
		this.config = new Config(this.getConfig());


		this.XMPPh = new XMPPHandler(this);
			try {
				XMPPh.connect();
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Could not log in to the XMPP server or MUC channel");
				e.printStackTrace();
			}

		getServer().getPluginManager().registerEvents(new PlayerChatListener(XMPPh), this);
		getServer().getPluginManager().registerEvents(new PlayerPresenceListener(XMPPh), this);
		new XMPPMessageListener(getServer(), XMPPh.getMuc(), config.getMcChatFormat());
	}

	@Override
	public void onDisable() {
		XMPPh.disconnect();
		HandlerList.unregisterAll();
	}

	public Config getPluginConfig() {
		return this.config;
	}
}
