package randy.listeners;

import java.util.ArrayList;
import java.util.List;

import randy.engine.EpicPlayer;
import randy.engine.EpicSystem;
import randy.epicquest.EpicMain;
import randy.quests.EpicQuest;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class OpenBookListener implements Listener{

	@EventHandler
	public void onEditBook(PlayerItemHeldEvent event){
		
		//Get player and the action
		Player player = event.getPlayer();
		EpicPlayer epicPlayer = EpicSystem.getEpicPlayer(player.getUniqueId());	
		ItemStack inHand = player.getInventory().getItem(event.getNewSlot());

		if(inHand != null && inHand.getType() == Material.WRITTEN_BOOK){
			
			BookMeta book = (BookMeta)inHand.getItemMeta();
			if(book.hasTitle() && book.getTitle().equalsIgnoreCase("Quest Book")){
				
				//Open zeh quest book!
				inHand.setItemMeta(SetQuestBookPages(epicPlayer, book));
			}
		}
	}

	public static BookMeta SetQuestBookPages(EpicPlayer epicPlayer, BookMeta book){
		
		//Empty the old book
		book.setPages(new ArrayList<String>());
		
		book.addPage("\n\n\n\n       Quest Book"); //Center this line
		List<EpicQuest> questList = epicPlayer.getQuestList();
		
		/*
		 * QUEST INDEX
		 */
		StringBuilder pageText = new StringBuilder();
		for(int questNo = 0; questNo < questList.size(); questNo++){
			
			EpicQuest quest = questList.get(questNo);
			String questName = quest.getQuestName();
			
			if(questName.length() >= 21){
				questName = quest.getQuestName().substring(0, 15) + "...";
			}
			
			pageText.append((questNo + 3));
			pageText.append(": ");
			pageText.append(questName);
			pageText.append("\n");
			
			if(questNo == 12 || questNo == (questList.size() - 1)){
				book.addPage(pageText.toString());				
				pageText = new StringBuilder();
			}
		}
		
		/*
		 * SPECIFIC QUEST INFO
		 */
		for(int questNo = 0; questNo < questList.size(); questNo++){
			
			pageText = new StringBuilder();
			
			EpicQuest quest = questList.get(questNo);
			String questName = quest.getQuestName();
			
			//Title
			pageText.append(ChatColor.GOLD);
			pageText.append(questName);
			pageText.append("\n\n");
			pageText.append(ChatColor.BLACK);
			
			//Description
			String questStart = quest.getQuestStart();
			pageText.append(questStart);
			pageText.append("\n\n");
			
			//Objectives
			StringBuilder taskInfo = new StringBuilder();
			for(int i = 0; i < quest.getTasks().size(); i++){
				taskInfo.append(quest.getTasks().get(i).getPlayerTaskProgressText());
				taskInfo.append("\n");
			}
			pageText.append(taskInfo.toString());
			
			book.addPage(pageText.toString());
		}
		
		StringBuilder statistics = new StringBuilder();
		statistics.append(""+ ChatColor.BLACK + ChatColor.ITALIC + "Statistics\n\n");
		statistics.append(ChatColor.RED + "Quests get: " + epicPlayer.playerStatistics.GetQuestsGet() + "\n");
		statistics.append(ChatColor.RED + "Quests finished: " + epicPlayer.playerStatistics.GetQuestsCompleted() + "\n");
		statistics.append(ChatColor.RED + "Quests dropped: " + epicPlayer.playerStatistics.GetQuestsDropped() + "\n");
		if(EpicSystem.enabledMoneyRewards())
			statistics.append(ChatColor.RED + EpicMain.economy.currencyNamePlural() + " earned: " + (int)epicPlayer.playerStatistics.GetMoneyEarned() + "\n");
		statistics.append(ChatColor.RED + "Tasks completed: " + epicPlayer.playerStatistics.GetTasksCompleted());
		book.addPage(statistics.toString());
		
		book.setAuthor("The Almighty One");
		book.setTitle("Quest Book");
		
		epicPlayer.getPlayer().updateInventory();
		
		return book;
	}
	
	public static void UpdateBook(EpicPlayer player){
		Inventory inventory = player.getPlayer().getInventory();
		if(inventory.contains(Material.WRITTEN_BOOK)){
			for(int i = 0; i < inventory.getContents().length; i++){
				ItemStack item = inventory.getContents()[i];
				if(item != null && item.getType() == Material.WRITTEN_BOOK){
					BookMeta bookMeta = (BookMeta)item.getItemMeta();
					SetQuestBookPages(player, bookMeta);
					
					ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
					book.setItemMeta(bookMeta);
					
					inventory.remove(item);
					inventory.setItem(i, book);
				}
			}
		}
	}
}
