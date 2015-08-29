package tk.torche.plugin.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import tk.torche.plugin.MCXMPP;
import tk.torche.plugin.XMPP.XMPPHandler;


/**
 * This class manages listeners on player chat events.
 * 
 * @author Coproglotte
 *
 */
public class PlayerChatListener implements Listener {

	private XMPPHandler XMPPh;

	/**
	 * Instanciates a new PlayerChatListener.
	 * @param XMPPh The XMPPHandler managing XMPP messages
	 */
	public PlayerChatListener(XMPPHandler XMPPh) {
		this.XMPPh = XMPPh;
	}

	/**
	 * Listens to AsyncPlayerChatEvent in order to send the player's message
	 * to XMPP when triggered.
	 * @param event Event representing a player sending a message
	 */
	@EventHandler
	public void onPlayerMessage(AsyncPlayerChatEvent event) {
		String message = "<";
		if (MCXMPP.getChat() != null)
			message += MCXMPP.getChat().getPlayerPrefix(event.getPlayer()).replaceAll("&.", "") + " ";
		message += event.getPlayer().getDisplayName() + "> " + event.getMessage();

		XMPPh.sendXMPPMessage(message);
	}

	/**
	 * Listens to PlayerCommandPreprocessEvent in order to send the player's command
	 * to XMPP when triggered.
	 * @param event Event representing a player sending a command
	 */
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		String message = "[" + event.getPlayer().getDisplayName()
				+ " sent command " + event.getMessage() + "]";
		XMPPh.sendXMPPMessage(message);
	}
}
