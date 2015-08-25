package tk.torche.plugin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

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
			try {
				XMPPh.connect();
			} catch (XMPPException e) {
				getLogger().log(Level.WARNING, "Could not log in to the XMPP server or MUC channel");
				e.printStackTrace();
			} catch (SmackException e) {
				getLogger().log(Level.WARNING, "Could not log in to the XMPP server or MUC channel");
				e.printStackTrace();
			} catch (IOException e) {
				getLogger().log(Level.WARNING, "Could not log in to the XMPP server or MUC channel");
				e.printStackTrace();
			}

		getServer().getPluginManager().registerEvents(new PlayerChatListener(XMPPh), this);
		getServer().getPluginManager().registerEvents(new PlayerPresenceListener(XMPPh), this);
		new XMPPMessageListener(getServer(), XMPPh.getMuc());

	}

	@Override
	public void onDisable() {
		XMPPh.disconnect();
		HandlerList.unregisterAll();
	}

	public Config getPluginConfig() {
		return this.config;
	}

	public Logger getPluginlogger() {
		return this.getLogger();
	}
}
