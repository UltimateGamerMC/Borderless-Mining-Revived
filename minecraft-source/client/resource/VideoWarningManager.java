/*
 * External method calls:
 *   Lnet/minecraft/util/profiler/Profiler;scoped(Ljava/lang/String;)Lnet/minecraft/util/profiler/ScopedProfiler;
 *   Lnet/minecraft/client/resource/VideoWarningManager$WarningPatternLoader;buildWarnings()Lcom/google/common/collect/ImmutableMap;
 *   Lnet/minecraft/resource/ResourceManager;openAsReader(Lnet/minecraft/util/Identifier;)Ljava/io/BufferedReader;
 *   Lnet/minecraft/util/StrictJsonParser;parse(Ljava/io/Reader;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/resource/VideoWarningManager;loadWarnlist(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Lcom/google/gson/JsonObject;
 *   Lnet/minecraft/client/resource/VideoWarningManager;compilePatterns(Lcom/google/gson/JsonArray;Ljava/util/List;)V
 *   Lnet/minecraft/client/resource/VideoWarningManager;apply(Lnet/minecraft/client/resource/VideoWarningManager$WarningPatternLoader;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V
 *   Lnet/minecraft/client/resource/VideoWarningManager;prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/client/resource/VideoWarningManager$WarningPatternLoader;
 */
package net.minecraft.client.resource;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.ScopedProfiler;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class VideoWarningManager
extends SinglePreparationResourceReloader<WarningPatternLoader> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Identifier GPU_WARNLIST_ID = Identifier.ofVanilla("gpu_warnlist.json");
    private ImmutableMap<String, String> warnings = ImmutableMap.of();
    private boolean warningScheduled;
    private boolean warned;

    public boolean hasWarning() {
        return !this.warnings.isEmpty();
    }

    public boolean canWarn() {
        return this.hasWarning() && !this.warned;
    }

    public void scheduleWarning() {
        this.warningScheduled = true;
    }

    public void acceptAfterWarnings() {
        this.warned = true;
    }

    public boolean shouldWarn() {
        return this.warningScheduled && !this.warned;
    }

    public void reset() {
        this.warningScheduled = false;
        this.warned = false;
    }

    public @Nullable String getRendererWarning() {
        return this.warnings.get("renderer");
    }

    public @Nullable String getVersionWarning() {
        return this.warnings.get("version");
    }

    public @Nullable String getVendorWarning() {
        return this.warnings.get("vendor");
    }

    public @Nullable String getWarningsAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        this.warnings.forEach((key, value) -> stringBuilder.append((String)key).append(": ").append((String)value));
        return stringBuilder.isEmpty() ? null : stringBuilder.toString();
    }

    @Override
    protected WarningPatternLoader prepare(ResourceManager arg, Profiler arg2) {
        ArrayList<Pattern> list = Lists.newArrayList();
        ArrayList<Pattern> list2 = Lists.newArrayList();
        ArrayList<Pattern> list3 = Lists.newArrayList();
        JsonObject jsonObject = VideoWarningManager.loadWarnlist(arg, arg2);
        if (jsonObject != null) {
            try (ScopedProfiler lv = arg2.scoped("compile_regex");){
                VideoWarningManager.compilePatterns(jsonObject.getAsJsonArray("renderer"), list);
                VideoWarningManager.compilePatterns(jsonObject.getAsJsonArray("version"), list2);
                VideoWarningManager.compilePatterns(jsonObject.getAsJsonArray("vendor"), list3);
            }
        }
        return new WarningPatternLoader(list, list2, list3);
    }

    @Override
    protected void apply(WarningPatternLoader arg, ResourceManager arg2, Profiler arg3) {
        this.warnings = arg.buildWarnings();
    }

    private static void compilePatterns(JsonArray array, List<Pattern> patterns) {
        array.forEach(json -> patterns.add(Pattern.compile(json.getAsString(), 2)));
    }

    /*
     * Enabled aggressive exception aggregation
     */
    private static @Nullable JsonObject loadWarnlist(ResourceManager resourceManager, Profiler profiler) {
        try (ScopedProfiler lv = profiler.scoped("parse_json");){
            JsonObject jsonObject;
            block14: {
                BufferedReader reader = resourceManager.openAsReader(GPU_WARNLIST_ID);
                try {
                    jsonObject = StrictJsonParser.parse(reader).getAsJsonObject();
                    if (reader == null) break block14;
                } catch (Throwable throwable) {
                    if (reader != null) {
                        try {
                            ((Reader)reader).close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                ((Reader)reader).close();
            }
            return jsonObject;
        } catch (JsonSyntaxException | IOException exception) {
            LOGGER.warn("Failed to load GPU warnlist", exception);
            return null;
        }
    }

    @Override
    protected /* synthetic */ Object prepare(ResourceManager manager, Profiler profiler) {
        return this.prepare(manager, profiler);
    }

    @Environment(value=EnvType.CLIENT)
    protected static final class WarningPatternLoader {
        private final List<Pattern> rendererPatterns;
        private final List<Pattern> versionPatterns;
        private final List<Pattern> vendorPatterns;

        WarningPatternLoader(List<Pattern> rendererPatterns, List<Pattern> versionPatterns, List<Pattern> vendorPatterns) {
            this.rendererPatterns = rendererPatterns;
            this.versionPatterns = versionPatterns;
            this.vendorPatterns = vendorPatterns;
        }

        private static String buildWarning(List<Pattern> warningPattern, String info) {
            ArrayList<String> list2 = Lists.newArrayList();
            for (Pattern pattern : warningPattern) {
                Matcher matcher = pattern.matcher(info);
                while (matcher.find()) {
                    list2.add(matcher.group());
                }
            }
            return String.join((CharSequence)", ", list2);
        }

        ImmutableMap<String, String> buildWarnings() {
            ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<String, String>();
            GpuDevice gpuDevice = RenderSystem.getDevice();
            if (gpuDevice.getBackendName().equals("OpenGL")) {
                String string3;
                String string2;
                String string = WarningPatternLoader.buildWarning(this.rendererPatterns, gpuDevice.getRenderer());
                if (!string.isEmpty()) {
                    builder.put("renderer", string);
                }
                if (!(string2 = WarningPatternLoader.buildWarning(this.versionPatterns, gpuDevice.getVersion())).isEmpty()) {
                    builder.put("version", string2);
                }
                if (!(string3 = WarningPatternLoader.buildWarning(this.vendorPatterns, gpuDevice.getVendor())).isEmpty()) {
                    builder.put("vendor", string3);
                }
            }
            return builder.build();
        }
    }
}

