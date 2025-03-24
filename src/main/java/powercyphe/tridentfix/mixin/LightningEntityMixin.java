package powercyphe.tridentfix.mixin;

import net.minecraft.entity.LightningEntity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static powercyphe.tridentfix.TFGamerules.CHANNELING_MAKES_FIRE;
import static powercyphe.tridentfix.TFGamerules.getGameRule;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin {
		@WrapWithCondition(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LightningEntity;spawnFire(I)V"))
		private boolean tridentfix$shouldSpawnFire(LightningEntity entity, int spreadAttempts) {
			return entity.getChanneler() == null || getGameRule(entity.getWorld(), CHANNELING_MAKES_FIRE).get();
		}
		
}
