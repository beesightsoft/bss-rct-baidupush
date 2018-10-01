
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNBssBaidupush : NSObject <RCTBridgeModule>
+ (void)registerBaidu:(NSDictionary *)launchOptions application:(UIApplication *)application apiKey:(NSString *)apiKey baiduMode:(NSString *)baiduMode;

+ (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;
+ (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler;

+ (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error;

@property (nonatomic, copy) id bPush;
@end
  
