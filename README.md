<p align="center">
  <img src="src/main/mod-contents/assets/nbtviewer/logo.png" alt="NamNBTview icon" width="256" height="256">
</p>

<h1 align="center">NamNBTview</h1>

<p align="center">
  A Fabric client mod for viewing item and entity NBT/SNBT directly in Minecraft.
</p>

## Features

- View item NBT/SNBT in tooltips.
- Copy displayed item NBT to the clipboard.
- Show an NBT overlay for the entity you are looking at.
- Lock the overlay onto an entity.
- Open a notebook-style screen containing the complete available entity NBT.
- Configure colors, formatting, tooltip scaling, character limits, notifications, and sounds.
- Change every shortcut from Minecraft's keybind settings.

## Supported Versions

| Minecraft | Release file | Minimum Fabric Loader |
| --- | --- | --- |
| 1.21.11 | `NamNBTview-2.3.4-mc1.21.11-fabric.jar` | 0.18.4 |
| 26.1.2 | `NamNBTview-2.3.4-mc26.1.2-fabric.jar` | 0.19.3 |
| 26.2 | `NamNBTview-2.3.4-mc26.2-fabric.jar` | 0.19.3 |
| 26.3 | `NamNBTview-2.3.4-mc26.3-fabric.jar` | 0.19.5 |

Each Minecraft version has a separate JAR because the Minecraft APIs and
mapping namespaces are not binary-compatible across all supported versions.

## Installing the Mod

1. Install Fabric Loader for your Minecraft version.
2. Install the following dependencies:
   - [Fabric API](https://modrinth.com/mod/fabric-api)
   - [Cloth Config API](https://modrinth.com/mod/cloth-config)
   - [Mod Menu](https://modrinth.com/mod/modmenu) is optional, but recommended.
3. Download the JAR matching your Minecraft version from the
   [latest GitHub release](https://github.com/Nattapat2871/NamNBTview/releases/latest).
4. Remove older NamNBTview or NBTviewer JARs from the profile's `mods` folder.
5. Place the downloaded JAR in the `mods` folder and launch Minecraft.

Do not install more than one NamNBTview version in the same profile.

## Using the Mod

### Item NBT tooltip

1. Open an inventory or container.
2. Hold `Shift`.
3. Hover over an item to display its NBT/SNBT tooltip.
4. While the tooltip is visible, press `Ctrl + Shift + C` to copy the displayed
   NBT to the clipboard.

The `Shift only` requirement can be disabled from the mod configuration.

### Entity NBT overlay

- Press `N` to enable or disable the entity NBT overlay.
- Look at an entity while the overlay is enabled to inspect its available NBT.
- Press `Shift + L` to lock or unlock the current entity target.
- Press `Shift + O` to open the complete entity NBT notebook.
- On Minecraft 26.3, `Ctrl + U` opens the NBT viewer for the block, block entity, or entity currently under the crosshair.

Some entity data is only available in singleplayer because multiplayer servers
do not synchronize every NBT field to clients.

### Configuration and keybinds

- Open `Mod Menu → NamNBTview` to configure display colors, formatting,
  tooltip scaling, character limits, copy notifications, and sounds.
- Open `Options → Controls → Key Binds → NamNBTview` to change shortcuts.
- Existing settings continue to use the internal `nbtviewer` configuration
  namespace for compatibility.

## Using This Repository

### Clone

```powershell
git clone https://github.com/Nattapat2871/NamNBTview.git
cd NamNBTview
```

### Repository layout

```text
src/main/mod-contents/                 Shared Minecraft 26.x mod contents
src/variants/1.21.11/mod-contents/    Minecraft 1.21.11 intermediary build
src/variants/26.2/                     Minecraft 26.2 compatibility classes
source/26.3/                           Buildable Java source for Minecraft 26.3
build.ps1                              Multi-version packaging script
```

This repository contains extracted, reproducible build inputs. The internal
Java package and translation namespace remain `org.hohigamer.nbtviewer` and
`nbtviewer` to retain compatibility with existing configuration and keybind
data.

### Build all supported versions

Requirements:

- Windows PowerShell
- A JDK containing `jar.exe`

Run:

```powershell
.\build.ps1
```

The artifacts are written to:

```text
build/libs/NamNBTview-2.3.4-mc1.21.11-fabric.jar
build/libs/NamNBTview-2.3.4-mc26.1.2-fabric.jar
build/libs/NamNBTview-2.3.4-mc26.2-fabric.jar
build/libs/NamNBTview-2.3.4-mc26.3-fabric.jar
```

The build script creates an isolated staging directory for each Minecraft
version, applies its compatibility classes, validates the square mod icon and
important API references, and then packages the corresponding Fabric JAR.

## Project Metadata

- Project: NamNBTview
- Mod ID: `namnbtview`
- Current version: `2.3.4`
- Author: [Nattapat2871](https://github.com/Nattapat2871)
- License: MIT
- Supported Minecraft versions: 1.21.11, 26.1.2, 26.2, and 26.3

## Credits

Forked from [Items NBT Viewer](https://modrinth.com/mod/items-nbt-viewer).
