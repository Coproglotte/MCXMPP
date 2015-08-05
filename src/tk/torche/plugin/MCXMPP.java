package tk.torche.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import tk.torche.plugin.util.Config;


public class MCXMPP extends JavaPlugin {

	@Override
	public void onEnable() {
		Config config = new Config(this.getConfig());
		System.out.println("[MCXMPP v1.0] Plugin loaded.");
	}
}
