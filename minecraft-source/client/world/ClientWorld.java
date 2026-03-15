/*
 * External method calls:
 *   Lnet/minecraft/client/network/PendingUpdateManager;processPendingUpdates(ILnet/minecraft/client/world/ClientWorld;)V
 *   Lnet/minecraft/entity/player/PlayerEntity;collidesWithStateAtPos(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z
 *   Lnet/minecraft/entity/player/PlayerEntity;updatePosition(DDD)V
 *   Lnet/minecraft/client/network/PendingUpdateManager;addPendingUpdate(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/client/network/ClientPlayerEntity;)V
 *   Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
 *   Lnet/minecraft/world/WorldProperties$SpawnPoint;create(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/math/BlockPos;FF)Lnet/minecraft/world/WorldProperties$SpawnPoint;
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess;builder()Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;build()Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess;
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;world(Lnet/minecraft/world/World;)Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;timeBased(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/world/attribute/EnvironmentAttributeFunction$TimeBased;)Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;
 *   Lnet/minecraft/client/render/EndLightFlashManager;tick(J)V
 *   Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;I)V
 *   Lnet/minecraft/client/world/BlockParticleEffectsManager;tick(Lnet/minecraft/client/world/ClientWorld;)V
 *   Lnet/minecraft/util/profiler/Profiler;scoped(Ljava/lang/String;)Lnet/minecraft/util/profiler/ScopedProfiler;
 *   Lnet/minecraft/client/world/ClientChunkManager;tick(Ljava/util/function/BooleanSupplier;Z)V
 *   Lnet/minecraft/util/profiling/jfr/FlightProfiler;onClientFps(I)V
 *   Lnet/minecraft/world/entity/EntityLookup;iterate()Ljava/lang/Iterable;
 *   Lnet/minecraft/world/EntityList;forEach(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/util/function/Supplier;)V
 *   Lnet/minecraft/world/entity/ClientEntityManager;stopTicking(Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/world/entity/ClientEntityManager;startTicking(Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/client/render/WorldRenderer;onChunkUnload(J)V
 *   Lnet/minecraft/world/entity/ClientEntityManager;addEntity(Lnet/minecraft/world/entity/EntityLike;)V
 *   Lnet/minecraft/network/ClientConnection;disconnect(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/block/Block;randomDisplayTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V
 *   Lnet/minecraft/fluid/FluidState;randomDisplayTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V
 *   Lnet/minecraft/world/attribute/AmbientParticle;particle()Lnet/minecraft/particle/ParticleEffect;
 *   Lnet/minecraft/world/World;addDetailsToCrashReport(Lnet/minecraft/util/crash/CrashReport;)Lnet/minecraft/util/crash/CrashReportSection;
 *   Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;
 *   Lnet/minecraft/client/particle/ParticleManager;addParticle(Lnet/minecraft/client/particle/Particle;)V
 *   Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/client/render/WorldRenderer;updateBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;I)V
 *   Lnet/minecraft/client/render/WorldRenderer;scheduleBlockRerenderIfNeeded(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)V
 *   Lnet/minecraft/client/render/WorldRenderer;scheduleChunkRenders3x3x3(III)V
 *   Lnet/minecraft/client/render/WorldRenderer;scheduleChunkRenders(IIIIII)V
 *   Lnet/minecraft/client/world/WorldEventHandler;processGlobalEvent(ILnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/client/world/WorldEventHandler;processWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;
 *   Lnet/minecraft/util/crash/CrashReport;addElement(Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReportSection;
 *   Lnet/minecraft/util/crash/CrashReportSection;createPositionString(Lnet/minecraft/world/HeightLimitView;Lnet/minecraft/util/math/BlockPos;)Ljava/lang/String;
 *   Lnet/minecraft/client/particle/ParticleManager;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;
 *   Lnet/minecraft/world/dimension/DimensionType;cardinalLightType()Lnet/minecraft/world/dimension/DimensionType$CardinalLightType;
 *   Lnet/minecraft/util/shape/VoxelShape;forEachBox(Lnet/minecraft/util/shape/VoxelShapes$BoxConsumer;)V
 *   Lnet/minecraft/client/particle/BlockDustParticle;move(F)Lnet/minecraft/client/particle/Particle;
 *   Lnet/minecraft/client/particle/Particle;scale(F)Lnet/minecraft/client/particle/Particle;
 *   Lnet/minecraft/client/network/ClientPlayNetworkHandler;registerForCleaning(Lnet/minecraft/client/world/DataCache;)V
 *   Lnet/minecraft/client/world/BlockParticleEffectsManager;scheduleBlockParticles(Lnet/minecraft/util/math/Vec3d;FILnet/minecraft/util/collection/Pool;)V
 *   Lnet/minecraft/util/crash/CrashReportSection;createPositionString(Lnet/minecraft/world/HeightLimitView;DDD)Ljava/lang/String;
 *   Lnet/minecraft/client/world/BiomeColorCache;reset(II)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/world/ClientWorld;addClientSideAttributes(Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;)Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;
 *   Lnet/minecraft/client/world/ClientWorld;tickPassenger(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/client/world/ClientWorld;removeEntity(ILnet/minecraft/entity/Entity$RemovalReason;)V
 *   Lnet/minecraft/client/world/ClientWorld;randomBlockDisplayTick(IIIILnet/minecraft/util/math/random/Random;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos$Mutable;)V
 *   Lnet/minecraft/client/world/ClientWorld;addParticle(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/particle/ParticleEffect;Z)V
 *   Lnet/minecraft/client/world/ClientWorld;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V
 *   Lnet/minecraft/client/world/ClientWorld;addParticle(DDDDDLnet/minecraft/particle/ParticleEffect;)V
 *   Lnet/minecraft/client/world/ClientWorld;addParticle(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/util/shape/VoxelShape;D)V
 *   Lnet/minecraft/client/world/ClientWorld;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V
 *   Lnet/minecraft/client/world/ClientWorld;addParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)V
 *   Lnet/minecraft/client/world/ClientWorld;ensureWithinBorder(Lnet/minecraft/world/WorldProperties$SpawnPoint;)Lnet/minecraft/world/WorldProperties$SpawnPoint;
 *   Lnet/minecraft/client/world/ClientWorld;tickEntity(Ljava/util/function/Consumer;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/client/world/ClientWorld;calculateColor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/ColorResolver;)I
 */
