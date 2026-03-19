## Contribution guidelines

> [!CAUTION]
> ### No AI generated code will be accepted.
>
> Keep the clankers away.

---

> [!NOTE]
> **Refactoring** or some **dev-faces improvements** might also be accepted. However, please stick
> to the following principles:
>
> + The app must remain **Single Activity**. If you have any doubts,
    check [the documentation](https://developer.android.com/topic/architecture#app_composition).
> + **Performance matters.** In the case of choosing between source code beauty and performance,
    performance should be a priority.
> + Keep features in their respective modules.
> + Please, **do not modify readme and other information files** (except for typos).
> + **Avoid adding new dependencies**: unless required. APK size is important.
> + **Please, explain your changes**.
> + If you want to **fix bugs** or **implement new features** that **already have
    an [issue card](https://github.com/LunaCrew/luna/issues):** please assign this issue to you
    and/or comment about it.
> + If you want to **implement a new feature:** open an issue or discussion regarding it to ensure
    it will be accepted.

## Specs

+ **Language:** Kotlin
+ **Android Studio:** [Otter 3 | 2025.2.3](https://androidstudio.googleblog.com/2026/01/android-studio-otter-3-feature-drop_0923772896.html) or newer
+ **Build System:** Gradle with Kotlin DSL
+ **Minimum SDK:** 29 (Android 10.0+)
+ **Target SDK:** 36
+ **Minimum JDK:** 21
+ **Architecture:** Single Activity & MVVM
+ **UI:** Jetpack Compose

## Gradle Commands

```bash
# Run the command bellow to see available tasks:
./gradlew tasks
```
