# NamNBTview

NamNBTview is a Minecraft Fabric client mod for Minecraft 1.21.11, 26.1.2, and 26.2. It displays item and entity NBT/SNBT in tooltips, overlays, and a notebook-style entity view.

This repository is structured as an extracted, reproducible build. Shared build input is stored under `src/main/mod-contents`, while version-specific compatibility classes are stored under `src/variants`. The build script packages a separate Fabric jar for each supported Minecraft version.

## Usage

### Install

1. Install Fabric Loader for Minecraft `1.21.11`, `26.1.2`, or `26.2`.
2. Put the matching jar in your profile's `mods` folder:
   - `NamNBTview-2.3.0-mc1.21.11-fabric.jar` for Minecraft 1.21.11
   - `NamNBTview-2.3.0-mc26.1.2-fabric.jar` for Minecraft 26.1.2
   - `NamNBTview-2.3.0-mc26.2-fabric.jar` for Minecraft 26.2
3. Install the required dependencies:
   - Fabric API
   - Cloth Config
   - Mod Menu is optional, but recommended for changing settings in-game.

### Item NBT Tooltip

- Hold `Shift` and hover an item to show its NBT/SNBT tooltip.
- By default, `Shift only` is enabled. You can disable it in the config if you want NBT to show whenever you hover an item.
- While the NBT tooltip is visible, press `Ctrl + Shift + C` to copy the shown NBT to your clipboard.

### Entity NBT Overlay

- Press `N` in-game to toggle the entity NBT overlay on or off.
- When the overlay is enabled, look at an entity to show its NBT panel.
- Press `Shift + L` while looking at an entity to lock or unlock the current entity target.
- Press `Shift + O` while looking at an entity to open the full entity NBT notebook screen.

### Config And Keybinds

- Open `Mod Menu -> NamNBTview` to change settings such as colors, tooltip scaling, max character count, single-line mode, copy popup, and copy sound.
- Keybinds can be changed from Minecraft's Controls menu under the `NamNBTview` category.
- The internal config namespace is still `nbtviewer`, so existing config/keybind data can continue to work.

## Build

```powershell
.\build.ps1
```

Output:

```text
build/libs/NamNBTview-2.3.0-mc1.21.11-fabric.jar
build/libs/NamNBTview-2.3.0-mc26.1.2-fabric.jar
build/libs/NamNBTview-2.3.0-mc26.2-fabric.jar
```

## Metadata

- Project name: NamNBTview
- Mod id: `namnbtview`
- Version: `2.3.0`
- Author: Nattapat2871
- Minecraft: `1.21.11`, `26.1.2`, `26.2`
- Fabric Loader: `>=0.19.3`

## Notes

The internal Java package and translation namespace remain `org.hohigamer.nbtviewer` / `nbtviewer` to preserve compatibility with the existing patched classes and keybinding/config names.

Version 2.2.1 updates the entity notebook screen to Minecraft 26.1.2's
`extractTransparentBackground` screen API, preventing the client crash when
opening it with `Shift + O`.

Version 2.3.0 adds a square 512x512 mod icon and a dedicated Minecraft 26.2
compatibility build. Minecraft 26.2 moved screen access to `Minecraft.gui` and
moved `NbtPredicate` to `net.minecraft.advancements.predicates`; the 26.2
variant contains the required compatibility classes without weakening the
26.1.2 build. It also packages the tested intermediary-mapped 1.21.11 build as
a dedicated variant, since 1.21.11 and 26.x do not share a binary-compatible
Minecraft namespace.
