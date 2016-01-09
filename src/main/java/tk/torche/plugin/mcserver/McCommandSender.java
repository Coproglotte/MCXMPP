package tk.torche.plugin.mcserver;

import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;

import tk.torche.plugin.MCXMPP;

public class McCommandSender {

	private Server server;

	public McCommandSender(Server server) {
		this.server = server;
	}

	/**
	 * Sends a command to the server. The command is executed by the console.<br>
	 * This method is synchronous.
	 * @param sender Nickname of the room user who sent the command (UNUSED)
	 * @param body Full command with arguments, omitting the first '/'
	 */
	public void sendMinecraftCommand(final String sender, final String body) {
		new BukkitRunnable() {

			public void run() {
				server.dispatchCommand(server.getConsoleSender(), body);		
			}

		}.runTask(MCXMPP.getInstance());
	}
}
