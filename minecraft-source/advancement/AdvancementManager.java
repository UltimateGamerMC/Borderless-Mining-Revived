/*
 * External method calls:
 *   Lnet/minecraft/advancement/AdvancementEntry;id()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/advancement/AdvancementManager$Listener;onRootRemoved(Lnet/minecraft/advancement/PlacedAdvancement;)V
 *   Lnet/minecraft/advancement/AdvancementManager$Listener;onDependentRemoved(Lnet/minecraft/advancement/PlacedAdvancement;)V
 *   Lnet/minecraft/advancement/Advancement;parent()Ljava/util/Optional;
 *   Lnet/minecraft/advancement/PlacedAdvancement;addChild(Lnet/minecraft/advancement/PlacedAdvancement;)V
 *   Lnet/minecraft/advancement/AdvancementManager$Listener;onRootAdded(Lnet/minecraft/advancement/PlacedAdvancement;)V
 *   Lnet/minecraft/advancement/AdvancementManager$Listener;onDependentAdded(Lnet/minecraft/advancement/PlacedAdvancement;)V
 */
package net.minecraft.advancement;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class AdvancementManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<Identifier, PlacedAdvancement> advancements = new Object2ObjectOpenHashMap<Identifier, PlacedAdvancement>();
    private final Set<PlacedAdvancement> roots = new ObjectLinkedOpenHashSet<PlacedAdvancement>();
    private final Set<PlacedAdvancement> dependents = new ObjectLinkedOpenHashSet<PlacedAdvancement>();
    private @Nullable Listener listener;

    private void remove(PlacedAdvancement advancement) {
        for (PlacedAdvancement lv : advancement.getChildren()) {
            this.remove(lv);
        }
        LOGGER.info("Forgot about advancement {}", (Object)advancement.getAdvancementEntry());
        this.advancements.remove(advancement.getAdvancementEntry().id());
        if (advancement.getParent() == null) {
            this.roots.remove(advancement);
            if (this.listener != null) {
                this.listener.onRootRemoved(advancement);
            }
        } else {
            this.dependents.remove(advancement);
            if (this.listener != null) {
                this.listener.onDependentRemoved(advancement);
            }
        }
    }

    public void removeAll(Set<Identifier> advancements) {
        for (Identifier lv : advancements) {
            PlacedAdvancement lv2 = this.advancements.get(lv);
            if (lv2 == null) {
                LOGGER.warn("Told to remove advancement {} but I don't know what that is", (Object)lv);
                continue;
            }
            this.remove(lv2);
        }
    }

    public void addAll(Collection<AdvancementEntry> advancements) {
        ArrayList<AdvancementEntry> list = new ArrayList<AdvancementEntry>(advancements);
        while (!list.isEmpty()) {
            if (list.removeIf(this::tryAdd)) continue;
            LOGGER.error("Couldn't load advancements: {}", (Object)list);
            break;
        }
        LOGGER.info("Loaded {} advancements", (Object)this.advancements.size());
    }

    private boolean tryAdd(AdvancementEntry advancement) {
        Optional<Identifier> optional = advancement.value().parent();
        PlacedAdvancement lv = optional.map(this.advancements::get).orElse(null);
        if (lv == null && optional.isPresent()) {
            return false;
        }
        PlacedAdvancement lv2 = new PlacedAdvancement(advancement, lv);
        if (lv != null) {
            lv.addChild(lv2);
        }
        this.advancements.put(advancement.id(), lv2);
        if (lv == null) {
            this.roots.add(lv2);
            if (this.listener != null) {
                this.listener.onRootAdded(lv2);
            }
        } else {
            this.dependents.add(lv2);
            if (this.listener != null) {
                this.listener.onDependentAdded(lv2);
            }
        }
        return true;
    }

    public void clear() {
        this.advancements.clear();
        this.roots.clear();
        this.dependents.clear();
        if (this.listener != null) {
            this.listener.onClear();
        }
    }

    public Iterable<PlacedAdvancement> getRoots() {
        return this.roots;
    }

    public Collection<PlacedAdvancement> getAdvancements() {
        return this.advancements.values();
    }

    public @Nullable PlacedAdvancement get(Identifier id) {
        return this.advancements.get(id);
    }

    public @Nullable PlacedAdvancement get(AdvancementEntry advancement) {
        return this.advancements.get(advancement.id());
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
        if (listener != null) {
            for (PlacedAdvancement lv : this.roots) {
                listener.onRootAdded(lv);
            }
            for (PlacedAdvancement lv : this.dependents) {
                listener.onDependentAdded(lv);
            }
        }
    }

    public static interface Listener {
        public void onRootAdded(PlacedAdvancement var1);

        public void onRootRemoved(PlacedAdvancement var1);

        public void onDependentAdded(PlacedAdvancement var1);

        public void onDependentRemoved(PlacedAdvancement var1);

        public void onClear();
    }
}

