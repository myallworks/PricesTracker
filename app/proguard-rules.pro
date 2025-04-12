-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

-keep class com.squareup.okhttp.** { *; }
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.example.pricestracker.models.** { *; }

-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.Entity

-keep class com.github.mikephil.charting.** { *; }
-keep class com.google.firebase.messaging.** { *; }

-keep class androidx.biometric.** { *; }
-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}