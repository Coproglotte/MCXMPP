package tk.torche.plugin.XMPP;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import tk.torche.plugin.MCXMPP;
import tk.torche.plugin.util.Constants;


/**
 * This class manages message listeners on XMPP conversations.
 * 
 * @author Coproglotte
 *
 */
public class XMPPMessageListener {

	private Server server;
	private MultiUserChat muc;
	private String mcChatFormat;

	/**
	 * Instanciates a new XMPPMessageListener.
	 * @param server Bukkit server on which the plugin is running
	 * @param muc Multi-user chat room to listen to
	 * @param mcChatFormat String representing how messages should be delivered on Minecraft
	 */
	public XMPPMessageListener(Server server, MultiUserChat muc, String mcChatFormat) {
		this.server = server;
		this.muc = muc;
		this.mcChatFormat = ChatColor.translateAlternateColorCodes('&', mcChatFormat);
		setXMPPMessageListener();
	}

	/**
	 * Adds a message listener on the MUC room.<br>
	 * This method is asynchronous.
	 */
	private void setXMPPMessageListener() {
		new BukkitRunnable() {

			public void run() {
				muc.addMessageListener(new MessageListener() {
					public void processMessage(Message message) {
						// Keep only the nickname part of the JID
						String sender = message.getFrom().substring(message.getFrom().indexOf('/') + 1);
						// Don't re-send messages sent by MCXMPP
						if (sender.equals(muc.getNickname())) return;

						if (message.getBody().toLowerCase().startsWith(Constants.COMMAND_PREFIX))
							sendMinecraftCommand(sender, message.getBody().substring(5));
						else {
							// Do nothing if the Minecraft server is empty
							if (!server.getOnlinePlayers().isEmpty())
								sendMinecraftMessage(sender, message.getBody());
						}
					}
				});
			}

		}.runTaskAsynchronously(MCXMPP.getInstance());
	}

	/**
	 * Sends a command to the server. The command is executed by the console.<br>
	 * This method is synchronous.
	 * @param sender Nickname of the room user who sent the command (UNUSED)
	 * @param body Full command with arguments, omitting the first '/'
	 */
	private void sendMinecraftCommand(final String sender, final String body) {
		new BukkitRunnable() {

			public void run() {
				server.dispatchCommand(server.getConsoleSender(), body);		
			}

		}.runTask(MCXMPP.getInstance());
	}

	/**
	 * Sends a message to the server. The message is received by every players and formatted
	 * according to the config file.<br>
	 * This method is synchronous.
	 * @param sender
	 * @param body
	 */
	private void sendMinecraftMessage(final String sender, final String body) {
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