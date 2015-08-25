package tk.torche.plugin.util.XMPP;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;


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
		muc.addMessageListener(new MessageListener() {
			@Override
			public void processMessage(Message message) {
				// Do nothing if the Minecraft server is empty
				if (server.getOnlinePlayers().isEmpty()) return;
				// Keep only the nickname part of the JID
				String sender = message.getFrom().substring(message.getFrom().indexOf('/') + 1);
				// Don't re-send messages sent from MCXMPP
				if (sender.equals(muc.getNickname())) return;

				if (message.getBody().toLowerCase().startsWith("!cmd "))
					sendMinecraftCommand(sender, message.getBody().substring(5));
				else
					sendMinecraftMessage(sender, message.getBody());
			}
		});
	}

	private void sendMinecraftCommand(String sender, String body) {
		server.dispatchCommand(server.getConsoleSender(), body);
	}

	private void sendMinecraftMessage(String sender, String body) {
		String message = mcChatFormat.replace("%s", sender).replace("%m", body);
		for (Player player : server.getOnlinePlayers()) {
			player.sendMessage(message);
		}
	}
}