package at.doebi;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
	public static int BlueMates = 0;
	public static int RedMates = 0;
	public static int GameStatus = 0;

	//create a HashMap for storing class loadouts
	public static HashMap<String, Inventory> Classes = new HashMap<String, Inventory>();
	
	//create a HashMap for storing spawns
	public static HashMap<String, Location> Spawns = new HashMap<String, Location>();

	//onEnable, run when server starts
	@Override
	public void onEnable() {
		//registering Listener
		getServer().getPluginManager().registerEvents(new MCMEPVPListener(this), this);
        for (Player currentplayer : Bukkit.getOnlinePlayers()) {
        	MCMEPVP.addTeam(currentplayer,"spectator");
        }
        GameStatus = 0;
        //TODO
        //check if config file is present and load it
        //if not create a new one
	}
	
	public static void addTeam(Player player,String Team){
		//clear Inventory
		player.getInventory().clear();
		if(Team == "spectator"){
			PlayerTeams.put(player.getName(), "spectator");
		}else{
			player.sendMessage(ChatColor.YELLOW + "You're now in Team " + Team.toUpperCase() + "!");
			if(Team == "red"){
				RedMates++;
				PlayerTeams.put(player.getName(), "red");
			}else{
				if(Team == "blue"){
					BlueMates++;
					PlayerTeams.put(player.getName(), "blue"); 
				}
			}
		}
		
	}
	
	public static void removeTeam(Player player){
		player.sendMessage(ChatColor.YELLOW + "You left your Team!");
		//set player Spectator
		PlayerTeams.put(player.getName(), "spectator");
		//clear Inventory
		player.getInventory().clear();
		//adjust Playercounter
		String Team = PlayerTeams.get(player.getName());
		if(Team == "red"){
			RedMates--;
		}else{
			if(Team == "blue"){
				BlueMates--;
			}
		}	
	}
	
	public static void sendToTeam(String Message, Player chatter) {
        for (Player player : Bukkit.getOnlinePlayers()) {
        	String Team = PlayerTeams.get(chatter.getName());
        	String PlayerTeam = PlayerTeams.get(player.getName());
        	String label = "[Jerk] ";
        	if(Team == "red"){
        		label = ChatColor.RED + "[Team Red] ";
        	}
        	if(Team == "blue"){
        		label = ChatColor.BLUE + "[Team Blue] ";
        	}
        	if(Team == "spectator"){
        		label = "[Spectator] ";
        	}
        	if(Team == "participant"){
        		label = "[Participant] ";
        	}
        	if(PlayerTeam == "spectator" || PlayerTeam == "participant" || PlayerTeam == Team){
        		player.sendMessage(label + chatter.getName() + ": " + ChatColor.WHITE + Message);
        	}
        }
    }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		//Identify the player
		Player player = sender.getServer().getPlayer(sender.getName());
		if(cmd.getName().equalsIgnoreCase("pvp")){
			//What to do when a player types /pvp
			if(args.length != 0){
				String method = args[0];
				//JOIN
				//TODO Prevent joining when game is running
				if(method.equalsIgnoreCase("join")){
					String Team = PlayerTeams.get(player.getName());
					//Check if player has a team already
					if(Team == "participant"){
						player.sendMessage(ChatColor.DARK_RED + "You are already participating in the PVP Event!");
					}else{
						//queue Player
						queuePlayer(player);
					}
					return true;
				}
				//LEAVE
				if(method.equalsIgnoreCase("leave")){
					//Remove Player from old Team and assign to spectator if they leave
					removeTeam(player);
					return true;
				}
				//CLASS
				if(method.equalsIgnoreCase("class")){
					String classname = args[1];
					//Allow mods to create classes by storing their inventory in a hashmap
					//with the class as the key
					//set a player's inventory to that of the given class if it exists
					Inventory classinventory = Classes.get(classname);
					if(classinventory == null){
						player.sendMessage(ChatColor.DARK_RED + "No such class '" + classname + "'!");
					}
					else{
						player.getInventory().clear();
						ItemStack[] contents = classinventory.getContents();
						player.getInventory().setContents(contents);
					}
					return true;
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("shout")){
			if(args.length != 0){
				String msg = args[0];
	            if (args.length > 1) {
	                for (int i = 1; i < args.length; i++) {
	                    msg += " " + args[i];
	                }
	            }
	            for (Player currentplayer : Bukkit.getOnlinePlayers()) {
	            	currentplayer.sendMessage(ChatColor.GRAY + player.getName() + " shouts: " + msg);
	    			return true;
	            }
			}
		}
		if(cmd.getName().equalsIgnoreCase("pa")){
			if(args.length == 0){
				player.sendMessage("/pa [start|set]");
				return true;
			}else{
				String method = args[0];
				if(method.equalsIgnoreCase("start")){
			        //Broadcast
					getServer().broadcastMessage(ChatColor.GREEN + "The PVP Event starts in a few seconds!");
					getServer().broadcastMessage(ChatColor.GREEN + "All Participants will be assigned to a team and teleported to their spawn!");
			        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			           public void run() {
					        for (Player user : Bukkit.getOnlinePlayers()) {
						        //Assign Teams
					        	if(PlayerTeams.get(user.getName()) == "participant"){
									if(BlueMates > RedMates){
										addTeam(user,"red");
									}else{
										if(BlueMates < RedMates){
											addTeam(user,"blue");
										}
										else{
											boolean random = (Math.random() < 0.5);
											if(random == true){
												addTeam(user,"red");
											}
											else{
												addTeam(user,"blue");
											}
										}
									}	
					        	}
					        	//Teleport User
				        		user.teleport(Spawns.get(PlayerTeams.get(user.getName())));
					        }
			           }
			        }, 100L);
					return true;
				}
				if(method.equalsIgnoreCase("set")){
					if(args.length > 1){			
						if(args[1].equalsIgnoreCase("class")){
							if(args.length < 3){
								player.sendMessage(ChatColor.DARK_RED + "Provide a name for the class you want to create!");
							}else{
								String classname = args[2];
								PlayerInventory inv = player.getInventory();
								Classes.put(classname, inv);
								player.sendMessage(ChatColor.YELLOW + "Saved your inventory as Class '" + classname + "'!");
								return true;
							}
						}
						if(args[1].equalsIgnoreCase("blue")){
							Location loc = player.getLocation();
							Spawns.put("blue", loc);
							player.sendMessage(ChatColor.YELLOW + "Saved your Location as Team Blue's Spawn!");
							return true;
						}
						if(args[1].equalsIgnoreCase("red")){
							Location loc = player.getLocation();
							Spawns.put("red", loc);
							player.sendMessage(ChatColor.YELLOW + "Saved your Location as Team Red's Spawn!");
							return true;
						}
						if(args[1].equalsIgnoreCase("spectator")){
							Location loc = player.getLocation();
							Spawns.put("spectator", loc);
							player.sendMessage(ChatColor.YELLOW + "Saved your Location as Spectator's Spawn!");
							return true;
						}
					}else{
						player.sendMessage("/pa set [blue|red|spectator|class]");
						return true;
					}
				}
			}
		}
		return false;
	}

	private void queuePlayer(Player player) {
		PlayerTeams.put(player.getName(), "participant");
		player.sendMessage(ChatColor.YELLOW + "You are participating! Wait for the next Event to start!");
	}
}