package net.minecraft.client.world;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.lang.runtime.SwitchBootstraps;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.EndLightFlashManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.sound.EndLightFlashSoundInstance;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.world.BiomeColorCache;
import net.minecraft.client.world.BlockParticleEffectsManager;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.DataCache;
import net.minecraft.client.world.WorldEventHandler;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.Packet;
import net.minecraft.particle.BlockParticleEffect;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticlesMode;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.CuboidBlockIterator;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.util.profiler.ScopedProfiler;
import net.minecraft.util.profiling.jfr.FlightProfiler;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.Difficulty;
import net.minecraft.world.EntityList;
import net.minecraft.world.GameMode;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.EnvironmentAttributeAccess;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.WorldEnvironmentAttributeAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.ColorResolver;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.entity.ClientEntityManager;
import net.minecraft.world.entity.EntityHandler;
import net.minecraft.world.entity.EntityLookup;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.tick.EmptyTickSchedulers;
import net.minecraft.world.tick.QueryableTickScheduler;
import net.minecraft.world.tick.TickManager;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class ClientWorld
extends World
implements DataCache.CacheContext<ClientWorld> {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Text QUITTING_MULTIPLAYER_TEXT = Text.translatable("multiplayer.status.quitting");
    private static final double PARTICLE_Y_OFFSET = 0.05;
    private static final int field_34805 = 10;
    private static final int field_34806 = 1000;
    final EntityList entityList = new EntityList();
    private final ClientEntityManager<Entity> entityManager = new ClientEntityManager<Entity>(Entity.class, new ClientEntityHandler());
    private final ClientPlayNetworkHandler networkHandler;
    private final WorldRenderer worldRenderer;
    private final WorldEventHandler worldEventHandler;
    private final Properties clientWorldProperties;
    private final TickManager tickManager;
    private final @Nullable EndLightFlashManager endLightFlashManager;
    private final MinecraftClient client = MinecraftClient.getInstance();
    final List<AbstractClientPlayerEntity> players = Lists.newArrayList();
    final List<EnderDragonPart> enderDragonParts = Lists.newArrayList();
    private final Map<MapIdComponent, MapState> mapStates = Maps.newHashMap();
    private int lightningTicksLeft;
    private final Object2ObjectArrayMap<ColorResolver, BiomeColorCache> colorCache = Util.make(new Object2ObjectArrayMap(3), map -> {
        map.put(BiomeColors.GRASS_COLOR, new BiomeColorCache(pos -> this.calculateColor((BlockPos)pos, BiomeColors.GRASS_COLOR)));
        map.put(BiomeColors.FOLIAGE_COLOR, new BiomeColorCache(pos -> this.calculateColor((BlockPos)pos, BiomeColors.FOLIAGE_COLOR)));
        map.put(BiomeColors.DRY_FOLIAGE_COLOR, new BiomeColorCache(pos -> this.calculateColor((BlockPos)pos, BiomeColors.DRY_FOLIAGE_COLOR)));
        map.put(BiomeColors.WATER_COLOR, new BiomeColorCache(pos -> this.calculateColor((BlockPos)pos, BiomeColors.WATER_COLOR)));
    });
    private final ClientChunkManager chunkManager;
    private final Deque<Runnable> chunkUpdaters = Queues.newArrayDeque();
    private int simulationDistance;
    private final PendingUpdateManager pendingUpdateManager = new PendingUpdateManager();
    private final Set<BlockEntity> blockEntities = new ReferenceOpenHashSet<BlockEntity>();
    private final BlockParticleEffectsManager blockParticlesManager = new BlockParticleEffectsManager();
    private final WorldBorder worldBorder = new WorldBorder();
    private final WorldEnvironmentAttributeAccess environmentAttributeAccess;
    private final int seaLevel;
    private boolean shouldTickTimeOfDay;
    private static final Set<Item> BLOCK_MARKER_ITEMS = Set.of(Items.BARRIER, Items.LIGHT);

    public void handlePlayerActionResponse(int sequence) {
        if (SharedConstants.BLOCK_BREAK) {
            LOGGER.debug("ACK {}", (Object)sequence);
        }
        this.pendingUpdateManager.processPendingUpdates(sequence, this);
    }

    @Override
    public void loadBlockEntity(BlockEntity blockEntity) {
        BlockEntityRenderer lv = this.client.getBlockEntityRenderDispatcher().get(blockEntity);
        if (lv != null && lv.rendersOutsideBoundingBox()) {
            this.blockEntities.add(blockEntity);
        }
    }

    public Set<BlockEntity> getBlockEntities() {
        return this.blockEntities;
    }

    public void handleBlockUpdate(BlockPos pos, BlockState state, @Block.SetBlockStateFlag int flags) {
        if (!this.pendingUpdateManager.hasPendingUpdate(pos, state)) {
            super.setBlockState(pos, state, flags, 512);
        }
    }

    public void processPendingUpdate(BlockPos pos, BlockState state, Vec3d playerPos) {
        BlockState lv = this.getBlockState(pos);
        if (lv != state) {
            this.setBlockState(pos, state, Block.NOTIFY_ALL | Block.FORCE_STATE);
            ClientPlayerEntity lv2 = this.client.player;
            if (this == lv2.getEntityWorld() && lv2.collidesWithStateAtPos(pos, state)) {
                lv2.updatePosition(playerPos.x, playerPos.y, playerPos.z);
            }
        }
    }

    PendingUpdateManager getPendingUpdateManager() {
        return this.pendingUpdateManager;
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, @Block.SetBlockStateFlag int flags, int maxUpdateDepth) {
        if (this.pendingUpdateManager.hasPendingSequence()) {
            BlockState lv = this.getBlockState(pos);
            boolean bl = super.setBlockState(pos, state, flags, maxUpdateDepth);
            if (bl) {
                this.pendingUpdateManager.addPendingUpdate(pos, lv, this.client.player);
            }
            return bl;
        }
        return super.setBlockState(pos, state, flags, maxUpdateDepth);
    }

    public ClientWorld(ClientPlayNetworkHandler networkHandler, Properties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimensionType, int loadDistance, int simulationDistance, WorldRenderer worldRenderer, boolean debugWorld, long seed, int seaLevel) {
        super(properties, registryRef, networkHandler.getRegistryManager(), dimensionType, true, debugWorld, seed, 1000000);
        this.networkHandler = networkHandler;
        this.chunkManager = new ClientChunkManager(this, loadDistance);
        this.tickManager = new TickManager();
        this.clientWorldProperties = properties;
        this.worldRenderer = worldRenderer;
        this.seaLevel = seaLevel;
        this.worldEventHandler = new WorldEventHandler(this.client, this);
        this.endLightFlashManager = dimensionType.value().getSkybox() ? new EndLightFlashManager() : null;
        this.setSpawnPoint(WorldProperties.SpawnPoint.create(registryRef, new BlockPos(8, 64, 8), 0.0f, 0.0f));
        this.simulationDistance = simulationDistance;
        this.environmentAttributeAccess = this.addClientSideAttributes(WorldEnvironmentAttributeAccess.builder()).build();
        this.calculateAmbientDarkness();
        if (this.canHaveWeather()) {
            this.initWeatherGradients();
        }
    }

    private WorldEnvironmentAttributeAccess.Builder addClientSideAttributes(WorldEnvironmentAttributeAccess.Builder builder) {
        builder.world(this);
        int i = ColorHelper.getArgb(204, 204, 255);
        builder.timeBased(EnvironmentAttributes.SKY_COLOR_VISUAL, (color, time) -> {
            if (this.getLightningTicksLeft() > 0) {
                return ColorHelper.lerp(0.22f, color, i);
            }
            return color;
        });
        builder.timeBased(EnvironmentAttributes.SKY_LIGHT_FACTOR_VISUAL, (factor, time) -> Float.valueOf(this.getLightningTicksLeft() > 0 ? 1.0f : factor.floatValue()));
        return builder;
    }

    public void enqueueChunkUpdate(Runnable updater) {
        this.chunkUpdaters.add(updater);
    }

    public void runQueuedChunkUpdates() {
        Runnable runnable;
        int i = this.chunkUpdaters.size();
        int j = i < 1000 ? Math.max(10, i / 10) : i;
        for (int k = 0; k < j && (runnable = this.chunkUpdaters.poll()) != null; ++k) {
            runnable.run();
        }
    }

    public @Nullable EndLightFlashManager getEndLightFlashManager() {
        return this.endLightFlashManager;
    }

    public void tick(BooleanSupplier shouldKeepTicking) {
        this.calculateAmbientDarkness();
        if (this.getTickManager().shouldTick()) {
            this.getWorldBorder().tick();
            this.tickTime();
        }
        if (this.lightningTicksLeft > 0) {
            this.setLightningTicksLeft(this.lightningTicksLeft - 1);
        }
        if (this.endLightFlashManager != null) {
            this.endLightFlashManager.tick(this.getTime());
            if (this.endLightFlashManager.shouldFlash() && !(this.client.currentScreen instanceof CreditsScreen)) {
                this.client.getSoundManager().play(new EndLightFlashSoundInstance(SoundEvents.WEATHER_END_FLASH, SoundCategory.WEATHER, this.random, this.client.gameRenderer.getCamera(), this.endLightFlashManager.getPitch(), this.endLightFlashManager.getYaw()), 30);
            }
        }
        this.blockParticlesManager.tick(this);
        try (ScopedProfiler lv = Profilers.get().scoped("blocks");){
            this.chunkManager.tick(shouldKeepTicking, true);
        }
        FlightProfiler.INSTANCE.onClientFps(this.client.getCurrentFps());
        this.getEnvironmentAttributes().tick();
    }

    private void tickTime() {
        this.clientWorldProperties.setTime(this.clientWorldProperties.getTime() + 1L);
        if (this.shouldTickTimeOfDay) {
            this.clientWorldProperties.setTimeOfDay(this.clientWorldProperties.getTimeOfDay() + 1L);
        }
    }

    public void setTime(long time, long timeOfDay, boolean shouldTickTimeOfDay) {
        this.clientWorldProperties.setTime(time);
        this.clientWorldProperties.setTimeOfDay(timeOfDay);
        this.shouldTickTimeOfDay = shouldTickTimeOfDay;
    }

    public Iterable<Entity> getEntities() {
        return this.getEntityLookup().iterate();
    }

    public void tickEntities() {
        this.entityList.forEach(entity -> {
            if (entity.isRemoved() || entity.hasVehicle() || this.tickManager.shouldSkipTick((Entity)entity)) {
                return;
            }
            this.tickEntity(this::tickEntity, entity);
        });
    }

    public boolean hasEntity(Entity entity) {
        return this.entityList.has(entity);
    }

    @Override
    public boolean shouldUpdatePostDeath(Entity entity) {
        return entity.getChunkPos().getChebyshevDistance(this.client.player.getChunkPos()) <= this.simulationDistance;
    }

    public void tickEntity(Entity entity) {
        entity.resetPosition();
        ++entity.age;
        Profilers.get().push(() -> Registries.ENTITY_TYPE.getId(entity.getType()).toString());
        entity.tick();
        Profilers.get().pop();
        for (Entity lv : entity.getPassengerList()) {
            this.tickPassenger(entity, lv);
        }
    }

    private void tickPassenger(Entity entity, Entity passenger) {
        if (passenger.isRemoved() || passenger.getVehicle() != entity) {
            passenger.stopRiding();
            return;
        }
        if (!(passenger instanceof PlayerEntity) && !this.entityList.has(passenger)) {
            return;
        }
        passenger.resetPosition();
        ++passenger.age;
        passenger.tickRiding();
        for (Entity lv : passenger.getPassengerList()) {
            this.tickPassenger(passenger, lv);
        }
    }

    public void unloadBlockEntities(WorldChunk chunk) {
        chunk.clear();
        this.chunkManager.getLightingProvider().setColumnEnabled(chunk.getPos(), false);
        this.entityManager.stopTicking(chunk.getPos());
    }

    public void resetChunkColor(ChunkPos chunkPos) {
        this.colorCache.forEach((resolver, cache) -> cache.reset(arg.x, arg.z));
        this.entityManager.startTicking(chunkPos);
    }

    public void onChunkUnload(long sectionPos) {
        this.worldRenderer.onChunkUnload(sectionPos);
    }

    public void reloadColor() {
        this.colorCache.forEach((resolver, cache) -> cache.reset());
    }

    @Override
    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        return true;
    }

    public int getRegularEntityCount() {
        return this.entityManager.getEntityCount();
    }

    public void addEntity(Entity entity) {
        this.removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
        this.entityManager.addEntity(entity);
    }

    public void removeEntity(int entityId, Entity.RemovalReason removalReason) {
        Entity lv = this.getEntityLookup().get(entityId);
        if (lv != null) {
            lv.setRemoved(removalReason);
            lv.onRemoved();
        }
    }

    @Override
    public List<Entity> getCrammedEntities(Entity entity, Box box) {
        ClientPlayerEntity lv = this.client.player;
        if (lv != null && lv != entity && lv.getBoundingBox().intersects(box) && EntityPredicates.canBePushedBy(entity).test(lv)) {
            return List.of(lv);
        }
        return List.of();
    }

    @Override
    public @Nullable Entity getEntityById(int id) {
        return this.getEntityLookup().get(id);
    }

    public void disconnect(Text reasonText) {
        this.networkHandler.getConnection().disconnect(reasonText);
    }

    public void doRandomBlockDisplayTicks(int centerX, int centerY, int centerZ) {
        int l = 32;
        Random lv = Random.create();
        Block lv2 = this.getBlockParticle();
        BlockPos.Mutable lv3 = new BlockPos.Mutable();
        for (int m = 0; m < 667; ++m) {
            this.randomBlockDisplayTick(centerX, centerY, centerZ, 16, lv, lv2, lv3);
            this.randomBlockDisplayTick(centerX, centerY, centerZ, 32, lv, lv2, lv3);
        }
    }

    private @Nullable Block getBlockParticle() {
        ItemStack lv;
        Item lv2;
        if (this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE && BLOCK_MARKER_ITEMS.contains(lv2 = (lv = this.client.player.getMainHandStack()).getItem()) && lv2 instanceof BlockItem) {
            BlockItem lv3 = (BlockItem)lv2;
            return lv3.getBlock();
        }
        return null;
    }

    public void randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, Random random, @Nullable Block block, BlockPos.Mutable pos) {
        int m = centerX + this.random.nextInt(radius) - this.random.nextInt(radius);
        int n = centerY + this.random.nextInt(radius) - this.random.nextInt(radius);
        int o = centerZ + this.random.nextInt(radius) - this.random.nextInt(radius);
        pos.set(m, n, o);
        BlockState lv = this.getBlockState(pos);
        lv.getBlock().randomDisplayTick(lv, this, pos, random);
        FluidState lv2 = this.getFluidState(pos);
        if (!lv2.isEmpty()) {
            lv2.randomDisplayTick(this, pos, random);
            ParticleEffect lv3 = lv2.getParticle();
            if (lv3 != null && this.random.nextInt(10) == 0) {
                boolean bl = lv.isSideSolidFullSquare(this, pos, Direction.DOWN);
                Vec3i lv4 = pos.down();
                this.addParticle((BlockPos)lv4, this.getBlockState((BlockPos)lv4), lv3, bl);
            }
        }
        if (block == lv.getBlock()) {
            this.addParticleClient(new BlockStateParticleEffect(ParticleTypes.BLOCK_MARKER, lv), (double)m + 0.5, (double)n + 0.5, (double)o + 0.5, 0.0, 0.0, 0.0);
        }
        if (!lv.isFullCube(this, pos)) {
            for (AmbientParticle lv5 : this.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.AMBIENT_PARTICLES_VISUAL, pos)) {
                if (!lv5.shouldAddParticle(this.random)) continue;
                this.addParticleClient(lv5.particle(), (double)pos.getX() + this.random.nextDouble(), (double)pos.getY() + this.random.nextDouble(), (double)pos.getZ() + this.random.nextDouble(), 0.0, 0.0, 0.0);
            }
        }
    }

    private void addParticle(BlockPos pos, BlockState state, ParticleEffect parameters, boolean solidBelow) {
        if (!state.getFluidState().isEmpty()) {
            return;
        }
        VoxelShape lv = state.getCollisionShape(this, pos);
        double d = lv.getMax(Direction.Axis.Y);
        if (d < 1.0) {
            if (solidBelow) {
                this.addParticle(pos.getX(), pos.getX() + 1, pos.getZ(), pos.getZ() + 1, (double)(pos.getY() + 1) - 0.05, parameters);
            }
        } else if (!state.isIn(BlockTags.IMPERMEABLE)) {
            double e = lv.getMin(Direction.Axis.Y);
            if (e > 0.0) {
                this.addParticle(pos, parameters, lv, (double)pos.getY() + e - 0.05);
            } else {
                BlockPos lv2 = pos.down();
                BlockState lv3 = this.getBlockState(lv2);
                VoxelShape lv4 = lv3.getCollisionShape(this, lv2);
                double f = lv4.getMax(Direction.Axis.Y);
                if (f < 1.0 && lv3.getFluidState().isEmpty()) {
                    this.addParticle(pos, parameters, lv, (double)pos.getY() - 0.05);
                }
            }
        }
    }

    private void addParticle(BlockPos pos, ParticleEffect parameters, VoxelShape shape, double y) {
        this.addParticle((double)pos.getX() + shape.getMin(Direction.Axis.X), (double)pos.getX() + shape.getMax(Direction.Axis.X), (double)pos.getZ() + shape.getMin(Direction.Axis.Z), (double)pos.getZ() + shape.getMax(Direction.Axis.Z), y, parameters);
    }

    private void addParticle(double minX, double maxX, double minZ, double maxZ, double y, ParticleEffect parameters) {
        this.addParticleClient(parameters, MathHelper.lerp(this.random.nextDouble(), minX, maxX), y, MathHelper.lerp(this.random.nextDouble(), minZ, maxZ), 0.0, 0.0, 0.0);
    }

    @Override
    public CrashReportSection addDetailsToCrashReport(CrashReport report) {
        CrashReportSection lv = super.addDetailsToCrashReport(report);
        lv.add("Server brand", () -> this.client.player.networkHandler.getBrand());
        lv.add("Server type", () -> this.client.getServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server");
        lv.add("Tracked entity count", () -> String.valueOf(this.getRegularEntityCount()));
        return lv;
    }

    @Override
    public void playSound(@Nullable Entity source, double x, double y, double z, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed) {
        if (source == this.client.player) {
            this.playSound(x, y, z, sound.value(), category, volume, pitch, false, seed);
        }
    }

    @Override
    public void playSoundFromEntity(@Nullable Entity source, Entity entity, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed) {
        if (source == this.client.player) {
            this.client.getSoundManager().play(new EntityTrackingSoundInstance(sound.value(), category, volume, pitch, entity, seed));
        }
    }

    @Override
    public void playSoundFromEntityClient(Entity entity, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        this.client.getSoundManager().play(new EntityTrackingSoundInstance(sound, category, volume, pitch, entity, this.random.nextLong()));
    }

    @Override
    public void playSoundClient(SoundEvent sound, SoundCategory category, float volume, float pitch) {
        if (this.client.player != null) {
            this.client.getSoundManager().play(new EntityTrackingSoundInstance(sound, category, volume, pitch, this.client.player, this.random.nextLong()));
        }
    }

    @Override
    public void playSoundClient(double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
        this.playSound(x, y, z, sound, category, volume, pitch, useDistance, this.random.nextLong());
    }

    private void playSound(double x, double y, double z, SoundEvent event, SoundCategory category, float volume, float pitch, boolean useDistance, long seed) {
        double i = this.client.gameRenderer.getCamera().getCameraPos().squaredDistanceTo(x, y, z);
        PositionedSoundInstance lv = new PositionedSoundInstance(event, category, volume, pitch, Random.create(seed), x, y, z);
        if (useDistance && i > 100.0) {
            double j = Math.sqrt(i) / 40.0;
            this.client.getSoundManager().play(lv, (int)(j * 20.0));
        } else {
            this.client.getSoundManager().play(lv);
        }
    }

    @Override
    public void addFireworkParticle(double x, double y, double z, double velocityX, double velocityY, double velocityZ, List<FireworkExplosionComponent> explosions) {
        if (explosions.isEmpty()) {
            for (int j = 0; j < this.random.nextInt(3) + 2; ++j) {
                this.addParticleClient(ParticleTypes.POOF, x, y, z, this.random.nextGaussian() * 0.05, 0.005, this.random.nextGaussian() * 0.05);
            }
        } else {
            this.client.particleManager.addParticle(new FireworksSparkParticle.FireworkParticle(this, x, y, z, velocityX, velocityY, velocityZ, this.client.particleManager, explosions));
        }
    }

    @Override
    public void sendPacket(Packet<?> packet) {
        this.networkHandler.sendPacket(packet);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }

    @Override
    public RecipeManager getRecipeManager() {
        return this.networkHandler.getRecipeManager();
    }

    @Override
    public TickManager getTickManager() {
        return this.tickManager;
    }

    @Override
    public WorldEnvironmentAttributeAccess getEnvironmentAttributes() {
        return this.environmentAttributeAccess;
    }

    @Override
    public QueryableTickScheduler<Block> getBlockTickScheduler() {
        return EmptyTickSchedulers.getClientTickScheduler();
    }

    @Override
    public QueryableTickScheduler<Fluid> getFluidTickScheduler() {
        return EmptyTickSchedulers.getClientTickScheduler();
    }

    @Override
    public ClientChunkManager getChunkManager() {
        return this.chunkManager;
    }

    @Override
    public @Nullable MapState getMapState(MapIdComponent id) {
        return this.mapStates.get(id);
    }

    public void putClientsideMapState(MapIdComponent id, MapState state) {
        this.mapStates.put(id, state);
    }

    @Override
    public Scoreboard getScoreboard() {
        return this.networkHandler.getScoreboard();
    }

    @Override
    public void updateListeners(BlockPos pos, BlockState oldState, BlockState newState, @Block.SetBlockStateFlag int flags) {
        this.worldRenderer.updateBlock(this, pos, oldState, newState, flags);
    }

    @Override
    public void scheduleBlockRerenderIfNeeded(BlockPos pos, BlockState old, BlockState updated) {
        this.worldRenderer.scheduleBlockRerenderIfNeeded(pos, old, updated);
    }

    public void scheduleBlockRenders(int x, int y, int z) {
        this.worldRenderer.scheduleChunkRenders3x3x3(x, y, z);
    }

    public void scheduleChunkRenders(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.worldRenderer.scheduleChunkRenders(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void setBlockBreakingInfo(int entityId, BlockPos pos, int progress) {
        this.worldRenderer.setBlockBreakingInfo(entityId, pos, progress);
    }

    @Override
    public void syncGlobalEvent(int eventId, BlockPos pos, int data) {
        this.worldEventHandler.processGlobalEvent(eventId, pos, data);
    }

    @Override
    public void syncWorldEvent(@Nullable Entity source, int eventId, BlockPos pos, int data) {
        try {
            this.worldEventHandler.processWorldEvent(eventId, pos, data);
        } catch (Throwable throwable) {
            CrashReport lv = CrashReport.create(throwable, "Playing level event");
            CrashReportSection lv2 = lv.addElement("Level event being played");
            lv2.add("Block coordinates", CrashReportSection.createPositionString(this, pos));
            lv2.add("Event source", source);
            lv2.add("Event type", eventId);
            lv2.add("Event data", data);
            throw new CrashException(lv);
        }
    }

    @Override
    public void addParticleClient(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this.addParticle(parameters, parameters.getType().shouldAlwaysSpawn(), false, x, y, z, velocityX, velocityY, velocityZ);
    }

    @Override
    public void addParticleClient(ParticleEffect parameters, boolean force, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this.addParticle(parameters, parameters.getType().shouldAlwaysSpawn() || force, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
    }

    @Override
    public void addImportantParticleClient(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this.addParticle(parameters, false, true, x, y, z, velocityX, velocityY, velocityZ);
    }

    @Override
    public void addImportantParticleClient(ParticleEffect parameters, boolean force, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this.addParticle(parameters, parameters.getType().shouldAlwaysSpawn() || force, true, x, y, z, velocityX, velocityY, velocityZ);
    }

    private void addParticle(ParticleEffect arg, boolean bl, boolean bl2, double d, double e, double f, double g, double h, double i) {
        try {
            Camera lv = this.client.gameRenderer.getCamera();
            ParticlesMode lv2 = this.getParticlesMode(bl2);
            if (bl) {
                this.client.particleManager.addParticle(arg, d, e, f, g, h, i);
                return;
            }
            if (lv.getCameraPos().squaredDistanceTo(d, e, f) > 1024.0) {
                return;
            }
            if (lv2 == ParticlesMode.MINIMAL) {
                return;
            }
            this.client.particleManager.addParticle(arg, d, e, f, g, h, i);
        } catch (Throwable throwable) {
            CrashReport lv3 = CrashReport.create(throwable, "Exception while adding particle");
            CrashReportSection lv4 = lv3.addElement("Particle being added");
            lv4.add("ID", Registries.PARTICLE_TYPE.getId(arg.getType()));
            lv4.add("Parameters", () -> ParticleTypes.TYPE_CODEC.encodeStart(this.getRegistryManager().getOps(NbtOps.INSTANCE), arg).toString());
            lv4.add("Position", () -> CrashReportSection.createPositionString((HeightLimitView)this, d, e, f));
            throw new CrashException(lv3);
        }
    }

    private ParticlesMode getParticlesMode(boolean bl) {
        ParticlesMode lv = this.client.options.getParticles().getValue();
        if (bl && lv == ParticlesMode.MINIMAL && this.random.nextInt(10) == 0) {
            lv = ParticlesMode.DECREASED;
        }
        if (lv == ParticlesMode.DECREASED && this.random.nextInt(3) == 0) {
            lv = ParticlesMode.MINIMAL;
        }
        return lv;
    }

    public List<AbstractClientPlayerEntity> getPlayers() {
        return this.players;
    }

    public List<EnderDragonPart> getEnderDragonParts() {
        return this.enderDragonParts;
    }

    @Override
    public RegistryEntry<Biome> getGeneratorStoredBiome(int biomeX, int biomeY, int biomeZ) {
        return this.getRegistryManager().getOrThrow(RegistryKeys.BIOME).getOrThrow(BiomeKeys.PLAINS);
    }

    private int getLightningTicksLeft() {
        return this.client.options.getHideLightningFlashes().getValue() != false ? 0 : this.lightningTicksLeft;
    }

    @Override
    public void setLightningTicksLeft(int lightningTicksLeft) {
        this.lightningTicksLeft = lightningTicksLeft;
    }

    @Override
    public float getBrightness(Direction direction, boolean shaded) {
        DimensionType.CardinalLightType lv = this.getDimension().cardinalLightType();
        if (!shaded) {
            return lv == DimensionType.CardinalLightType.NETHER ? 0.9f : 1.0f;
        }
        return switch (direction) {
            default -> throw new MatchException(null, null);
            case Direction.DOWN -> {
                if (lv == DimensionType.CardinalLightType.NETHER) {
                    yield 0.9f;
                }
                yield 0.5f;
            }
            case Direction.UP -> {
                if (lv == DimensionType.CardinalLightType.NETHER) {
                    yield 0.9f;
                }
                yield 1.0f;
            }
            case Direction.NORTH, Direction.SOUTH -> 0.8f;
            case Direction.WEST, Direction.EAST -> 0.6f;
        };
    }

    @Override
    public int getColor(BlockPos pos, ColorResolver colorResolver) {
        BiomeColorCache lv = this.colorCache.get(colorResolver);
        return lv.getBiomeColor(pos);
    }

    public int calculateColor(BlockPos pos, ColorResolver colorResolver) {
        int i = MinecraftClient.getInstance().options.getBiomeBlendRadius().getValue();
        if (i == 0) {
            return colorResolver.getColor(this.getBiome(pos).value(), pos.getX(), pos.getZ());
        }
        int j = (i * 2 + 1) * (i * 2 + 1);
        int k = 0;
        int l = 0;
        int m = 0;
        CuboidBlockIterator lv = new CuboidBlockIterator(pos.getX() - i, pos.getY(), pos.getZ() - i, pos.getX() + i, pos.getY(), pos.getZ() + i);
        BlockPos.Mutable lv2 = new BlockPos.Mutable();
        while (lv.step()) {
            lv2.set(lv.getX(), lv.getY(), lv.getZ());
            int n = colorResolver.getColor(this.getBiome(lv2).value(), lv2.getX(), lv2.getZ());
            k += (n & 0xFF0000) >> 16;
            l += (n & 0xFF00) >> 8;
            m += n & 0xFF;
        }
        return (k / j & 0xFF) << 16 | (l / j & 0xFF) << 8 | m / j & 0xFF;
    }

    @Override
    public void setSpawnPoint(WorldProperties.SpawnPoint spawnPoint) {
        this.properties.setSpawnPoint(this.ensureWithinBorder(spawnPoint));
    }

    @Override
    public WorldProperties.SpawnPoint getSpawnPoint() {
        return this.properties.getSpawnPoint();
    }

    public String toString() {
        return "ClientLevel";
    }

    @Override
    public Properties getLevelProperties() {
        return this.clientWorldProperties;
    }

    @Override
    public void emitGameEvent(RegistryEntry<GameEvent> event, Vec3d emitterPos, GameEvent.Emitter emitter) {
    }

    protected Map<MapIdComponent, MapState> getMapStates() {
        return ImmutableMap.copyOf(this.mapStates);
    }

    protected void putMapStates(Map<MapIdComponent, MapState> mapStates) {
        this.mapStates.putAll(mapStates);
    }

    @Override
    protected EntityLookup<Entity> getEntityLookup() {
        return this.entityManager.getLookup();
    }

    @Override
    public String asString() {
        return "Chunks[C] W: " + this.chunkManager.getDebugString() + " E: " + this.entityManager.getDebugString();
    }

    @Override
    public void addBlockBreakParticles(BlockPos pos, BlockState state) {
        if (state.isAir() || !state.hasBlockBreakParticles()) {
            return;
        }
        VoxelShape lv = state.getOutlineShape(this, pos);
        double d = 0.25;
        lv.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double j = Math.min(1.0, maxX - minX);
            double k = Math.min(1.0, maxY - minY);
            double l = Math.min(1.0, maxZ - minZ);
            int m = Math.max(2, MathHelper.ceil(j / 0.25));
            int n = Math.max(2, MathHelper.ceil(k / 0.25));
            int o = Math.max(2, MathHelper.ceil(l / 0.25));
            for (int p = 0; p < m; ++p) {
                for (int q = 0; q < n; ++q) {
                    for (int r = 0; r < o; ++r) {
                        double s = ((double)p + 0.5) / (double)m;
                        double t = ((double)q + 0.5) / (double)n;
                        double u = ((double)r + 0.5) / (double)o;
                        double v = s * j + minX;
                        double w = t * k + minY;
                        double x = u * l + minZ;
                        this.client.particleManager.addParticle(new BlockDustParticle(this, (double)pos.getX() + v, (double)pos.getY() + w, (double)pos.getZ() + x, s - 0.5, t - 0.5, u - 0.5, state, pos));
                    }
                }
            }
        });
    }

    public void spawnBlockBreakingParticle(BlockPos pos, Direction direction) {
        BlockState lv = this.getBlockState(pos);
        if (lv.getRenderType() == BlockRenderType.INVISIBLE || !lv.hasBlockBreakParticles()) {
            return;
        }
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        float f = 0.1f;
        Box lv2 = lv.getOutlineShape(this, pos).getBoundingBox();
        double d = (double)i + this.random.nextDouble() * (lv2.maxX - lv2.minX - (double)0.2f) + (double)0.1f + lv2.minX;
        double e = (double)j + this.random.nextDouble() * (lv2.maxY - lv2.minY - (double)0.2f) + (double)0.1f + lv2.minY;
        double g = (double)k + this.random.nextDouble() * (lv2.maxZ - lv2.minZ - (double)0.2f) + (double)0.1f + lv2.minZ;
        if (direction == Direction.DOWN) {
            e = (double)j + lv2.minY - (double)0.1f;
        }
        if (direction == Direction.UP) {
            e = (double)j + lv2.maxY + (double)0.1f;
        }
        if (direction == Direction.NORTH) {
            g = (double)k + lv2.minZ - (double)0.1f;
        }
        if (direction == Direction.SOUTH) {
            g = (double)k + lv2.maxZ + (double)0.1f;
        }
        if (direction == Direction.WEST) {
            d = (double)i + lv2.minX - (double)0.1f;
        }
        if (direction == Direction.EAST) {
            d = (double)i + lv2.maxX + (double)0.1f;
        }
        this.client.particleManager.addParticle(new BlockDustParticle(this, d, e, g, 0.0, 0.0, 0.0, lv, pos).move(0.2f).scale(0.6f));
    }

    public void setSimulationDistance(int simulationDistance) {
        this.simulationDistance = simulationDistance;
    }

    public int getSimulationDistance() {
        return this.simulationDistance;
    }

    @Override
    public FeatureSet getEnabledFeatures() {
        return this.networkHandler.getEnabledFeatures();
    }

    @Override
    public BrewingRecipeRegistry getBrewingRecipeRegistry() {
        return this.networkHandler.getBrewingRecipeRegistry();
    }

    @Override
    public FuelRegistry getFuelRegistry() {
        return this.networkHandler.getFuelRegistry();
    }

    @Override
    public void createExplosion(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, ParticleEffect smallParticle, ParticleEffect largeParticle, Pool<BlockParticleEffect> blockParticles, RegistryEntry<SoundEvent> soundEvent) {
    }

    @Override
    public int getSeaLevel() {
        return this.seaLevel;
    }

    @Override
    public int getBlockColor(BlockPos pos) {
        return MinecraftClient.getInstance().getBlockColors().getColor(this.getBlockState(pos), this, pos, 0);
    }

    @Override
    public void registerForCleaning(DataCache<ClientWorld, ?> arg) {
        this.networkHandler.registerForCleaning(arg);
    }

    public void addBlockParticleEffects(Vec3d center, float radius, int blockCount, Pool<BlockParticleEffect> particles) {
        this.blockParticlesManager.scheduleBlockParticles(center, radius, blockCount, particles);
    }

    @Override
    public /* synthetic */ WorldProperties getLevelProperties() {
        return this.getLevelProperties();
    }

    public /* synthetic */ Collection getEnderDragonParts() {
        return this.getEnderDragonParts();
    }

    @Override
    public /* synthetic */ ChunkManager getChunkManager() {
        return this.getChunkManager();
    }

    @Override
    public /* synthetic */ EnvironmentAttributeAccess getEnvironmentAttributes() {
        return this.getEnvironmentAttributes();
    }

    @Environment(value=EnvType.CLIENT)
    final class ClientEntityHandler
    implements EntityHandler<Entity> {
        ClientEntityHandler() {
        }

        @Override
        public void create(Entity arg) {
        }

        @Override
        public void destroy(Entity arg) {
        }

        @Override
        public void startTicking(Entity arg) {
            ClientWorld.this.entityList.add(arg);
        }

        @Override
        public void stopTicking(Entity arg) {
            ClientWorld.this.entityList.remove(arg);
        }

        @Override
        public void startTracking(Entity arg) {
            Entity entity = arg;
            Objects.requireNonNull(entity);
            Entity entity2 = entity;
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{AbstractClientPlayerEntity.class, EnderDragonEntity.class}, (Object)entity2, n)) {
                case 0: {
                    AbstractClientPlayerEntity lv = (AbstractClientPlayerEntity)entity2;
                    ClientWorld.this.players.add(lv);
                    break;
                }
                case 1: {
                    EnderDragonEntity lv2 = (EnderDragonEntity)entity2;
                    ClientWorld.this.enderDragonParts.addAll(Arrays.asList(lv2.getBodyParts()));
                    break;
                }
            }
        }

        @Override
        public void stopTracking(Entity arg) {
            arg.detach();
            Entity entity = arg;
            Objects.requireNonNull(entity);
            Entity entity2 = entity;
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{AbstractClientPlayerEntity.class, EnderDragonEntity.class}, (Object)entity2, n)) {
                case 0: {
                    AbstractClientPlayerEntity lv = (AbstractClientPlayerEntity)entity2;
                    ClientWorld.this.players.remove(lv);
                    break;
                }
                case 1: {
                    EnderDragonEntity lv2 = (EnderDragonEntity)entity2;
                    ClientWorld.this.enderDragonParts.removeAll(Arrays.asList(lv2.getBodyParts()));
                    break;
                }
            }
        }

        @Override
        public void updateLoadStatus(Entity arg) {
        }

        @Override
        public /* synthetic */ void updateLoadStatus(Object entity) {
            this.updateLoadStatus((Entity)entity);
        }

        @Override
        public /* synthetic */ void stopTracking(Object entity) {
            this.stopTracking((Entity)entity);
        }

        @Override
        public /* synthetic */ void startTracking(Object entity) {
            this.startTracking((Entity)entity);
        }

        @Override
        public /* synthetic */ void startTicking(Object entity) {
            this.startTicking((Entity)entity);
        }

        @Override
        public /* synthetic */ void destroy(Object entity) {
            this.destroy((Entity)entity);
        }

        @Override
        public /* synthetic */ void create(Object entity) {
            this.create((Entity)entity);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static class Properties
    implements MutableWorldProperties {
        private final boolean hardcore;
        private final boolean flatWorld;
        private WorldProperties.SpawnPoint position;
        private long time;
        private long timeOfDay;
        private boolean raining;
        private Difficulty difficulty;
        private boolean difficultyLocked;

        public Properties(Difficulty difficulty, boolean hardcore, boolean flatWorld) {
            this.difficulty = difficulty;
            this.hardcore = hardcore;
            this.flatWorld = flatWorld;
        }

        @Override
        public WorldProperties.SpawnPoint getSpawnPoint() {
            return this.position;
        }

        @Override
        public long getTime() {
            return this.time;
        }

        @Override
        public long getTimeOfDay() {
            return this.timeOfDay;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public void setTimeOfDay(long timeOfDay) {
            this.timeOfDay = timeOfDay;
        }

        @Override
        public void setSpawnPoint(WorldProperties.SpawnPoint spawnPoint) {
            this.position = spawnPoint;
        }

        @Override
        public boolean isThundering() {
            return false;
        }

        @Override
        public boolean isRaining() {
            return this.raining;
        }

        @Override
        public void setRaining(boolean raining) {
            this.raining = raining;
        }

        @Override
        public boolean isHardcore() {
            return this.hardcore;
        }

        @Override
        public Difficulty getDifficulty() {
            return this.difficulty;
        }

        @Override
        public boolean isDifficultyLocked() {
            return this.difficultyLocked;
        }

        @Override
        public void populateCrashReport(CrashReportSection reportSection, HeightLimitView world) {
            MutableWorldProperties.super.populateCrashReport(reportSection, world);
        }

        public void setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
        }

        public void setDifficultyLocked(boolean difficultyLocked) {
            this.difficultyLocked = difficultyLocked;
        }

        public double getSkyDarknessHeight(HeightLimitView world) {
            if (this.flatWorld) {
                return world.getBottomY();
            }
            return 63.0;
        }

        public float getVoidDarknessRange() {
            if (this.flatWorld) {
                return 1.0f;
            }
            return 32.0f;
        }
    }
}

