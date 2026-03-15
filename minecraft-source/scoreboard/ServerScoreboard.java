/*
 * External method calls:
 *   Lnet/minecraft/scoreboard/ScoreboardState$Packed;objectives()Ljava/util/List;
 *   Lnet/minecraft/scoreboard/ScoreboardState$Packed;scores()Ljava/util/List;
 *   Lnet/minecraft/scoreboard/ScoreboardState$Packed;displaySlots()Ljava/util/Map;
 *   Lnet/minecraft/scoreboard/ScoreboardState$Packed;teams()Ljava/util/List;
 *   Lnet/minecraft/scoreboard/Scoreboard;updateScore(Lnet/minecraft/scoreboard/ScoreHolder;Lnet/minecraft/scoreboard/ScoreboardObjective;Lnet/minecraft/scoreboard/ScoreboardScore;)V
 *   Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/scoreboard/Scoreboard;resetScore(Lnet/minecraft/scoreboard/ScoreHolder;Lnet/minecraft/scoreboard/ScoreboardObjective;)V
 *   Lnet/minecraft/scoreboard/Scoreboard;onScoreHolderRemoved(Lnet/minecraft/scoreboard/ScoreHolder;)V
 *   Lnet/minecraft/scoreboard/Scoreboard;onScoreRemoved(Lnet/minecraft/scoreboard/ScoreHolder;Lnet/minecraft/scoreboard/ScoreboardObjective;)V
 *   Lnet/minecraft/scoreboard/Scoreboard;addScoreHolderToTeam(Ljava/lang/String;Lnet/minecraft/scoreboard/Team;)Z
 *   Lnet/minecraft/network/packet/s2c/play/TeamS2CPacket;changePlayerTeam(Lnet/minecraft/scoreboard/Team;Ljava/lang/String;Lnet/minecraft/network/packet/s2c/play/TeamS2CPacket$Operation;)Lnet/minecraft/network/packet/s2c/play/TeamS2CPacket;
 *   Lnet/minecraft/scoreboard/Scoreboard;removeScoreHolderFromTeam(Ljava/lang/String;Lnet/minecraft/scoreboard/Team;)V
 *   Lnet/minecraft/scoreboard/Scoreboard;updateObjective(Lnet/minecraft/scoreboard/ScoreboardObjective;)V
 *   Lnet/minecraft/scoreboard/Scoreboard;updateExistingObjective(Lnet/minecraft/scoreboard/ScoreboardObjective;)V
 *   Lnet/minecraft/scoreboard/Scoreboard;updateRemovedObjective(Lnet/minecraft/scoreboard/ScoreboardObjective;)V
 *   Lnet/minecraft/scoreboard/Scoreboard;updateScoreboardTeamAndPlayers(Lnet/minecraft/scoreboard/Team;)V
 *   Lnet/minecraft/network/packet/s2c/play/TeamS2CPacket;updateTeam(Lnet/minecraft/scoreboard/Team;Z)Lnet/minecraft/network/packet/s2c/play/TeamS2CPacket;
 *   Lnet/minecraft/scoreboard/Scoreboard;updateScoreboardTeam(Lnet/minecraft/scoreboard/Team;)V
 *   Lnet/minecraft/scoreboard/Scoreboard;updateRemovedTeam(Lnet/minecraft/scoreboard/Team;)V
 *   Lnet/minecraft/network/packet/s2c/play/TeamS2CPacket;updateRemovedTeam(Lnet/minecraft/scoreboard/Team;)Lnet/minecraft/network/packet/s2c/play/TeamS2CPacket;
 *   Lnet/minecraft/scoreboard/ScoreboardDisplaySlot;values()[Lnet/minecraft/scoreboard/ScoreboardDisplaySlot;
 *   Lnet/minecraft/scoreboard/ScoreboardEntry;owner()Ljava/lang/String;
 *   Lnet/minecraft/scoreboard/ScoreboardEntry;display()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/scoreboard/ScoreboardEntry;numberFormatOverride()Lnet/minecraft/scoreboard/number/NumberFormat;
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/server/network/ServerWaypointHandler;refreshTracking(Lnet/minecraft/world/waypoint/ServerWaypoint;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/scoreboard/ServerScoreboard;pack()Ljava/util/List;
 *   Lnet/minecraft/scoreboard/ServerScoreboard;countDisplaySlots(Lnet/minecraft/scoreboard/ScoreboardObjective;)I
 *   Lnet/minecraft/scoreboard/ServerScoreboard;stopSyncing(Lnet/minecraft/scoreboard/ScoreboardObjective;)V
 *   Lnet/minecraft/scoreboard/ServerScoreboard;startSyncing(Lnet/minecraft/scoreboard/ScoreboardObjective;)V
 *   Lnet/minecraft/scoreboard/ServerScoreboard;refreshWaypointTrackingFor(Ljava/lang/String;)V
 *   Lnet/minecraft/scoreboard/ServerScoreboard;refreshWaypointTrackingFor(Lnet/minecraft/scoreboard/Team;)V
 *   Lnet/minecraft/scoreboard/ServerScoreboard;toPacked()Lnet/minecraft/scoreboard/ScoreboardState$Packed;
 *   Lnet/minecraft/scoreboard/ServerScoreboard;createChangePackets(Lnet/minecraft/scoreboard/ScoreboardObjective;)Ljava/util/List;
 *   Lnet/minecraft/scoreboard/ServerScoreboard;createRemovePackets(Lnet/minecraft/scoreboard/ScoreboardObjective;)Ljava/util/List;
 *   Lnet/minecraft/scoreboard/ServerScoreboard;addTeam(Lnet/minecraft/scoreboard/Team$Packed;)V
 *   Lnet/minecraft/scoreboard/ServerScoreboard;addEntry(Lnet/minecraft/scoreboard/Scoreboard$PackedEntry;)V
 *   Lnet/minecraft/scoreboard/ServerScoreboard;addObjective(Lnet/minecraft/scoreboard/ScoreboardObjective$Packed;)V
 */
