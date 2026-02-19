## Contribution guidelines

> [!CAUTION]
> ### No AI generated code will be accepted.
>
> Despite the environmental cost and intellectual theft, you won't in fact learn how to code if you
> let the clankers do everything.
>
> Own your learning journey and your mistakes, it'll help you grow.

> [!NOTE]
> **Refactoring** or some **dev-faces improvements** might also be accepted. However, please stick
> to the following principles:
>
> + The app must remain **Single Activity**. If you have any doubts, check [the documentation](https://developer.android.com/topic/architecture#app_composition).
> + **Performance matters.** In the case of choosing between source code beauty and performance,
    performance should be a priority.
> + Keep features in their respective modules.
> + Please, **do not modify readme and other information files** (except for typos).
> + **Avoid adding new dependencies**: unless required. APK size is important.
> + **Please, explain your changes**.

> [!IMPORTANT]
> **Language:** Kotlin  
> **Build System:** Gradle with Kotlin DSL  
> **Minimum SDK:** 29 (Android 10.0+)  
> **Target SDK:** 36
> **Minimum JDK:** 21
> **Architecture:** Single Activity & MVVM
> **UI:** Jetpack Compose
>
> + If you want to **fix bugs** or **implement new features** that **already have
    an [issue card](https://github.com/LunaCrew/luna/issues):** please assign this issue to you
    and/or comment about it.
> + If you want to **implement a new feature:** open an issue or discussion regarding it to ensure
    it will be accepted.

## Gradle Commands

```bash
# Debug build (recommended for development)
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk

# Release build (requires signing setup)
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk
```
