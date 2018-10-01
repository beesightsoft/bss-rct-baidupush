
#import "RNBssBaidupush.h"
#import <AudioToolbox/AudioToolbox.h>
#import <Foundation/Foundation.h>
#import <React/RCTEventDispatcher.h>
#import "BPush.h"
#import <React/RCTEventDispatcher.h>

#import <UserNotifications/UserNotifications.h>
#import <React/RCTLog.h>
static NSString * const DidReceiveMessage = @"DidReceiveMessage";
static NSString * const DidOpenMessage = @"DidOpenMessage";
static BOOL isBackGroundActivateApplication;
static RNBssBaidupush *_instance = nil;

@implementation RNBssBaidupush
@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()
+ (instancetype)sharedInstance {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if(_instance == nil) {
            
            _instance = [[self alloc] init];
        }
    });
    return _instance;
}
+ (instancetype)allocWithZone:(struct _NSZone *)zone {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if(_instance == nil) {
            
            _instance = [super allocWithZone:zone];
            
        }
    });
    return _instance;
}
+ (void)registerBaidu:(NSDictionary *)launchOptions application:(UIApplication *)application apiKey:(NSString *)apiKey baiduMode:(NSString *)baiduMode {
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 10.0) {
#ifdef NSFoundationVersionNumber_iOS_9_x_Max
        UNUserNotificationCenter* center = [UNUserNotificationCenter currentNotificationCenter];
        
        [center requestAuthorizationWithOptions:(UNAuthorizationOptionAlert + UNAuthorizationOptionSound + UNAuthorizationOptionBadge)
                              completionHandler:^(BOOL granted, NSError * _Nullable error) {
                                  // Enable or disable features based on authorization.
                                  if (granted) {
                                      [application registerForRemoteNotifications];
                                  }
                              }];
#endif
    }
    else if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0) {
        UIUserNotificationType myTypes = UIUserNotificationTypeBadge | UIUserNotificationTypeSound | UIUserNotificationTypeAlert;
        
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:myTypes categories:nil];
        [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
    }else {
        UIRemoteNotificationType myTypes = UIRemoteNotificationTypeBadge|UIRemoteNotificationTypeAlert|UIRemoteNotificationTypeSound;
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:myTypes];
    }
    
#warning: When testing the development environment, you need to modify BPushMode to BPushModeDevelopment. You need to modify Apikey to be your own Apikey.
    
    // To register Baidu cloud push service when the app starts, you need to provide Apikey, pushMode
    
    [BPush registerChannel:launchOptions apiKey:apiKey pushMode:baiduMode withFirstAction:@"打开" withSecondAction:@"关闭" withCategory:@"test" useBehaviorTextInput:YES isDebug:YES];
    
    // App is the user launching by clicking on the push message
    NSDictionary *userInfo = [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey];
    if (userInfo) {
        NSLog(@"userInfo:%@",userInfo);
        [BPush handleNotification:userInfo];
    }
#if TARGET_IPHONE_SIMULATOR
    Byte dt[32] = {0xc6, 0x1e, 0x5a, 0x13, 0x2d, 0x04, 0x83, 0x82, 0x12, 0x4c, 0x26, 0xcd, 0x0c, 0x16, 0xf6, 0x7c, 0x74, 0x78, 0xb3, 0x5f, 0x6b, 0x37, 0x0a, 0x42, 0x4f, 0xe7, 0x97, 0xdc, 0x9f, 0x3a, 0x54, 0x10};
    [self application:application didRegisterForRemoteNotificationsWithDeviceToken:[NSData dataWithBytes:dt length:32]];
#endif
    //Corner mark clear
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
}

+ (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    NSLog(@"test device token:%@",deviceToken);
    [BPush registerDeviceToken:deviceToken];
    [BPush bindChannelWithCompleteHandler:^(id result, NSError *error) {
        // The settag listtag deletetag unbind operation needs to be performed after the binding is successful, otherwise it will fail.
        
        // Network Error
        if (error) {
            NSLog(@"didRegisterForRemoteNotificationsWithDeviceToken error %@",error);
            return ;
        }
        if (result) {
            NSLog(@"didRegisterForRemoteNotificationsWithDeviceToken ok %@",result);
            // Confirm that the binding is successful
            if ([result[@"error_code"]intValue]!=0) {
                return;
            }
            // Get channel_id
            NSString *myChannel_id = [BPush getChannelId];
            NSLog(@"Chanel_id is %@",myChannel_id);
            
            [BPush listTagsWithCompleteHandler:^(id result, NSError *error) {
                if (result) {
                    NSLog(@"result bpush %@",result);
                }
            }];
            [BPush setTag:@"Mytag" withCompleteHandler:^(id result, NSError *error) {
                if (result) {
                    NSLog(@"Set the tag successfully");
                }
            }];
        }
    }];
    NSLog(@"testend:%@",deviceToken);
}

