package powercyphe.tridentfix;

import net.fabricmc.api.ModInitializer;

public class TridentFix implements ModInitializer {
		public static final String MOD_ID = "tridentfix";
		
		public static final String RETURN_SLOT_KEY = MOD_ID + "_return_slot";
		
		@Override
		public void onInitialize() {
				TFGamerules.init();
		}
		
}