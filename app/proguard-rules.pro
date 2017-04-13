# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#1.基本指令区
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-ignorewarning
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

#2.默认保留区
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}

#3.webview
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
    }

#    # AndroidEventBus
#    -keep class org.simple.** { *; }
#    -keep interface org.simple.** { *; }
#    -keepclassmembers class * {
#        @org.simple.eventbus.Subscriber;
#    }
#
#    # 百度地图（jar包换成自己的版本，记得签名要匹配）
#    -libraryjars libs/baidumapapi_v4_2_0.jar
#    -keep class com.baidu.** {*;}
#    -keep class vi.com.** {*;}
#    -keep class com.sinovoice.** {*;}
#    -keep class pvi.com.** {*;}
#    -dontwarn com.baidu.**
#    -dontwarn vi.com.**
#    -dontwarn pvi.com.**
#
#    # ButterKnife
#    -keep class butterknife.** { *; }
#    -dontwarn butterknife.internal.**
#    -keep class **$$ViewBinder { *; }
#    -keepclasseswithmembernames class * {
#        @butterknife.* ;
#    }
#    -keepclasseswithmembernames class * {
#        @butterknife.* ;
#    }
#
#    # EventBus
#    -keepattributes *Annotation*
#    -keepclassmembers class ** {
#        @org.greenrobot.eventbus.Subscribe ;
#    }
#    -keep enum org.greenrobot.eventbus.ThreadMode { *; }
#
#    # Glide
#    -keep public class * implements com.bumptech.glide.module.GlideModule
#    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#      **[] $VALUES;
#      public *;
#    }
#
#    # 极光推送

-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#
#    # OkHttp3
#    -dontwarn com.squareup.okhttp3.**
#    -keep class com.squareup.okhttp3.** { *;}
#    -dontwarn okio.**
#
#    # 微信支付
#    -dontwarn com.tencent.mm.**
#    -dontwarn com.tencent.wxop.stat.**
#    -keep class com.tencent.mm.** {*;}
#    -keep class com.tencent.wxop.stat.**{*;}
#
#    # 支付宝钱包
#    -dontwarn com.alipay.**
#    -dontwarn HttpUtils.HttpFetcher
#    -dontwarn com.ta.utdid2.**
#    -dontwarn com.ut.device.**
#    -keep class com.alipay.android.app.IAlixPay{*;}
#    -keep class com.alipay.android.app.IAlixPay$Stub{*;}
#    -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
#    -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
#    -keep class com.alipay.sdk.app.PayTask{ public *;}
#    -keep class com.alipay.sdk.app.AuthTask{ public *;}
#    -keep class com.alipay.mobilesecuritysdk.*
#    -keep class com.ut.*
#
#    # RxJava RxAndroid
#    -dontwarn sun.misc.**
#    -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
#        long producerIndex;
#        long consumerIndex;
#    }
#    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
#        rx.internal.util.atomic.LinkedQueueNode producerNode;
#    }
#    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
#        rx.internal.util.atomic.LinkedQueueNode consumerNode;
#    }
#
#    # Gson
#    -keepattributes Signature-keepattributes *Annotation*
#    -keep class sun.misc.Unsafe { *; }
#    -keep class com.google.gson.stream.** { *; }
#    # 使用Gson时需要配置Gson的解析对象及变量都不混淆。不然Gson会找不到变量。
#    # 将下面替换成自己的实体类
#    -keep class com.dcch.sharebike.moudle.home.bean.** { *; }
#    -keep class com.dcch.sharebike.moudle.user.bean.** { *; }