package powercyphe.tridentfix.mixin;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
// This mixin works, but is required on both the server and the client to actually work :(
//		@Shadow
//		protected int riptideTicks;
//
//		@Expression("(0.54600006 - ?) * ?")
//		@ModifyExpressionValue(method = "travelInFluid", at = @At("MIXINEXTRAS:EXPRESSION"))
//		private float riptideCancelDrag(float original) {
//				return this.riptideTicks > 0 ? 0 : original;
//		}
		
}
