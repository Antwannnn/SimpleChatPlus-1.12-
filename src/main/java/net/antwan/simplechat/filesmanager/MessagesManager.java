package net.antwan.simplechat.filesmanager;

import net.antwan.simplechat.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class MessagesManager {

    private Main main;

    private File messagesFile = null;

    private FileConfiguration messagesConfig = null;

    public MessagesManager (Main main){
        this.main = main;
        saveDefaultConfig();
    }

    public void reloadConfig(){
        if(this.messagesFile == null)
            this.messagesFile = new File(this.main.getDataFolder() + "messages.yml");

        this.messagesConfig = YamlConfiguration.loadConfiguration(this.messagesFile);

        InputStream defaultStream = this.main.getResource("messages.yml");
        if(defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.messagesConfig.setDefaults(defaultConfig);
        }
    }
    public FileConfiguration getConfig(){
        if(this.messagesConfig == null)
            reloadConfig();
        return this.messagesConfig;
    }
    public void saveConfig(){
        if(this.messagesConfig == null || this.messagesFile == null ){
            return;
        }
        try{
            this.getConfig().save(this.messagesFile);
        } catch(Exception e){
            main.getLogger().log(Level.SEVERE, "Could not save config to: " + this.messagesConfig, e);
        }
    }
    public void saveDefaultConfig(){
        if(this.messagesFile == null)
           this.messagesFile = new File(this.main.getDataFolder(), "messages.yml");
        if(!this.messagesFile.exists()){
            this.main.saveResource("messages.yml", false);
        }
    }

}
