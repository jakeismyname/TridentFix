package powercyphe.tridentfix.mixin;

import powercyphe.tridentfix.ItemDataUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static powercyphe.tridentfix.TridentFix.RETURN_SLOT_KEY;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;

@Mixin(TridentItem.class)
public class TridentItemMixin {
		@Inject(method = "releaseUsing", at = @At("HEAD"))
		private void storeSlotMixin(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
				if (!user.level().isClientSide() && user instanceof Player player) {
						if (stack.getItem() == Items.TRIDENT) {
								ItemDataUtil.modifyCustomData(
									stack,
									nbt -> nbt.putInt(RETURN_SLOT_KEY, player.getInventory().findSlotMatchingItem(stack))
								);
						}
				}
		}
		
}