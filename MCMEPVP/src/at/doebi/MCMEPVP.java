package at.doebi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MCMEPVP extends JavaPlugin{
	
	//onEnable, run when server starts
	@Override
	public void onEnable() {
		//registering Listener
		getServer().getPluginManager().registerEvents(new MCMEPVPListener(this), this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("pvp")){
			//What to do when a player types /pvp
			String method = args[0];
			if(method == "join"){
				//Assign player to a team
			}
			if(method == "leave"){
			}
			if(method == "class"){
				String classname = args[1];
				
			}
			return true;
		}
		return false; 
	}
}