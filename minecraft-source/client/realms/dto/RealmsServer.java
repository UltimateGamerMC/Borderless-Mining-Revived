/*
 * External method calls:
 *   Lnet/minecraft/client/realms/CheckedGson;fromJson(Ljava/lang/String;Ljava/lang/Class;)Lnet/minecraft/client/realms/RealmsSerializable;
 *   Lnet/minecraft/client/realms/dto/RealmsSlot;create(I)Lnet/minecraft/client/realms/dto/RealmsSlot;
 *   Lnet/minecraft/client/MinecraftClient;uuidEquals(Ljava/util/UUID;)Z
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/dto/RealmsServer;replaceNullsWithDefaults(Lnet/minecraft/client/realms/dto/RealmsServer;)V
 *   Lnet/minecraft/client/realms/dto/RealmsServer;sortInvited(Lnet/minecraft/client/realms/dto/RealmsServer;)V
 *   Lnet/minecraft/client/realms/dto/RealmsServer;populateSlots(Lnet/minecraft/client/realms/dto/RealmsServer;)V
 *   Lnet/minecraft/client/realms/dto/RealmsServer;cloneSlots(Ljava/util/Map;)Ljava/util/Map;
 */
package net.minecraft.client.realms.dto;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import com.mojang.util.UUIDTypeAdapter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.realms.CheckedGson;
import net.minecraft.client.realms.RealmsSerializable;
import net.minecraft.client.realms.dto.PlayerInfo;
import net.minecraft.client.realms.dto.RealmsRegionSelectionPreference;
import net.minecraft.client.realms.dto.RealmsSlot;
import net.minecraft.client.realms.dto.ValueObject;
import net.minecraft.client.realms.util.DontSerialize;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class RealmsServer
extends ValueObject
implements RealmsSerializable {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int NO_PARENT = -1;
    public static final Text REALM_CLOSED_TEXT = Text.translatable("mco.play.button.realm.closed");
    @SerializedName(value="id")
    public long id = -1L;
    @SerializedName(value="remoteSubscriptionId")
    public @Nullable String remoteSubscriptionId;
    @SerializedName(value="name")
    public @Nullable String name;
    @SerializedName(value="motd")
    public String description = "";
    @SerializedName(value="state")
    public State state = State.CLOSED;
    @SerializedName(value="owner")
    public @Nullable String owner;
    @SerializedName(value="ownerUUID")
    @JsonAdapter(value=UUIDTypeAdapter.class)
    public UUID ownerUUID = Util.NIL_UUID;
    @SerializedName(value="players")
    public List<PlayerInfo> players = Lists.newArrayList();
    @SerializedName(value="slots")
    private List<RealmsSlot> emptySlots = RealmsServer.getEmptySlots();
    @DontSerialize
    public Map<Integer, RealmsSlot> slots = new HashMap<Integer, RealmsSlot>();
    @SerializedName(value="expired")
    public boolean expired;
    @SerializedName(value="expiredTrial")
    public boolean expiredTrial = false;
    @SerializedName(value="daysLeft")
    public int daysLeft;
    @SerializedName(value="worldType")
    public WorldType worldType = WorldType.NORMAL;
    @SerializedName(value="isHardcore")
    public boolean hardcore = false;
    @SerializedName(value="gameMode")
    public int gameMode = -1;
    @SerializedName(value="activeSlot")
    public int activeSlot = -1;
    @SerializedName(value="minigameName")
    public @Nullable String minigameName;
    @SerializedName(value="minigameId")
    public int minigameId = -1;
    @SerializedName(value="minigameImage")
    public @Nullable String minigameImage;
    @SerializedName(value="parentWorldId")
    public long parentWorldId = -1L;
    @SerializedName(value="parentWorldName")
    public @Nullable String parentWorldName;
    @SerializedName(value="activeVersion")
    public String activeVersion = "";
    @SerializedName(value="compatibility")
    public Compatibility compatibility = Compatibility.UNVERIFIABLE;
    @SerializedName(value="regionSelectionPreference")
    public @Nullable RealmsRegionSelectionPreference regionSelectionPreference;

    public String getDescription() {
        return this.description;
    }

    public @Nullable String getName() {
        return this.name;
    }

    public @Nullable String getMinigameName() {
        return this.minigameName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static RealmsServer parse(CheckedGson gson, String json) {
        try {
            RealmsServer lv = gson.fromJson(json, RealmsServer.class);
            if (lv == null) {
                LOGGER.error("Could not parse McoServer: {}", (Object)json);
                return new RealmsServer();
            }
            RealmsServer.replaceNullsWithDefaults(lv);
            return lv;
        } catch (Exception exception) {
            LOGGER.error("Could not parse McoServer", exception);
            return new RealmsServer();
        }
    }

    public static void replaceNullsWithDefaults(RealmsServer server) {
        if (server.players == null) {
            server.players = Lists.newArrayList();
        }
        if (server.emptySlots == null) {
            server.emptySlots = RealmsServer.getEmptySlots();
        }
        if (server.slots == null) {
            server.slots = new HashMap<Integer, RealmsSlot>();
        }
        if (server.worldType == null) {
            server.worldType = WorldType.NORMAL;
        }
        if (server.activeVersion == null) {
            server.activeVersion = "";
        }
        if (server.compatibility == null) {
            server.compatibility = Compatibility.UNVERIFIABLE;
        }
        if (server.regionSelectionPreference == null) {
            server.regionSelectionPreference = RealmsRegionSelectionPreference.DEFAULT;
        }
        RealmsServer.sortInvited(server);
        RealmsServer.populateSlots(server);
    }

    private static void sortInvited(RealmsServer server) {
        server.players.sort((a, b) -> ComparisonChain.start().compareFalseFirst(b.accepted, a.accepted).compare((Comparable<?>)((Object)a.name.toLowerCase(Locale.ROOT)), (Comparable<?>)((Object)b.name.toLowerCase(Locale.ROOT))).result());
    }

    private static void populateSlots(RealmsServer server) {
        server.emptySlots.forEach(slot -> arg.slots.put(slot.slotId, (RealmsSlot)slot));
        for (int i = 1; i <= 3; ++i) {
            if (server.slots.containsKey(i)) continue;
            server.slots.put(i, RealmsSlot.create(i));
        }
    }

    private static List<RealmsSlot> getEmptySlots() {
        ArrayList<RealmsSlot> list = new ArrayList<RealmsSlot>();
        list.add(RealmsSlot.create(1));
        list.add(RealmsSlot.create(2));
        list.add(RealmsSlot.create(3));
        return list;
    }

    public boolean isCompatible() {
        return this.compatibility.isCompatible();
    }

    public boolean needsUpgrade() {
        return this.compatibility.needsUpgrade();
    }

    public boolean needsDowngrade() {
        return this.compatibility.needsDowngrade();
    }

    public boolean shouldAllowPlay() {
        boolean bl = !this.expired && this.state == State.OPEN;
        return bl && (this.isCompatible() || this.needsUpgrade() || this.isPlayerOwner());
    }

    private boolean isPlayerOwner() {
        return MinecraftClient.getInstance().uuidEquals(this.ownerUUID);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id, this.name, this.description, this.state, this.owner, this.expired});
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        RealmsServer lv = (RealmsServer)o;
        return new EqualsBuilder().append(this.id, lv.id).append(this.name, lv.name).append(this.description, lv.description).append((Object)this.state, (Object)lv.state).append(this.owner, lv.owner).append(this.expired, lv.expired).append((Object)this.worldType, (Object)this.worldType).isEquals();
    }

    public RealmsServer copy() {
        RealmsServer lv = new RealmsServer();
        lv.id = this.id;
        lv.remoteSubscriptionId = this.remoteSubscriptionId;
        lv.name = this.name;
        lv.description = this.description;
        lv.state = this.state;
        lv.owner = this.owner;
        lv.players = this.players;
        lv.emptySlots = this.emptySlots.stream().map(RealmsSlot::copy).toList();
        lv.slots = this.cloneSlots(this.slots);
        lv.expired = this.expired;
        lv.expiredTrial = this.expiredTrial;
        lv.daysLeft = this.daysLeft;
        lv.worldType = this.worldType;
        lv.hardcore = this.hardcore;
        lv.gameMode = this.gameMode;
        lv.ownerUUID = this.ownerUUID;
        lv.minigameName = this.minigameName;
        lv.activeSlot = this.activeSlot;
        lv.minigameId = this.minigameId;
        lv.minigameImage = this.minigameImage;
        lv.parentWorldName = this.parentWorldName;
        lv.parentWorldId = this.parentWorldId;
        lv.activeVersion = this.activeVersion;
        lv.compatibility = this.compatibility;
        lv.regionSelectionPreference = this.regionSelectionPreference != null ? this.regionSelectionPreference.copy() : null;
        return lv;
    }

    public Map<Integer, RealmsSlot> cloneSlots(Map<Integer, RealmsSlot> slots) {
        HashMap<Integer, RealmsSlot> map2 = Maps.newHashMap();
        for (Map.Entry<Integer, RealmsSlot> entry : slots.entrySet()) {
            map2.put(entry.getKey(), new RealmsSlot(entry.getKey(), entry.getValue().options.copy(), entry.getValue().settings));
        }
        return map2;
    }

    public boolean isPrerelease() {
        return this.parentWorldId != -1L;
    }

    public boolean isMinigame() {
        return this.worldType == WorldType.MINIGAME;
    }

    public String getWorldName(int slotId) {
        if (this.name == null) {
            return this.slots.get((Object)Integer.valueOf((int)slotId)).options.getSlotName(slotId);
        }
        return this.name + " (" + this.slots.get((Object)Integer.valueOf((int)slotId)).options.getSlotName(slotId) + ")";
    }

    public ServerInfo createServerInfo(String address) {
        return new ServerInfo(Objects.requireNonNullElse(this.name, "unknown server"), address, ServerInfo.ServerType.REALM);
    }

    @Environment(value=EnvType.CLIENT)
    public static enum State {
        CLOSED,
        OPEN,
        UNINITIALIZED;

    }

    @Environment(value=EnvType.CLIENT)
    public static enum WorldType {
        NORMAL("normal"),
        MINIGAME("minigame"),
        ADVENTUREMAP("adventureMap"),
        EXPERIENCE("experience"),
        INSPIRATION("inspiration"),
        UNKNOWN("unknown");

        private static final String TRANSLATION_KEY_PREFIX = "mco.backup.entry.worldType.";
        private final Text text;

        private WorldType(String id) {
            this.text = Text.translatable(TRANSLATION_KEY_PREFIX + id);
        }

        public Text getText() {
            return this.text;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Compatibility {
        UNVERIFIABLE,
        INCOMPATIBLE,
        RELEASE_TYPE_INCOMPATIBLE,
        NEEDS_DOWNGRADE,
        NEEDS_UPGRADE,
        COMPATIBLE;


        public boolean isCompatible() {
            return this == COMPATIBLE;
        }

        public boolean needsUpgrade() {
            return this == NEEDS_UPGRADE;
        }

        public boolean needsDowngrade() {
            return this == NEEDS_DOWNGRADE;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static class McoServerComparator
    implements Comparator<RealmsServer> {
        private final String refOwner;

        public McoServerComparator(String owner) {
            this.refOwner = owner;
        }

        @Override
        public int compare(RealmsServer arg, RealmsServer arg2) {
            return ComparisonChain.start().compareTrueFirst(arg.isPrerelease(), arg2.isPrerelease()).compareTrueFirst(arg.state == State.UNINITIALIZED, arg2.state == State.UNINITIALIZED).compareTrueFirst(arg.expiredTrial, arg2.expiredTrial).compareTrueFirst(Objects.equals(arg.owner, this.refOwner), Objects.equals(arg2.owner, this.refOwner)).compareFalseFirst(arg.expired, arg2.expired).compareTrueFirst(arg.state == State.OPEN, arg2.state == State.OPEN).compare(arg.id, arg2.id).result();
        }

        @Override
        public /* synthetic */ int compare(Object one, Object two) {
            return this.compare((RealmsServer)one, (RealmsServer)two);
        }
    }
}

