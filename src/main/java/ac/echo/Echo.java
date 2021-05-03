package ac.echo;

import org.bukkit.plugin.java.JavaPlugin;

import ac.echo.classes.Config;
import ac.echo.commands.EchoCommand;

public class Echo extends JavaPlugin {

    Config config;

    @Override
    public void onEnable() {
        config = new Config();
        config.importConfig();
        
        getCommand("echo").setExecutor(new EchoCommand(this));
    }

    @Override
    public void onDisable() {
        config.exportConfig();
        this.getServer().getScheduler().cancelTasks(this);
    }

    public Config getConfiguration(){
        return config;
    }
}