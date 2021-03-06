package myblock;

import java.util.HashMap;

import cn.nukkit.*;
import cn.nukkit.plugin.*;
import cn.nukkit.event.*;
import cn.nukkit.event.block.*;
import cn.nukkit.utils.*;
import cn.nukkit.level.*;
import cn.nukkit.block.*;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class Main extends PluginBase implements Listener{
	public Config blockDB;
	private HashMap<Player, Boolean> edit;
	public void onEnable() {
		getDataFolder().mkdirs();
		blockDB = new Config(this.getDataFolder() + "/blockDB.json", Config.JSON);
		edit = new HashMap<Player, Boolean>();
		getServer().getPluginManager().registerEvents(this, this);
	}
	public void onDisable() {
		blockDB.save();
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (edit.containsKey(player)) {
			return;
		}
		Block block = event.getBlock();
		blockDB.set(posToString(new Position(block.getX(), block.getY(), block.getZ(), block.getLevel())), player.getName().toLowerCase());
	}
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		String pos = posToString(new Position(block.getX(), block.getY(), block.getZ(), block.getLevel()));
		String name = blockDB.get(pos, null);
		if (name == null) {
			return;
		}
		if (!name.equals(player.getName().toLowerCase()) && !player.isOp()) {
			event.setCancelled();
		} else {
			blockDB.remove(pos);
		}
	}
	public String posToString(Position pos) {
		return (int)pos.getX()+":"+(int)pos.getY()+":"+(int)pos.getZ()+":"+pos.getLevel().getFolderName();
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!edit.containsKey((Player)sender)){
			edit.put((Player)sender, true);
			sender.sendMessage("편집모드로 변경되었습니다.");
		} else {
			edit.remove((Player)sender);
			sender.sendMessage("편집모드가 해제되었습니다.");
		}
		return true;
	}
}
