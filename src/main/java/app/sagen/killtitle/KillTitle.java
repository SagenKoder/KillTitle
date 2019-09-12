package app.sagen.killtitle;

import app.sagen.killtitle.config.Configuration;
import app.sagen.killtitle.config.ConfigurationManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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

        String titleMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.title"))
                .replace("%player", player.getName());
        String subtitleMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.subtitle"))
                .replace("%player", player.getName());

        sendTitle(
                player.getKiller(),
                titleMessage, subtitleMessage,
                config.getInt("title.fadeIn"),
                config.getInt("title.stay"),
                config.getInt("title.fadeOut"));
    }

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {

        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;

        IChatBaseComponent jsonTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}");
        IChatBaseComponent jsonSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subtitle + "\"}");

        PacketPlayOutTitle sendTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, jsonTitle, fadeIn, stay, fadeOut);
        PacketPlayOutTitle sendSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, jsonSubtitle);

        connection.sendPacket(sendSubtitle);
        connection.sendPacket(sendTitle);

    }
}
