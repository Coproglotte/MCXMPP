package tk.torche.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import tk.torche.plugin.XMPP.XMPPConnectionListener;
import tk.torche.plugin.XMPP.XMPPHandler;
import tk.torche.plugin.XMPP.XMPPUserStatusListener;
import tk.torche.plugin.util.Constants;

public class CommandsHandler implements CommandExecutor {


	private XMPPHandler xmppHandler;
	private final String PERM_ERROR_MSG = "You are not allowed to use this command";

	public CommandsHandler(XMPPHandler xmppHandler) {
		this.xmppHandler = xmppHandler;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			// EMPTY
			sendMessage(sender, "Version " + MCXMPP.getInstance().getDescription().getVersion(), false);
			sendMessage(sender, "To get the commands list type " + ChatColor.GOLD + "/xmpp help", false);
		} else {
			final String command = args[0].toLowerCase();
			if (command.equals("help")) {
				// HELP
				sendMessage(sender, "Available commands:", false);
				sender.sendMessage("For the status of the bot type " + ChatColor.GOLD + "/xmpp status");
				sender.sendMessage("To reconnect to the server type " + ChatColor.GOLD + "/xmpp connect");
				sender.sendMessage("To join the MUC room type " + ChatColor.GOLD + "/xmpp join");
				sender.sendMessage("To leave the MUC room type " + ChatColor.GOLD + "/xmpp leave");
				sender.sendMessage("To disconnect from the server type " + ChatColor.GOLD + "/xmpp quit"
						+ ChatColor.WHITE + " or " + ChatColor.GOLD + " /xmpp disconnect");
			} else if (command.equals("status")) {
				// STATUS
				if (sender.hasPermission(Constants.PERM_ADMIN)) {
					String message = "";
					if (XMPPConnectionListener.isConnected()) {
						message = "Connected to " + ChatColor.DARK_GREEN + xmppHandler.getServer() + ChatColor.WHITE
								+ " as " + ChatColor.DARK_GREEN + xmppHandler.getJID();
						if (XMPPUserStatusListener.isInRoom())
							message += ChatColor.WHITE + " in room " + ChatColor.DARK_GREEN
							+ xmppHandler.getMuc().getRoom();
						else
							message += ChatColor.DARK_RED + " in no MUC room";
						sendMessage(sender, message, false);
					} else {
						sendMessage(sender, "Not connected to any server", true);
					}
				} else {
					sendMessage(sender, PERM_ERROR_MSG, true);
				}
			} else if (command.equals("connect")) {
				// CONNECT TO SERVER
				if (sender.hasPermission(Constants.PERM_ADMIN)) {
					try {
						xmppHandler.connect();
						sendMessage(sender, "Successfully connected!", false);
					} catch (Exception e) {
						sendMessage(sender, "Failed to connect: " + e.getMessage(), true);
					}
				} else {
					sendMessage(sender, PERM_ERROR_MSG, true);
				}
			} else if (command.equals("join")) {
				// JOIN MUC ROOM
				if (sender.hasPermission(Constants.PERM_ADMIN)) {
					try {
						xmppHandler.joinMucRoom();
					} catch (Exception e) {
						sendMessage(sender, "Failed to join MUC room: " + e.getMessage(), true);
					}
				} else {
					sendMessage(sender, PERM_ERROR_MSG, true);
				}
			} else if (command.equals("leave")) {
				// LEAVE MUC ROOM
				if (sender.hasPermission(Constants.PERM_ADMIN)) {
					try {
						xmppHandler.leaveMucRoom();
					} catch (Exception e) {
						sendMessage(sender, "Failed to leave MUC room: " + e.getMessage(), true);
					}
				}
			} else if (command.equals("quit") || command.equals("disconnect")) {
				// QUIT SERVER
				if (sender.hasPermission(Constants.PERM_ADMIN)) {
					try {
						xmppHandler.disconnect();
					} catch (Exception e) {
						sendMessage(sender, "Failed to quit the server: " + e.getMessage(), true);
					}
				}
			} else {
				sendMessage(sender, "Unknown command", true);
				return false;
			}
		}

		return true;
	}

	private void sendMessage(CommandSender target, String message, boolean error) {
		if (!error)
			target.sendMessage(ChatColor.AQUA + "[MCXMPP] " + ChatColor.WHITE + message);
		else
			target.sendMessage(ChatColor.AQUA + "[MCXMPP] " + ChatColor.DARK_RED + message);
	}
}
