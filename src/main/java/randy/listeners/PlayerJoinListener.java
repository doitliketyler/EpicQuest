package randy.listeners;

import java.util.List;

import randy.engine.EpicPlayer;
import randy.engine.EpicSystem;
import randy.filehandlers.SaveLoader;
import randy.questentities.QuestEntity;
import randy.questentities.QuestEntityHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		SaveLoader.loadPlayer(event.getPlayer().getUniqueId());
		
		//Set basic stuff for villager
		List<QuestEntity> entityList = QuestEntityHandler.GetQuestEntityList();
		for(int i = 0; i < entityList.size(); i++){
			entityList.get(i).SetFirstInteraction(EpicSystem.getEpicPlayer(event.getPlayer()));
		}
		
		EpicSystem.getEpicPlayer(event.getPlayer()).setPlayerName(event.getPlayer().getName());
		
		EpicSystem.idMap.put(event.getPlayer().getUniqueId(), event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		if(EpicSystem.getEpicPlayer(event.getPlayer().getUniqueId()) != null){
			EpicPlayer epicPlayer = EpicSystem.getEpicPlayer(event.getPlayer().getUniqueId());
			SaveLoader.savePlayer(epicPlayer);
		}
		EpicSystem.idMap.remove(event.getPlayer());
	}
}
