# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
#-----------basic configuration--------------
# Code obfuscation compression ratio, between 0 ~ 7, default is 5, generally do not need to be changed
-optimizationpasses 5

# Do not use mixed case when obfuscating, the obfuscated class name is lowercase
-dontusemixedcaseclassnames

# Specify classes that do not ignore non-public libraries
-dontskipnonpubliclibraryclasses

# Specify members of classes that do not ignore non-public libraries
-dontskipnonpubliclibraryclassmembers

# No pre-calibration can speed up obfuscation
# preverify is one of 4 steps for proguard
# Android does not require preverify, removing this step can speed up the confusion
-dontpreverify

# Does not optimize input class files
-dontoptimize

# Generate log files when obfuscated, ie map files
-verbose

# Specify the name of the mapping file
-printmapping proguardMapping.txt

#Algorithm used when obfuscating
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Protect Annotation in Code from Obfuscation
-keepattributes *Annotation*

# Ignore warning
-ignorewarnings

# Protecting generics from obfuscation
-keepattributes Signature

# Keep line numbers when exceptions are thrown
-keepattributes SourceFile,LineNumberTable

#-----------Things to keep--------------
# Keep all native methods unobfuscated
-keepclasseswithmembernames class * {
    native <methods>;
}

# Retained subclasses inherited from Activity, Application, Fragment
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
# support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep public class * extends android.support.v7.**

#----------------Protects specified classes and class members, but only if all specified classes and class members exist ------------------------------------
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    *** get*();
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

#enum
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#Parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#Serializable
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class **.R$* { *; }

-keepclassmembers class * {
    void *(**On*Event);
}
# WebView
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}

# androidx
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

# For Google Play Services
-keep public class com.google.android.gms.ads.**{
   public *;
}

-keep @interface com.google.android.gms.common.annotation.KeepName
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
  @com.google.android.gms.common.annotation.KeepName *;
}

-keep @interface com.google.android.gms.common.util.DynamiteApi
-keep public @com.google.android.gms.common.util.DynamiteApi class * {
  public <fields>;
  public <methods>;
}

-dontwarn android.security.NetworkSecurityPolicy

-keep class com.stripe.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**


# Firebase Authentication
-keepattributes Signature
-keepattributes *Annotation*

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


-dontwarn jp.wasabeef.glide.**
-dontnote jp.wasabeef.glide.**

-keep,allowobfuscation,allowshrinking class id.paniclabs.** { *; }

#Try Fix Fatal Exception: java.lang.NoSuchMethodError:
#No direct method <init>()V in class Landroid/security/IKeyChainService$Stub; or its super classes (declaration of 'android.security.IKeyChainService$Stub' appears in /system/framework/framework.jar!classes2.dex)
-keep class android.security.IKeyChainService { *; }

-keep class androidx.test.espresso.**
# keep the class and specified members from being removed or renamed
-keep class androidx.test.espresso.IdlingRegistry { *; }
-keep class androidx.test.espresso.IdlingResource { *; }

-dontnote junit.framework.**
-dontnote junit.runner.**

-dontwarn androidx.test.**
-dontwarn org.junit.**
-dontwarn org.hamcrest.**
-dontwarn com.squareup.javawriter.JavaWriter

# Uncomment this if you use Mockito
-dontwarn org.mockito.**
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

# Timber
-dontwarn org.jetbrains.annotations.**

##########################FIREBASE ##########################
-dontwarn com.google.firebase.messaging.**
-keep class com.google.firebase.** { *; }
-keep class io.grpc.** {*;}
-keep class com.firebase.** { *; }
-keep class org.shaded.apache.** { *; }
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepnames class com.shaded.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**


########################## DEEP LINK ##########################
-keep @interface com.airbnb.deeplinkdispatch.DeepLink
-keepclasseswithmembers class * {
    @com.airbnb.deeplinkdispatch.DeepLink <methods>;
}
-keep class com.airbnb.deeplinkdispatch.** { *; }
-keep @interface com.androidpk.features.mall.deeplink.** { *; }
-keepclasseswithmembers class * {
    @com.android.features.mall.deeplink.* <methods>;
}
-keep @interface com.androidpk.features.detail.deeplink.** { *; }
-keepclasseswithmembers class * {
    @com.android.features.detail.deeplink.* <methods>;
}


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keep,allowobfuscation @interface com.google.gson.annotations.SerializedName

##---------------End: proguard configuration for Gson  ----------

# Keep model classes
-keepclassmembers class com.android.model.** { <fields>; }
-keepclassmembers class com.android.remote.** { <fields>; }
-keepclassmembers class com.android.network.** { <fields>; }
-keepclassmembers class com.android.local.** { <fields>; }
-keepclassmembers class com.android.event.** { <fields>; }
-keepclassmembers class com.android.corejournal.database.** { <fields>; }
-keepclassmembers class com.android.corejournal.entities.** { <fields>; }
-keepclassmembers class com.android.corejournal.model.** { <fields>; }

# Facebook Conceal
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.crypto.proguard.annotations.DoNotStrip
-keep,allowobfuscation @interface com.facebook.crypto.proguard.annotations.KeepGettersAndSetters

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.crypto.proguard.annotations.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.crypto.proguard.annotations.DoNotStrip *;
}

-keepclassmembers @com.facebook.crypto.proguard.annotations.KeepGettersAndSetters class * {
  void set*(***);
  *** get*();
}

# from first-party/fbjni/java/com/facebook/jni/fbjni.pro

# For common use cases for the hybrid pattern, keep symbols which may
# be referenced only from C++.

-keepclassmembers class * {
    com.facebook.jni.HybridData *;
    <init>(com.facebook.jni.HybridData);
}

-keepclasseswithmembers class * {
    com.facebook.jni.HybridData *;
}


# from first-party/proguard/annotations/proguard_annotations.pro

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.proguard.annotations.DoNotStrip
-keep,allowobfuscation @interface com.facebook.proguard.annotations.KeepGettersAndSetters

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.proguard.annotations.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.proguard.annotations.DoNotStrip *;
}

-keepclassmembers @com.facebook.proguard.annotations.KeepGettersAndSetters class * {
  void set*(***);
  *** get*();
}

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

-dontwarn org.reactivestreams.FlowAdapters
-dontwarn org.reactivestreams.**
-dontwarn java.util.concurrent.flow.**
-dontwarn java.util.concurrent.**
-dontwarn org.conscrypt.ConscryptHostnameVerifier
# These classes are only required by kotlinx.coroutines.debug.AgentPremain, which is only loaded when
# kotlinx-coroutines-core is used as a Java agent, so these are not needed in contexts where ProGuard is used.
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler
-dontwarn java.lang.instrument.Instrumentation
-dontwarn sun.misc.Signal
