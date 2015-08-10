package tk.torche.plugin.util.XMPP;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class XMPPMessageListener {

	private Server server;
	private MultiUserChat muc;

	public XMPPMessageListener(Server server, MultiUserChat muc) {
		this.server = server;
		this.muc = muc;
		setXMPPMessageListener();
	}

	private void setXMPPMessageListener() {
		muc.addMessageListener(new MessageListener() {
			@Override
			public void processMessage(Message message) {
				String sender = message.getFrom().substring(message.getFrom().indexOf('/') + 1);
				if (sender.equals(muc.getNickname()))
					return;
				for (Player player : server.getOnlinePlayers())
					player.sendMessage("(XMPP) " + sender
							+ ": " + message.getBody());
			}
		});
	}
}