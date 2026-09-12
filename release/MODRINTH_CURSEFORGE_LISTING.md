# Titanium — Modrinth & CurseForge Publishing Assets

---

## 📋 Quick Metadata (For Mod Creation Form)

* **Name**: `Titanium`
* **Slug / Mod ID**: `titanium`
* **Short Description / Summary** *(under 100 characters)*:
  > Extreme FPS Booster & Renderer Optimizer companion for Sodium.
* **Environment**: `Client-Side`
* **Mod Loaders**: `Fabric`, `Quilt`
* **Categories / Tags**: `Optimization`, `Performance`, `Utility`
* **License**: `LGPL-3.0`
* **Project Icon**: Use `release/titanium_avatar_icon.png` (or drag and drop)

---

## 📦 Jars to Upload (in `release/` folder)

| File Name | Supported Minecraft Versions | Loader | Release Type |
| :--- | :--- | :--- | :--- |
| **`Titanium-mc26.2-1.0.0.jar`** | `26.2`, `26.1.2`, `1.21.5+` | Fabric | Release |
| **`Titanium-mc1.21.1-1.0.0.jar`** | `1.21`, `1.21.1` | Fabric | Release |
| **`Titanium-mc1.20.6-1.0.0.jar`** | `1.20.5`, `1.20.6` | Fabric | Release |
| **`Titanium-mc1.20.1-1.0.0.jar`** | `1.20`, `1.20.1`, `1.20.2` | Fabric | Release |
| **`Titanium-mc1.19.4-1.0.0.jar`** | `1.19`, `1.19.1`, `1.19.2`, `1.19.3`, `1.19.4` | Fabric | Release |
| **`Titanium-mc1.18.2-1.0.0.jar`** | `1.18`, `1.18.1`, `1.18.2` | Fabric | Release |
| **`Titanium-mc1.17.1-1.0.0.jar`** | `1.17`, `1.17.1` | Fabric | Release |
| **`Titanium-mc1.16.5-1.0.0.jar`** | `1.16.4`, `1.16.5` | Fabric | Release |

---

## 📝 Project Description (Copy-paste directly into Modrinth & CurseForge)

```markdown
# ⚡ Titanium

**Titanium** is an ultra-lightweight, high-performance rendering optimization mod engineered as a native companion to **Sodium**. It adds aggressive culling algorithms, frustum optimization, tile entity distance scaling, and dynamic frame pacing to push your frame rates to their absolute maximum.

---

### 🚀 Key Features

* **👁️ Aggressive Frustum Culling**: Skips rendering mobs, animals, item frames, armor stands, and dropped items that are outside your camera's field of view.
* **📦 Tile Entity & Storage Culling**: Drastically reduces stutter and lag in massive storage rooms, sorting systems, and megabases by culling offscreen chests, hoppers, shulkers, signs, and banners.
* **⏱️ Dynamic Frame Pacer**: Continuously monitors instantaneous frame times. During sudden framerate drops or dense chunk loading, it dynamically adjusts render distances to smooth out 1% low FPS spikes and eliminate micro-stutters.
* **⚙️ Native Sodium Integration**:
  * **Sodium 0.9+ (26.2)**: Fully integrates via the official `"sodium:config_api_user"` specification with custom toggles right inside Sodium's Video Settings.
  * **Sodium 0.2 - 0.6**: Seamlessly injects a dedicated Titanium settings tab into the Sodium video options GUI.
* **🛡️ Mod Synergy & Compatibility**:
  * Works out-of-the-box alongside **Iris Shaders**, **Entity Culling**, **More Culling**, **ImmediatelyFast**, **Enhanced Block Entities (EBE)**, **FerriteCore**, and **Reese's Sodium Options (RSO)**.

---

### 🎛️ Presets Available

1. **Potato PC**: Ultra-aggressive culling (32 block radius), dynamic scaling down to 40%, throttled offscreen particles. Perfect for low-end laptops and integrated GPUs.
2. **Max FPS** *(Default)*: High performance with 48 block entity radius, dynamic distance scaling, and full offscreen frustum culling.
3. **Balanced**: Standard 64 block radius, maintains sign and item frame visibility while eliminating unseen background load.
4. **Custom**: Individually toggle mob culling, tile entity culling, particle culling, and dynamic frame pacing.

---

### 📥 Requirements

* **Fabric Loader** (0.15.0 or newer)
* **Sodium** (0.2.0 or newer)
```

---

## 🚀 How to Publish

### Option A: Manual Upload via Web Dashboard (Recommended)
1. **Modrinth**:
   * Go to [modrinth.com/create](https://modrinth.com/create)
   * Choose **Fabric Mod**, Name: `Titanium`, Summary from above.
   * Upload `release/titanium_avatar_icon.png` as the icon.
   * Paste the Markdown description into the description editor.
   * Under **Versions** $\rightarrow$ **Add Version**, upload the 8 jars from the `release/` folder.
2. **CurseForge**:
   * Go to [authors.curseforge.com](https://authors.curseforge.com) $\rightarrow$ **Create Project**.
   * Category: *Performance / Quality of Life*.
   * Upload `release/titanium_avatar_icon.png` and paste the description.
   * Under **Files**, upload each `.jar` file with its corresponding Minecraft version.

### Option B: Automated Upload via Browser or API
If you have **API Tokens** (`MODRINTH_TOKEN` / `CURSEFORGE_API_KEY`) or would like me to use the **browser subagent** to assist you on Modrinth / Curseforge, let me know!
