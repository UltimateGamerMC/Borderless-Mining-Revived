package net.minecraft.command.permission;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionPredicate;

public class OrPermissionPredicate
implements PermissionPredicate {
    private final ReferenceSet<PermissionPredicate> predicates = new ReferenceArraySet<PermissionPredicate>();

    OrPermissionPredicate(PermissionPredicate a, PermissionPredicate b) {
        this.predicates.add(a);
        this.predicates.add(b);
        this.validate();
    }

    private OrPermissionPredicate(ReferenceSet<PermissionPredicate> predicates, PermissionPredicate predicate) {
        this.predicates.addAll(predicates);
        this.predicates.add(predicate);
        this.validate();
    }

    private OrPermissionPredicate(ReferenceSet<PermissionPredicate> a, ReferenceSet<PermissionPredicate> b) {
        this.predicates.addAll(a);
        this.predicates.addAll(b);
        this.validate();
    }

    @Override
    public boolean hasPermission(Permission arg) {
        for (PermissionPredicate lv : this.predicates) {
            if (!lv.hasPermission(arg)) continue;
            return true;
        }
        return false;
    }

    @Override
    public PermissionPredicate or(PermissionPredicate other) {
        if (other instanceof OrPermissionPredicate) {
            OrPermissionPredicate lv = (OrPermissionPredicate)other;
            return new OrPermissionPredicate(this.predicates, lv.predicates);
        }
        return new OrPermissionPredicate(this.predicates, other);
    }

    @VisibleForTesting
    public ReferenceSet<PermissionPredicate> getPredicates() {
        return new ReferenceArraySet<PermissionPredicate>(this.predicates);
    }

    private void validate() {
        for (PermissionPredicate lv : this.predicates) {
            if (!(lv instanceof OrPermissionPredicate)) continue;
            throw new IllegalArgumentException("Cannot have PermissionSetUnion within another PermissionSetUnion");
        }
    }
}

