package net.minecraft.client.render.item.tint;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public record TeamTintSource(int defaultColor) implements TintSource
{
    public static final MapCodec<TeamTintSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codecs.RGB.fieldOf("default")).forGetter(TeamTintSource::defaultColor)).apply((Applicative<TeamTintSource, ?>)instance, TeamTintSource::new));

    @Override
    public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
        Formatting lv2;
        Team lv;
        if (user != null && (lv = user.getScoreboardTeam()) != null && (lv2 = ((AbstractTeam)lv).getColor()).getColorValue() != null) {
            return ColorHelper.fullAlpha(lv2.getColorValue());
        }
        return ColorHelper.fullAlpha(this.defaultColor);
    }

    public MapCodec<TeamTintSource> getCodec() {
        return CODEC;
    }
}

