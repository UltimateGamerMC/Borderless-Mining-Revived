package net.minecraft.entity;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public interface Tameable {
    public @Nullable LazyEntityReference<LivingEntity> getOwnerReference();

    public World getEntityWorld();

    default public @Nullable LivingEntity getOwner() {
        return LazyEntityReference.getLivingEntity(this.getOwnerReference(), this.getEntityWorld());
    }

    default public @Nullable LivingEntity getTopLevelOwner() {
        ObjectArraySet set = new ObjectArraySet();
        LivingEntity lv = this.getOwner();
        set.add(this);
        while (lv instanceof Tameable) {
            Tameable lv2 = (Tameable)((Object)lv);
            LivingEntity lv3 = lv2.getOwner();
            if (set.contains(lv3)) {
                return null;
            }
            set.add(lv);
            lv = lv2.getOwner();
        }
        return lv;
    }
}

