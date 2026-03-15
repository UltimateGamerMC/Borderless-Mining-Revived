/*
 * External method calls:
 *   Lnet/minecraft/entity/attribute/EntityAttributeInstance;pack()Lnet/minecraft/entity/attribute/EntityAttributeInstance$Packed;
 *   Lnet/minecraft/entity/attribute/EntityAttributeInstance$Packed;attribute()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/entity/attribute/EntityAttributeInstance;unpack(Lnet/minecraft/entity/attribute/EntityAttributeInstance$Packed;)V
 *   Lnet/minecraft/entity/attribute/EntityAttributeInstance;addPersistentModifiers(Ljava/util/Collection;)V
 *   Lnet/minecraft/entity/attribute/EntityAttributeModifier;id()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/entity/attribute/EntityAttributeInstance;removeModifier(Lnet/minecraft/util/Identifier;)Z
 *   Lnet/minecraft/entity/attribute/EntityAttributeInstance;addTemporaryModifier(Lnet/minecraft/entity/attribute/EntityAttributeModifier;)V
 *   Lnet/minecraft/entity/attribute/DefaultAttributeContainer;createOverride(Ljava/util/function/Consumer;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/entity/attribute/EntityAttributeInstance;
 */
package net.minecraft.entity.attribute;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

public class AttributeContainer {
    private final Map<RegistryEntry<EntityAttribute>, EntityAttributeInstance> custom = new Object2ObjectOpenHashMap<RegistryEntry<EntityAttribute>, EntityAttributeInstance>();
    private final Set<EntityAttributeInstance> tracked = new ObjectOpenHashSet<EntityAttributeInstance>();
    private final Set<EntityAttributeInstance> pendingUpdate = new ObjectOpenHashSet<EntityAttributeInstance>();
    private final DefaultAttributeContainer defaultAttributes;

    public AttributeContainer(DefaultAttributeContainer defaultAttributes) {
        this.defaultAttributes = defaultAttributes;
    }

    private void updateTrackedStatus(EntityAttributeInstance instance) {
        this.pendingUpdate.add(instance);
        if (instance.getAttribute().value().isTracked()) {
            this.tracked.add(instance);
        }
    }

    public Set<EntityAttributeInstance> getTracked() {
        return this.tracked;
    }

    public Set<EntityAttributeInstance> getPendingUpdate() {
        return this.pendingUpdate;
    }

    public Collection<EntityAttributeInstance> getAttributesToSend() {
        return this.custom.values().stream().filter(attribute -> attribute.getAttribute().value().isTracked()).collect(Collectors.toList());
    }

    public @Nullable EntityAttributeInstance getCustomInstance(RegistryEntry<EntityAttribute> attribute2) {
        return this.custom.computeIfAbsent(attribute2, attribute -> this.defaultAttributes.createOverride(this::updateTrackedStatus, (RegistryEntry<EntityAttribute>)attribute));
    }

    public boolean hasAttribute(RegistryEntry<EntityAttribute> attribute) {
        return this.custom.get(attribute) != null || this.defaultAttributes.has(attribute);
    }

    public boolean hasModifierForAttribute(RegistryEntry<EntityAttribute> attribute, Identifier id) {
        EntityAttributeInstance lv = this.custom.get(attribute);
        return lv != null ? lv.getModifier(id) != null : this.defaultAttributes.hasModifier(attribute, id);
    }

    public double getValue(RegistryEntry<EntityAttribute> attribute) {
        EntityAttributeInstance lv = this.custom.get(attribute);
        return lv != null ? lv.getValue() : this.defaultAttributes.getValue(attribute);
    }

    public double getBaseValue(RegistryEntry<EntityAttribute> attribute) {
        EntityAttributeInstance lv = this.custom.get(attribute);
        return lv != null ? lv.getBaseValue() : this.defaultAttributes.getBaseValue(attribute);
    }

    public double getModifierValue(RegistryEntry<EntityAttribute> attribute, Identifier id) {
        EntityAttributeInstance lv = this.custom.get(attribute);
        return lv != null ? lv.getModifier(id).value() : this.defaultAttributes.getModifierValue(attribute, id);
    }

    public void addTemporaryModifiers(Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifiersMap) {
        modifiersMap.forEach((attribute, modifier) -> {
            EntityAttributeInstance lv = this.getCustomInstance((RegistryEntry<EntityAttribute>)attribute);
            if (lv != null) {
                lv.removeModifier(modifier.id());
                lv.addTemporaryModifier((EntityAttributeModifier)modifier);
            }
        });
    }

    public void removeModifiers(Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifiersMap) {
        modifiersMap.asMap().forEach((attribute, modifiers) -> {
            EntityAttributeInstance lv = this.custom.get(attribute);
            if (lv != null) {
                modifiers.forEach(modifier -> lv.removeModifier(modifier.id()));
            }
        });
    }

    public void setFrom(AttributeContainer other) {
        other.custom.values().forEach(attributeInstance -> {
            EntityAttributeInstance lv = this.getCustomInstance(attributeInstance.getAttribute());
            if (lv != null) {
                lv.setFrom((EntityAttributeInstance)attributeInstance);
            }
        });
    }

    public void setBaseFrom(AttributeContainer other) {
        other.custom.values().forEach(attributeInstance -> {
            EntityAttributeInstance lv = this.getCustomInstance(attributeInstance.getAttribute());
            if (lv != null) {
                lv.setBaseValue(attributeInstance.getBaseValue());
            }
        });
    }

    public void addPersistentModifiersFrom(AttributeContainer other) {
        other.custom.values().forEach(attributeInstance -> {
            EntityAttributeInstance lv = this.getCustomInstance(attributeInstance.getAttribute());
            if (lv != null) {
                lv.addPersistentModifiers(attributeInstance.getPersistentModifiers());
            }
        });
    }

    public boolean resetToBaseValue(RegistryEntry<EntityAttribute> attribute) {
        if (!this.defaultAttributes.has(attribute)) {
            return false;
        }
        EntityAttributeInstance lv = this.custom.get(attribute);
        if (lv != null) {
            lv.setBaseValue(this.defaultAttributes.getBaseValue(attribute));
        }
        return true;
    }

    public List<EntityAttributeInstance.Packed> pack() {
        ArrayList<EntityAttributeInstance.Packed> list = new ArrayList<EntityAttributeInstance.Packed>(this.custom.values().size());
        for (EntityAttributeInstance lv : this.custom.values()) {
            list.add(lv.pack());
        }
        return list;
    }

    public void unpack(List<EntityAttributeInstance.Packed> packedList) {
        for (EntityAttributeInstance.Packed lv : packedList) {
            EntityAttributeInstance lv2 = this.getCustomInstance(lv.attribute());
            if (lv2 == null) continue;
            lv2.unpack(lv);
        }
    }
}

