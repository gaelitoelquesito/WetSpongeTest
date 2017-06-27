package es.minetsii.wetspongetest;


import com.degoos.wetsponge.WetSponge;
import com.degoos.wetsponge.enums.EnumTextColor;
import com.degoos.wetsponge.event.WSListener;
import com.degoos.wetsponge.event.entity.player.connection.WSPlayerJoinEvent;
import com.degoos.wetsponge.plugin.WSPlugin;
import com.degoos.wetsponge.text.WSText;

public class WetSpongeTest extends WSPlugin {

    @Override
    public void onEnable() {
        WetSponge.getEventManager().registerListener(this, this);
    }


    @Override
    public void onDisable() {
        WetSponge.getServer().getConsole().sendMessage(WSText.builder("Goodbye!").color(EnumTextColor.RED).build());
    }

    @WSListener
    public void onPlayerJoin(WSPlayerJoinEvent event) {
        event.getPlayer().sendMessage(WSText.builder("A").newLine().append(WSText.of("B")).build());
    }

}
