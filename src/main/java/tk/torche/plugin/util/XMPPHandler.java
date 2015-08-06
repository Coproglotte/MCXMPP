package tk.torche.plugin.util;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import tk.torche.plugin.MCXMPP;


public class XMPPHandler {

	private XMPPTCPConnectionConfiguration connConf;
	private AbstractXMPPConnection conn;

	public XMPPHandler(MCXMPP instance) {
		setXMPPConnectionConfiguration(instance.getLoadedConfig());
	}

	private void setXMPPConnectionConfiguration(Config config) {
		connConf = XMPPTCPConnectionConfiguration.builder()
				.setUsernameAndPassword(config.getUsername(), config.getPassword())
				.setHost(config.getHost())
				.setPort(config.getPort())
				.build();
		AbstractXMPPConnection conn = new XMPPTCPConnection(connConf);
	}

	public void connect() {
		conn = new XMPPTCPConnection(connConf);
	}

	public AbstractXMPPConnection getXMPPConnection() {
		return conn;
	}
}
