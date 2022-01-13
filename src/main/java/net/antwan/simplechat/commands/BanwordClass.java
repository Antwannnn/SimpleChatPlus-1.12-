package net.antwan.simplechat.commands;

import net.antwan.simplechat.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BanwordClass implements CommandExecutor, Listener {

    private Main main;

    public static HashMap<UUID, Boolean> inEditor = new HashMap<UUID, Boolean>();

    public BanwordClass(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        FileConfiguration messageConfig = main.messages.getConfig();

        FileConfiguration mainConfig = main.config;

        Player p = (Player) sender;

        if(p.hasPermission("scp.banword")){

            String prefix = ChatColor.translateAlternateColorCodes('&',mainConfig.getString("prefix")).replace("%server_name%", Bukkit.getServer().getName())  + " ";

            if(args.length == 0)
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', messageConfig.getString("not-enough-args-message")));

            if(args.length == 1){
                switch(args[0]){
                    case "modify":
                        if(!inEditor.containsKey(p.getUniqueId())) {
                            inEditor.put(p.getUniqueId(), true);
                            List<String> lines = messageConfig.getStringList("editor-mode-enter-messages");
                            for (String line : lines) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', line.replace("%prefix%", mainConfig.getString("prefix"))));
                            }
                        }
                        else{
                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messageConfig.getString("already-editor-mode-message")));
                        }
                        break;
                    default:
                        p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',messageConfig.getString("unknown-arg-message")));
                }

                }
            }

        else{
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', messageConfig.getString("permission-message")));
        }

        return false;
    }

}
