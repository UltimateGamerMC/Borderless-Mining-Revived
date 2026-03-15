package net.fabricmc.fabric.api.itemgroup.v1;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupBuilderImpl;

/**
 * Contains a method to create an item group builder.
 */
public final class FabricItemGroup {
	private FabricItemGroup() {
	}

	/**
	 * Creates a new builder for {@link CreativeModeTab}. Item groups are used to group items in the creative
	 * inventory.
	 *
	 * <p>You must register the newly created {@link CreativeModeTab} to the {@link BuiltInRegistries#CREATIVE_MODE_TAB} registry.
	 *
	 * <p>You must also set a display name by calling {@link CreativeModeTab.Builder#title(Component)}
	 *
	 * <p>Example:
	 *
	 * <pre>{@code
	 * private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.fromNamespaceAndPath("modid", "custom_group"));
	 *
	 * @Override
	 * public void onInitialize() {
	 *    Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
	 *       .displayName(Text.translatable("modid.test_group"))
	 *       .icon(() -> new ItemStack(Items.DIAMOND))
	 *       .entries((context, entries) -> {
	 *          entries.add(TEST_ITEM);
	 *       })
	 *       .build()
	 *    );
	 * }
	 * }</pre>
	 *
	 * @return a new {@link CreativeModeTab.Builder} instance
	 */
	public static CreativeModeTab.Builder builder() {
		return new FabricItemGroupBuilderImpl();
	}
}
