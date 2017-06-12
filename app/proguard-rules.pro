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
    #mob
    -keep class cn.sharesdk.**{*;}
    -keep class com.sina.**{*;}
    -keep class **.R$* {*;}
    -keep class **.R{*;}
    -dontwarn cn.sharesdk.**
    -dontwarn **.R$*
    -keep class m.framework.**{*;}
    -keep class android.net.http.SslError
    -keep class android.webkit.**{*;}
    -keep class com.mob.tools.utils

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

    -keep class android.net.http.SslError
    -keep class android.webkit.**{*;}
    -keep class cn.sharesdk.**{*;}
    -keep class com.sina.**{*;}
    -keep class m.framework.**{*;}

    # AndroidEventBus
    -keep class org.simple.** { *; }
    -keep interface org.simple.** { *; }
    -keepclassmembers class * {
        @org.simple.eventbus.Subscriber <methods>;
    }
    -keepattributes *Annotation*
#
#    # 百度地图（jar包换成自己的版本，记得签名要匹配）
    -keep class com.baidu.** {*;}
    -keep class vi.com.** {*;}
    -dontwarn com.baidu.**
#
    # ButterKnife
    -keep class butterknife.** { *; }
    -dontwarn butterknife.internal.**
    -keep class **$$ViewBinder { *; }

    -keepclasseswithmembernames class * {
        @butterknife.* <fields>;
    }

    -keepclasseswithmembernames class * {
        @butterknife.* <methods>;
    }


    # Glide

    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
        **[] $VALUES;
        public *;
      }
#    -keepresourcexmlelements manifest/application/meta-data@value=GlideModule

    # 极光推送
    -dontoptimize
    -dontpreverify
    -dontwarn cn.jpush.**
    -keep class cn.jpush.** { *; }
    -dontwarn cn.jiguang.**
    -keep class cn.jiguang.** { *; }
#
    # OkHttp3
    -dontwarn com.squareup.okhttp3.**
    -keep class com.squareup.okhttp3.** { *;}
    -dontwarn okio.**
