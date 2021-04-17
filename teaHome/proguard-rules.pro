# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#fastJson
#noinspection ShrinkerUnresolvedReference
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**

#mqtt
-keep class com.tuya.smart.mqttclient.mqttv3.** { *; }
-dontwarn com.tuya.smart.mqttclient.mqttv3.**

#OkHttp3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-keep class okio.** { *; }
-dontwarn okio.**

-keep class com.tuya.**{*;}
-dontwarn com.tuya.*
  # react-native
  -keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
  -keep,allowobfuscation @interface com.facebook.proguard.annotations.DoNotStrip
  -keep,allowobfuscation @interface com.facebook.proguard.annotations.KeepGettersAndSetters
  -keep class * extends com.facebook.react.bridge.JavaScriptModule { *; }
  -keep class * extends com.facebook.react.bridge.NativeModule { *; }
  -keepclassmembers,includedescriptorclasses class * { native <methods>; }
  -dontwarn com.facebook.react.**
  -keep,includedescriptorclasses class com.facebook.react.bridge.** { *; }

  #高德地图
  -dontwarn com.amap.**
  -keep class com.amap.api.maps.** { *; }
  -keep class com.autonavi.** { *; }
  -keep class com.amap.api.trace.** { *; }
  -keep class com.amap.api.navi.** { *; }
  -keep class com.autonavi.** { *; }
  -keep class com.amap.api.location.** { *; }
  -keep class com.amap.api.fence.** { *; }
  -keep class com.autonavi.aps.amapapi.model.** { *; }
  -keep class com.amap.api.maps.model.** { *; }
  -keep class com.amap.api.services.** { *; }

  #MPAndroidChart
  -keep class com.github.mikephil.charting.** { *; }
  -dontwarn com.github.mikephil.charting.**

  -keep class com.tuya.**.**{*;}
  -dontwarn com.tuya.**.**