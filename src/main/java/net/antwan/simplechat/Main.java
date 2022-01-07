package net.antwan.simplechat;

import net.antwan.simplechat.commands.ChatClass;
import net.antwan.simplechat.filesmanager.MessagesManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    public FileConfiguration config = this.getConfig();

    public MessagesManager messages;

    public String configAccessString(String path){
        return config.getString(path);
    }
    public boolean configAccessBoolean(String path){
        return config.getBoolean(path);
    }
    public int configAccessInt(String path){
        return config.getInt(path);
    }

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
        getLogger().info("Commands successfully enabled");
    }
    public void loadFiles(){
        this.saveDefaultConfig();

        this.messages = new MessagesManager(this);
        messages.saveDefaultConfig();
    }
}
