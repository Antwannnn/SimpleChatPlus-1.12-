package net.antwan.simplechat.listeners;

import net.antwan.simplechat.Main;
import net.antwan.simplechat.commands.BanwordClass;
import net.antwan.simplechat.commands.ChatClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PluginListener implements Listener {

    private Main main;

    public PluginListener(Main main){
        this.main = main;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event){

        Player p = event.getPlayer();

        boolean isEnabled = main.getConfig().getBoolean("chat.state");

        FileConfiguration mainConfig = main.getConfig();

        FileConfiguration messageConfig = main.messages.getConfig();

        String prefix = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("prefix")).replace("%server_name%", Bukkit.getServer().getName())  + " ";

        String message = event.getMessage();

        if(!isEnabled){
            if(p.hasPermission("scp.chat.bypass"))
                if(main.getConfig().getBoolean("chat.bypass")) {
                    p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',main.messages.getConfig().getString("bypass-message")));
                    return;
                }
            event.setCancelled(true);
            p.sendMessage(main.messages.getConfig().getString("disabled-message").replace('&', '§'));

        }
        if(BanwordClass.inEditor.containsKey(p.getUniqueId())){
            if(!message.equals("exit")){
                mainConfig.getStringList("ban-word").add(message);
            }
            else{
                BanwordClass.inEditor.remove(p.getUniqueId());
            }
            event.setCancelled(true);
        }
        else {
            if (p.hasPermission("scp.banword.bypass"))
                if (main.getConfig().getBoolean("ban-word-bypass")) {
                    p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', main.messages.getConfig().getString("ban-word-bypass-message")));
                    return;
                }
            event.setCancelled(true);
            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', main.messages.getConfig().getString("ban-word-warning-message")));
        }

    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Inventory current = e.getInventory();
        if(current.getName().equals("§aChat Management")){
            ItemStack itemCurrent = e.getCurrentItem();
            if(itemCurrent.hasItemMeta()) {
                String name = itemCurrent.getItemMeta().getDisplayName();
                if (itemCurrent.getType().equals(Material.PAPER) && name.equals("§aClear chat")) {
                    interaction(p, "chat clear", e);
                }
                if (itemCurrent.getType().equals(Material.BOOK_AND_QUILL)) {
                    interaction(p, name.equals("§cDisable chat") ? "chat disable" : "chat enable", e);
                }
                if (itemCurrent.getType().equals(Material.FIREBALL) && name.equals("§6Add a banword")) {
                    interaction(p, "gamemode creative", e);
                }
            }
        }
    }
    public void interaction(Player player, String command, InventoryClickEvent e){
        e.setCancelled(true);
        player.closeInventory();
        player.performCommand(command);

    }

}
