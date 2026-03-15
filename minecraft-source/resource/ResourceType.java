/*
 * Internal private/static methods:
 *   Lnet/minecraft/resource/ResourceType;method_36582()[Lnet/minecraft/resource/ResourceType;
 */
package net.minecraft.resource;

public enum ResourceType {
    CLIENT_RESOURCES("assets"),
    SERVER_DATA("data");

    private final String directory;

    private ResourceType(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return this.directory;
    }
}

