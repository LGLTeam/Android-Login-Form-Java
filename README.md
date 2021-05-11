# Android Login Form in Java
Simple login form in Java only with okhttp dependency

This project is for experienced modders and programmers only and must be able to search on Google for help. We will not explain here

Keep in mind that it is mainly for APK modding and not for general app development purposes

![](https://i.imgur.com/K7z1R6J.png)

# PHP

Add `login.php` to your server that supports PHP, use 000webhost.com for free webserver. Repo based hosting such as Github does not support PHP

Change URL on `public static String URL` on `MainActivity.java` file

# Merging into your project

It's easy, just copy and paste the code of `public static void Start(final Context context)` into your project and make some changes

See the example folder how the login code was merged into the mod menu project on `MainActivity.java` file

# How to implement to APK:

Build the project to APK file.

**Build** -> **Build Bundle(s)/APK(s)** -> **Build APK(s)**

If no errors occured, you did everything right and build will succeded. You will be notified that it build successfully

![](https://i.imgur.com/WpSKV1L.png)

Click on **locate** to show you the location of **build.apk**. It is stored at `(your-project)\app\build\outputs\apk\app-debug.apk`

![](https://i.imgur.com/wBTPSLi.png) 

Now decompile your **app-debug.apk**.

Copy all your smali folders to the game's smali folder to the game's decompiled directory `(game name)\smali`. If the game have multidexes, add your smali to the last classes dex if possible to prevent compilation errors such as `Unsigned short value out of range: xxxxx`

If the game has already okhttp smalis, don't add it.

Now, you must know the game's launch activity/main activity

You can either use APK Easy Tool to view main activity like screenshot below:

![](https://i.imgur.com/JQdPjyZ.png)

Or decompile your **game APK** using apktool.jar, or any 3rd party CLI or GUI tools, and open `androidmanifest.xml` and search after `<action android:name="android.intent.action.MAIN"/>`

In this case, my game's main activity was `com.unity3d.player.UnityPlayerActivity`

![](https://i.imgur.com/FfOtc1K.png)

On the game's `androidmanifest.xml`, besure that `<uses-permission android:name="android.permission.INTERNET"/>` exist

![](https://i.imgur.com/k0sLVUF.png)

Locate to the game's path of main activity and open the **smali** file.

Example if it was `com.unity3d.player.UnityPlayerActivity`, the path would be `(decompiled game)/com/unity3d/player/UnityPlayerActivity.smali`

If the game have multi dexes, it may be located in smali_classes2... please check all

Open the main acitivity's smali file, search for OnCreate method and paste this code inside (change the package name if you had changed it)
```
invoke-static {p0}, Lcom/example/androidloginjava/MainActivity;->Start(Landroid/content/Context;)V
```

![](https://i.imgur.com/yjsAaHD.png)

Save the file

Now compile the game APK

It should launch the login screen and successfully launch the activity after login :)

![](https://i.imgur.com/ALFTXi2.gif)

# Simple protection in lib

See `native-lib.cpp` for example how to check if the user has logged in

There are the codes called `Check()` and `System.loadLibrary("yourlib");`. They are commented out

You may need to protect your dex and lib files for that