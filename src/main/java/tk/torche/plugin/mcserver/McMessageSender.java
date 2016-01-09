package tk.torche.plugin.mcserver;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import tk.torche.plugin.MCXMPP;

public class McMessageSender {
	
	private Server server;
	private String mcChatFormat;

	public McMessageSender(Server server, String mcChatFormat) {
		this.server = server;
		this.mcChatFormat = ChatColor.translateAlternateColorCodes('&', mcChatFormat);
	}


	/**
	 * Sends a message to the server. The message is received by every players and formatted
	 * according to the config file.<br>
	 * This method is synchronous.
	 * @param sender
	 * @param body
	 */
	public void sendMinecraftMessage(final String sender, final String body) {
		new BukkitRunnable() {

			public void run() {
				String message = mcChatFormat.replace("%sender", sender).replace("%message", body);
				server.getLogger().log(Level.INFO, message);
				for (Player player : server.getOnlinePlayers()) {
					player.sendMessage(message);
				}
			}

		}.runTask(MCXMPP.getInstance());
	}
}
