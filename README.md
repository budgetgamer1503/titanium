# Sodium Improver

**Author**: budgetgamer1503  
**Mod ID**: `sodiumimprover`  
**License**: LGPL-3.0  

Sodium Improver is an all-in-one renderer optimization and companion mod for Sodium on Minecraft. It targets the areas of rendering that Sodium leaves un-optimized: entity frustum and distance culling, tile entity / block entity culling (chests, hoppers, banners, signs), particle throttling, and dynamic frame-pacing.

---

## Features

- **Entity Frustum & Distance Culling**: Skips rendering calculations and draw calls for monsters, animals, item drops, item frames, and armor stands when outside the field of view or behind camera.
- **Tile Entity Culling**: Culls chest arrays, hoppers, shulkers, signs, and banners to eliminate storage room lag.
- **Dynamic Frame Pacing**: Monitors rolling frame times and scales entity distance and fog during sudden frame drops to keep framerates steady.
- **Particle Culling**: Drops off-screen and distant particles.
- **Reese's Sodium Options & Sodium Integration**: Integrates directly with Sodium's option screens and Reese's Sodium Options with automatic JSON config fallback.

---

## Presets

- **POTATO_PC**: Maximum aggressive culling, 32-block distance limits, particle throttling, aggressive frame pacer.
- **MAX_FPS**: High performance, 48-block distance limits, full tile entity culling, active frame pacer.
- **BALANCED**: Default 64-block distance, essential tile entity culling, gentle frame pacer.
- **CUSTOM**: User customized via `config/sodiumimprover.json`.

---

## Supported Versions

- Minecraft 1.16.5
- Minecraft 1.17.1
- Minecraft 1.18.2
- Minecraft 1.19.x
- Minecraft 1.20.1 / 1.20.2 / 1.20.4
- Minecraft 1.21.x / 1.21.1
- Forward-compatible with future updates (26.2 / 1.26+)

---

## Build Artifacts

The built distribution JARs are located in:
- `mc1201/build/libs/SodiumImprover-mc1.20.1-1.0.0.jar`
- `mc1211/build/libs/SodiumImprover-mc1.21.1-1.0.0.jar`
- `common/build/libs/SodiumImprover-common-1.0.0.jar`

To build again:
```powershell
.\gradlew.bat build
```
