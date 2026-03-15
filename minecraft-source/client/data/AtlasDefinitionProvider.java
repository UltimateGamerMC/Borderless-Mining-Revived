/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;withPath(Ljava/util/function/UnaryOperator;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/util/SpriteMapper;prefix()Ljava/lang/String;
 *   Lnet/minecraft/data/DataOutput$PathResolver;resolveJson(Lnet/minecraft/util/Identifier;)Ljava/nio/file/Path;
 *   Lnet/minecraft/data/DataProvider;writeCodecToPath(Lnet/minecraft/data/DataWriter;Lcom/mojang/serialization/Codec;Ljava/lang/Object;Ljava/nio/file/Path;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/item/equipment/trim/ArmorTrimAssets;base()Lnet/minecraft/item/equipment/trim/ArmorTrimAssets$AssetId;
 *   Lnet/minecraft/item/equipment/trim/ArmorTrimAssets;overrides()Ljava/util/Map;
 *   Lnet/minecraft/item/equipment/trim/ArmorTrimAssets$AssetId;suffix()Ljava/lang/String;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;createDirectoryAtlasSource(Lnet/minecraft/client/util/SpriteMapper;)Lnet/minecraft/client/texture/atlas/AtlasSource;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;createSingleAtlasSource(Lnet/minecraft/client/util/SpriteIdentifier;)Lnet/minecraft/client/texture/atlas/AtlasSource;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;createArmorTrimsAtlasSources()Ljava/util/List;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;runForAtlas(Lnet/minecraft/data/DataWriter;Lnet/minecraft/util/Identifier;Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;createBannerPatternsAtlasSources()Ljava/util/List;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;createAtlasSources(Lnet/minecraft/client/util/SpriteMapper;)Ljava/util/List;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;createBlocksAtlasSources()Ljava/util/List;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;createItemsAtlasSources()Ljava/util/List;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;createGuiAtlasSources()Ljava/util/List;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;createAtlasSources(Ljava/lang/String;)Ljava/util/List;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;createShieldAtlasSources()Ljava/util/List;
 *   Lnet/minecraft/client/data/AtlasDefinitionProvider;streamTrimAssets()Ljava/util/stream/Stream;
 */
package net.minecraft.client.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BellBlockEntityRenderer;
import net.minecraft.client.render.block.entity.ConduitBlockEntityRenderer;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.AtlasSourceManager;
import net.minecraft.client.texture.atlas.DirectoryAtlasSource;
import net.minecraft.client.texture.atlas.PalettedPermutationsAtlasSource;
import net.minecraft.client.texture.atlas.SingleAtlasSource;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.SpriteMapper;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.equipment.trim.ArmorTrimAssets;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.item.equipment.trim.ArmorTrimPatterns;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Atlases;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class AtlasDefinitionProvider
implements DataProvider {
    private static final Identifier TRIM_PALETTES_ID = Identifier.ofVanilla("trims/color_palettes/trim_palette");
    private static final Map<String, Identifier> TRIM_ASSET_SUFFIX_TO_COLOR_PALETTE = AtlasDefinitionProvider.streamTrimAssets().collect(Collectors.toMap(ArmorTrimAssets.AssetId::suffix, assetId -> Identifier.ofVanilla("trims/color_palettes/" + assetId.suffix())));
    private static final List<RegistryKey<ArmorTrimPattern>> ARMOR_TRIM_PATTERN_KEYS = List.of(ArmorTrimPatterns.SENTRY, ArmorTrimPatterns.DUNE, ArmorTrimPatterns.COAST, ArmorTrimPatterns.WILD, ArmorTrimPatterns.WARD, ArmorTrimPatterns.EYE, ArmorTrimPatterns.VEX, ArmorTrimPatterns.TIDE, ArmorTrimPatterns.SNOUT, ArmorTrimPatterns.RIB, ArmorTrimPatterns.SPIRE, ArmorTrimPatterns.WAYFINDER, ArmorTrimPatterns.SHAPER, ArmorTrimPatterns.SILENCE, ArmorTrimPatterns.RAISER, ArmorTrimPatterns.HOST, ArmorTrimPatterns.FLOW, ArmorTrimPatterns.BOLT);
    private static final List<EquipmentModel.LayerType> EQUIPMENT_MODEL_LAYER_TYPES = List.of(EquipmentModel.LayerType.HUMANOID, EquipmentModel.LayerType.HUMANOID_LEGGINGS);
    private final DataOutput.PathResolver pathResolver;

    public AtlasDefinitionProvider(DataOutput output) {
        this.pathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "atlases");
    }

    private static List<Identifier> getArmorTrimTextures() {
        ArrayList<Identifier> list = new ArrayList<Identifier>(ARMOR_TRIM_PATTERN_KEYS.size() * EQUIPMENT_MODEL_LAYER_TYPES.size());
        for (RegistryKey<ArmorTrimPattern> lv : ARMOR_TRIM_PATTERN_KEYS) {
            Identifier lv2 = ArmorTrimPatterns.getId(lv);
            for (EquipmentModel.LayerType lv3 : EQUIPMENT_MODEL_LAYER_TYPES) {
                list.add(lv2.withPath(trimPatternPath -> lv3.getTrimsDirectory() + "/" + trimPatternPath));
            }
        }
        return list;
    }

    private static AtlasSource createSingleAtlasSource(SpriteIdentifier spriteId) {
        return new SingleAtlasSource(spriteId.getTextureId());
    }

    private static AtlasSource createDirectoryAtlasSource(SpriteMapper spriteMapper) {
        return new DirectoryAtlasSource(spriteMapper.prefix(), spriteMapper.prefix() + "/");
    }

    private static List<AtlasSource> createAtlasSources(SpriteMapper spriteMapper) {
        return List.of(AtlasDefinitionProvider.createDirectoryAtlasSource(spriteMapper));
    }

    private static List<AtlasSource> createAtlasSources(String directorySource) {
        return List.of(new DirectoryAtlasSource(directorySource, ""));
    }

    private static Stream<ArmorTrimAssets.AssetId> streamTrimAssets() {
        return ItemModelGenerator.TRIM_MATERIALS.stream().map(ItemModelGenerator.TrimMaterial::assets).flatMap(assets -> Stream.concat(Stream.of(assets.base()), assets.overrides().values().stream())).sorted(Comparator.comparing(ArmorTrimAssets.AssetId::suffix));
    }

    private static List<AtlasSource> createArmorTrimsAtlasSources() {
        return List.of(new PalettedPermutationsAtlasSource(AtlasDefinitionProvider.getArmorTrimTextures(), TRIM_PALETTES_ID, TRIM_ASSET_SUFFIX_TO_COLOR_PALETTE));
    }

    private static List<AtlasSource> createBlocksAtlasSources() {
        return List.of(AtlasDefinitionProvider.createDirectoryAtlasSource(TexturedRenderLayers.BLOCK_SPRITE_MAPPER), AtlasDefinitionProvider.createDirectoryAtlasSource(ConduitBlockEntityRenderer.SPRITE_MAPPER), AtlasDefinitionProvider.createSingleAtlasSource(BellBlockEntityRenderer.BELL_BODY_TEXTURE), AtlasDefinitionProvider.createSingleAtlasSource(EnchantingTableBlockEntityRenderer.BOOK_TEXTURE));
    }

    private static List<AtlasSource> createItemsAtlasSources() {
        return List.of(AtlasDefinitionProvider.createDirectoryAtlasSource(TexturedRenderLayers.ITEM_SPRITE_MAPPER), new PalettedPermutationsAtlasSource(List.of(ItemModelGenerator.HELMET_TRIM_ID_PREFIX, ItemModelGenerator.CHESTPLATE_TRIM_ID_PREFIX, ItemModelGenerator.LEGGINGS_TRIM_ID_PREFIX, ItemModelGenerator.BOOTS_TRIM_ID_PREFIX), TRIM_PALETTES_ID, TRIM_ASSET_SUFFIX_TO_COLOR_PALETTE));
    }

    private static List<AtlasSource> createBannerPatternsAtlasSources() {
        return List.of(AtlasDefinitionProvider.createSingleAtlasSource(ModelBaker.BANNER_BASE), AtlasDefinitionProvider.createDirectoryAtlasSource(TexturedRenderLayers.BANNER_PATTERN_SPRITE_MAPPER));
    }

    private static List<AtlasSource> createShieldAtlasSources() {
        return List.of(AtlasDefinitionProvider.createSingleAtlasSource(ModelBaker.SHIELD_BASE), AtlasDefinitionProvider.createSingleAtlasSource(ModelBaker.SHIELD_BASE_NO_PATTERN), AtlasDefinitionProvider.createDirectoryAtlasSource(TexturedRenderLayers.SHIELD_PATTERN_SPRITE_MAPPER));
    }

    private static List<AtlasSource> createGuiAtlasSources() {
        return List.of(new DirectoryAtlasSource("gui/sprites", ""), new DirectoryAtlasSource("mob_effect", "mob_effect/"));
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return CompletableFuture.allOf(this.runForAtlas(writer, Atlases.ARMOR_TRIMS, AtlasDefinitionProvider.createArmorTrimsAtlasSources()), this.runForAtlas(writer, Atlases.BANNER_PATTERNS, AtlasDefinitionProvider.createBannerPatternsAtlasSources()), this.runForAtlas(writer, Atlases.BEDS, AtlasDefinitionProvider.createAtlasSources(TexturedRenderLayers.BED_SPRITE_MAPPER)), this.runForAtlas(writer, Atlases.BLOCKS, AtlasDefinitionProvider.createBlocksAtlasSources()), this.runForAtlas(writer, Atlases.ITEMS, AtlasDefinitionProvider.createItemsAtlasSources()), this.runForAtlas(writer, Atlases.CHESTS, AtlasDefinitionProvider.createAtlasSources(TexturedRenderLayers.CHEST_SPRITE_MAPPER)), this.runForAtlas(writer, Atlases.DECORATED_POT, AtlasDefinitionProvider.createAtlasSources(TexturedRenderLayers.DECORATED_POT_SPRITE_MAPPER)), this.runForAtlas(writer, Atlases.GUI, AtlasDefinitionProvider.createGuiAtlasSources()), this.runForAtlas(writer, Atlases.MAP_DECORATIONS, AtlasDefinitionProvider.createAtlasSources("map/decorations")), this.runForAtlas(writer, Atlases.PAINTINGS, AtlasDefinitionProvider.createAtlasSources("painting")), this.runForAtlas(writer, Atlases.PARTICLES, AtlasDefinitionProvider.createAtlasSources("particle")), this.runForAtlas(writer, Atlases.SHIELD_PATTERNS, AtlasDefinitionProvider.createShieldAtlasSources()), this.runForAtlas(writer, Atlases.SHULKER_BOXES, AtlasDefinitionProvider.createAtlasSources(TexturedRenderLayers.SHULKER_SPRITE_MAPPER)), this.runForAtlas(writer, Atlases.SIGNS, AtlasDefinitionProvider.createAtlasSources(TexturedRenderLayers.SIGN_SPRITE_MAPPER)), this.runForAtlas(writer, Atlases.CELESTIALS, AtlasDefinitionProvider.createAtlasSources("environment/celestial")));
    }

    private CompletableFuture<?> runForAtlas(DataWriter writer, Identifier atlasId, List<AtlasSource> atlasSources) {
        return DataProvider.writeCodecToPath(writer, AtlasSourceManager.LIST_CODEC, atlasSources, this.pathResolver.resolveJson(atlasId));
    }

    @Override
    public String getName() {
        return "Atlas Definitions";
    }
}

