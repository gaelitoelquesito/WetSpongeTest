package es.minetsii.wetspongetest;


import com.degoos.wetsponge.WetSponge;
import com.degoos.wetsponge.command.WSCommand;
import com.degoos.wetsponge.command.WSCommandSource;
import com.degoos.wetsponge.entity.living.player.WSPlayer;
import com.degoos.wetsponge.enums.EnumEnvironment;
import com.degoos.wetsponge.enums.EnumTextColor;
import com.degoos.wetsponge.material.blockType.WSBlockType;
import com.degoos.wetsponge.material.blockType.WSBlockTypes;
import com.degoos.wetsponge.plugin.WSPlugin;
import com.degoos.wetsponge.text.WSText;
import com.degoos.wetsponge.world.*;

import java.util.List;

public class WetSpongeTest extends WSPlugin {

    private WSWorld world;

    @Override
    public void onEnable() {
        WetSponge.getEventManager().registerListener(this, this);
        WSWorldProperties worldProperties = new UnlinkedWorldProperties("Test");
        worldProperties.setGenerateSpawnOnLoad(false);
        world = WetSponge.getServer().createWorld(worldProperties, EnumEnvironment.OVERWORLD).orElseThrow(NullPointerException::new);
        world.getGenerator().setBaseGenerationPopulator((world, volume) -> {
            for (int x = volume.getBlockMin().getX(); x <= volume.getBlockMax().getX(); x++)
                for (int z = volume.getBlockMin().getZ(); z <= volume.getBlockMax().getZ(); z++)
                    for (int y = 0; y < 5; y++) {
                        WSBlockType type;
                        switch (y) {
                            case 0:
                                type = WSBlockTypes.BEDROCK.getDefaultType();
                                break;
                            case 1:
                            case 2:
                            case 3:
                                type = WSBlockTypes.DIRT.getDefaultType();
                                break;
                            case 4:
                                type = WSBlockTypes.GRASS.getDefaultType();
                                break;
                            default:
                                type = WSBlockTypes.AIR.getDefaultType();
                                break;
                        }
                        volume.setBlock(x, y, z, type);
                    }
        });
        world.setAutoSave(false);

        WetSponge.getCommandManager().addCommand(new WSCommand("tpToWorld", "Tp to world", "ttw") {
            @Override
            public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
                if (commandSource instanceof WSPlayer)
                    ((WSPlayer) commandSource).setLocation(WSLocation.of(world, 0, 4, 0));
            }

            @Override
            public List<String> sendTab(WSCommandSource commandSource, String command, String[] arguments) {
                return null;
            }
        });

        WetSponge.getCommandManager().addCommand(new WSCommand("unloadChunk", "Unload Chunk", "ttw") {
            @Override
            public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
                if (commandSource instanceof WSPlayer) {
                    WSPlayer player = (WSPlayer) commandSource;
                    WSChunk chunk = player.getWorld().getChunkAtLocation(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
                    player.sendMessage("Unloaded: "+chunk.unload());
                }
            }

            @Override
            public List<String> sendTab(WSCommandSource commandSource, String command, String[] arguments) {
                return null;
            }
        });
    }


    @Override
    public void onDisable() {
        WetSponge.getServer().getConsole().sendMessage(WSText.builder("Goodbye!").color(EnumTextColor.RED).build());
    }


}