package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardScoreResetS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardScoreUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardScore;
import net.minecraft.scoreboard.ScoreboardState;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.waypoint.ServerWaypoint;
import org.jspecify.annotations.Nullable;

public class ServerScoreboard
extends Scoreboard {
    private final MinecraftServer server;
    private final Set<ScoreboardObjective> syncableObjectives = Sets.newHashSet();
    private boolean dirty;

    public ServerScoreboard(MinecraftServer server) {
        this.server = server;
    }

    public void read(ScoreboardState.Packed packed) {
        packed.objectives().forEach(objective -> this.addObjective((ScoreboardObjective.Packed)objective));
        packed.scores().forEach(score -> this.addEntry((Scoreboard.PackedEntry)score));
        packed.displaySlots().forEach((slot, objective) -> {
            ScoreboardObjective lv = this.getNullableObjective((String)objective);
            this.setObjectiveSlot((ScoreboardDisplaySlot)slot, lv);
        });
        packed.teams().forEach(team -> this.addTeam((Team.Packed)team));
    }

    private ScoreboardState.Packed toPacked() {
        return new ScoreboardState.Packed(this.getPackedObjectives(), this.pack(), this.getObjectivesBySlots(), this.getPackedTeams());
    }

    @Override
    protected void updateScore(ScoreHolder scoreHolder, ScoreboardObjective objective, ScoreboardScore score) {
        super.updateScore(scoreHolder, objective, score);
        if (this.syncableObjectives.contains(objective)) {
            this.server.getPlayerManager().sendToAll(new ScoreboardScoreUpdateS2CPacket(scoreHolder.getNameForScoreboard(), objective.getName(), score.getScore(), Optional.ofNullable(score.getDisplayText()), Optional.ofNullable(score.getNumberFormat())));
        }
        this.markDirty();
    }

    @Override
    protected void resetScore(ScoreHolder scoreHolder, ScoreboardObjective objective) {
        super.resetScore(scoreHolder, objective);
        this.markDirty();
    }

    @Override
    public void onScoreHolderRemoved(ScoreHolder scoreHolder) {
        super.onScoreHolderRemoved(scoreHolder);
        this.server.getPlayerManager().sendToAll(new ScoreboardScoreResetS2CPacket(scoreHolder.getNameForScoreboard(), null));
        this.markDirty();
    }

    @Override
    public void onScoreRemoved(ScoreHolder scoreHolder, ScoreboardObjective objective) {
        super.onScoreRemoved(scoreHolder, objective);
        if (this.syncableObjectives.contains(objective)) {
            this.server.getPlayerManager().sendToAll(new ScoreboardScoreResetS2CPacket(scoreHolder.getNameForScoreboard(), objective.getName()));
        }
        this.markDirty();
    }

    @Override
    public void setObjectiveSlot(ScoreboardDisplaySlot slot, @Nullable ScoreboardObjective objective) {
        ScoreboardObjective lv = this.getObjectiveForSlot(slot);
        super.setObjectiveSlot(slot, objective);
        if (lv != objective && lv != null) {
            if (this.countDisplaySlots(lv) > 0) {
                this.server.getPlayerManager().sendToAll(new ScoreboardDisplayS2CPacket(slot, objective));
            } else {
                this.stopSyncing(lv);
            }
        }
        if (objective != null) {
            if (this.syncableObjectives.contains(objective)) {
                this.server.getPlayerManager().sendToAll(new ScoreboardDisplayS2CPacket(slot, objective));
            } else {
                this.startSyncing(objective);
            }
        }
        this.markDirty();
    }

    @Override
    public boolean addScoreHolderToTeam(String scoreHolderName, Team team) {
        if (super.addScoreHolderToTeam(scoreHolderName, team)) {
            this.server.getPlayerManager().sendToAll(TeamS2CPacket.changePlayerTeam(team, scoreHolderName, TeamS2CPacket.Operation.ADD));
            this.refreshWaypointTrackingFor(scoreHolderName);
            this.markDirty();
            return true;
        }
        return false;
    }

    @Override
    public void removeScoreHolderFromTeam(String scoreHolderName, Team team) {
        super.removeScoreHolderFromTeam(scoreHolderName, team);
        this.server.getPlayerManager().sendToAll(TeamS2CPacket.changePlayerTeam(team, scoreHolderName, TeamS2CPacket.Operation.REMOVE));
        this.refreshWaypointTrackingFor(scoreHolderName);
        this.markDirty();
    }

    @Override
    public void updateObjective(ScoreboardObjective objective) {
        super.updateObjective(objective);
        this.markDirty();
    }

    @Override
    public void updateExistingObjective(ScoreboardObjective objective) {
        super.updateExistingObjective(objective);
        if (this.syncableObjectives.contains(objective)) {
            this.server.getPlayerManager().sendToAll(new ScoreboardObjectiveUpdateS2CPacket(objective, ScoreboardObjectiveUpdateS2CPacket.UPDATE_MODE));
        }
        this.markDirty();
    }

    @Override
    public void updateRemovedObjective(ScoreboardObjective objective) {
        super.updateRemovedObjective(objective);
        if (this.syncableObjectives.contains(objective)) {
            this.stopSyncing(objective);
        }
        this.markDirty();
    }

    @Override
    public void updateScoreboardTeamAndPlayers(Team team) {
        super.updateScoreboardTeamAndPlayers(team);
        this.server.getPlayerManager().sendToAll(TeamS2CPacket.updateTeam(team, true));
        this.markDirty();
    }

    @Override
    public void updateScoreboardTeam(Team team) {
        super.updateScoreboardTeam(team);
        this.server.getPlayerManager().sendToAll(TeamS2CPacket.updateTeam(team, false));
        this.refreshWaypointTrackingFor(team);
        this.markDirty();
    }

    @Override
    public void updateRemovedTeam(Team team) {
        super.updateRemovedTeam(team);
        this.server.getPlayerManager().sendToAll(TeamS2CPacket.updateRemovedTeam(team));
        this.refreshWaypointTrackingFor(team);
        this.markDirty();
    }

    protected void markDirty() {
        this.dirty = true;
    }

    public void writeTo(ScoreboardState state) {
        if (this.dirty) {
            this.dirty = false;
            state.set(this.toPacked());
        }
    }

    public List<Packet<?>> createChangePackets(ScoreboardObjective objective) {
        ArrayList<Packet<?>> list = Lists.newArrayList();
        list.add(new ScoreboardObjectiveUpdateS2CPacket(objective, ScoreboardObjectiveUpdateS2CPacket.ADD_MODE));
        for (ScoreboardDisplaySlot lv : ScoreboardDisplaySlot.values()) {
            if (this.getObjectiveForSlot(lv) != objective) continue;
            list.add(new ScoreboardDisplayS2CPacket(lv, objective));
        }
        for (ScoreboardEntry lv2 : this.getScoreboardEntries(objective)) {
            list.add(new ScoreboardScoreUpdateS2CPacket(lv2.owner(), objective.getName(), lv2.value(), Optional.ofNullable(lv2.display()), Optional.ofNullable(lv2.numberFormatOverride())));
        }
        return list;
    }

    public void startSyncing(ScoreboardObjective objective) {
        List<Packet<?>> list = this.createChangePackets(objective);
        for (ServerPlayerEntity lv : this.server.getPlayerManager().getPlayerList()) {
            for (Packet<?> lv2 : list) {
                lv.networkHandler.sendPacket(lv2);
            }
        }
        this.syncableObjectives.add(objective);
    }

    public List<Packet<?>> createRemovePackets(ScoreboardObjective objective) {
        ArrayList<Packet<?>> list = Lists.newArrayList();
        list.add(new ScoreboardObjectiveUpdateS2CPacket(objective, ScoreboardObjectiveUpdateS2CPacket.REMOVE_MODE));
        for (ScoreboardDisplaySlot lv : ScoreboardDisplaySlot.values()) {
            if (this.getObjectiveForSlot(lv) != objective) continue;
            list.add(new ScoreboardDisplayS2CPacket(lv, objective));
        }
        return list;
    }

    public void stopSyncing(ScoreboardObjective objective) {
        List<Packet<?>> list = this.createRemovePackets(objective);
        for (ServerPlayerEntity lv : this.server.getPlayerManager().getPlayerList()) {
            for (Packet<?> lv2 : list) {
                lv.networkHandler.sendPacket(lv2);
            }
        }
        this.syncableObjectives.remove(objective);
    }

    public int countDisplaySlots(ScoreboardObjective objective) {
        int i = 0;
        for (ScoreboardDisplaySlot lv : ScoreboardDisplaySlot.values()) {
            if (this.getObjectiveForSlot(lv) != objective) continue;
            ++i;
        }
        return i;
    }

    private void refreshWaypointTrackingFor(String playerName) {
        ServerPlayerEntity lv = this.server.getPlayerManager().getPlayer(playerName);
        if (lv != null) {
            lv.getEntityWorld().getWaypointHandler().refreshTracking(lv);
        }
    }

    private void refreshWaypointTrackingFor(Team team) {
        for (ServerWorld lv : this.server.getWorlds()) {
            team.getPlayerList().stream().map(playerName -> this.server.getPlayerManager().getPlayer((String)playerName)).filter(Objects::nonNull).forEach(player -> lv.getWaypointHandler().refreshTracking((ServerWaypoint)player));
        }
    }
}

