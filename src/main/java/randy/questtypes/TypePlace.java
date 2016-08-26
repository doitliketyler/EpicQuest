package randy.questtypes;

import java.util.List;

import randy.engine.EpicPlayer;
import randy.engine.EpicSystem;
import randy.quests.EpicQuestTask;
import randy.quests.EpicQuestTask.TaskTypes;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TypePlace extends TypeBase implements Listener{

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){

		//Get player and questlist
		Player player = event.getPlayer();
		EpicPlayer epicPlayer = EpicSystem.getEpicPlayer(player.getUniqueId());
		List<EpicQuestTask> taskList = epicPlayer.getTasksByType(TaskTypes.PLACE_BLOCK);

		//Block information
		Block block = event.getBlock();
		Material blockPlaced = block.getType();
		
		//Check block list
		if(EpicSystem.getBlockList().contains(block.getLocation().toVector())) return;

		for(EpicQuestTask task : taskList){
			String blockneeded = task.getTaskID();

			if(blockPlaced == Material.matchMaterial(blockneeded)){
				task.ProgressTask(1, epicPlayer, true);
				EpicSystem.getBlockList().add(block.getLocation().toVector());
			}
		}
	}
}
