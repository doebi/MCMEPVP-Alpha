package at.doebi;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MCMEPVPListener implements Listener{

	public MCMEPVPListener(MCMEPVP instance) {
	}

	void onPlayerJoin(final PlayerLoginEvent event){
		//If a new Player joins the server make him a spectator
		//and make sure his inventory is empty
		String Playername = event.getPlayer().getName().toLowerCase();
		MCMEPVP.PlayerTeams.put(Playername, new Integer(0));
		event.getPlayer().getInventory().clear();
	}

	void onPlayerLeave(final PlayerQuitEvent event){
		//If Player leaves the server remove him from his team
		//and make him a spectator
		//and make sure his inventory is empty
		String Playername = event.getPlayer().getName().toLowerCase();
		int team = MCMEPVP.PlayerTeams.get(Playername);
		if(team == 1){
			MCMEPVP.Red = MCMEPVP.Red - 1;
		}
		if(team == 2){
			MCMEPVP.Blue = MCMEPVP.Blue - 1;
		}
		MCMEPVP.PlayerTeams.put(Playername, new Integer(0));
		event.getPlayer().getInventory().clear();
	}

	void onPlayerKick(final PlayerKickEvent event){
		//If Player is kicked remove him from his team
		//and make him a spectator
		//and make sure his inventory is empty
		String Playername = event.getPlayer().getName().toLowerCase();
		int team = MCMEPVP.PlayerTeams.get(Playername);
		if(team == 1){
			MCMEPVP.Red = MCMEPVP.Red - 1;
		}
		if(team == 2){
			MCMEPVP.Blue = MCMEPVP.Blue - 1;
		}
		MCMEPVP.PlayerTeams.put(Playername, new Integer(0));
		event.getPlayer().getInventory().clear();
	}

	void onPlayerDeath(final PlayerDeathEvent event){
		//If Player dies remove him from his team
		//and make him a spectator
		//and make sure his inventory is empty
		String Playername = event.getEntity().getName().toLowerCase();
		//***I'm not sure if getEntity() will get the player or the
		//player's killer... getPlayer() doesn't exist for PlayerDeathEvent
		int team = MCMEPVP.PlayerTeams.get(Playername);
		if(team == 1){
			MCMEPVP.Red = MCMEPVP.Red - 1;
		}
		if(team == 2){
			MCMEPVP.Blue = MCMEPVP.Blue - 1;
		}
		MCMEPVP.PlayerTeams.put(Playername, new Integer(0));
		event.getEntity().getInventory().clear();
	}
}