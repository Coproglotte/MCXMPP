package tk.torche.plugin.XMPP;

import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import tk.torche.plugin.MCXMPP;
import tk.torche.plugin.mcserver.McCommandSender;
import tk.torche.plugin.mcserver.McMessageSender;
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
	private McMessageSender mcMessageSender;
	private McCommandSender mcCommandSender;
	private XMPPCommandMessage xmppCommandMessage;

	/**
	 * Instanciates a new XMPPMessageListener.
	 * @param server Bukkit server on which the plugin is running
	 * @param xmppHandler The XMPPHandler managing XMPP messages
	 * @param mcChatFormat String representing how messages should be delivered on Minecraft
	 */
	public XMPPMessageListener(Server server, XMPPHandler xmppHandler, McMessageSender mcMessageSender,
			McCommandSender mcCommandSender) {
		this.server = server;
		this.muc = xmppHandler.getMuc();
		this.mcMessageSender = mcMessageSender;
		this.mcCommandSender = mcCommandSender;
		this.xmppCommandMessage = new XMPPCommandMessage(xmppHandler, server);
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
						final String sender = message.getFrom().substring(message.getFrom().indexOf('/') + 1);
						// Don't re-send messages sent by MCXMPP
						if (sender.equals(muc.getNickname())) return;

						if (message.getBody().toLowerCase().startsWith(Constants.COMMAND_PREFIX)) {
							final String cmd = message.getBody().substring(Constants.COMMAND_PREFIX.length());
							if (Constants.IMPLEMENTED_CMDS.contains(cmd)) {
								xmppCommandMessage.processCommand(cmd);
							}
							else {
								mcCommandSender.sendMinecraftCommand(sender, cmd);
							}
						}
						else {
							mcMessageSender.sendGlobalMessage(sender, message.getBody());
						}
					}
				});
			}

		}.runTaskAsynchronously(MCXMPP.getInstance());
	}

}