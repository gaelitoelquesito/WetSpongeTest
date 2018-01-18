package es.minetsii.wetspongetest;


import com.degoos.wetsponge.WetSponge;
import com.degoos.wetsponge.command.WSCommand;
import com.degoos.wetsponge.command.WSCommandSource;
import com.degoos.wetsponge.entity.living.animal.WSCow;
import com.degoos.wetsponge.entity.living.animal.WSPig;
import com.degoos.wetsponge.entity.living.monster.WSCreeper;
import com.degoos.wetsponge.entity.living.player.WSPlayer;
import com.degoos.wetsponge.enums.EnumTextColor;
import com.degoos.wetsponge.plugin.WSPlugin;
import com.degoos.wetsponge.text.WSText;
import java.util.ArrayList;
import java.util.List;

public class WetSpongeTest extends WSPlugin {

	private static WetSpongeTest instance;

	@Override
	public void onEnable() {
		instance = this;
		WetSponge.getEventManager().registerListener(this, this);

		WetSponge.getCommandManager().addCommand(new WSCommand("wsSend", "test") {
			@Override
			public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
				if (!(commandSource instanceof WSPlayer)) return;
				WSPlayer player = (WSPlayer) commandSource;
				new SParachute(player);
			}

			@Override
			public List<String> sendTab(WSCommandSource commandSource, String command, String[] arguments) {
				return new ArrayList<>();
			}
		});

		WetSponge.getCommandManager().addCommand(new WSCommand("test", "test") {
			@Override
			public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
				if (!(commandSource instanceof WSPlayer)) return;
				WSPlayer player = (WSPlayer) commandSource;
				player.getWorld().spawnEntity(WSCreeper.class, player.getLocation().toVector3d()).ifPresent(creeper -> {
					player.getWorld().spawnEntity(WSCow.class, player.getLocation().toVector3d()).ifPresent(creeper::addPassenger);
					player.getWorld().spawnEntity(WSPig.class, player.getLocation().toVector3d()).ifPresent(creeper::addPassenger);
				});
			}

			@Override
			public List<String> sendTab(WSCommandSource commandSource, String command, String[] arguments) {
				return new ArrayList<>();
			}
		});
	}

	public static WetSpongeTest getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
		WetSponge.getServer().getConsole().sendMessage(WSText.builder("Goodbye!").color(EnumTextColor.RED).build());
	}
}
