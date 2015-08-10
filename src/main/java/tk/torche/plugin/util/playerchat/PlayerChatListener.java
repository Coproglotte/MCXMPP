package tk.torche.plugin.util.playerchat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jivesoftware.smack.SmackException.NotConnectedException;

import tk.torche.plugin.util.XMPP.XMPPHandler;

public class PlayerChatListener implements Listener {

	private XMPPHandler XMPPh;

	public PlayerChatListener(XMPPHandler XMPPh) {
		this.XMPPh = XMPPh;
	}

	@EventHandler
	public void onPlayerMessage(AsyncPlayerChatEvent event) {
		String message = event.getPlayer().getDisplayName()
				+ ": " + event.getMessage();
		sendXMPPMessage(message);
	}

	private void sendXMPPMessage(String message) {
		try {
			XMPPh.getMuc().sendMessage(message);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
}
