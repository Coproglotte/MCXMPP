package tk.torche.plugin.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tk.torche.plugin.XMPP.XMPPHandler;
import tk.torche.plugin.util.Constants.EventPermEnum;


/**
 * This class manages listeners on several player-triggerd events.
 * 
 * @author Coproglotte
 *
 */
public class PlayerPresenceListener implements Listener {

	private XMPPHandler XMPPh;

	/**
	 * Instanciates a new PlayerPresenceListener.
	 * @param XMPPh The XMPPHandler managing XMPP messages
	 */
	public PlayerPresenceListener(XMPPHandler XMPPh) {
		this.XMPPh = XMPPh;
	}

	/**
	 * Listens to PlayerJoinEvent in order to send an XMPP message when triggered.
	 * @param event Event representing a player joining the server
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (PlayerInfo.isIgnored(event.getPlayer(), EventPermEnum.PRESENCE))
			return;

		XMPPh.sendXMPPMessage("[" + event.getPlayer().getDisplayName() +
				" joined the game]");
	}

	/**
	 * Listens to PlayerQuitEvent in order to send an XMPP message when triggered.
	 * @param event Event representing a player leaving the server
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (PlayerInfo.isIgnored(event.getPlayer(), EventPermEnum.PRESENCE))
			return;

		XMPPh.sendXMPPMessage("[" + event.getPlayer().getDisplayName() +
				" left the game]");
	}

	/**
	 * Listens to PlayerKickEvent in order to send an XMPP message when triggered.
	 * @param event Event representing a player being kicked from the server
	 */
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {	
		XMPPh.sendXMPPMessage("[" + event.getPlayer().getDisplayName() +
				" has been kicked from the server : " +
				event.getReason() + "]");
	}
}
