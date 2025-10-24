# 保持全部并允许混淆
-keep,allowobfuscation class com.jacknic.android.wanandroid.core.model.**

# GSON 字段 R8 混淆问题
-keepclassmembers,allowobfuscation class * {
 @com.google.gson.annotations.SerializedName <fields>;
}
-keepattributes Signature
-keep,allowobfuscation class com.google.gson.reflect.TypeToken { *; }
-keep,allowobfuscation class * extends com.google.gson.reflect.TypeToken