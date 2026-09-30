# Changelog

## 2.3.3 - Minecraft 26.3 input API fix

- Fixed the actual Minecraft 26.3 keyboard-input regression that prevented Shift-held NBT tooltips from activating.
- Replaced legacy GLFW-style numeric key codes with Minecraft 26.3 InputConstants values for left/right Shift, left/right Ctrl, C, N, O, and L.
- Restored Ctrl+Shift+C clipboard copying under the 26.3 input backend.
- Corrected the default N/O/L key mappings for Minecraft 26.3.
- Added a serialization fallback that always produces inspectable item data from id, count, and DataComponentPatch when ItemStack.CODEC cannot encode an item.

Root cause: Minecraft 26.3 uses a different keyboard code space. For example LSHIFT/RSHIFT are 225/229 instead of the legacy 340/344 values used by 2.3.2.

Validation: compiled bytecode was inspected and contains the Minecraft 26.3 key codes; the Java 25 / Minecraft 26.3 Gradle build completes successfully.

## 2.3.2 - Minecraft 26.3 fixes

- Fixed item NBT tooltips on Minecraft 26.3 by attaching the NBT lines to the vanilla item tooltip text path instead of the tooltip-image slot.
- Holding Shift now reveals the serialized item NBT reliably; the copy shortcut continues to use the NBT currently shown in the tooltip.
- Changed Ctrl+U to open the NBT viewer for the block, block entity, or entity currently under the crosshair instead of opening configuration.
- Block entities use their synced/full metadata when available; ordinary blocks fall back to their block-state SNBT plus coordinates.
- Kept the notebook Copy button and clipboard path on the Minecraft 26.3 client API.

Validation: Minecraft 26.3 Gradle build completed successfully after the input, tooltip and target-viewer changes.

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
