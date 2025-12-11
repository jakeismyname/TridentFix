package powercyphe.tridentfix;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.ramixin.mixson.inline.Mixson;
import net.ramixin.mixson.util.MixsonUtil;

import java.io.IOException;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TridentFix implements ModInitializer {
		public static final String MOD_ID = "tridentfix";
		
		public static final String RETURN_SLOT_KEY = MOD_ID + "_return_slot";
		
		private static final JsonObject BEDROCK_IMPALING_PATCH;
		
		static {
				try (final var reader = TridentFix.class.getClassLoader().getResourceAsStream("data/tridentfix/patch/impaling_patch.json")) {
						BEDROCK_IMPALING_PATCH = new Gson().fromJson(new InputStreamReader(reader, UTF_8), JsonObject.class);
				} catch (IOException exception) {
						throw new RuntimeException(exception);
				}
		}
		
		@Override
		public void onInitialize() {
				Mixson.registerEvent(0, MixsonUtil.getLocatorFromString("enchantment/impaling"), "bedrock style impaling", ctx -> {
						traverse(ctx.getFile(), "effects/minecraft:damage/0").getAsJsonObject().add("requirements", BEDROCK_IMPALING_PATCH);
				}, false);
				Mixson.registerEvent(0, MixsonUtil.getLocatorFromString("enchantment/channeling"), "bedrock style channeling", ctx -> {
						final var condition = traverse(ctx.getFile(), "effects/minecraft:hit_block/0/requirements/terms/0").getAsJsonObject();
						condition.remove("thundering");
						condition.addProperty("raining", true);
				}, false);
				TFGamerules.init();
		}
		
		private static JsonElement traverse(JsonElement root, String path) {
				var current = root;
				for (final var subpath : path.split("/")) {
						if (current instanceof JsonObject obj) {
								current = obj.get(subpath);
						} else if (current instanceof JsonArray array) {
								current = array.get(Integer.parseInt(subpath));
						}
				}
				return current;
		}
		
}