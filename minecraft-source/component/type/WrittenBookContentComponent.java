/*
 * External method calls:
 *   Lnet/minecraft/text/RawFilteredPair;createCodec(Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/text/RawFilteredPair;resolve(Ljava/util/function/Function;)Ljava/util/Optional;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Texts;parse(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/text/Text;Lnet/minecraft/entity/Entity;I)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/util/dynamic/Codecs;rangedInt(II)Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/text/RawFilteredPair;of(Ljava/lang/Object;)Lnet/minecraft/text/RawFilteredPair;
 *   Lnet/minecraft/text/TextCodecs;withJsonLengthLimit(I)Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/network/codec/PacketCodecs;string(I)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/text/RawFilteredPair;createPacketCodec(Lnet/minecraft/network/codec/PacketCodec;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;toList()Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function5;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/component/type/WrittenBookContentComponent;createPageCodec(Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/component/type/WrittenBookContentComponent;resolve(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/component/type/WrittenBookContentComponent;
 *   Lnet/minecraft/component/type/WrittenBookContentComponent;asResolved()Lnet/minecraft/component/type/WrittenBookContentComponent;
 *   Lnet/minecraft/component/type/WrittenBookContentComponent;resolve(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/text/RawFilteredPair;)Ljava/util/Optional;
 *   Lnet/minecraft/component/type/WrittenBookContentComponent;withPages(Ljava/util/List;)Lnet/minecraft/component/type/WrittenBookContentComponent;
 *   Lnet/minecraft/component/type/WrittenBookContentComponent;exceedsSerializedLengthLimit(Lnet/minecraft/text/Text;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Z
 *   Lnet/minecraft/component/type/WrittenBookContentComponent;createPagesCodec(Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;
 */
package net.minecraft.component.type;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BookContent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.RawFilteredPair;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.StringHelper;
import net.minecraft.util.dynamic.Codecs;
import org.jspecify.annotations.Nullable;

