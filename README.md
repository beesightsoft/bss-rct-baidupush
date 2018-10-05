
# rct-baidupush

## Getting started

`$ npm install @beesight/rct-baidupush --save`

### Mostly automatic installation

`$ react-native link @beesight/rct-baidupush`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `@beesight/rct-baidupush` and add `RNBssBaidupush.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNBssBaidupush.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Open up your `AppDelegate.m` 
  - Add '#import "RNBssBaidupush.h"'
  - Register
    + Replace your_api_key in `your_api_key`
    + Choose mode you want: BPushModeDevelopment or BPushModeProduction in `baidu_Mode`
  ```
  - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
     {
     .....
       [RNBssBaidupush registerBaidu:launchOptions application:application apiKey:@"your_api_key" baiduMode:@"baidu_Mode"];
       return YES;
      }
      
      //This method is triggered when the user clicks on the notification, the application is in the foreground or the background is turned on and the application is in the background.
      - (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler
      {
        NSLog(@"Receive mess:%@",userInfo);
        [RNBssBaidupush application:application didReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];
      
      }
      
      // In the iOS8, you also need to add this method. Sign up for push services with the new API
      - (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
      {
        [application registerForRemoteNotifications];
      
      }
      
      //Register deviceToken
      - (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
      {
      
        [RNBssBaidupush application:application didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
      
      }
      
      //When the DeviceToken acquisition fails, the system will call back this method.
      - (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
      {
        [RNBssBaidupush application:application didFailToRegisterForRemoteNotificationsWithError:error];
      }
      
      
      - (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
      {
        NSLog(@"userInfo: %@",userInfo);
        if (application.applicationState == UIApplicationStateActive || application.applicationState == UIApplicationStateBackground) {
          NSLog(@"acitve or background");
          /*UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Error display content" message:@"Error connecting to server, no local database" preferredStyle:UIAlertControllerStyleAlert];
          [self presentViewController:_alertController animated:YES completion:nil];*/
        }
      }
      
      // Show notification when app is foreground
      -(void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions options))completionHandler
      {
        completionHandler(UNAuthorizationOptionSound | UNAuthorizationOptionAlert | UNAuthorizationOptionBadge);
        NSLog(@"Userinfo %@",notification.request.content.userInfo);
      }
   ```

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.rct_baidupush.RNBssBaidupushPackage;` to the imports at the top of the file
  - Add `new RNBssBaidupushPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':rct-baidupush'
  	project(':rct-baidupush').projectDir = new File(rootProject.projectDir, 	'../node_modules/@beesight/rct-baidupush/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':rct-baidupush')
  	```
4. Open up `android/app/src/main/AndroidManifest.xml` add permissions
      ```
    <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    
      <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
      <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
      <uses-permission android:name="baidu.push.permission.WRITE_PUSHINFOPROVIDER.com.eflink.mobile"/>
      <uses-permission android:name="android.permission.READ_PHONE_STATE" />
      <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
      <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
      <uses-permission android:name="android.permission.WRITE_SETTINGS" />
      <uses-permission android:name="android.permission.VIBRATE" />
      <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
      <uses-permission android:name="android.permission.ACCESS_DOWNLOAD_MANAGER" />
      <uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" />
      <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
      <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
      <uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />
    ```
    
    Add permissions to access push notification (replace your app package):
    ```
    <permission
           android:name="baidu.push.permission.WRITE_PUSHINFOPROVIDER.your_app_package"
           android:protectionLevel="normal"> 
     </permission>
     ```
     Under `<application>` (replace your app package)
     ``` 
      <provider
             android:name="com.baidu.android.pushservice.PushInfoProvider"
             android:authorities="com.eflink.mobile.bdpush"
             android:writePermission="baidu.push.permission.WRITE_PUSHINFOPROVIDER.your_app_package"
             android:protectionLevel = "signature"
             android:exported="true" />
      ```
      ```
       <meta-data
                 android:name="api_key"
                 android:value="enter_your_api_key" />
      ```
## Usage
```javascript
      import RNBssBaidupush from '@beesight/rct-baidupush';

```

```
      // when receive a message
      RNBssBaidupush.monitorReceiveMessage((message) => {
         console.log('monitorReceiveMessage=' + JSON.stringify(message))
       })
       
       // when click on message
       RNBssBaidupush.monitorBackstageOpenMessage((message) => {
         console.log('monitorClickMessage=' + JSON.stringify(message))
       })
       
       // get ChannelId
       RNBssBaidupush.getChannelId().then((ChannelId) => {
         if (!isNil(ChannelId)) { this.setState({tokenId: ChannelId}) }
         console.log('ChannelId=' + ChannelId)
       })
   ```
