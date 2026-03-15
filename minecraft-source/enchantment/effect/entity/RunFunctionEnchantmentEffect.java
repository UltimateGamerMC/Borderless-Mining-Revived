/*
 * External method calls:
 *   Lnet/minecraft/server/command/ServerCommandSource;withPermissions(Lnet/minecraft/command/permission/PermissionPredicate;)Lnet/minecraft/server/command/ServerCommandSource;
 *   Lnet/minecraft/server/command/ServerCommandSource;withSilent()Lnet/minecraft/server/command/ServerCommandSource;
 *   Lnet/minecraft/server/command/ServerCommandSource;withEntity(Lnet/minecraft/entity/Entity;)Lnet/minecraft/server/command/ServerCommandSource;
 *   Lnet/minecraft/server/command/ServerCommandSource;withWorld(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/server/command/ServerCommandSource;
 *   Lnet/minecraft/server/command/ServerCommandSource;withPosition(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/server/command/ServerCommandSource;
 *   Lnet/minecraft/server/command/ServerCommandSource;withRotation(Lnet/minecraft/util/math/Vec2f;)Lnet/minecraft/server/command/ServerCommandSource;
 *   Lnet/minecraft/server/function/CommandFunctionManager;execute(Lnet/minecraft/server/function/CommandFunction;Lnet/minecraft/server/command/ServerCommandSource;)V
 */
package net.minecraft.enchantment.effect.entity;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.command.permission.LeveledPermissionPredicate;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;

public record RunFunctionEnchantmentEffect(Identifier function) implements EnchantmentEntityEffect
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<RunFunctionEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("function")).forGetter(RunFunctionEnchantmentEffect::function)).apply((Applicative<RunFunctionEnchantmentEffect, ?>)instance, RunFunctionEnchantmentEffect::new));

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        MinecraftServer minecraftServer = world.getServer();
        CommandFunctionManager lv = minecraftServer.getCommandFunctionManager();
        Optional<CommandFunction<ServerCommandSource>> optional = lv.getFunction(this.function);
        if (optional.isPresent()) {
            ServerCommandSource lv2 = minecraftServer.getCommandSource().withPermissions(LeveledPermissionPredicate.GAMEMASTERS).withSilent().withEntity(user).withWorld(world).withPosition(pos).withRotation(user.getRotationClient());
            lv.execute(optional.get(), lv2);
        } else {
            LOGGER.error("Enchantment run_function effect failed for non-existent function {}", (Object)this.function);
        }
    }

    public MapCodec<RunFunctionEnchantmentEffect> getCodec() {
        return CODEC;
    }
}

