# NamNBTview

NamNBTview is a Minecraft Fabric client mod for Minecraft 26.1.2. It displays item and entity NBT/SNBT in tooltips, overlays, and a notebook-style entity view.

This repository is structured as an extracted, reproducible build of the working Minecraft 26.1.2 port. The build input is stored under `src/main/mod-contents`, and `build.ps1` packages those contents into a Fabric jar.

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
