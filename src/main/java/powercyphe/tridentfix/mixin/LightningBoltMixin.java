package powercyphe.tridentfix.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static powercyphe.tridentfix.TFGamerules.CHANNELING_MAKES_FIRE;
import static powercyphe.tridentfix.TFGamerules.getGameRule;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin {
		@WrapWithCondition(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LightningBolt;spawnFire(I)V"))
		private boolean tridentfix$shouldSpawnFire(LightningBolt entity, int spreadAttempts) {
			return entity.getCause() == null || getGameRule(entity.level(), CHANNELING_MAKES_FIRE);
		}
		
}
