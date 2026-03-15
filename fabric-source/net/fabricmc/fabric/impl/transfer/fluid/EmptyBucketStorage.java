package net.fabricmc.fabric.impl.transfer.fluid;

import java.util.Iterator;
import java.util.List;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.BlankVariantView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;

/**
 * Storage implementation for empty buckets, accepting any fluid with a bidirectional fluid &lt;-&gt; bucket mapping.
 */
public class EmptyBucketStorage implements InsertionOnlyStorage<FluidVariant> {
	private final ContainerItemContext context;
	private final List<StorageView<FluidVariant>> blankView = List.of(new BlankVariantView<>(FluidVariant.blank(), FluidConstants.BUCKET));

	public EmptyBucketStorage(ContainerItemContext context) {
		this.context = context;
	}

	@Override
	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(resource, maxAmount);

		if (!context.getItemVariant().isOf(Items.BUCKET)) return 0;

		Item fullBucket = resource.getFluid().getBucket();

		// Make sure the resource is a correct fluid mapping: the fluid <-> bucket mapping must be bidirectional.
		if (fullBucket instanceof BucketItemAccessor accessor && resource.isOf(accessor.fabric_getFluid())) {
			if (maxAmount >= FluidConstants.BUCKET) {
				ItemVariant newVariant = ItemVariant.of(fullBucket, context.getItemVariant().getComponents());

				if (context.exchange(newVariant, 1, transaction) == 1) {
					return FluidConstants.BUCKET;
				}
			}
		}

		return 0;
	}

	@Override
	public Iterator<StorageView<FluidVariant>> iterator() {
		return blankView.iterator();
	}

	@Override
	public String toString() {
		return "EmptyBucketStorage[" + context + "]";
	}
}
