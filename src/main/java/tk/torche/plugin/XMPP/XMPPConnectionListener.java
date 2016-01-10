package tk.torche.plugin.XMPP;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import tk.torche.plugin.mcserver.McMessageSender;

public class XMPPConnectionListener implements ConnectionListener {


	private static boolean isConnected;
	private McMessageSender mcMessageSender;

	public XMPPConnectionListener(McMessageSender mcMessageSender) {
		isConnected = true;
		this.mcMessageSender = mcMessageSender; 
	}


	public static boolean isConnected() {
		return isConnected;
	}

	public void authenticated(XMPPConnection connection, boolean resumed) {}

	public void connected(XMPPConnection connection) {
		isConnected = true;
		mcMessageSender.sendNotice("Connected to server");
	}

	public void connectionClosed() {
		isConnected = false;
		mcMessageSender.sendNotice("Disconnected from server");
	}

	public void connectionClosedOnError(Exception e) {
		isConnected = false;
		mcMessageSender.sendNotice("Disconnected from server with error: " + e.getMessage());
	}

	public void reconnectingIn(int seconds) {}

	public void reconnectionFailed(Exception e) {
		isConnected = false;
		mcMessageSender.sendNotice("Reconnection to server failed");
	}

	public void reconnectionSuccessful() {
		mcMessageSender.sendNotice("Connected to server");
	}
}