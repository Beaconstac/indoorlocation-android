# Beaconstac Indoor Navigation SDK for Android

You will need an API key for this SDK service to work. Please email dan.egan@beaconstac.com to get an API key.

## Integration with your Application project in Android Studio

### In the `build.gradle` file of the app, add the following in the dependencies section:

#### - This SDK uses `Google Play Services 10.2.6`.

```groovy
dependencies {
    …
    compile 'com.mobstac.beaconstac.IndoorLocation:0.5'
}
```

##### Latest version

[ ![Download](https://api.bintray.com/packages/mobstac/maven/IndoorLocation/images/download.svg) ](https://bintray.com/mobstac/maven/IndoorLocation/_latestVersion) 

### Proguard rules

Add the following lines to `proguard-rules.pro`

```
-dontwarn com.fasterxml.jackson.**
-keep class com.mobstac.beaconstac.IndoorLocation.** { *; }
```


## Permissions
#### Beaconstac SDK requires the following permissions.
```xml
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

It is not necessary to explicitly add these permissions to your app. They will be added automatically when you include the SDK.

### Runtime permissions
Since Android 6.0 Android has introduced the concept of runtime permissions. Beaconstac SDK requires one runtime permission - 

#### Location
Beaconstac requires the location permission to perform Bluetooth scanning. The Android OS does not allow bluetooth scan without location permission. Also the user's location is required to sync Beacon and Location data for navigation.

## Usage

#### • Initialise the SDK with your API key (preferably in the Application class)

##### Note: The API key is tied to the package name of your app and will not work with another app.

```java
BeaconstacIndoorLocation.initialise(this, "MY_API_KEY", new AuthListener() {
    @Override
    public void onComplete(BeaconstacNavException e) {
        if (e == null)
            Log.d("Beaconstac", "Initialised");
        else
            Log.d("Beaconstac", "Initialization failed");
    }
});
```

#### • Get BeaconstacIndoorLocation instance

```java
BeaconstacIndoorLocation beaconstacIndoorLocation = BeaconstacIndoorLocation.getInstance();
```

#### • Requirements for starting navigation/tracking
 
 - Android 4.3 and above
 - BLE Hardware support
 - Bluetooth enabled
 - Location enabled
 - Location permission

#### • Starting navigation.

Navigation can be used in two ways

##### Option 1

You can call a method to start indoor navigation. This will start an activity which is bundled with the SDK. It uses Open Street Maps to show the current location of the user.
This method does not provide any callbacks, so user tracking is not possible. 

```java
BeaconstacIndoorLocation.getInstance().startNavigation(this, new ErrorListener() {
    @Override
    public void onError(BeaconstacNavException e) {
        // Will throw exceptions if requirements for starting navigation are not met
        // No callback will be given if Navigation started successfully
        e.printStackTrace();
    }
});
```

##### Option 2

You can attach Beaconstac SDK's Navigation fragment to your activity for Indoor navigation. This uses Open Street Maps to show the current location of the user.

##### 1. Add the fragment to the layout.xml
 
 ```xml
 <fragment
         android:id="@+id/beaconstac_navigation_fragment"
         android:name="com.mobstac.beaconstac.indoorlocation.BeaconstacNavigation"
         android:layout_width="match_parent"
         android:layout_height="match_parent" />
 ```

##### 2. Make your activity implement *LocationCallBack*

This will provide callbacks when the user's position changes

```java
public class MyNavigationActivity extends AppCompatActivity implements LocationCallBack {

    …
    
    @Override
    public void onBeaconProximity(Beacon beacon) {
        // The user is close to this beacon
        // You can get the position name and location coordinates
    }

    @Override
    public void onRegionExit() {
        // The user has exited the area where beacons were placed for navigation
    }

    @Override
    public void onError(BeaconstacNavException exception) {
        // Requirements for starting tracking are not met
    }

    @Override
    public void onRegionEntry(Place place) {
        // The user has entered a place where beacon navigation is suppported.
        // You can get the place name and coordinates
    }

}
```

##### 3. Get indoor navigation instance
     
     ```java
     BeaconstacNavigation.BeaconstacIndoorNavigation beaconstacIndoorNavigation = 
     ((BeaconstacNavigation) getSupportFragmentManager()
                     .findFragmentById(R.id.beaconstac_navigation_fragment))
                     .getIndoorNavigationInstance();
     ```
 
 ##### 4. Get indoor navigation instance
 
 ```java
 BeaconstacNavigation.BeaconstacIndoorNavigation beaconstacIndoorNavigation = 
 ((BeaconstacNavigation) getSupportFragmentManager()
                 .findFragmentById(R.id.beaconstac_navigation_fragment))
                 .getIndoorNavigationInstance();
 ```
 
 ##### 5. Start navigation
 ```java
 beaconstacIndoorNavigation.startNavigation();
 ```
 
 ##### 6. Stop navigation
  ```java
  beaconstacIndoorNavigation.stopNavigation();
  ```
 

#### • User tracking

This method allows to track the user's movement, without showing the map. The Beaconstac SDK will deliver callbacks when the user's position changes.

```java
BeaconstacIndoorLocation.getInstance().startTracking(this, new LocationCallBack() {
    @Override
    public void onBeaconProximity(Beacon beacon) {
        // The user is close to this beacon
        // You can get the place name and location coordinates
    }

    @Override
    public void onRegionExit() {
        // The user has exited the area where beacons were placed for navigation
    }

    @Override
    public void onError(BeaconstacNavException exception) {
        // Requirements for starting tracking are not met
    }
    
    @Override
    public void onRegionEntry(Place place) {
        // The user has entered a place where beacon navigation is suppported.
        // You can get the place name and coordinates
    }
        
});
```

#### • Stop tracking

This method will stop tracking user's movements
  
```java
BeaconstacIndoorLocation.getInstance().stopTracking();
```

#### • Change signal filtering mode

Changing the filtering mode will have an impact on how fast the user's location can update by scanning beacons. This also has an impact on the stability of the position.
##### [Read more about Signal filter modes](media/FilterModes.md)
  
```java
BeaconstacIndoorLocation.getInstance().setFilteringMode(FilterMode.ARMA);
```

#### • Change update speed for ARMA filter

This method will change the speed with which location data updates when using ARMA filter.
Higher values will mean that the position updates faster but it will also get a little more unstable.
This method expects a value between 0.1 - 0.5. If the filtering mode has been changed to anything other than ARMA, this method call will be ignored.

```java
BeaconstacIndoorLocation.getInstance().setArmaFilterUpdateSpeed(0.18D);
```