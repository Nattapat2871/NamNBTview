# NamNBTview

NamNBTview is a Minecraft Fabric client mod for Minecraft 26.1.2. It displays item and entity NBT/SNBT in tooltips, overlays, and a notebook-style entity view.

This repository is structured as an extracted, reproducible build of the working Minecraft 26.1.2 port. The build input is stored under `src/main/mod-contents`, and `build.ps1` packages those contents into a Fabric jar.

## Usage

### Install

1. Install Fabric Loader for Minecraft `26.1.2`.
2. Put `NamNBTview-2.2.0-mc26.1.2-fabric.jar` in your profile's `mods` folder.
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
build/libs/NamNBTview-2.2.0-mc26.1.2-fabric.jar
```

## Metadata

- Project name: NamNBTview
- Mod id: `namnbtview`
- Version: `2.2.0`
- Author: Nattapat2871
- Minecraft: `26.1.2`
- Fabric Loader: `>=0.19.3`

## Notes

The internal Java package and translation namespace remain `org.hohigamer.nbtviewer` / `nbtviewer` to preserve compatibility with the existing patched classes and keybinding/config names.
