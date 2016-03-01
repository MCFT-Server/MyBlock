package myblock;

import cn.nukkit.*;
import cn.nukkit.plugin.*;
import cn.nukkit.event.*;
import cn.nukkit.event.block.*;
import cn.nukkit.utils.*;
import cn.nukkit.level.*;
import cn.nukkit.block.*;

public class Main extends PluginBase implements Listener{
	public Config blockDB;
	public void onEnable() {
		getDataFolder().mkdirs();
		blockDB = new Config(this.getDataFolder() + "/blockDB.json", Config.JSON);
		getServer().getPluginManager().registerEvents(this, this);
	}
	public void onDisable() {
		blockDB.save();
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		blockDB.set(posToString(new Position(block.getX(), block.getY(), block.getZ(), block.getLevel())), player.getName().toLowerCase());
	}
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (player.isOp()) {
			return;
		}
		String name = blockDB.get(posToString(new Position(block.getX(), block.getY(), block.getZ(), block.getLevel())), null);
		if (name == null) {
			return;
		}
		if (!name.equals(player.getName().toLowerCase())) {
			event.setCancelled();
		}
	}
	public String posToString(Position pos) {
		return (int)pos.getX()+":"+(int)pos.getY()+":"+(int)pos.getZ()+":"+pos.getLevel().getFolderName();
	}
}