public record WrittenBookContentComponent(RawFilteredPair<String> title, String author, int generation, List<RawFilteredPair<Text>> pages, boolean resolved) implements BookContent<Text, WrittenBookContentComponent>,
TooltipAppender
{
    public static final WrittenBookContentComponent DEFAULT = new WrittenBookContentComponent(RawFilteredPair.of(""), "", 0, List.of(), true);
    public static final int MAX_SERIALIZED_PAGE_LENGTH = Short.MAX_VALUE;
    public static final int field_49377 = 16;
    public static final int MAX_TITLE_LENGTH = 32;
    public static final int MAX_GENERATION = 3;
    public static final int UNCOPIABLE_GENERATION = 2;
    public static final Codec<Text> PAGE_CODEC = TextCodecs.withJsonLengthLimit(Short.MAX_VALUE);
    public static final Codec<List<RawFilteredPair<Text>>> PAGES_CODEC = WrittenBookContentComponent.createPagesCodec(PAGE_CODEC);
    public static final Codec<WrittenBookContentComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)RawFilteredPair.createCodec(Codec.string(0, 32)).fieldOf("title")).forGetter(WrittenBookContentComponent::title), ((MapCodec)Codec.STRING.fieldOf("author")).forGetter(WrittenBookContentComponent::author), Codecs.rangedInt(0, 3).optionalFieldOf("generation", 0).forGetter(WrittenBookContentComponent::generation), PAGES_CODEC.optionalFieldOf("pages", List.of()).forGetter(WrittenBookContentComponent::pages), Codec.BOOL.optionalFieldOf("resolved", false).forGetter(WrittenBookContentComponent::resolved)).apply((Applicative<WrittenBookContentComponent, ?>)instance, WrittenBookContentComponent::new));
    public static final PacketCodec<RegistryByteBuf, WrittenBookContentComponent> PACKET_CODEC = PacketCodec.tuple(RawFilteredPair.createPacketCodec(PacketCodecs.string(32)), WrittenBookContentComponent::title, PacketCodecs.STRING, WrittenBookContentComponent::author, PacketCodecs.VAR_INT, WrittenBookContentComponent::generation, RawFilteredPair.createPacketCodec(TextCodecs.REGISTRY_PACKET_CODEC).collect(PacketCodecs.toList()), WrittenBookContentComponent::pages, PacketCodecs.BOOLEAN, WrittenBookContentComponent::resolved, WrittenBookContentComponent::new);

    public WrittenBookContentComponent {
        if (generation < 0 || generation > 3) {
            throw new IllegalArgumentException("Generation was " + generation + ", but must be between 0 and 3");
        }
    }

    private static Codec<RawFilteredPair<Text>> createPageCodec(Codec<Text> textCodec) {
        return RawFilteredPair.createCodec(textCodec);
    }

    public static Codec<List<RawFilteredPair<Text>>> createPagesCodec(Codec<Text> textCodec) {
        return WrittenBookContentComponent.createPageCodec(textCodec).listOf();
    }

    public @Nullable WrittenBookContentComponent copy() {
        if (this.generation >= 2) {
            return null;
        }
        return new WrittenBookContentComponent(this.title, this.author, this.generation + 1, this.pages, this.resolved);
    }

    public static boolean resolveInStack(ItemStack stack, ServerCommandSource commandSource, @Nullable PlayerEntity player) {
        WrittenBookContentComponent lv = stack.get(DataComponentTypes.WRITTEN_BOOK_CONTENT);
        if (lv != null && !lv.resolved()) {
            WrittenBookContentComponent lv2 = lv.resolve(commandSource, player);
            if (lv2 != null) {
                stack.set(DataComponentTypes.WRITTEN_BOOK_CONTENT, lv2);
                return true;
            }
            stack.set(DataComponentTypes.WRITTEN_BOOK_CONTENT, lv.asResolved());
        }
        return false;
    }

    public @Nullable WrittenBookContentComponent resolve(ServerCommandSource source, @Nullable PlayerEntity player) {
        if (this.resolved) {
            return null;
        }
        ImmutableList.Builder builder = ImmutableList.builderWithExpectedSize(this.pages.size());
        for (RawFilteredPair<Text> lv : this.pages) {
            Optional<RawFilteredPair<Text>> optional = WrittenBookContentComponent.resolve(source, player, lv);
            if (optional.isEmpty()) {
                return null;
            }
            builder.add(optional.get());
        }
        return new WrittenBookContentComponent(this.title, this.author, this.generation, (List<RawFilteredPair<Text>>)((Object)builder.build()), true);
    }

    public WrittenBookContentComponent asResolved() {
        return new WrittenBookContentComponent(this.title, this.author, this.generation, this.pages, true);
    }

    private static Optional<RawFilteredPair<Text>> resolve(ServerCommandSource source, @Nullable PlayerEntity player, RawFilteredPair<Text> page) {
        return page.resolve(text -> {
            try {
                MutableText lv = Texts.parse(source, text, (Entity)player, 0);
                if (WrittenBookContentComponent.exceedsSerializedLengthLimit(lv, source.getRegistryManager())) {
                    return Optional.empty();
                }
                return Optional.of(lv);
            } catch (Exception exception) {
                return Optional.of(text);
            }
        });
    }

    private static boolean exceedsSerializedLengthLimit(Text text, RegistryWrapper.WrapperLookup registries) {
        DataResult<JsonElement> dataResult = TextCodecs.CODEC.encodeStart(registries.getOps(JsonOps.INSTANCE), text);
        return dataResult.isSuccess() && JsonHelper.isTooLarge(dataResult.getOrThrow(), Short.MAX_VALUE);
    }

    public List<Text> getPages(boolean shouldFilter) {
        return Lists.transform(this.pages, page -> (Text)page.get(shouldFilter));
    }

    @Override
    public WrittenBookContentComponent withPages(List<RawFilteredPair<Text>> list) {
        return new WrittenBookContentComponent(this.title, this.author, this.generation, list, false);
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        if (!StringHelper.isBlank(this.author)) {
            textConsumer.accept(Text.translatable("book.byAuthor", this.author).formatted(Formatting.GRAY));
        }
        textConsumer.accept(Text.translatable("book.generation." + this.generation).formatted(Formatting.GRAY));
    }

    @Override
    public /* synthetic */ Object withPages(List pages) {
        return this.withPages(pages);
    }
}

