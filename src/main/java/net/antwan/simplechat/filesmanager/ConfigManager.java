package net.antwan.simplechat.filesmanager;

import net.antwan.simplechat.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigManager {

    private Main main;

    private File configFile = null;

    private FileConfiguration configuration = null;

    public ConfigManager (Main main){
        this.main = main;
        saveDefaultConfig();
    }

    public void reloadConfig(){
        if(this.configFile == null)
            this.configFile = new File(this.main.getDataFolder() + "config.yml");

        this.configuration = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.main.getResource("config.yml");
        if(defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.configuration.setDefaults(defaultConfig);
        }
    }
    public FileConfiguration getConfig(){
        if(this.configuration == null)
            reloadConfig();
        return this.configuration;
    }
    public void saveConfig(){
        if(this.configuration == null || this.configFile == null ){
            return;
        }
        try{
            this.getConfig().save(this.configFile);
        } catch(Exception e){
            main.getLogger().log(Level.SEVERE, "Could not save config to: " + this.configuration, e);
        }
    }
    public void saveDefaultConfig(){
        if(this.configFile == null)
            this.configFile = new File(this.main.getDataFolder(), "config.yml");
        if(!this.configFile.exists()){
            this.main.saveResource("config.yml", false);
        }
    }

}
