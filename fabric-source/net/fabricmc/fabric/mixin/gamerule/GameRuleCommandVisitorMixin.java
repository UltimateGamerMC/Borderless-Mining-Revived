package net.fabricmc.fabric.mixin.gamerule;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.gamerules.GameRule;

import net.fabricmc.fabric.impl.gamerule.EnumRuleCommand;
import net.fabricmc.fabric.impl.gamerule.RuleTypeExtensions;
import net.fabricmc.fabric.impl.gamerule.rpc.FabricGameRuleType;

@Mixin(targets = "net.minecraft.server.commands.GameRuleCommand$1")
public abstract class GameRuleCommandVisitorMixin {
	@Final
	@Shadow
	LiteralArgumentBuilder<CommandSourceStack> val$base;

	@Inject(at = @At("HEAD"), method = "visit", cancellable = true)
	private <T> void onRegisterCommand(GameRule<T> rule, CallbackInfo ci) {
		// Check if our type is a EnumRuleType
		if (((RuleTypeExtensions) (Object) rule).fabric_getType() == FabricGameRuleType.ENUM) {
			//noinspection rawtypes,unchecked
			EnumRuleCommand.register(this.val$base, (GameRule<? extends Enum>) rule);
			ci.cancel();
		}
	}
}
