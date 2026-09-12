# Titanium

**Author**: budgetgamer1503  
**Mod ID**: `titanium`  
**License**: LGPL-3.0  

Titanium is an all-in-one renderer optimization and companion mod for **Sodium** on Minecraft Fabric. It targets the areas of rendering that Sodium leaves un-optimized: entity frustum and distance culling, tile entity / block entity culling (chests, hoppers, banners, signs), particle throttling, and dynamic frame-pacing.

---

## Required Dependencies

- **Fabric Loader**: `>=0.14.0` (or `>=0.15.0` on 1.20.6 / 1.21.1+)
- **Sodium**: `>=0.2.0` (Mandatory - works across all Sodium 0.2.x - 0.6.x+ releases)

---

## Companion Mod Synergies & Optional Dependencies

Titanium contains an intelligent runtime `CompatibilityManager` that automatically detects and synergizes with popular rendering and performance mods:

| Mod | Mod ID | Synergy / Optimization Behavior |
| --- | --- | --- |
| **Entity Culling** | `entityculling` | **Drastically reduces background raytrace load.** Titanium's instantaneous frustum and dynamic distance culling eliminate off-screen and distant entities first, sparing EntityCulling's background raytracer from redundant voxel traversal. |
| **More Culling** | `moreculling` | **Conflict-free coordination.** Harmonizes block entity and sign culling to prevent conflicting cancellation or matrix stack anomalies. |
| **Enhanced Block Entities (EBE)** | `enhancedblockentities` | **Chunk mesh awareness.** Automatically detects when chests, bells, and signs are converted into baked chunk meshes, bypassing redundant block entity dispatcher checks. |
| **ImmediatelyFast** | `immediatelyfast` | **Immediate-mode rendering boost.** Coordinates immediate mode batching with culling passes. |
| **Reese's Sodium Options** | `reeses-sodium-options` | Injects a dedicated vertical configuration tab directly into Sodium's settings screen. |
| **Sodium Extra** | `sodium-extra` | Seamlessly co-exists alongside Sodium Extra's detail sliders. |
| **FerriteCore** | `ferritecore` | Memory reduction pairs with Titanium's renderer memory and draw-call savings. |

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
- **CUSTOM**: User customized via `config/titanium.json`.

---

## Supported Versions

| Minecraft Version | Module | Target Range | Status |
| --- | --- | --- | --- |
| **1.16.5** | `mc1165` | `1.16.0` - `1.16.5` | ✅ Fully Supported |
| **1.17.1** | `mc1171` | `1.17.0` - `1.17.1` | ✅ Fully Supported |
| **1.18.2** | `mc1182` | `1.18.0` - `1.18.2` | ✅ Fully Supported |
| **1.19.4** | `mc1194` | `1.19.0` - `1.19.4` | ✅ Fully Supported |
| **1.20.1** | `mc1201` | `1.20.0` - `1.20.4` | ✅ Fully Supported |
| **1.20.6** | `mc1206` | `1.20.5` - `1.20.6` | ✅ Fully Supported |
| **1.21.1** | `mc1211` | `1.21.0` - `1.21.4` | ✅ Fully Supported |
| **26.2** | `mc262` | `>=26.2` | ✅ Fully Supported |

---

## Optimization Methods Researched from Popular Mods

Titanium studies and incorporates core algorithms from top-tier rendering mods:
1. **Dynamic Frustum & Visual Edge Padding (from EntityCulling)**: Bounding boxes are padded by 0.25 blocks before frustum visibility checks, preventing mob extremities (wings, arms, tails) from popping at screen edges.
2. **Glowing & Boss Protection (from EntityCulling)**: Spectral outlines and bosses (Ender Dragon, Wither) are never culled, preserving critical gameplay visuals.
3. **Directional Backface Culling (from More Culling)**: Directional normal vectors for Item Frames, Wall Signs, and Paintings are tested against the camera vector. If the camera is behind the mounting wall, rendering is culled instantly.
4. **Chunk-Baked Mesh Detection (from Enhanced Block Entities)**: Skips dispatcher-level culling when EBE converts chests and bells into Sodium-rendered chunk meshes.

---

## Build Distribution JARs

All distribution JARs are compiled and packaged in each subproject's `build/libs` directory:
- `mc1165/build/libs/Titanium-mc1.16.5-1.0.0.jar`
- `mc1171/build/libs/Titanium-mc1.17.1-1.0.0.jar`
- `mc1182/build/libs/Titanium-mc1.18.2-1.0.0.jar`
- `mc1194/build/libs/Titanium-mc1.19.4-1.0.0.jar`
- `mc1201/build/libs/Titanium-mc1.20.1-1.0.0.jar`
- `mc1206/build/libs/Titanium-mc1.20.6-1.0.0.jar`
- `mc1211/build/libs/Titanium-mc1.21.1-1.0.0.jar`
- `mc262/build/libs/Titanium-mc26.2-1.0.0.jar`
- `common/build/libs/Titanium-common-1.0.0.jar`

To build all JARs in one command:
```powershell
.\gradlew.bat build
```
