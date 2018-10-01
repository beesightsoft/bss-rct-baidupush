//
//  BPush.h
//  Version: 1.5.4
//  Baidu cloud push iOS version //
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

extern NSString *const BPushRequestErrorCodeKey;
extern NSString *const BPushRequestErrorMsgKey;
extern NSString *const BPushRequestRequestIdKey;
extern NSString *const BPushRequestAppIdKey;
extern NSString *const BPushRequestUserIdKey;
extern NSString *const BPushRequestChannelIdKey;
extern NSString *const BPushRequestResponseParamsKey; // The original value returned by the server, the content and the above partial values may coincide

/**
 * @brief Callback method name
 *
 */
extern NSString *const BPushRequestMethodBind;
extern NSString *const BPushRequestMethodUnbind;
extern NSString *const BPushRequestMethodSetTag;
extern NSString *const BPushRequestMethodDelTag;
extern NSString *const BPushRequestMethodListTag;

/**
 * @brief Callback method name
 */
typedef NS_ENUM(NSInteger, BPushMode){
    BPushModeDevelopment, // Development test environment
    BPushModeProduction, // AppStore online environment AdHoc internal test production environment
};

/**
 *	@brief BPushCallBack
 *
 *	@discussion Callback to set up asynchronous calls
 */
typedef void (^BPushCallBack)(id result, NSError *error);


@interface BPush : NSObject
/**
 * @brief Register Baidu Cloud Push SDK
   * @param
   * launchOptions - The parameters provided by the system when the app starts, indicating how the app is launched by apiKey - Baidu push registered via apikey, mode - the current push environment, isdebug - whether it is debug mode.
   * iOS 8 new parameters
   * @param rightAction - The first button name of the quick reply notification defaults to opening the app
   * @param leftAction - The second button name will close the app by default. iOS 9 Quick Reply needs to set this parameter first.
   * @param category Custom parameters A unique identifier for a set of actions needs to match the category field of the server aps to display the notification style. iOS 9 Quick Reply needs to set this parameter first.
   * IOS 9 new parameters
   * @param behaviorTextInput Whether to enable iOS 9 quick reply
 */
+ (void)registerChannel:(NSDictionary *)launchOptions apiKey:(NSString *)apikey pushMode:(BPushMode)mode withFirstAction:(NSString *)rightAction withSecondAction:(NSString *)leftAction withCategory:(NSString *)category useBehaviorTextInput:(BOOL)behaviorTextInput isDebug:(BOOL)isdebug;

/**
 * @brief pushes the registration device token to the cloud, which can only be bound after registering deviceToken
 * @param
 *     deviceToken - Obtained via the didRegisterForRemoteNotificationsWithDeviceToken callback in AppDelegate
 * @return
 *     none
 */
+ (void)registerDeviceToken:(NSData *)deviceToken;

/**
 * Set access token. Called before bindChannel, if the access token is changed, it must be reset and re-bindChannel
 * @param
 *     token - Access Token
 * @return
 *     none
 */
+ (void)setAccessToken:(NSString *)token;

/**
 * 关闭 lbs
 * @param
 *      - Turn off lbs push mode, the default is on, the user can choose to close
 * @return
 *     none
 */
+ (void)disableLbs;

/**
 * Turn on BPush crash log collection
 * @param
 *      - Turn on BPush crash log collection If you are not using other third-party crash collection tools, it is recommended to call this interface. BPush will collect crashes caused by BPush SDK itself. It is convenient for SDK to collect known problems and fix problems faster.
 * @return
 *     none
 */
+ (void)uploadBPushCrashLog;


/**
 * @brief Binding channel. will see the channnelid appid userid and so on in the callback.
 * @param
 *     none
 * @return
 *     none
 */


+ (void)bindChannelWithCompleteHandler:(BPushCallBack)handler;

/**
 * @brief unbinds the channel.
 * @param
 *     none
 * @return
 *     none
 */
+ (void)unbindChannelWithCompleteHandler:(BPushCallBack)handler;

/**
 * @brief设置tag。
 * @param
 *     Tag - the tag to be set
 * @return
 *     none
 */
+ (void)setTag:(NSString *)tag withCompleteHandler:(BPushCallBack)handler;

