package at.doebi;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MCMEPVPListener implements Listener{
	
	public MCMEPVPListener(MCMEPVP instance) {
	}

	void onPlayerJoin(final PlayerLoginEvent event){
		MCMEPVP.addTeam(event.getPlayer(),"spectator");
	}

	void onPlayerLeave(final PlayerQuitEvent event){
		MCMEPVP.removeTeam(event.getPlayer());
	}

	/*
	 * 
	 * i think when kicking someone a "leave" event is goin to be casted too, so we would have it double
	 * 
	 * void onPlayerKick(final PlayerKickEvent event){
		//If Player is kicked remove him from his team
		//and make him a spectator
		//and make sure his inventory is empty
		String Playername = event.getPlayer().getName().toLowerCase();
		String team = MCMEPVP.PlayerTeams.get(Playername);
		if(team == 1){
			MCMEPVP.Red = MCMEPVP.Red - 1;
		}
		if(team == 2){
			MCMEPVP.Blue = MCMEPVP.Blue - 1;
		}
		MCMEPVP.PlayerTeams.put(Playername, new Integer(0));
		event.getPlayer().getInventory().clear();
	}*/
	void onPlayerChat(final PlayerChatEvent event){
		if(MCMEPVP.PlayerTeams.get(event.getPlayer().getName()) == "spectator"){
			//Spectator chats
			
		}else{
			event.setCancelled(true);
			MCMEPVP.sendToTeam(event.getMessage(), event.getPlayer());
		}
	}
	
	void onPlayerDeath(final PlayerDeathEvent event){
		MCMEPVP.removeTeam(event.getEntity());
	}
}