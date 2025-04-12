# Real-Time Prices Tracker 📈
An Android application that tracks real-time price changes with biometric authentication, Firebase notifications, and interactive price charts.

## Features ✨

- **Real-Time Price Tracking** with automatic refresh
- **Biometric Authentication** (Fingerprint/Face ID)
- **Firebase Cloud Messaging** for price change alerts
- **Interactive Charts** using MPAndroidChart
- **Offline Support** with Room Database
- **Dark/Light Theme** support
- **Search Functionality** by item ID or name

## Tech Stack 🛠️

- **Language**: Java
- **Architecture**: MVVM
- **Authentication**: Biometric API + EncryptedSharedPreferences
- **Networking**: Retrofit 2
- **Database**: Room
- **Charts**: MPAndroidChart
- **Notifications**: Firebase Cloud Messaging
- **DI**: Manual Dependency Injection

## Prerequisites 📋

- Android Studio Flamingo or later
- Android SDK 34
- Java 11+
- Firebase project with FCM enabled

## Installation ⚙️

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/prices-tracker.git
   ```

2. Add your `google-services.json` to `app/` directory

3. Configure API endpoints in:
   ```java
   com.example.pricestracker.utils.Constants
   ```

4. Build and run in Android Studio

## API Configuration 🌐

The app uses these endpoints:

- `POST https://api.prepstripe.com/login`
  ```json
  {
    "username": "admin",
    "password": "A7ge#hu&dt(wer"
  }
  ```

- `GET https://api.prepstripe.com/prices` (requires auth token)

## Notification Setup 🔔

To test notifications:

1. Subscribe to topics:
   ```java
   FirebaseMessaging.getInstance().subscribeToTopic("price_updates");
   ```

2. Send test notification from Firebase Console

## Build Variants 🏗️

- **Debug**: Full logging enabled
- **Release**: Proguard/R8 optimized

## Contributing 🤝

Pull requests are welcome! For major changes, please open an issue first.

## Acknowledgements 🙏

- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) for chart visualization
- [Firebase](https://firebase.google.com) for cloud messaging
- [Material Components](https://material.io) for UI components
```
