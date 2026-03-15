/*
 * External method calls:
 *   Lnet/minecraft/server/dedicated/ServerPropertiesHandler;load(Ljava/nio/file/Path;)Lnet/minecraft/server/dedicated/ServerPropertiesHandler;
 *   Lnet/minecraft/server/dedicated/ServerPropertiesHandler;saveProperties(Ljava/nio/file/Path;)V
 */
package net.minecraft.server.dedicated;

import java.nio.file.Path;
import java.util.function.UnaryOperator;
import net.minecraft.server.dedicated.ServerPropertiesHandler;

public class ServerPropertiesLoader {
    private final Path path;
    private ServerPropertiesHandler propertiesHandler;

    public ServerPropertiesLoader(Path path) {
        this.path = path;
        this.propertiesHandler = ServerPropertiesHandler.load(path);
    }

    public ServerPropertiesHandler getPropertiesHandler() {
        return this.propertiesHandler;
    }

    public void store() {
        this.propertiesHandler.saveProperties(this.path);
    }

    public ServerPropertiesLoader apply(UnaryOperator<ServerPropertiesHandler> applier) {
        this.propertiesHandler = (ServerPropertiesHandler)applier.apply(this.propertiesHandler);
        this.propertiesHandler.saveProperties(this.path);
        return this;
    }
}

