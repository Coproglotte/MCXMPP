package tk.torche.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import tk.torche.plugin.XMPP.XMPPConnectionListener;
import tk.torche.plugin.XMPP.XMPPHandler;
import tk.torche.plugin.XMPP.XMPPUserStatusListener;

public class CommandsHandler implements CommandExecutor {

	
	private XMPPHandler xmppHandler;

	public CommandsHandler(XMPPHandler xmppHandler) {
		this.xmppHandler = xmppHandler;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// EMPTY
		if (args.length == 0) {
			sendMessage(sender, "version " + MCXMPP.getInstance().getDescription().getVersion(), false);
		} else {
			final String command = args[0].toLowerCase();
			// HELP
			if (command.equals("help")) {
				sendMessage(sender, "Available command: /xmpp status", false);
			// STATUS
			} else if (command.equals("status")) {
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
			// CONNECT
			} else if (command.equals("connect")) {
				try {
					xmppHandler.connect();
					sendMessage(sender, "Successfully connected!", false);
				} catch (Exception e) {
					sendMessage(sender, "Failed to connect: " + e.getMessage(), true);
				}
			// JOIN
			} else if (command.equals("join")) {
				try {
					xmppHandler.joinMucRoom();
				} catch (Exception e) {
					sendMessage(sender, "Failed to join MUC room: " + e.getMessage(), true);
				}
			} else {
				sendMessage(sender, "Unknown command", true);
			}
		}

		return false;
	}

	private void sendMessage(CommandSender target, String message, boolean error) {
		if (!error)
			target.sendMessage(ChatColor.AQUA + "[MCXMPP] " + ChatColor.WHITE + message);
		else
			target.sendMessage(ChatColor.AQUA + "[MCXMPP] " + ChatColor.DARK_RED + message);
	}
}
