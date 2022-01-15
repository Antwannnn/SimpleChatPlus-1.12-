package net.antwan.simplechat;

import net.antwan.simplechat.commands.BanwordClass;
import net.antwan.simplechat.commands.ChatClass;
import net.antwan.simplechat.filesmanager.ConfigManager;
import net.antwan.simplechat.filesmanager.MessagesManager;
import net.antwan.simplechat.listeners.PluginListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    public ConfigManager config;

    public MessagesManager messages;

    private final PluginDescriptionFile getPluginDescriptionFile = this.getDescription();

    private final String pluginAuthors = getPluginDescriptionFile.getAuthors().get(0);

    private final String pluginVersion = getPluginDescriptionFile.getVersion();

    private final String pluginDescription = getPluginDescriptionFile.getDescription();

    @Override
    public void onEnable() {
        loadExec();
        loadFiles();

    }


    @Override
    public void onDisable() {
    }

    public void displayInfo(){
        getLogger().info("Plugin made by: " + pluginAuthors);
        getLogger().info("Running on version " + pluginVersion);
    }

    public void loadExec(){
        getLogger().info("Loading commands...");
        getCommand("chat").setExecutor(new ChatClass(this));
        getCommand("banword").setExecutor(new BanwordClass(this));
        getServer().getPluginManager().registerEvents(new PluginListener(this), this);
        getLogger().info("Commands successfully enabled");
    }
    public void loadFiles(){
        this.config = new ConfigManager(this);
        this.messages = new MessagesManager(this);

        this.config.saveDefaultConfig();
        this.messages.saveDefaultConfig();
    }
    public void clearChat(){
        for(int a = 0; a < 125; a++){
            Bukkit.broadcastMessage(" ");
        }
    }
    public void reloadConfigs(){
        this.config.reloadConfig();
        this.messages.reloadConfig();
    }

    public ItemStack createItem(Material id, String displayName){
        ItemStack current = new ItemStack(id);
        ItemMeta currentMeta = current.getItemMeta();
        currentMeta.setDisplayName(displayName);
        current.setItemMeta(currentMeta);
        return current;
        }
    }




