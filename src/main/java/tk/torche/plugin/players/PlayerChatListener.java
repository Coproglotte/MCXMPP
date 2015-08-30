package tk.torche.plugin.players;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import tk.torche.plugin.MCXMPP;
import tk.torche.plugin.XMPP.XMPPHandler;
import tk.torche.plugin.util.Constants.EventPermEnum;


/**
 * This class manages listeners on player chat events.
 * 
 * @author Coproglotte
 *
 */
public class PlayerChatListener implements Listener {

	private XMPPHandler XMPPh;
	private String xmppChatFormat;

	/**
	 * Instanciates a new PlayerChatListener.
	 * @param XMPPh The XMPPHandler managing XMPP messages
	 */
	public PlayerChatListener(XMPPHandler XMPPh, String xmppChatFormat) {
		this.XMPPh = XMPPh;
		this.xmppChatFormat = xmppChatFormat;
	}

	/**
	 * Listens to AsyncPlayerChatEvent in order to send players' messages
	 * to XMPP when triggered.
	 * @param event Event representing a player sending a message
	 */
	@EventHandler
	public void onPlayerMessage(AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		if (PlayerInfo.isIgnored(player, EventPermEnum.MESSAGE))
			return;

		final Chat chat = MCXMPP.getChat();
		String message = xmppChatFormat;

		if (chat != null)
			message = message.replace("%prefix", chat.getPlayerPrefix(player))
				.replace("%suffix", chat.getPlayerSuffix(player));

		message = message.replace("%sender", player.getDisplayName())
				.replace("%world", player.getWorld().getName())
				.replaceAll("&[^&]", "") // Remove color codes
				.replace("&&", "&")
				.replace("%message", event.getMessage());

		XMPPh.sendXMPPMessage(message);
	}

	/**
	 * Listens to PlayerCommandPreprocessEvent in order to send players' commands
	 * to XMPP when triggered.
	 * @param event Event representing a player sending a command
	 */
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (PlayerInfo.isIgnored(event.getPlayer(), EventPermEnum.COMMAND))
			return;

		String message = "[" + event.getPlayer().getDisplayName()
				+ " sent command " + event.getMessage() + "]";
		XMPPh.sendXMPPMessage(message);
	}
}
