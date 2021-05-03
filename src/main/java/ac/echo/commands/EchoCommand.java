package ac.echo.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ac.echo.Echo;
import ac.echo.classes.API;
import net.md_5.bungee.api.ChatColor;

public class EchoCommand implements CommandExecutor {

    private Echo echo;

    public EchoCommand(Echo echo){
        this.echo = echo;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;

            if(args.length != 0){
                String subcommand = args[0];
                List<String> subargs = new ArrayList<>();
                Collections.addAll(subargs, args);
                subargs.remove(0);

                switch (subcommand.toLowerCase()) {
                    case "key":
                    updateKey(p, subargs);
                    break;
                    
                    case "alts":
                    getAlts(p, subargs);

                    break;
                    case "no":
                    break;

                    case "maybe":
                    echo.getConfiguration().importConfig();
                    break;

                    default:
                    p.sendMessage("ygsdfs");
                }
            }else{
                sendHelp(p);
            }
        }
        return true;
    }

    private void updateKey(Player p, List<String> subargs){
        if(subargs.size() > 0){
            new Thread(() -> {
                API api = new API();

                if(api.isValidKey(subargs.get(0), p)){
                    echo.getConfiguration().addUser(p.getUniqueId().toString(), subargs.get(0));
                }
            }).start();

        }else{
            sendHelp(p);
        }
    }

    private void getAlts(Player p, List<String> subargs){
        if(subargs.size() > 0){
            new Thread(() -> {

                p.sendMessage("Searching Echo for aliases of " + ChatColor.AQUA + subargs.get(0) + ChatColor.WHITE + "...");
                API api = new API();

                String final_alt_list = String.join(", ", api.getAlts(echo.getConfiguration().getKey(p.getUniqueId().toString()), subargs.get(0), p));;

                if(!final_alt_list.contains(", ")){
                    p.sendMessage(ChatColor.RED + "Cannot find any aliases for this player.");
                }else{

                p.sendMessage(final_alt_list);
                }
            }).start();

        }else{
            sendHelp(p);
        }
    }

    private void sendHelp(Player p){

    }
    
}
