package net.antwan.simplechat.commands;

import net.antwan.simplechat.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatClass implements CommandExecutor {

    private Main main;

    public ChatClass(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        boolean isPrefixDisplayed = main.configAccessBoolean("prefix.is-displayed");

        boolean isMuted = main.configAccessBoolean("chat.is-enabled");

        String prefix = main.configAccessString("prefix.message").replace("&", "§") + " ";

        if(p.hasPermission("scp.chat")){
            if(args.length == 0){
                p.sendMessage(prefix + "§cNot enough arguments. Type /&l<scp help>&r&c.");
            }
        }

        return false;
    }
}
