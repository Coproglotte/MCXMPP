package tk.torche.plugin.players;

import org.bukkit.entity.Player;

import tk.torche.plugin.util.Constants.EventPermEnum;

/**
 * This class contains static methods to check player status and
 * permissions.
 * 
 * @author Coproglotte
 *
 */
public class PlayerInfo {


	/**
	 * Checks if the plugin should listen to a player's action.
	 * @param player The player to check
	 * @param event The event to check
	 * @return True if the plugin listens to events of type
	 * {@code event} from {@code player}
	 */
	public static boolean isIgnored(Player player, EventPermEnum event) {
		if (player.hasPermission(EventPermEnum.ALL.toString())
				|| player.hasPermission(event.toString()))
			return true;
		return false;
	}
}
