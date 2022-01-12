package net.antwan.simplechat;

import net.antwan.simplechat.commands.ChatClass;
import net.antwan.simplechat.filesmanager.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    public FileConfiguration config = this.getConfig();

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
        getServer().getPluginManager().registerEvents(new ChatClass(this), this);
        getLogger().info("Commands successfully enabled");
    }
    public void loadFiles(){
        this.saveDefaultConfig();
        this.messages = new MessagesManager(this);
        messages.saveDefaultConfig();
    }
    // Clear chat method
    public void clearChat(){
        for(int a = 0; a < 125; a++){
            Bukkit.broadcastMessage(" ");
        }
    }

    }




