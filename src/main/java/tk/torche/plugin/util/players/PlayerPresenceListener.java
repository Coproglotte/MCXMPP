package tk.torche.plugin.util.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tk.torche.plugin.util.XMPP.XMPPHandler;


public class PlayerPresenceListener implements Listener {

	private XMPPHandler XMPPh;

	public PlayerPresenceListener(XMPPHandler XMPPh) {
		this.XMPPh = XMPPh;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		XMPPh.sendXMPPMessage("[" + event.getPlayer().getDisplayName() +
				" joined the game]");
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		XMPPh.sendXMPPMessage("[" + event.getPlayer().getDisplayName() +
				" left the game]");
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {	
		XMPPh.sendXMPPMessage("[" + event.getPlayer().getDisplayName() +
				" has been kicked from the server : " +
				event.getReason() + "]");
	}
}
