package net.antwan.simplechat.commands;

import net.antwan.simplechat.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatClass implements CommandExecutor {

    private Main main;

    public ChatClass(Main main) {
        this.main = main;
    }

    public HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        FileConfiguration messageConfig = main.messages.getConfig();

        FileConfiguration mainConfig = main.getConfig();

        String prefix = mainConfig.getString("prefix").replace('&', '§').replace("%server_name%", Bukkit.getServer().getName())  + " ";

        Player p = (Player) sender;

        UUID playerId = p.getUniqueId();

        int cooldownTime = mainConfig.getInt("clear-delay");

        boolean isEnabled = mainConfig.getBoolean("chat-state");

        if(p.hasPermission("scp.chat")){
            if(args.length == 0){
                p.sendMessage(prefix + messageConfig.getString("not-enough-args-msg").replace('&', '§'));
                return true;
            }
            if(args.length == 1){
                switch(args[0]){
                    case "clear":
                        // Must refactor to method
                        if(cooldown.containsKey(playerId)) {
                            if(cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                                long timeLeft = (cooldown.get(playerId) - System.currentTimeMillis()) / 1000;
                                p.sendMessage(prefix + messageConfig.getString("clear-delay-message").replace("%seconds-left%", String.valueOf(timeLeft)).replace('&', '§'));
                                return true;
                            }
                        }
                        for(int a = 0; a < 125; a++){
                            Bukkit.broadcastMessage(" ");
                        }
                        p.sendMessage(prefix + messageConfig.getString("clear-message").replace("%sender%", p.getName()).replace('&', '§'));
                        cooldown.put(playerId, System.currentTimeMillis() + ((long) cooldownTime * 1000));
                    break;

                    case "enable":



                default:
                    p.sendMessage(prefix + messageConfig.getString("unknown-arg-msg").replace('&', '§'));
                }

            }
            else{
                p.sendMessage(prefix + messageConfig.getString("too-many-args-msg").replace('&', '§'));
                return true;
            }
        }
        else{
            p.sendMessage(messageConfig.getString("permission-message").replace('&', '§'));
        }

        return false;
    }
}
