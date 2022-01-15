package net.antwan.simplechat.commands;

import net.antwan.simplechat.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

        FileConfiguration mainConfig = main.config.getConfig();

        Player p = (Player) sender;

        boolean isEnabled = mainConfig.getBoolean("chat.state");

            if (p.hasPermission("scp.chat")) {

                String prefix = ChatColor.translateAlternateColorCodes('&',mainConfig.getString("prefix")).replace("%server_name%", Bukkit.getServer().getName())  + " ";

                if (args.length == 0) {
                    String guiName = mainConfig.getString("gui-name");
                    Inventory menu = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', guiName));
                    ItemStack clear = main.createItem(Material.PAPER, "§aClear chat");
                    ItemStack toggle = main.createItem(Material.BOOK_AND_QUILL, isEnabled ? "§cDisable chat" : "§aEnable chat");
                    ItemStack banword = main.createItem(Material.CLAY_BALL, "§6Banword Editor");
                    ItemStack history = main.createItem(Material.INK_SACK, "§bHistory viewer");
                    ItemStack close = main.createItem(Material.REDSTONE, "§cLeave menu");
                    menu.setItem(0, clear); menu.setItem(1, toggle); menu.setItem(2, banword); menu.setItem(3, history) ;menu.setItem(8, close);
                    p.openInventory(menu);
                    return true;
                }
                if (args.length == 1) {
                    switch (args[0]) {
                        //Must add console sender thing
                        case "clear":
                            UUID playerId = p.getUniqueId();
                            int cooldownTime = mainConfig.getInt("clear-delay");
                            if (cooldown.containsKey(playerId)) {
                                if (cooldown.get(p.getUniqueId()) > System.currentTimeMillis()) {
                                    long timeLeft = (cooldown.get(playerId) - System.currentTimeMillis()) / 1000;
                                    p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messageConfig.getString("clear-delay-message").replace("%seconds-left%", String.valueOf(timeLeft))));
                                    return true;
                                }
                            }
                                main.clearChat();

                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messageConfig.getString("clear-message").replace("%sender%", p.getName())));
                            cooldown.put(playerId, System.currentTimeMillis() + ((long) cooldownTime * 1000));
                            break;

                        case "enable":
                            if (!isEnabled) {
                                mainConfig.set("chat.state", true);
                                p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',messageConfig.getString("enabling-message").replace("%sender%", p.getName())));
                                return true;

                            } else {
                                p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',messageConfig.getString("already-enabled-message")));
                            }
                            break;
                        case "disable":
                            if (isEnabled) {
                                p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',messageConfig.getString("disabling-message").replace("%sender%", p.getName())));
                                mainConfig.set("chat.state", false);
                                return true;
                            } else {

                                p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',messageConfig.getString("already-disabled-message")));
                            }
                        break;
                        case "reload":
                            main.reloadConfigs();
                            p.sendMessage(prefix + "§aConfigs have been successfully reloaded");
                        break;

                        default:
                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',messageConfig.getString("unknown-arg-message")));
                    }

                } else {
                    p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',messageConfig.getString("too-many-args-message")));
                    return true;
                }
            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',messageConfig.getString("permission-message")));
            }


        return false;
    }

}
