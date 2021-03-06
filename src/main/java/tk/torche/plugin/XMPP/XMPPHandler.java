package tk.torche.plugin.XMPP;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.scheduler.BukkitRunnable;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.java7.Java7SmackInitializer;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.dns.HostAddress;
import org.jivesoftware.smack.util.dns.javax.JavaxResolver;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;

import tk.torche.plugin.Config;
import tk.torche.plugin.MCXMPP;
import tk.torche.plugin.mcserver.McMessageSender;


/**
 * This class provides tools to connect and disconnect from an XMPP server and
 * join, send and receive messages in a MUC room.
 * 
 * @author Coproglotte
 *
 */
public class XMPPHandler {

	private MCXMPP plugin;
	private Config config;
	private McMessageSender mcMessageSender;
	private XMPPTCPConnectionConfiguration connConf;
	private AbstractXMPPConnection conn;
	private MultiUserChatManager mucManager;
	private MultiUserChat muc;
	private XMPPConnectionListener connectionListener;
	private XMPPUserStatusListener mucStatusListener;

	private static final String XMPPCONNECTIONERROR = "Could not log in to the XMPP server.";
	private static final String ROOMJOININGERROR = "Could not join the MUC room.";
	private static final String FORMCREATIONERROR = "Could not generate the form to create a MUC room.";

	/**
	 * Instanciates a new XMPPHandler by creating an XMPPTCPConnectionConfiguration.
	 */
	public XMPPHandler() {
		this.plugin = MCXMPP.getInstance();
		this.config = plugin.getPluginConfig();
		setXMPPConnectionConfiguration();
	}

	/**
	 * Sets every data needed to connect to the XMPP server defined in the config.<br>
	 * Instanciates an XMPPTCPConnectionConfiguration, then an XMPPTCPConnection.
	 */
	private void setXMPPConnectionConfiguration() {
		new Java7SmackInitializer().initialize();

		// Get the IP address of the XMPP server
		DNSUtil.setDNSResolver(new JavaxResolver());
		HostAddress ha = DNSUtil.resolveXMPPDomain(config.getService(), null).get(0);

		// Generate the connection configuration
		this.connConf = XMPPTCPConnectionConfiguration.builder()
				.setUsernameAndPassword(config.getJid(),
						config.getPassword())
						.setHost(ha.getFQDN())
						.setPort(ha.getPort())
						.setServiceName(config.getService())
						.build();
		this.conn = new XMPPTCPConnection(connConf);
		conn.setPacketReplyTimeout(10000);
	}

	/**
	 * Connects to the XMPP server using the previously generated XMPPTCPConnection and a new instance of
	 * McMessageSender.
	 * @param mcMessageSender McMessageSender instance
	 * @throws XMPPException Thrown when the connection or room joining fails
	 * @throws IOException Thrown when the connection fails
	 * @throws SmackException Thrown when the connection or room joining fails
	 */
	public void connect(McMessageSender mcMessageSender) throws XMPPException, IOException, SmackException {
		this.mcMessageSender = mcMessageSender;
		connect();
	}

	/**
	 * Connects to the XMPP server using the previously generated XMPPTCPConnection.
	 * @throws XMPPException Thrown when the connection fails
	 * @throws IOException Thrown when the connection fails
	 * @throws SmackException Thrown when the connection or room joining fails
	 */
	public void connect() throws XMPPException, IOException, SmackException {
		final String RESOURCE = "MCXMPP";

		// Connect and login
		try {
			conn.connect();
			conn.login(config.getUsername(),
					connConf.getPassword(), RESOURCE);
		} catch (XMPPException e) {
			plugin.getLogger().log(Level.WARNING, XMPPCONNECTIONERROR);
			throw e;
		} catch (IOException e) {
			plugin.getLogger().log(Level.WARNING, XMPPCONNECTIONERROR);
			throw e;
		} catch (SmackException e) {
			plugin.getLogger().log(Level.WARNING, XMPPCONNECTIONERROR);
			throw e;
		}

		// Add connection listener
		connectionListener = new XMPPConnectionListener(mcMessageSender);
		conn.addConnectionListener(connectionListener);
	}

