# Project Config
```json
{
  "minecraft_version": "1.21.11",
  "name": "borderless",
  "author": "UltimateGamerMC",
  "enviorment": "client-side",
  "model": "cursor-agent",
  "mappings": "yarn",
  "mod_git_url": "https://github.com/comp500/BorderlessMining",
  "debug": true,
  "objective": "Make this mode work fully with 1.21.11 make sure no errors no mistakes fully working\n"
}
```

# Important Constraints
- ONLY work within this project directory
- DO NOT access files outside of this workspace
- All operations must be contained to the mod project folder
- **CRITICAL:** `minecraft-source-code/` and `fabric-source-code/` are **READ-ONLY REFERENCE** directories.
  - **NEVER** modify files in these directories. Your changes will be ignored and lost.
  - **ALWAYS** use Fabric Mixins in `src/main/java/` to modify game behavior.
- Keep the existing package structure and naming conventions from the cloned repository.

# Code Style
- No comments
- Max compatibility mixins
- Prefer Fabric API when possible over mixins
- If you must use a custom texture, create an empty file for the texture and note the texture name in the README.md file.

# Documentation References
- **Fabric Wiki**: https://fabricmc.net/wiki/
- **Recipes & Data Generation**: https://fabricmc.net/wiki/tutorial:recipes
- **Item Registration**: https://fabricmc.net/wiki/tutorial:items
- **Block Registration**: https://fabricmc.net/wiki/tutorial:blocks
- **Entity Registration**: https://fabricmc.net/wiki/tutorial:entity
- When implementing features, refer to official Fabric documentation for correct syntax and best practices

# Common Pitfalls to Avoid
- **Item models**: Use `"parent": "item/generated"` for regular items (NOT `"item/handheld"` unless it's a tool/weapon)
- **Recipe format**: Ensure recipe JSON uses correct Minecraft 1.21+ format (shaped/shapeless)
- **Texture paths**: Must match exact file structure (e.g., `"girlfriend-mod:item/name"` → `assets/girlfriend-mod/textures/item/name.png`)
- **Empty textures**: NEVER create empty PNG files - always download or generate valid images
- **Namespace consistency**: Use mod ID consistently in all resource locations (items, recipes, textures)

# Project Structure
- `fabric-source-code/`: Fabric API source code for Minecraft 1.21.11
- `minecraft-source-code/`: Decompiled Minecraft 1.21.11 source code
- `src/main/java/`: Source code for the mod
- `src/main/resources/`: Resource files for the mod (icon, mixins, fabric.mod.json, etc.)
- `gradle.properties`: Gradle properties for the mod
- `build.gradle`: Gradle build file for the mod

# External Dependencies
If the mod requires external Fabric mods (e.g. "fabric-api", "sodium", "lithium"), you MUST create a `DEPENDENCIES.json` file in the root of the project directory.
This file should contain a JSON array of mod names (slugs or search terms) that will be automatically searched for on Modrinth and downloaded to the test server.
Example `DEPENDENCIES.json`:
```json
["fabric-api", "sodium"]
```
**Fabric API is almost always required for Fabric mods.**

# Mixin Development Guide
Each file in `minecraft-source-code/` has mixin descriptors at the top:
- **External method calls**: Methods this class calls on other Minecraft classes
- **Internal methods**: Private/static methods within the class (common injection points)

These descriptors exclude obvious methods (simple getters/setters, constructors, no-param methods returning primitives, etc.) to reduce noise. Use these to write accurate mixin targets without hallucinating method signatures.

# Completion
When you have completed ALL tasks and the mod is fully implemented and working,
First, create a conscise and basic README.md file in the root of the project directory, simply explaining what the mod does and how to use it.
THEN, and **ONLY AFTER ALL CODE FILES HAVE BEEN WRITTEN**, output exactly this message on a new line:
```
[FABRICATOR_SESSION_DONE]
```
**WARNING:** If you output this marker before you have actually created the Java source files, JSON configs, and build files, the session will end immediately and the build will fail.
**DO NOT** output this marker during the planning or research phase.
