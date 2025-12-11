package powercyphe.tridentfix;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import static net.minecraft.core.component.DataComponents.CUSTOM_DATA;

public final class ItemDataUtil {
		private ItemDataUtil() {}
		
		public static int getLevel(ResourceKey<Enchantment> key, ItemStack stack, RegistryAccess registryManager) {
				return registryManager.lookupOrThrow(Registries.ENCHANTMENT).get(key)
					.map(ench -> EnchantmentHelper.getItemEnchantmentLevel(ench, stack)).orElse(0);
		}
		
		public static void modifyCustomData(ItemStack stack, Consumer<CompoundTag> modifier) {
				stack.set(CUSTOM_DATA, stack.getOrDefault(CUSTOM_DATA, CustomData.EMPTY).update(modifier));
		}
		
		@SuppressWarnings("unchecked")
		public static <T extends Tag> Optional<T> getCustomData(ItemStack stack, String key, TagType<T> type) {
				final Tag element = stack.getOrDefault(CUSTOM_DATA, CustomData.EMPTY).copyTag().get(key);
				if (element != null && element.getType().equals(type)) {
						return Optional.of((T) element);
				}
				return Optional.empty();
		}
		
}
