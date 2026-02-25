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

## Generating `google-services.json` file.

### Step 1: Create a Firebase Project and setup

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Click Add project.
3. Follow the prompts to name your project and choose your Google Analytics preferences.

### Step 2: Register a new Android app

Once you are inside your project dashboard:

1. Click the Android icon (the little green robot) in the center of the page to add a new app.

2. Register App: Under Android package name, enter exactly:
    ```md
    lunacrew.luna
    ```
3. (Optional) App nickname: You can call it "Luna Android."
4. (Optional) Debug signing certificate SHA-1: Leave this blank for now unless you are setting up
   Google Sign-In or Dynamic Links immediately.
5. Click Register app.
6. After clicking Register, the console will present a Download `google-services.json` button, but
   don't download it yet.

### Step 3: Setup authentication providers

1. On the left sidebar, click Authentication or access
   `https://console.firebase.google.com/project/YOUR_APP_ID/authentication`.
2. Go to the Sign-in method tab.
3. Click Add new provider and enable the following:
    + Email/Password: Toggle "Enabled" and click Save.
    + Google: Toggle "Enabled." You will be asked to select a Project support email. Click Save.
    + Anonymous: Toggle "Enabled" and click Save.
4. If it doesn't prompt you to download the `google-services.json` file, go back to apps list and
   download it.

### Step 4: Place the File in Your Project

1. Open your project in Android Studio.
2. Switch the project view from "Android" to "Project" using the dropdown in the top-left corner.
3. Navigate to your app module directory: `./app/`
4. Drag and drop the `google-services.json` file into that app folder.

> [!CAUTION]
> Do not rename this file (e.g., `google-services(1).json`). It must be exactly
`google-services.json`.
> 
> Do not edit the file as well.

## Gradle Commands

```bash
# Run the command bellow to see available tasks:
./gradlew tasks
```
