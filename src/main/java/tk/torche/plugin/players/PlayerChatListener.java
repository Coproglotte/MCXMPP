package tk.torche.plugin.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import tk.torche.plugin.XMPP.XMPPHandler;


public class PlayerChatListener implements Listener {

	private XMPPHandler XMPPh;

	public PlayerChatListener(XMPPHandler XMPPh) {
		this.XMPPh = XMPPh;
	}

	@EventHandler
	public void onPlayerMessage(AsyncPlayerChatEvent event) {
		String message = "<" + event.getPlayer().getDisplayName()
				+ "> " + event.getMessage();
		XMPPh.sendXMPPMessage(message);
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		String message = "[" + event.getPlayer().getDisplayName()
				+ " sent command " + event.getMessage() + "]";
		XMPPh.sendXMPPMessage(message);
	}
}