- (NSDictionary<NSString *, id> *)constantsToExport {
    return @{
             DidReceiveMessage: DidReceiveMessage,
             DidOpenMessage: DidOpenMessage,
             };
}

// When the DeviceToken acquisition fails, the system will call back this method.
+ (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
    NSLog(@"didFailToRegisterForRemoteNotificationsWithError ：%@",error);
}

// This method is triggered when the user clicks on the notification, the application is in the foreground or the background is turned on and the application is in the background.
+ (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler
{
    NSLog(@"The app receives a message");
    completionHandler(UIBackgroundFetchResultNewData);
    // Print to log in textView
    NSLog(@"********** iOS7.0之后 background **********");
    // The application is in the foreground, does not jump to the page, let the user choose.
    if (application.applicationState == UIApplicationStateActive) {
        NSMutableDictionary* contentMap = [[NSMutableDictionary alloc] init];
        NSMutableDictionary* tmpMap = [[NSMutableDictionary alloc] init];
        [contentMap setValue:userInfo[@"aps"][@"alert"] forKey:@"description"];
        for (NSString *key in userInfo) {
            if(![key isEqualToString:@"aps"] && ![key isEqualToString:@""]){
                [tmpMap setValue:userInfo[key] forKey:key];
            }
        }
        [contentMap setValue:tmpMap forKey:@"customContentString"];
        [[RNBssBaidupush sharedInstance] sendMessage:contentMap type:@"1"];
        NSDate *fireDate = [[NSDate new] dateByAddingTimeInterval:5];
        UNUserNotificationCenter* center = [UNUserNotificationCenter currentNotificationCenter];
    }
    //In the kill state, jump directly to the jump page.
    if (application.applicationState == UIApplicationStateInactive && !isBackGroundActivateApplication)
    {
        NSLog(@"The app is in the background and receives a message");
        NSMutableDictionary* contentMap = [[NSMutableDictionary alloc] init];
        NSMutableDictionary* tmpMap = [[NSMutableDictionary alloc] init];
        [contentMap setValue:userInfo[@"aps"][@"alert"] forKey:@"description"];
        for (NSString *key in userInfo) {
            if(![key isEqualToString:@"aps"] && ![key isEqualToString:@""]){
                [tmpMap setValue:userInfo[key] forKey:key];
            }
        }
        [contentMap setValue:tmpMap forKey:@"customContentString"];
        [[RNBssBaidupush sharedInstance] sendMessage:contentMap type:@"2"];
    }
    //The app is in the background. When the background setting aps field has a content-available value of 1 and enable remote notification to activate the app's options
    if (application.applicationState == UIApplicationStateBackground) {
        NSLog(@"background is Activated Application ");
        // Here you can choose to activate the app to download email images and other content in advance.
        isBackGroundActivateApplication = YES;
        //        UIAlertView *alertView =[[UIAlertView alloc]initWithTitle:@"Receive a message" message:userInfo[@"aps"][@"alert"] delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"OK", nil ];
        //        [alertView show];
    }
    
    NSLog(@"end test%@",userInfo);
}

- (void)sendMessage:(NSDictionary *)userInfo type:(NSString*)type
{
    
    if([type isEqualToString:@"1"]){
        
        [self.bridge.eventDispatcher sendAppEventWithName:@"DidReceiveMessage" body:userInfo];
        
    }else{
        
        [self.bridge.eventDispatcher sendAppEventWithName:@"DidOpenMessage" body:userInfo];
    }
}

RCT_EXPORT_METHOD(addEvent:(NSString *)name location:(NSString *)location)
{
    RCTLogInfo(@"Pretending to create an event %@ at %@", name, location);
}

RCT_EXPORT_METHOD(getChannelId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    @try {
        resolve([BPush getChannelId]);
        
    } @catch (NSException *exception) {
        NSError *error = [NSError errorWithDomain:@"Error getting ChannelId！" code:1 userInfo: exception.userInfo];
        reject(exception.name,exception.reason,error);
        
    }
}
@end
  
