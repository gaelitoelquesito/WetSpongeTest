package es.minetsii.wetspongetest;


import es.minetsii.wetsponge.WetSponge;
import es.minetsii.wetsponge.entity.living.animal.WSPig;
import es.minetsii.wetsponge.plugin.WSPlugin;

public class WetSpongeTest extends WSPlugin {

	@Override
	public void onEnable () {
		System.out.println("Hi! I'm a plugin!");
		WetSponge.getEventManager().registerListener(this, this);
		new Thread(() -> {
			while (WetSpongeTest.this.isEnabled()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				WetSponge.getServer().getWorlds().forEach(world -> world.getEntities().stream().filter(wsEntity -> wsEntity instanceof WSPig).forEach(pig -> {
					((WSPig)pig).setSaddled(true);
				}));
			}
		}).start();
	}


	@Override
	public void onDisable () {
		System.out.println("Goodbye!");
	}
}
