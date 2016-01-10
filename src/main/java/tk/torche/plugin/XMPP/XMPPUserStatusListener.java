package tk.torche.plugin.XMPP;

import org.jivesoftware.smackx.muc.UserStatusListener;

import tk.torche.plugin.mcserver.McMessageSender;

public class XMPPUserStatusListener implements UserStatusListener {


	private static boolean isInRoom;
	private McMessageSender mcMessageSender;

	public XMPPUserStatusListener(McMessageSender mcMessageSender) {
		isInRoom = true;
		this.mcMessageSender = mcMessageSender;
	}

	public static boolean isInRoom() {
		return isInRoom;
	}

	@Override
	public void adminGranted() {}

	@Override
	public void adminRevoked() {}

	@Override
	public void banned(String actor, String reason) {
		isInRoom = false;
		mcMessageSender.sendNotice("Banned from MUC room by " + actor + " - Reason: " + reason);
	}

	@Override
	public void kicked(String actor, String reason) {
		isInRoom = false;
		mcMessageSender.sendNotice("Kicked from MUC room by " + actor + " - Reason: " + reason);
	}

	@Override
	public void membershipGranted() {}

	@Override
	public void membershipRevoked() {}

	@Override
	public void moderatorGranted() {}

	@Override
	public void moderatorRevoked() {}

	@Override
	public void ownershipGranted() {}

	@Override
	public void ownershipRevoked() {}

	@Override
	public void voiceGranted() {}

	@Override
	public void voiceRevoked() {}

}
