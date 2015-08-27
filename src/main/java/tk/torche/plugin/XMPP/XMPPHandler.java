package tk.torche.plugin.XMPP;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.scheduler.BukkitRunnable;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.java7.Java7SmackInitializer;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.dns.HostAddress;
import org.jivesoftware.smack.util.dns.javax.JavaxResolver;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

import tk.torche.plugin.Config;
import tk.torche.plugin.MCXMPP;


public class XMPPHandler {

	private MCXMPP plugin;
	private Config config;
	private XMPPTCPConnectionConfiguration connConf;
	private AbstractXMPPConnection conn;
	private MultiUserChatManager mucManager;
	private MultiUserChat muc;

	public XMPPHandler() {
		this.plugin = MCXMPP.getInstance();
		this.config = plugin.getPluginConfig();
		setXMPPConnectionConfiguration();
	}

	private void setXMPPConnectionConfiguration() {
		new Java7SmackInitializer().initialize();

		// Resolve the address used to connect to the XMPP server
		DNSUtil.setDNSResolver(new JavaxResolver());
		HostAddress ha = DNSUtil.resolveXMPPDomain(config.getService(), null).get(0);

		// Generate the connection configuration
		this.connConf = XMPPTCPConnectionConfiguration.builder()
				.setUsernameAndPassword(config.getJid(),
						config.getPassword())
						.setHost(ha.getFQDN())
						.setPort(ha.getPort())
						.setServiceName(config.getService())
//						.setDebuggerEnabled(true)
						.build();
		this.conn = new XMPPTCPConnection(connConf);
		conn.setPacketReplyTimeout(10000);
	}

	public void connect() throws XMPPException, IOException, SmackException {
		// Connect and login
		try {
			conn.connect();
			conn.login(config.getUsername(),
					connConf.getPassword(), "MCXMPP");
		} catch(XMPPException | IOException | SmackException e) {
			plugin.getLogger().log(Level.WARNING, "Could not log in to the XMPP server");
			throw e;
		}

		// Join MUC channel
		try {
			this.mucManager = MultiUserChatManager.getInstanceFor(this.conn);
			this.muc = mucManager.getMultiUserChat(config.getChannel());
			if (config.getChannelPassword() == null ||
					config.getChannelPassword().isEmpty())
				muc.join(config.getNickname());
			else
				muc.join(config.getNickname(), config.getChannelPassword());
		} catch(XMPPException | SmackException e) {
			plugin.getLogger().log(Level.WARNING, "Could not join MUC channel");
			throw e;
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

	public void sendXMPPMessage(final String message) {
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					muc.sendMessage(message);
				} catch (NotConnectedException e) {
					e.printStackTrace();
				}
			}

		}.runTaskAsynchronously(MCXMPP.getInstance());
	}
}
