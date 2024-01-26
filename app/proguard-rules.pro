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


#-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

#-keep class com.google.firebase.** { *; }
#-keep class com.google.android.gms.** { *; }
#-dontwarn com.google.firebase.**
#-dontwarn com.google.android.gms.**
#-keep class com.google.maps.** { *; }




-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**
-keep class com.google.maps.** { *; }

# Stripe
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn com.cardinalcommerce.**
-dontwarn com.stripe.android.**

-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**
-keep class com.stripe.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.stream.** { *; }

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Okio
-dontwarn okio.**
-keep class okio.** { *; }

# Retrofit
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Logging
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-dontwarn org.slf4j.**
-keep class org.slf4j.** { *; }

#Handle
-keep class android.os.Handler { *; }

# Other
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }

