# Realms of Reverie

A NeoForge Minecraft mod (1.21.1) that serves as the core mod for the "Realms of Reverie" modpack. Built with Java 21.

## What This Mod Does

Integrates **Project MMO (PMMO)** skill progression with **ParCool** parkour mechanics. Players unlock parkour abilities (wall jump, climbing, flipping, etc.) as they level up acrobatics and swimming skills. Custom features include a Hunter potion effect (reveals nearby mobs) and an advanced structure placement algorithm.

## Project Structure

- `src/main/java/net/silvertide/realmsofreverie/` - Mod source code
  - `commands/` - `/ror` command tree
  - `config/ServerConfigs.java` - Runtime config (skills, XP multiplier, debug)
  - `effects/HunterEffect.java` - Custom potion effect (mob scanning/glowing)
  - `events/` - Event handlers (ParCool XP, PMMO skill-ups, player join)
  - `mixins/` - Mixin configurations
  - `placements/AdvancedRandomSpread.java` - Custom world gen placement
  - `registry/` - Effect and placement registries
  - `utils/ParcoolUtils.java` - ParCool action unlock logic by skill level
- `src/main/resources/data/realmsofreverie/advancement/` - Parkour advancement JSONs
- `src/main/resources/assets/realmsofreverie/` - Textures, lang files

## Build & Run

```bash
./gradlew build          # Build the mod
./gradlew runClient      # Run Minecraft client with mod loaded
./gradlew runServer      # Run dedicated server
./gradlew runData        # Generate data (advancements, etc.)
./gradlew publishMods    # Publish to CurseForge (requires CF_TOKEN)
```

## Key Dependencies

| Dependency | Purpose |
|---|---|
| NeoForge 21.1.143+ | Mod loader |
| Project MMO (PMMO) 2.7.35+ | Skill/XP progression system |
| ParCool 3.4.2.0+ | Parkour movement mechanics |

## Configuration

Runtime config: `realmsofreverie-server.toml`
- `parcoolLandSkill` / `parcoolWaterSkill` - PMMO skills tied to land/water parkour
- `vaultXpMultiplier` - XP reward multiplier for parkour actions
- `debug` - Enable debug logging

## Key Versions

- Mod version: defined in `gradle.properties` (`mod_version`)
- Minecraft: 1.21.1
- Java: 21
- CurseForge project ID: 1236190

## CI

GitHub Actions runs `./gradlew build` on push and PRs (`.github/workflows/build.yml`).
