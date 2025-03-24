package powercyphe.tridentfix;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public interface TFGamerules {
		GameRules.Key<GameRules.BooleanRule> CHANNELING_MAKES_FIRE =
			register("channelingMakesFire", GameRuleFactory.createBooleanRule(false));
		
		GameRules.Key<GameRules.IntRule> LOYALTY_FORCE_RETURN_DISTANCE =
			register("loyaltyForceReturnDist", GameRuleFactory.createIntRule(64));
		
		GameRules.Key<GameRules.BooleanRule> LOYALTY_RETURN_TO_THROWN_SLOT =
			register("loyaltyReturnToThrownSlot", GameRuleFactory.createBooleanRule(true));
		
		private static <T1 extends GameRules.Rule<T1>, T2 extends GameRules.Type<T1>> GameRules.Key<T1> register(String name, T2 rule) {
				return GameRuleRegistry.register(TridentFix.MOD_ID + "_" + name, GameRules.Category.MISC, rule);
		}
		
		static <T1 extends GameRules.Rule<T1>> T1 getGameRule(World world, GameRules.Key<T1> key) {
				if (world instanceof ServerWorld serverWorld) {
						return serverWorld.getServer().getGameRules().get(key);
				} else throw new IllegalStateException("Tried to get gamerule on client!");
		}
		
		static void init() {}
		
}
