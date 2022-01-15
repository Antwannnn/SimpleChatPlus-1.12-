package net.antwan.simplechat.listeners;

import net.antwan.simplechat.Main;
import net.antwan.simplechat.commands.BanwordClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class PluginListener implements Listener {

    private Main main;

    public PluginListener(Main main){
        this.main = main;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event){

        Player p = event.getPlayer();

        FileConfiguration mainConfig = main.config.getConfig();

        FileConfiguration messageConfig = main.messages.getConfig();

        boolean isEnabled = mainConfig.getBoolean("chat.state");

        String prefix = ChatColor.translateAlternateColorCodes('&', mainConfig.getString("prefix")).replace("%server_name%", Bukkit.getServer().getName())  + " ";

        String message = event.getMessage();

        String[] msgList = event.getMessage().split(" ");

        List<String> bWord = mainConfig.getStringList("ban-word");

        if(!BanwordClass.inEditor.containsKey(p.getUniqueId())) {

            if(!isEnabled){
                if (p.hasPermission("scp.chat.bypass"))
                    if (mainConfig.getBoolean("chat.bypass")) {
                        p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', main.messages.getConfig().getString("bypass-message")));
                        return;
                    }
            event.setCancelled(true);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', main.messages.getConfig().getString("disabled-message")));
            }
            else {
                for (String word : msgList) {
                    for (String cword : bWord) {
                        if (cword.equalsIgnoreCase(word)) {
                            if (p.hasPermission("scp.banword.bypass")) {
                                if (mainConfig.getBoolean("ban-word-bypass")) {
                                    p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', main.messages.getConfig().getString("ban-word-bypass-message").replace("%word%", word)));
                                    return;
                                }
                            }
                            event.setCancelled(true);
                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', main.messages.getConfig().getString("ban-word-warning-message").replace("%word%", word)));
                        }

                    }
                }
            }
        }
        else{
            if(!message.equals("exit")){

                if(bWord.contains(message)) {
                    bWord.remove(message);
                    mainConfig.set("ban-word", bWord);
                    p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',main.messages.getConfig().getString("remove-ban-word-message").replace("%word%", message)));

                }
                else{
                    bWord.add(message);
                    mainConfig.set("ban-word", bWord);
                    p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',main.messages.getConfig().getString("add-ban-word-message").replace("%word%", message)));
                }
                main.config.saveConfig();
            }
            else{
                BanwordClass.inEditor.remove(p.getUniqueId());
                p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',main.messages.getConfig().getString("exit-editor-message").replace("%word%", message)));
            }
            event.setCancelled(true);
        }


        }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Inventory current = e.getInventory();
        if(current.getName().equals(ChatColor.translateAlternateColorCodes('&', main.config.getConfig().getString("gui-name")))){
            ItemStack itemCurrent = e.getCurrentItem();
            if(itemCurrent.hasItemMeta()) {
                ClickType c = e.getClick();
                String name = itemCurrent.getItemMeta().getDisplayName();
                if (itemCurrent.getType().equals(Material.PAPER) && name.equals("§aClear chat")) {
                    interaction(p, "chat clear", e);
                }
                if (itemCurrent.getType().equals(Material.BOOK_AND_QUILL)) {
                    interaction(p, name.equals("§cDisable chat") ? "chat disable" : "chat enable", e);
                }
                if (itemCurrent.getType().equals(Material.CLAY_BALL) && name.equals("§6Banword Editor")) {
                    interaction(p, "bw edit", e);
                }
            }
        }
    }
    public void interaction(Player player, String command, InventoryClickEvent e) {
        ClickType c = e.getClick();
        if (c.isShiftClick() || c.isRightClick() || c.isLeftClick() || c.isKeyboardClick()) {
            e.setCancelled(true);
            player.closeInventory();
            player.performCommand(command);
        }
    }

}
