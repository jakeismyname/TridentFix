package powercyphe.tridentfix.mixin;

import powercyphe.tridentfix.ItemDataUtil;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static powercyphe.tridentfix.TridentFix.RETURN_SLOT_KEY;

@Mixin(TridentItem.class)
public class TridentItemMixin {
		@Inject(method = "onStoppedUsing", at = @At("HEAD"))
		private void storeSlotMixin(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
				if (!user.getEntityWorld().isClient() && user instanceof PlayerEntity player) {
						if (stack.getItem() == Items.TRIDENT) {
								ItemDataUtil.modifyCustomData(
									stack,
									nbt -> nbt.putInt(RETURN_SLOT_KEY, player.getInventory().getSlotWithStack(stack))
								);
						}
				}
		}
		
}