package at.doebi;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class MCMEPVP extends JavaPlugin{
	//create a HashMap and a count for player teams
	public static HashMap<String, String> PlayerTeams = new HashMap<String, String>();
	public static int Blue = 0;
	public static int Red = 0;

	//create a HashMap for storing class loadouts
	public static HashMap<String, Inventory> Classes = new HashMap<String, Inventory>();

	//onEnable, run when server starts
	@Override
	public void onEnable() {
		//registering Listener
		getServer().getPluginManager().registerEvents(new MCMEPVPListener(this), this);
	}
	
	public static void addTeam(Player player,String Team){
		//clear Inventory
		player.getInventory().clear();
		if(Team == "red"){
			Red++;
			PlayerTeams.put(player.getName(), "blue");
		}else{
			if(Team == "blue"){
				Blue++;
				PlayerTeams.put(player.getName(), "red"); 
			}
		}
		
	}
	
	public static void removeTeam(Player player){
		//set player Spectator
		PlayerTeams.put(player.getName(), "spectator");
		//clear Inventory
		player.getInventory().clear();
		//adjust Playercounter
		String Team = PlayerTeams.get(player.getName());
		if(Team == "red"){
			Red--;
		}else{
			if(Team == "blue"){
				Blue--;
			}
		}
		
	}
	
	public static void sendToTeam(String Message, Player chatter) {
        for (Player player : Bukkit.getOnlinePlayers()) {
        	String Team = PlayerTeams.get(chatter.getName());
        	ChatColor Color = ChatColor.WHITE;
        	if(Team == "red"){
        		Color = ChatColor.RED;
        	}
        	if(Team == "blue"){
        		Color = ChatColor.BLUE;
        	}
            if (PlayerTeams.get(player.getName()) == Team || PlayerTeams.get(player.getName()) == "spectator") {
                player.sendMessage(Color +"[Team "+ Team + "] " + chatter.getName() + ": " + ChatColor.WHITE + Message);
            }
        }
    }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("pvp")){
			//Identify the player
			Player player = sender.getServer().getPlayer(sender.getName());
			//What to do when a player types /pvp
			String method = args[0];
			//JOIN
			if(method == "join"){
				//Assign player to a team with fewest members
				if(Blue > Red){
					addTeam(player,"Red");
				}else{
					if(Blue < Red){
						addTeam(player,"Blue");
					}
					else{
						boolean random = (Math.random() < 0.5);
						if(random == true){
							addTeam(player,"Red");
						}
						else{
							addTeam(player,"Blue"); 
						}
					}
				}
			}else{
				//LEAVE
				if(method == "leave"){
					//Remove Player from old Team and assign to spectator if they leave
					removeTeam(player);
				}else{
					//CLASS
					if(method == "class"){
						String classname = args[1];
						//Allow mods to create classes by storing their inventory in a hashmap
						//with the class as the key
						if(classname == "set"){
							String classtoset = args[2];
							PlayerInventory createinventory = player.getInventory();
							Classes.put(classtoset, createinventory);
						}
						else{
							//set a player's inventory to that of the given class if it exists
							Inventory classinventory = Classes.get(classname);
							if(classinventory == null){
								player.sendMessage("no such class");
							}
							else{
								player.getInventory().clear();
								ItemStack[] contents = classinventory.getContents();
								player.getInventory().setContents(contents);
							}
						}
					}else{
						//METHOD NOT VALID
						sender.sendMessage("Method "+method+" is no valid!");
					}
				}
			}
			return true;
		}
		return false; 
	}
}
