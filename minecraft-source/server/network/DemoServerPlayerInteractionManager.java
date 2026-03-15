/*
 * External method calls:
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/server/network/ServerPlayerEntity;sendMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/server/network/ServerPlayerInteractionManager;processBlockBreakingAction(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/network/packet/c2s/play/PlayerActionC2SPacket$Action;Lnet/minecraft/util/math/Direction;II)V
 *   Lnet/minecraft/server/network/ServerPlayerInteractionManager;interactItem(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/server/network/ServerPlayerInteractionManager;interactBlock(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;
 */
package net.minecraft.server.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DemoServerPlayerInteractionManager
extends ServerPlayerInteractionManager {
    public static final int DEMO_DAYS = 5;
    public static final int DEMO_TIME = 120500;
    private boolean sentHelp;
    private boolean demoEnded;
    private int reminderTicks;
    private int tick;

    public DemoServerPlayerInteractionManager(ServerPlayerEntity arg) {
        super(arg);
    }

    @Override
    public void update() {
        super.update();
        ++this.tick;
        long l = this.world.getTime();
        long m = l / 24000L + 1L;
        if (!this.sentHelp && this.tick > 20) {
            this.sentHelp = true;
            this.player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
        }
        boolean bl = this.demoEnded = l > 120500L;
        if (this.demoEnded) {
            ++this.reminderTicks;
        }
        if (l % 24000L == 500L) {
            if (m <= 6L) {
                if (m == 6L) {
                    this.player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN, GameStateChangeS2CPacket.DEMO_EXPIRY_NOTICE));
                } else {
                    this.player.sendMessage(Text.translatable("demo.day." + m));
                }
            }
        } else if (m == 1L) {
            if (l == 100L) {
                this.player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN, GameStateChangeS2CPacket.DEMO_MOVEMENT_HELP));
            } else if (l == 175L) {
                this.player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN, GameStateChangeS2CPacket.DEMO_JUMP_HELP));
            } else if (l == 250L) {
                this.player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN, GameStateChangeS2CPacket.DEMO_INVENTORY_HELP));
            }
        } else if (m == 5L && l % 24000L == 22000L) {
            this.player.sendMessage(Text.translatable("demo.day.warning"));
        }
    }

    private void sendDemoReminder() {
        if (this.reminderTicks > 100) {
            this.player.sendMessage(Text.translatable("demo.reminder"));
            this.reminderTicks = 0;
        }
    }

    @Override
    public void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence) {
        if (this.demoEnded) {
            this.sendDemoReminder();
            return;
        }
        super.processBlockBreakingAction(pos, action, direction, worldHeight, sequence);
    }

    @Override
    public ActionResult interactItem(ServerPlayerEntity player, World world, ItemStack stack, Hand hand) {
        if (this.demoEnded) {
            this.sendDemoReminder();
            return ActionResult.PASS;
        }
        return super.interactItem(player, world, stack, hand);
    }

    @Override
    public ActionResult interactBlock(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult) {
        if (this.demoEnded) {
            this.sendDemoReminder();
            return ActionResult.PASS;
        }
        return super.interactBlock(player, world, stack, hand, hitResult);
    }
}

