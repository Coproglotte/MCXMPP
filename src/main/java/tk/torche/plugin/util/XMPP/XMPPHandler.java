package tk.torche.plugin.util.XMPP;

import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.java7.Java7SmackInitializer;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

import tk.torche.plugin.MCXMPP;
import tk.torche.plugin.util.Config;


public class XMPPHandler {

	private MCXMPP instance;
	private XMPPTCPConnectionConfiguration connConf;
	private AbstractXMPPConnection conn;
	private MultiUserChatManager mucManager;
	private MultiUserChat muc;

	public XMPPHandler(MCXMPP instance) {
		this.instance = instance;
		setXMPPConnectionConfiguration(instance.getLoadedConfig());
	}

	private void setXMPPConnectionConfiguration(Config config) {
		this.connConf = XMPPTCPConnectionConfiguration.builder()
				.setUsernameAndPassword(config.getJid(),
						config.getPassword())
						.setHost(config.getHost())
						.setPort(config.getPort())
						.setServiceName(config.getService())
						.setDebuggerEnabled(true)
						.build();
		this.conn = new XMPPTCPConnection(connConf);
		new Java7SmackInitializer().initialize();
	}

	public void connect() {
		try {
			// Connect and login
			conn.connect();
			conn.login(instance.getLoadedConfig().getUsername(),
					connConf.getPassword(), "MCXMPP");

			// Join MUC channel
			this.mucManager = MultiUserChatManager.getInstanceFor(this.conn);
			this.muc = mucManager.getMultiUserChat(instance.getLoadedConfig().getChannel());
			if (instance.getLoadedConfig().getChannelPassword() == null ||
					instance.getLoadedConfig().getChannelPassword().isEmpty())
				muc.join("MCXMPP");
			else
				muc.join("MCXMPP", instance.getLoadedConfig().getChannelPassword());
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		if (conn.isConnected()) {
			try {
				muc.leave();
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
			conn.disconnect();
		}
	}

	public MultiUserChat getMuc() {
		return this.muc;
	}

	public void sendXMPPMessage(String message) {
		try {
			muc.sendMessage(message);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
}
