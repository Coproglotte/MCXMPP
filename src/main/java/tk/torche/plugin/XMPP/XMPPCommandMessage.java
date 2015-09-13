package tk.torche.plugin.XMPP;

import java.util.ArrayList;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import tk.torche.plugin.MCXMPP;


/**
 * This class provides methods to send the returned text of a few vanilla commands, like /list or /time.
 * 
 * @author Coproglotte
 *
 */
public class XMPPCommandMessage {


	private XMPPHandler xmppHandler;
	private Server server;
	final Chat chat = MCXMPP.getChat();

	/**
	 * Instanciates a new XMPPCommandMessage.
	 * @param xmppHandler The XMPPHandler managing XMPP messages
	 * @param server The server MCXMPP is running on
	 */
	public XMPPCommandMessage(XMPPHandler xmppHandler, Server server) {
		this.xmppHandler = xmppHandler;
		this.server = server;
	}

	/**
	 * The main method of this class. It calls the appropriate method depending on which command is run.
	 * @param cmd The command that has been sent
	 */
	void processCommand(final String cmd) {
		switch (cmd) {
		case "list" :
			list();
		}
	}

	/**
	 * Implementation of the /list command. Sends to XMPP the number of used slots, and if the server
	 * isn't empty, the list of connected players.<br>
	 * This method is synchronous.
	 */
	private void list() {
		new BukkitRunnable() {
			@Override
			public void run() {
				final boolean isServerEmpty;
				final ArrayList<Player> players;
				final String usedSlots;
				String playerList;
				isServerEmpty = server.getOnlinePlayers().isEmpty();

				if (isServerEmpty) {
					usedSlots = "There are 0/" + server.getMaxPlayers() + " players online.";
					xmppHandler.sendXMPPMessage(usedSlots);
				} else {
					players = new ArrayList<Player>();
					players.addAll(server.getOnlinePlayers());
					usedSlots = "There are " + players.size() + "/" + server.getMaxPlayers() + " players online:";

					playerList = chat.getPlayerPrefix(players.get(0)) + " "  + players.get(0).getDisplayName();
					for (int i = 1; i < players.size(); ++i) {
						playerList += ", " + chat.getPlayerPrefix(players.get(i)) + players.get(i).getDisplayName();
					}
					playerList = playerList.replaceAll("&[^&]", "") // Remove color codes
							.replace("&&", "&");

					xmppHandler.sendXMPPMessage(usedSlots + " " + playerList);
				}
			}
		}.runTask(MCXMPP.getInstance());
	}
}