package tk.torche.plugin;

import java.util.logging.Level;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import tk.torche.plugin.XMPP.XMPPHandler;
import tk.torche.plugin.XMPP.XMPPMessageListener;
import tk.torche.plugin.mcserver.McCommandSender;
import tk.torche.plugin.mcserver.McMessageSender;
import tk.torche.plugin.mcserver.PlayerChatListener;
import tk.torche.plugin.players.PlayerPresenceListener;


/**
 * This is the main class of the plugin.
 * It handles basic interactions between itself and the server.
 * 
 * @author Coproglotte
 *
 */
public class MCXMPP extends JavaPlugin {

	private static MCXMPP instance;
	private static Chat chat = null;
	private Config config;
	private McMessageSender mcMessageSender;
	private McCommandSender mcCommandSender;
	private XMPPHandler xmppHandler;
	private CommandsHandler commandsHandler;

	/**
	 * Saves the default config file on disk and/or loads it in memory.<br>
	 * Attempts to connect to the XMPP server and register events, or fails miserably
	 * and disables itself.
	 */
	@Override
	public void onEnable() {
		MCXMPP.instance = this;
		saveDefaultConfig();
		this.config = new Config(this.getConfig());

		this.xmppHandler = new XMPPHandler();
		this.commandsHandler = new CommandsHandler(xmppHandler);
		this.mcMessageSender = new McMessageSender(this.getServer(), config.getMcChatFormat());
		this.mcCommandSender = new McCommandSender(this.getServer());

		try {
			xmppHandler.connect(mcMessageSender);
			xmppHandler.joinMucRoom();

			getServer().getPluginManager().registerEvents(new PlayerChatListener(xmppHandler,
					config.getXmppChatFormat()), this);
			getServer().getPluginManager().registerEvents(new PlayerPresenceListener(xmppHandler), this);
			getCommand("mcxmpp").setExecutor(commandsHandler);

			RegisteredServiceProvider<net.milkbowl.vault.chat.Chat> chatProvider =
					getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
			if (chatProvider != null)
				chat = chatProvider.getProvider();

			new XMPPMessageListener(getServer(), xmppHandler, mcMessageSender, mcCommandSender);
		} catch (Exception e) {
			e.printStackTrace();
			disable();
		}
	}

	@Override
	public void onDisable() {
		disable();
	}

	/**
	 * Disconnects from XMPP and unregisters events.
	 */
	public void disable() {
		xmppHandler.disconnect();
		HandlerList.unregisterAll(this);
		getLogger().log(Level.INFO, "Plugin disabled");
	}

	/**
	 * Gets the loaded config.
	 * @return The loaded config
	 */
	public Config getPluginConfig() {
		return this.config;
	}

	/**
	 * Gets the current instance of the plugin.
	 * @return The instance of the plugin
	 */
	public static MCXMPP getInstance() {
		return MCXMPP.instance;
	}

	/**
	 * Gets the chat managager that can be used.
	 * @return Vault Chat instance, or null if it isn't available
	 */
	public static Chat getChat() {
		return MCXMPP.chat;
	}
}
