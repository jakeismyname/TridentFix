package powercyphe.tridentfix.mixin;

import powercyphe.tridentfix.ItemDataUtil;
import powercyphe.tridentfix.TFGamerules;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.item.Items.TRIDENT;
import static powercyphe.tridentfix.TFGamerules.LOYALTY_RETURN_TO_THROWN_SLOT;
import static powercyphe.tridentfix.TridentFix.RETURN_SLOT_KEY;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
		@Shadow private @Final static TrackedData<Byte> LOYALTY;
		@Shadow public int returnTimer;
		@Shadow private boolean dealtDamage;
		
		protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
				super(entityType, world);
		}
		
		@Shadow
		protected abstract boolean isOwnerAlive();
		
		@Inject(method = "tick", at = @At("HEAD"))
		private void tridentfix$tickMixin(CallbackInfo ci) {
				if (!this.getWorld().isClient()) {
						if (!this.dealtDamage && this.isOwnerAlive() && this.dataTracker.get(LOYALTY) > 0) {
								final int forceReturnDist = TFGamerules.getGameRule(this.getWorld(), TFGamerules.LOYALTY_FORCE_RETURN_DISTANCE).get();
								if (
									this.getWorld().isOutOfHeightLimit(this.getBlockY()) ||
									(forceReturnDist > -1 && this.getPos().distanceTo(this.getOwner().getPos()) >= forceReturnDist) ||
									this.isRegionUnloaded()
								) {
										this.dealtDamage = true;
										this.returnTimer = 0;
								}
						}
				}
		}
		
		@WrapMethod(method = "tryPickup")
		private boolean tridentfix$returnToSlot(PlayerEntity player, Operation<Boolean> original) {
				if (
						!player.getWorld().isClient() &&
						!player.isCreative() &&
						TFGamerules.getGameRule(player.getWorld(), LOYALTY_RETURN_TO_THROWN_SLOT).get() &&
						this.getOwner() instanceof ServerPlayerEntity owner &&
						player.equals(owner) &&
						this.getItemStack().isOf(TRIDENT) &&
						ItemDataUtil.getLevel(Enchantments.LOYALTY, this.getItemStack(), this.getRegistryManager()) > 0
				) {
						final ItemStack tridentStack = this.asItemStack();
						int slot = ItemDataUtil.getCustomData(tridentStack, RETURN_SLOT_KEY, NbtInt.TYPE).orElse(NbtInt.of(-1)).intValue();
						final PlayerInventory inventory = owner.getInventory();
						if ((inventory.getEmptySlot() != -1 || owner.getOffHandStack().isEmpty()) && tridentStack.getCount() >= 1) {
								if ((slot == -1 || inventory.getEmptySlot() == -1) && owner.getOffHandStack().isEmpty()) {
										owner.equipStack(EquipmentSlot.OFFHAND, tridentStack);
								} else {
										if (slot == -1) slot = 0;
										if (inventory.getStack(slot).isEmpty()) inventory.insertStack(slot, tridentStack);
										else inventory.offerOrDrop(tridentStack);
								}
								owner.sendPickup(this, 1);
								this.discard();
								return false;
						}
				}
				return original.call(player);
		}
		
}

