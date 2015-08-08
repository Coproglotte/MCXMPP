package tk.torche.plugin.util;

import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.java7.Java7SmackInitializer;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import tk.torche.plugin.MCXMPP;


public class XMPPHandler {

	private MCXMPP instance;
	private XMPPTCPConnectionConfiguration connConf;
	private AbstractXMPPConnection conn;

	public XMPPHandler(MCXMPP instance) {
		this.instance = instance;
		setXMPPConnectionConfiguration(instance.getLoadedConfig());
	}

	private void setXMPPConnectionConfiguration(Config config) {
		this.connConf = XMPPTCPConnectionConfiguration.builder()
				.setUsernameAndPassword(config.getUsername() + "@" + config.getService(),
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
			conn.connect();
			conn.login(instance.getLoadedConfig().getUsername(),
					connConf.getPassword(), "MCXMPP");
			System.out.println("!!SÉCURISÉ : " + conn.isSecureConnection());
		} catch (SmackException | IOException | XMPPException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		if (conn.isConnected())
			conn.disconnect();
	}

	public AbstractXMPPConnection getXMPPConnection() {
		return conn;
	}
}
