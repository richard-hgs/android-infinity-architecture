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

# ------------------------------ Reflection list ----------------------------
# TextInputLayout - Field - collapsingTextHelper;
# TextInputLayout - Field - hint;
# TextInputLayout - Field - tmpRectF;
# TextInputLayout - Field - boxLabelCutoutPaddingPx;
# CollapsingTextHelper - Field - collapsedBounds;
# CollapsingTextHelper - Method - setCollapsedBounds(int.class, int.class, int.class, int.class);
# CollapsingTextHelper - Method - recalculate();