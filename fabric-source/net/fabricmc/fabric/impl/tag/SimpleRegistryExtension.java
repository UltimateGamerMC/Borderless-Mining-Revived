package net.fabricmc.fabric.impl.tag;

public interface SimpleRegistryExtension {
	void fabric_applyPendingTagAliases();
	void fabric_refreshTags();
}
