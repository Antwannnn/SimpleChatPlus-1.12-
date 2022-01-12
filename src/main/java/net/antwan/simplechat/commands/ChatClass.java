package net.antwan.simplechat.commands;

import net.antwan.simplechat.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

public class ChatClass implements CommandExecutor, Listener {

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

        boolean isEnabled = mainConfig.getBoolean("chat.state");

        UUID playerId = p.getUniqueId();

        int cooldownTime = mainConfig.getInt("clear-delay");

            if (p.hasPermission("scp.chat")) {
                if (args.length == 0) {
                    p.sendMessage(prefix + messageConfig.getString("not-enough-args-message").replace('&', '§'));
                    return true;
                }
                if (args.length == 1) {
                    switch (args[0]) {
                        //Must add console sender thing
                        case "clear":
                            // Must refactor to method
                            if (cooldown.containsKey(playerId)) {
                                if (cooldown.get(p.getUniqueId()) > System.currentTimeMillis()) {
                                    long timeLeft = (cooldown.get(playerId) - System.currentTimeMillis()) / 1000;
                                    p.sendMessage(prefix + messageConfig.getString("clear-delay-message").replace("%seconds-left%", String.valueOf(timeLeft)).replace('&', '§'));
                                    return true;
                                }
                            }
                            for (int a = 0; a < 125; a++) {
                                Bukkit.broadcastMessage(" ");
                            }
                            p.sendMessage(prefix + messageConfig.getString("clear-message").replace("%sender%", p.getName()).replace('&', '§'));
                            cooldown.put(playerId, System.currentTimeMillis() + ((long) cooldownTime * 1000));
                            break;

                        case "enable":
                            if (!isEnabled) {
                                main.getConfig().set("chat.state", true);
                                p.sendMessage(prefix + messageConfig.getString("enabling-message").replace('&', '§').replace("%sender%", p.getName()));
                                return true;

                            } else {
                                p.sendMessage(prefix + messageConfig.getString("already-enabled-message").replace('&', '§'));
                            }
                            break;
                        case "disable":
                            if (isEnabled) {
                                p.sendMessage(prefix + messageConfig.getString("disabling-message").replace('&', '§').replace("%sender%", p.getName()));
                                main.getConfig().set("chat.state", false);
                                return true;
                            } else {

                                p.sendMessage(prefix + messageConfig.getString("already-disabled-message").replace('&', '§'));
                            }
                            break;

                        default:
                            p.sendMessage(prefix + messageConfig.getString("unknown-arg-message").replace('&', '§'));
                    }

                } else {
                    p.sendMessage(prefix + messageConfig.getString("too-many-args-message").replace('&', '§'));
                    return true;
                }
            } else {
                p.sendMessage(messageConfig.getString("permission-message").replace('&', '§'));
            }


        return false;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event){

        Player p = event.getPlayer();

        boolean isEnabled = main.getConfig().getBoolean("chat.state");

        String prefix = main.getConfig().getString("prefix").replace('&', '§').replace("%server_name%", Bukkit.getServer().getName())  + " ";

        if(!isEnabled){
            if(!p.hasPermission("scp.chat.bypass")) {
                // Must add hashmap to declare who turned the chat off
                // Must refactor all this trash code
                event.setCancelled(true);
                p.sendMessage(main.messages.getConfig().getString("disabled-message").replace('&', '§'));
            }
            else{
                p.sendMessage(prefix + main.messages.getConfig().getString("bypass-message").replace('&', '§'));

            }
        }
    }
}
