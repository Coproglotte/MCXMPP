package tk.torche.plugin.XMPP;

import java.util.logging.Level;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import tk.torche.plugin.MCXMPP;


public class XMPPMessageListener {

	private Server server;
	private MultiUserChat muc;
	private String mcChatFormat;

	public XMPPMessageListener(Server server, MultiUserChat muc, String mcChatFormat) {
		this.server = server;
		this.muc = muc;
		this.mcChatFormat = ChatColor.translateAlternateColorCodes('&', mcChatFormat);
		setXMPPMessageListener();
	}

	private void setXMPPMessageListener() {
		new BukkitRunnable() {

			@Override
			public void run() {
				muc.addMessageListener(new MessageListener() {
					@Override
					public void processMessage(Message message) {
						// Keep only the nickname part of the JID
						String sender = message.getFrom().substring(message.getFrom().indexOf('/') + 1);
						// Don't re-send messages sent from MCXMPP
						if (sender.equals(muc.getNickname())) return;

						if (message.getBody().toLowerCase().startsWith("!cmd "))
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

	private void sendMinecraftCommand(final String sender, final String body) {
		new BukkitRunnable() {

			@Override
			public void run() {
				server.dispatchCommand(server.getConsoleSender(), body);		
			}

		}.runTask(MCXMPP.getInstance());
	}

	private void sendMinecraftMessage(final String sender, final String body) {
		new BukkitRunnable() {

			@Override
			public void run() {
				String message = mcChatFormat.replace("%s", sender).replace("%m", body);
				server.getLogger().log(Level.INFO, message);
				for (Player player : server.getOnlinePlayers()) {
					player.sendMessage(message);
				}
			}

		}.runTask(MCXMPP.getInstance());
	}
}