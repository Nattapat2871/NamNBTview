# Changelog

## 2.3.1 - Minecraft 26.3

- Added a native Minecraft 26.3 Fabric build compiled from Java source.
- Added a Ctrl+U compatibility shortcut for the NBT UI on 26.3.
  - With an entity selected, Ctrl+U opens the entity NBT notebook.
  - Without an entity selected, Ctrl+U opens the NamNBTview configuration screen.
- Retained the existing Shift+O notebook shortcut and existing configuration/keybind compatibility.
- Updated the 26.3 build to Fabric Loader 0.19.5, Fabric API 0.161.0+26.3, Cloth Config 26.3.159, and Mod Menu 21.0.0.
- Kept existing 1.21.11, 26.1.2, and 26.2 packaging support.

Validation: the 26.3 Gradle source build completed successfully and the compiled bytecode contains the Ctrl+U notebook/config routing.

## 2.3.0

- Added multi-version packaging for Minecraft 1.21.11, 26.1.2, and 26.2.
- Added entity NBT notebook and overlay compatibility.
- Added repository documentation and reproducible extracted build inputs.
