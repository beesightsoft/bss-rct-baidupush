
package com.bss_baidupush;

import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.IllegalViewOperationException;

import java.util.HashMap;
import java.util.Map;


public class RNBssBaidupushModule extends ReactContextBaseJavaModule {

  static public RNBssBaidupushModule myPush;
  static public String DidReceiveMessage = "DidReceiveMessage";
  static public String DidOpenMessage = "DidOpenMessage";
  static public String channelId = "";

  public RNBssBaidupushModule(ReactApplicationContext reactContext) {
    super(reactContext);
    myPush = this;
    this.initialise();
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(DidReceiveMessage, DidReceiveMessage);
    constants.put(DidOpenMessage, DidOpenMessage);
    return constants;
  }

  @Override
  public String getName() {
    return "RNBssBaidupush";
  }

  public void initialise(){
    Log.d("initialise", Utils.getMetaValue(getReactApplicationContext(), "api_key"));
    PushManager.startWork(getReactApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,Utils.getMetaValue(getReactApplicationContext(), "api_key"));
  }

  public void sendMsg(String title,String description,String customContentString,String type){

    Log.d("sendMsg", "sendMsg");
    WritableMap params = Arguments.createMap();
    params.putString("title",title);
    params.putString("description",description);
    params.putString("customContentString",customContentString);

    if(type.equals(DidReceiveMessage)){
      sendEvent(getReactApplicationContext(), DidReceiveMessage, params);
    }else{
      sendEvent(getReactApplicationContext(), DidOpenMessage, params);
    }
  }
  private void sendEvent(ReactContext reactContext,
                         String eventName,
                         @Nullable WritableMap params) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }

  //恢复推送
  @ReactMethod
  public void getChannelId(Promise promise){
    try {
      Log.d("RNMyLibraryModult", "oooooooo");
      Log.d("RNMyLibraryModult", RNBssBaidupushModule.channelId);

      promise.resolve(RNBssBaidupushModule.channelId);

    } catch (IllegalViewOperationException e) {

      promise.reject(e.getMessage());

    }
  }
}
