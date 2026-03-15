/*
 * External method calls:
 *   Lnet/minecraft/world/attribute/BedRule$Condition;test(Lnet/minecraft/world/World;)Z
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.world.attribute;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;

public record BedRule(Condition canSleep, Condition canSetSpawn, boolean explodes, Optional<Text> errorMessage) {
    public static final BedRule OVERWORLD = new BedRule(Condition.WHEN_DARK, Condition.ALWAYS, false, Optional.of(Text.translatable("block.minecraft.bed.no_sleep")));
    public static final BedRule OTHER_DIMENSION = new BedRule(Condition.NEVER, Condition.NEVER, true, Optional.empty());
    public static final Codec<BedRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Condition.CODEC.fieldOf("can_sleep")).forGetter(BedRule::canSleep), ((MapCodec)Condition.CODEC.fieldOf("can_set_spawn")).forGetter(BedRule::canSetSpawn), Codec.BOOL.optionalFieldOf("explodes", false).forGetter(BedRule::explodes), TextCodecs.CODEC.optionalFieldOf("error_message").forGetter(BedRule::errorMessage)).apply((Applicative<BedRule, ?>)instance, BedRule::new));

    public boolean canSleep(World world) {
        return this.canSleep.test(world);
    }

    public boolean canSetSpawn(World world) {
        return this.canSetSpawn.test(world);
    }

    public PlayerEntity.SleepFailureReason getFailureReason() {
        return new PlayerEntity.SleepFailureReason(this.errorMessage.orElse(null));
    }

    public static enum Condition implements StringIdentifiable
    {
        ALWAYS("always"),
        WHEN_DARK("when_dark"),
        NEVER("never");

        public static final Codec<Condition> CODEC;
        private final String name;

        private Condition(String name) {
            this.name = name;
        }

        public boolean test(World world) {
            return switch (this.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> true;
                case 1 -> world.isNight();
                case 2 -> false;
            };
        }

        @Override
        public String asString() {
            return this.name;
        }

        static {
            CODEC = StringIdentifiable.createCodec(Condition::values);
        }
    }
}

