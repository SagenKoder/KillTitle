package app.sagen.killtitle;

import app.sagen.killtitle.config.Configuration;
import app.sagen.killtitle.config.ConfigurationManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class KillTitle extends JavaPlugin implements Listener {

    ConfigurationManager configurationManager;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {

        if(!getDataFolder().exists() || !getDataFolder().isDirectory()) getDataFolder().mkdir();
        configurationManager = new ConfigurationManager(this);
        Configuration config = configurationManager.getConfiguration("config");

        boolean save = false;
        if(!config.isSet("messages.title")) {
            config.set("messages.title", "&6+2 money stuff! Player: %player");
            save = true;
        }
        if(!config.isSet("messages.subtitle")) {
            config.set("messages.subtitle", "&6You totally did that guy! Player: %player");
            save = true;
        }
        if(!config.isSet("title.fadeIn")) {
            config.set("title.fadeIn", 2);
            save = true;
        }
        if(!config.isSet("title.stay")) {
            config.set("title.stay", 2);
            save = true;
        }
        if(!config.isSet("title.fadeout")) {
            config.set("title.fadeOut", 2);
            save = true;
        }
        if(save) config.forceSave();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if(player.getKiller() == null) return;

        Configuration config = configurationManager.getConfiguration("config");

        player.sendTitle(
                ChatColor.translateAlternateColorCodes('&', config.getString("messages.title")),
                ChatColor.translateAlternateColorCodes('&', config.getString("messages.subtitle")),
                config.getInt("title.fadeIn"),
                config.getInt("title.stay"),
                config.getInt("title.fadeOut"));
    }
}
