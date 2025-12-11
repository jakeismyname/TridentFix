package powercyphe.tridentfix;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;

public interface TFGamerules {
		GameRule<Boolean> CHANNELING_MAKES_FIRE =
			register("channeling_makes_fire", GameRuleBuilder.forBoolean(false));
		
		GameRule<Integer> LOYALTY_FORCE_RETURN_DISTANCE =
			register("loyalty_force_return_distance", GameRuleBuilder.forInteger(64));
		
		GameRule<Boolean> LOYALTY_RETURN_TO_THROWN_SLOT =
			register("loyalty_return_to_thrown_slot", GameRuleBuilder.forBoolean(true));
		
		private static <T> GameRule<T> register(String name, GameRuleBuilder<T> builder) {
				return builder.buildAndRegister(Identifier.fromNamespaceAndPath(TridentFix.MOD_ID, name));
		}
		
		static <T> T getGameRule(Level world, GameRule<T> rule) {
				if (world instanceof ServerLevel serverWorld) {
						return serverWorld.getGameRules().get(rule);
				} else throw new IllegalStateException("Tried to get gamerule on client!");
		}
		
		static void init() {}
		
}