	/**
	 * Join the MUC room defined in the config.
	 * @throws SmackException Thrown when the MUC room joining fails
	 * @throws XMPPException Thrown when the MUC room joining fails
	 */
	public void joinMucRoom() throws SmackException, XMPPException {
		this.mucManager = MultiUserChatManager.getInstanceFor(this.conn);
		this.muc = mucManager.getMultiUserChat(config.getRoom());
		DiscussionHistory dh = new DiscussionHistory();
		dh.setMaxStanzas(0);

		try {
			// Prosody considers that every room exists
			// TODO: Find a way to check if the room existed or was just created on Prosody
			if (muc.createOrJoin(config.getNickname(), config.getRoomPassword(),
					dh, SmackConfiguration.getDefaultPacketReplyTimeout()))
				muc.sendConfigurationForm(getRoomForm());
		} catch (SmackException e) {
			plugin.getLogger().log(Level.WARNING, ROOMJOININGERROR);
			throw e;
		} catch (XMPPException e) {
			plugin.getLogger().log(Level.WARNING, ROOMJOININGERROR);
			throw e;
		}

		// Add MUC room status listener
		mucStatusListener = new XMPPUserStatusListener(mcMessageSender);
		muc.addUserStatusListener(mucStatusListener);
	}

	/**
	 * Generates and gets a form to define room options.
	 * @throws NoResponseException Thrown if the form creation failed
	 * @throws XMPPErrorException Thrown if the form creation failed
	 * @throws NotConnectedException Thrown if the form creation failed
	 */
	private Form getRoomForm() throws NoResponseException, XMPPErrorException, NotConnectedException {
		Form form = null;
		try {
			form = muc.getConfigurationForm().createAnswerForm();
			form.setAnswer("muc#roomconfig_publicroom", false);
			form.setAnswer("muc#roomconfig_passwordprotectedroom", true);
			form.setAnswer("muc#roomconfig_roomname", "MCXMPP");
			form.setAnswer("muc#roomconfig_roomsecret", config.getRoomPassword());
		} catch (NoResponseException e) {
			plugin.getLogger().log(Level.WARNING, FORMCREATIONERROR);
			throw e;
		} catch (XMPPErrorException e) {
			plugin.getLogger().log(Level.WARNING, FORMCREATIONERROR);
			throw e;
		} catch (NotConnectedException e) {
			plugin.getLogger().log(Level.WARNING, FORMCREATIONERROR);
			throw e;
		}

		return form;
	}

	/**
	 * Leave the MUC room
	 * @throws NotConnectedException Thrown if the bot wasn't connected to a server
	 */
	public void leaveMucRoom() throws NotConnectedException {
		muc.removeUserStatusListener(mucStatusListener);
		muc.leave();
	}

	/**
	 * Leave the MUC room and disconnect from the XMPP server.
	 * @throws NotConnectedException Thrown if the bot wasn't connected to a server
	 */
	public void disconnect() throws NotConnectedException {
		if (conn.isConnected()) {
			leaveMucRoom();
			conn.removeConnectionListener(connectionListener);
			conn.disconnect();
		}
	}

	/**
	 * Gets the service name of the server we are connected to.
	 * @return Service name of the server
	 */
	public String getServer() {
		return conn.getServiceName();
	}

	/**
	 * Gets the JID used on the server.
	 * @return JID used on the server
	 */
	public String getJID() {
		return conn.getUser();
	}

	/**
	 * Gets the MUC currently handled.
	 * @return The MUC the plugin should use
	 */
	public MultiUserChat getMuc() {
		return this.muc;
	}

	/**
	 * Sends a message in the MUC room.<br>
	 * This is an asynchronous method.
	 * @param message Message to be sent on XMPP
	 */
	public void sendXMPPMessage(final String message) {
		new BukkitRunnable() {
			public void run() {
				try {
					muc.sendMessage(message);
				} catch (NotConnectedException e) {
					e.printStackTrace();
					MCXMPP.getInstance().disable();
				}
			}
		}.runTaskAsynchronously(MCXMPP.getInstance());
	}
}
