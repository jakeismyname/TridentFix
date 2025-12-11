package powercyphe.tridentfix.mixin;

import powercyphe.tridentfix.ItemDataUtil;
import powercyphe.tridentfix.TFGamerules;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.item.Items.TRIDENT;
import static powercyphe.tridentfix.TFGamerules.LOYALTY_RETURN_TO_THROWN_SLOT;
import static powercyphe.tridentfix.TridentFix.RETURN_SLOT_KEY;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
		@Shadow private @Final static EntityDataAccessor<Byte> ID_LOYALTY;
		@Shadow public int clientSideReturnTridentTickCount;
		@Shadow private boolean dealtDamage;
		
		protected ThrownTridentMixin(EntityType<? extends AbstractArrow> entityType, Level world) {
				super(entityType, world);
		}
		
		@Shadow
		protected abstract boolean isAcceptibleReturnOwner();
		
		@Inject(method = "tick", at = @At("HEAD"))
		private void tridentfix$tickMixin(CallbackInfo ci) {
				if (!this.level().isClientSide()) {
						if (!this.dealtDamage && this.isAcceptibleReturnOwner() && this.entityData.get(ID_LOYALTY) > 0) {
								final int forceReturnDist = TFGamerules.getGameRule(this.level(), TFGamerules.LOYALTY_FORCE_RETURN_DISTANCE);
								if (
									this.level().isOutsideBuildHeight(this.getBlockY()) ||
									(forceReturnDist > -1 && this.position().distanceTo(this.getOwner().position()) >= forceReturnDist) ||
									this.touchingUnloadedChunk()
								) {
										this.dealtDamage = true;
										this.clientSideReturnTridentTickCount = 0;
								}
						}
				}
		}
		
		@WrapMethod(method = "tryPickup")
		private boolean tridentfix$returnToSlot(Player player, Operation<Boolean> original) {
				if (
						!player.level().isClientSide() &&
						!player.isCreative() &&
						TFGamerules.getGameRule(player.level(), LOYALTY_RETURN_TO_THROWN_SLOT) &&
						this.getOwner() instanceof ServerPlayer owner &&
						player.equals(owner) &&
						this.getPickupItemStackOrigin().is(TRIDENT) &&
						ItemDataUtil.getLevel(Enchantments.LOYALTY, this.getPickupItemStackOrigin(), this.registryAccess()) > 0
				) {
						final ItemStack tridentStack = this.getPickupItem();
						int slot = ItemDataUtil.getCustomData(tridentStack, RETURN_SLOT_KEY, IntTag.TYPE).orElse(IntTag.valueOf(-1)).intValue();
						final Inventory inventory = owner.getInventory();
						if ((inventory.getFreeSlot() != -1 || owner.getOffhandItem().isEmpty()) && tridentStack.getCount() >= 1) {
								if ((slot == -1 || inventory.getFreeSlot() == -1) && owner.getOffhandItem().isEmpty()) {
										owner.setItemSlot(EquipmentSlot.OFFHAND, tridentStack);
								} else {
										if (slot == -1) slot = 0;
										if (inventory.getItem(slot).isEmpty()) inventory.add(slot, tridentStack);
										else inventory.placeItemBackInInventory(tridentStack);
								}
								owner.take(this, 1);
								this.discard();
								return false;
						}
				}
				return original.call(player);
		}
		
}