#
    # 微信支付
    -dontwarn com.tencent.mm.**
    -dontwarn com.tencent.wxop.stat.**
    -keep class com.tencent.mm.** {*;}
    -keep class com.tencent.wxop.stat.**{*;}

    # 支付宝钱包
    -dontwarn com.alipay.**
    -dontwarn HttpUtils.HttpFetcher
    -dontwarn com.ta.utdid2.**
    -dontwarn com.ut.device.**
    -keep class com.alipay.android.app.IAlixPay{*;}
    -keep class com.alipay.android.app.IAlixPay$Stub{*;}
    -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
    -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
    -keep class com.alipay.sdk.app.PayTask{ public *;}
    -keep class com.alipay.sdk.app.AuthTask{ public *;}
    -keep class com.alipay.mobilesecuritysdk.*
    -keep class com.ut.*

    # RxJava RxAndroid
    -dontwarn sun.misc.**
    -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
        long producerIndex;
        long consumerIndex;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode producerNode;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode consumerNode;
    }

    # Gson
    -keepattributes Signature
    -keep class sun.misc.Unsafe { *; }
    -keep class com.google.gson.stream.** { *; }
    # 使用Gson时需要配置Gson的解析对象及变量都不混淆。不然Gson会找不到变量。
    # 将下面替换成自己的实体类
    -keep class com.dcch.sharebike.moudle.home.bean.** { *; }
    -keep class com.dcch.sharebike.moudle.user.bean.** { *; }

    #-optimizationpasses 7
    #-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
    -dontoptimize
    -dontusemixedcaseclassnames
    -verbose
    -dontskipnonpubliclibraryclasses
    -dontskipnonpubliclibraryclassmembers
    -dontwarn dalvik.**
    #-overloadaggressively

    #@proguard_debug_start
    # ------------------ Keep LineNumbers and properties ---------------- #
    -keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
    -renamesourcefileattribute TbsSdkJava
    -keepattributes SourceFile,LineNumberTable
    #@proguard_debug_end

    # --------------------------------------------------------------------------
    # Addidional for x5.sdk classes for apps

    -keep class com.tencent.smtt.export.external.**{
        *;
    }

    -keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
        *;
    }

    -keep class com.tencent.smtt.sdk.CacheManager {
        public *;
    }

    -keep class com.tencent.smtt.sdk.CookieManager {
        public *;
    }

    -keep class com.tencent.smtt.sdk.WebHistoryItem {
        public *;
    }

    -keep class com.tencent.smtt.sdk.WebViewDatabase {
        public *;
    }

    -keep class com.tencent.smtt.sdk.WebBackForwardList {
        public *;
    }

    -keep public class com.tencent.smtt.sdk.WebView {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
        public static final <fields>;
        public java.lang.String getExtra();
        public int getType();
    }

    -keep public class com.tencent.smtt.sdk.WebView$WebViewTransport {
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.WebView$PictureListener {
        public <fields>;
        public <methods>;
    }


    -keepattributes InnerClasses

    -keep public enum com.tencent.smtt.sdk.WebSettings$** {
        *;
    }

    -keep public enum com.tencent.smtt.sdk.QbSdk$** {
        *;
    }

    -keep public class com.tencent.smtt.sdk.WebSettings {
        public *;
    }


    -keepattributes Signature
    -keep public class com.tencent.smtt.sdk.ValueCallback {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.WebViewClient {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.DownloadListener {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.WebChromeClient {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.WebChromeClient$FileChooserParams {
        public <fields>;
        public <methods>;
    }

    -keep class com.tencent.smtt.sdk.SystemWebChromeClient{
        public *;
    }
    # 1. extension interfaces should be apparent
    -keep public class com.tencent.smtt.export.external.extension.interfaces.* {
        public protected *;
    }

    # 2. interfaces should be apparent
    -keep public class com.tencent.smtt.export.external.interfaces.* {
        public protected *;
    }

    -keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
        public protected *;
    }

    -keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.WebIconDatabase {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.WebStorage {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.DownloadListener {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.QbSdk {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.QbSdk$PreInitCallback {
        public <fields>;
        public <methods>;
    }
    -keep public class com.tencent.smtt.sdk.CookieSyncManager {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.Tbs* {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.utils.LogFileUtils {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.utils.TbsLog {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.utils.TbsLogClient {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.CookieSyncManager {
        public <fields>;
        public <methods>;
    }

    # Added for game demos
    -keep public class com.tencent.smtt.sdk.TBSGamePlayer {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.TBSGamePlayerClient* {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.TBSGamePlayerClientExtension {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.TBSGamePlayerService* {
        public <fields>;
        public <methods>;
    }

    -keep public class com.tencent.smtt.utils.Apn {
        public <fields>;
        public <methods>;
    }
    # end


    -keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {
        public <fields>;
        public <methods>;
    }

    -keep class MTT.ThirdAppInfoNew {
        *;
    }

    -keep class com.tencent.mtt.MttTraceEvent {
        *;
    }

    # Game related
    -keep public class com.tencent.smtt.gamesdk.* {
        public protected *;
    }

    -keep public class com.tencent.smtt.sdk.TBSGameBooter {
            public <fields>;
            public <methods>;
    }

    -keep public class com.tencent.smtt.sdk.TBSGameBaseActivity {
        public protected *;
    }

    -keep public class com.tencent.smtt.sdk.TBSGameBaseActivityProxy {
        public protected *;
    }

    -keep public class com.tencent.smtt.gamesdk.internal.TBSGameServiceClient {
        public *;
    }
    #---------------------------------------------------------------------------


    #------------------  下方是android平台自带的排除项，这里不要动         ----------------

    -keep public class * extends android.app.Activity{
        public <fields>;
        public <methods>;
    }
    -keep public class * extends android.app.Application{
        public <fields>;
        public <methods>;
    }
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference

    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }

    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet, int);
    }

    -keepattributes *Annotation*

    -keepclasseswithmembernames class *{
        native <methods>;
    }

    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #------------------  下方是共性的排除项目         ----------------
    # 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
    # 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除

    -keepclasseswithmembers class * {
        ... *JNI*(...);
    }

    -keepclasseswithmembernames class * {
        ... *JRI*(...);
    }

    -keep class **JNI* {*;}

    #1.support-v7-appcompat
    -keep public class android.support.v7.widget.** { *; }
    -keep public class android.support.v7.internal.widget.** { *; }
    -keep public class android.support.v7.internal.view.menu.** { *; }

    -keep public class * extends android.support.v4.view.ActionProvider {
        public <init>(android.content.Context);
    }

#    #2.rx
#    -dontwarn io.reactivex.**
#    -keep io.reactivex.**
#    -keepclassmembers class io.reactivex.** { *; }

    #3.retrolambda
    -dontwarn java.lang.invoke.*

    #4.support-v4
    -keep class android.support.v4.** { *; }
    -keep interface android.support.v4.** { *; }

    #5.ucrop
    -dontwarn com.yalantis.ucrop**
    -keep class com.yalantis.ucrop** { *; }
    -keep interface com.yalantis.ucrop** { *; }

    #6.photoview
    -keep class uk.co.senab.photoview** { *; }
    -keep interface uk.co.senab.photoview** { *; }

    #7.rxgalleryfinal
    -keep class cn.finalteam.rxgalleryfinal.ui.widget** { *; }

    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    -keepattributes *Annotation*
    -keepclasseswithmembernames class * {
        native <methods>;
    }
    -keepclassmembers public class * extends android.view.View {
       void set*(***);
       *** get*();
    }
