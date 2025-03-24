package powercyphe.tridentfix;

import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.Optional;
import java.util.function.Consumer;

import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;

public class ItemDataUtil {
		public static int getLevel(RegistryKey<Enchantment> key, ItemStack stack, DynamicRegistryManager registryManager) {
				return registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(key)
					.map(ench -> EnchantmentHelper.getLevel(ench, stack)).orElse(0);
		}
		
		public static void modifyCustomData(ItemStack stack, Consumer<NbtCompound> modifier) {
				stack.set(CUSTOM_DATA, stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).apply(modifier));
		}
		
		@SuppressWarnings("unchecked")
		public static <T extends NbtElement> Optional<T> getCustomData(ItemStack stack, String key, NbtType<T> type) {
				final NbtElement element = stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().get(key);
				if (element != null && element.getNbtType().equals(type)) {
						return Optional.of((T) element);
				}
				return Optional.empty();
		}
		
}
