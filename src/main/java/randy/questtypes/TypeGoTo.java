package randy.questtypes;

import java.util.List;

import randy.engine.EpicPlayer;
import randy.engine.EpicSystem;
import randy.quests.EpicQuestTask;
import randy.quests.EpicQuestTask.TaskTypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TypeGoTo extends TypeBase implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		
		//Get player and questlist
		Player player = event.getPlayer();
		EpicPlayer epicPlayer = EpicSystem.getEpicPlayer(player.getUniqueId());
		List<EpicQuestTask> taskList = epicPlayer.getTasksByType(TaskTypes.GO_TO);
		
		for(EpicQuestTask task : taskList){
			String[] locationArray = task.getTaskID().split("=");
			Location location = new Location(Bukkit.getWorld(locationArray[0]), Integer.parseInt(locationArray[1]), Integer.parseInt(locationArray[2]), Integer.parseInt(locationArray[3]));
			
			if(player.getLocation().distance(location) < 15){
				task.ProgressTask(1, epicPlayer, true);
			}
		}
	}
}
