package tk.torche.plugin.util;

/**
 * This class provides several useful constants.
 * 
 * @author Coproglotte
 *
 */
public class Constants {

	public static final String COMMAND_PREFIX = "!cmd ";

	private static final String PERM_IGNORE_ALL = "mcxmpp.ignore.*";
	private static final String PERM_IGNORE_MESSAGES = "mcxmpp.ignore.messages";
	private static final String PERM_IGNORE_COMMANDS = "mcxmpp.ignore.commands";
	private static final String PERM_IGNORE_PRESENCE = "mcxmpp.ignore.joinquit";


	public static enum EventPermEnum {
		ALL {
			public String toString() {
				return PERM_IGNORE_ALL;
			}
		},
		MESSAGE {
			public String toString() {
				return PERM_IGNORE_MESSAGES;
			}
		},
		COMMAND {
			public String toString() {
				return PERM_IGNORE_COMMANDS;
			}
		},
		PRESENCE {
			public String toString() {
				return PERM_IGNORE_PRESENCE;
			}
		};
	}
}