/**
 * @brief sets multiple tags.
 * @param
 *     Tags - the array of tags to be set
 * @return
 *     none
 */
+ (void)setTags:(NSArray *)tags withCompleteHandler:(BPushCallBack)handler;

/**
 * @brief deletes the tag.
 * @param
 *    Tag - the tag to be deleted
 * @return
 *     none
 */
+ (void)delTag:(NSString *)tag withCompleteHandler:(BPushCallBack)handler;

/**
 * @brief delete multiple tags。
 * @param
 *     tags - The array of tags that need to be deleted
 * @return
 *     none
 */
+ (void)delTags:(NSArray *)tags withCompleteHandler:(BPushCallBack)handler;

/**
 * @brief gets a list of tags for the current device app.
 * @param
 *     none
 * @return
 *     none
 */
+ (void)listTagsWithCompleteHandler:(BPushCallBack)handler;

/**
 * @brief is called in didReceiveRemoteNotification for push feedback
 * @param
 *     userInfo
 * @return
 *     none
 */
+ (void)handleNotification:(NSDictionary *)userInfo;

/**
 * @brief is used for iOS 10 request grayscale statistics interface, called in didRegisterForRemoteNotificationsWithDeviceToken, used to count the arrival rate of some grayscale users.
 * @param
 *     isOpen open grayscale request interface
 * @return
 *     none
 */
+ (void)statsGrayInterface:(BOOL)isOpen withAppGroupName:(NSString *)appGroupName withAPPid:(NSString *)appid;

/**
 * @brief gets the app ID, Channel ID, User ID. If the application is not bound, then return empty
 * @param
 *     none
 * @return
 *     appid/channelid/userid
 */
+ (NSString *)getChannelId;
+ (NSString *)getUserId;
+ (NSString *)getAppId;

/**
 * Local push, up to 64
  * @param fireDate Local push trigger time
  * @param alertBody Local push to display content
  * @param badge The number of the corner mark. If you don't need to change the angle mark -1
  * @param alertAction The content displayed by the button of the bulletin box (IOS 8 defaults to "on", others default to "start")
  * @param userInfo custom parameters that can be used to identify push and add additional information
  * @param soundName Custom notification sound, set to nil as the default sound
  
  * IOS8 new parameters
  * @param rightAction - The first button name of the quick reply notification defaults to opening the app
  * @param leftAction - The second button name will close the app by default. iOS 9 Quick Reply needs to set this parameter first.
  * @param region custom parameters
  * @param regionTriggersOnce Custom Parameter Whether local notification is triggered when an area is reached
  * @param category Custom parameters Unique identifier for a set of actions Default is nil iOS 9 Quick reply needs to be set first
  * IOS 9 new parameters
  * @param behaviorTextInput Whether to enable iOS 9 quick reply
 
 */


+ (void)localNotification:(NSDate *)date alertBody:(NSString *)body badge:(int)bage  withFirstAction:(NSString *)rightAction withSecondAction:(NSString *)leftAction userInfo:(NSDictionary *)userInfo soundName:(NSString *)soundName region:(CLRegion *)region regionTriggersOnce:(BOOL)regionTriggersOnce category:(NSString *)category useBehaviorTextInput:(BOOL)behaviorTextInput;

/**
 * Local push is pushed at the front desk. The default App does not pop up when running in the foreground, and the program receives notifications to call this interface to implement the specified push popup.
   * @param notification Local push object
   * @param notificationKey Identifier for local push notifications that need to be displayed in the foreground
 */
+ (void)showLocalNotificationAtFront:(UILocalNotification *)notification identifierKey:(NSString *)notificationKey;
/**
 * Delete local push
 * @param notificationKey Local push identifier
 * @param localNotification Local push object
 */
+ (void)deleteLocalNotificationWithIdentifierKey:(NSString *)notificationKey;
+ (void)deleteLocalNotification:(UILocalNotification *)localNotification;

/**
 * Get the specified notification
 * @param notificationKey Local push identifier
 * @return  Local push object array, when [array count] is 0, it means not found
 */
+ (NSArray *)findLocalNotificationWithIdentifier:(NSString *)notificationKey;

/**
 * Clear all local push objects
 */
+ (void)clearAllLocalNotifications;


@end




