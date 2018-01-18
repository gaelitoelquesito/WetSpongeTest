package es.minetsii.wetspongetest;

import com.degoos.wetsponge.WetSponge;
import com.degoos.wetsponge.entity.living.WSArmorStand;
import com.degoos.wetsponge.entity.living.player.WSPlayer;
import com.degoos.wetsponge.enums.EnumEquipType;
import com.degoos.wetsponge.event.WSListener;
import com.degoos.wetsponge.event.entity.WSEntityDismountEvent;
import com.degoos.wetsponge.item.WSItemStack;
import com.degoos.wetsponge.material.blockType.WSBlockTypes;
import com.degoos.wetsponge.task.WSTask;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector4d;
import java.util.HashMap;
import java.util.Map;

public class SParachute {


	private WSArmorStand armorStand;
	private WSPlayer matchPlayer;
	private WSTask task;
	private boolean canDismount;
	private Map<Vector4d, WSArmorStand> armorStands;

	public SParachute(WSPlayer matchPlayer) {
		this.matchPlayer = matchPlayer;
		this.task = null;
		this.canDismount = false;
		this.armorStands = new HashMap<>();

		this.armorStand = matchPlayer.getWorld().spawnEntity(WSArmorStand.class, matchPlayer.getLocation().toVector3d()).orElse(null);
		if (armorStand == null) return;

		getArmorStands();

		matchPlayer.setFlying(false);
		armorStand.setInvisible(true);
		armorStand.setBasePlate(false);
		armorStand.addPassenger(matchPlayer);

		WetSponge.getEventManager().registerListener(this, WetSpongeTest.getInstance());

		task = WSTask.of(() -> {
			Vector3d vector3d = matchPlayer.getLocation().getFacingDirection().mul(0.4);
			if (vector3d.getY() > -0.15) vector3d = vector3d.mul(1, 0, 1).add(0, -0.15, 0);
			armorStand.setVelocity(vector3d);
			armorStands.forEach((relative, wsArmorStand) -> {
				wsArmorStand.setLocation(armorStand.getLocation().add(
					relative.getX() * Math.cos(Math.toRadians(matchPlayer.getRotation().getY())), relative.getY(),
					relative.getX() * Math.sin(Math.toRadians(matchPlayer.getRotation().getY()))));
				wsArmorStand.setHeadDirection(new Vector3d(wsArmorStand.getHeadDirection().getX(), relative.getW() - matchPlayer.getRotation().getY(), 180));
			});
			for (double i = -1.5; i < 1; i += 0.5) {
				if (armorStand.getLocation().add(0, i, 0).getBlock().getId() != 0) {
					armorStand.setLocation(armorStand.getLocation().add(0, 2 - i, 0));
					canDismount = true;
					matchPlayer.dismountRidingEntity();
					armorStand.remove();
					armorStands.forEach((vector4d, wsArmorStand) -> wsArmorStand.remove());
					armorStand = null;
					stopMoving();
					WetSponge.getEventManager().unregisterListener(SParachute.class, WetSpongeTest.getInstance());
					break;
				}
			}
		});
		task.runTaskTimer(1, 1, WetSpongeTest.getInstance());
	}

	private void getArmorStands() {
		generateArmorStand(new Vector4d(1.95, 4.8, 0, 90), 40);
		generateArmorStand(new Vector4d(0.45, 5.5, 0, 90), 60);
		generateArmorStand(new Vector4d(1.3, 5.33, 0, -90), 90);
		generateArmorStand(new Vector4d(-0.5, 5.5, 0, -90), 60);
		generateArmorStand(new Vector4d(-2, 4.8, 0, -90), 40);
	}

	private void generateArmorStand(Vector4d relative, double xRotation) {
		WSArmorStand armorStand = this.armorStand.getWorld().spawnEntity(WSArmorStand.class, this.armorStand.getLocation().add(relative.toVector3()).toVector3d())
			.orElse(null);
		if (armorStand == null) return;
		armorStand.setGravity(false);
		armorStand.setInvisible(true);
		armorStand.setArms(true);
		armorStand.setEquippedItem(EnumEquipType.HELMET, WSItemStack.of(WSBlockTypes.BANNER));
		armorStand.setHeadDirection(new Vector3d(xRotation, relative.getW(), 180));
		armorStands.put(relative, armorStand);
	}

	public WSArmorStand getArmorStand() {
		return armorStand;
	}

	public WSPlayer getMatchPlayer() {
		return matchPlayer;
	}

	public WSTask getTask() {
		return task;
	}

	public SParachute stopMoving() {
		if (task == null) return this;
		task.cancel();
		task = null;
		return this;
	}

	@WSListener
	public void onDismount(WSEntityDismountEvent event) {
		if (event.getVehicle().equals(armorStand) && !canDismount) event.setCancelled(true);
	}

}
