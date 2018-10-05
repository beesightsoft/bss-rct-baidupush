package com.rct_baidupush;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushMessageReceiver;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by qiepeipei on 16/8/13.
 */
public class MyPushMessageReceiver extends PushMessageReceiver {


  @Override
  public void onBind(Context context, int errorCode, String appid,
                     String userId, String channelId, String requestId) {

    String responseString = "onBind errorCode=" + errorCode + " appid="
      + appid + " userId=" + userId + " channelId=" + channelId
      + " requestId=" + requestId;
    Log.d("responseString", responseString);

    RNBssBaidupushModule.channelId = channelId;

  }

  @Override
  public void onUnbind(Context context, int errorCode, String s) {
    Log.d("百度推送", "onUnbind");
  }

  @Override
  public void onSetTags(Context context, int errorCode, List<String> list, List<String> list1, String s) {
    Log.d("百度推送", "onSetTags");


  }

  @Override
  public void onDelTags(Context context, int errorCode, List<String> list, List<String> list1, String s) {
    Log.d("百度推送", "onDelTags");


  }

  @Override
  public void onListTags(Context context, int i, List<String> list, String s) {
    Log.d("百度推送", "onListTags");
  }

  //接收透传消息
    /*
    *   context 上下文
        message 推送的消息
        customContentString 自定义内容，为空或者json字符串
    * */
  @Override
  public void onMessage(Context context, String message, String customContentString) {
    Log.d("Baidu push", "Receive a notification bar");

  }

  /*接收通知点击的函数

  *   context 上下文
      title 推送的通知的标题
      description 推送的通知的描述
      customContentString 自定义内容，为空或者json字符串
  * */
  @Override
  public void onNotificationClicked(Context context,final String title, final String description, final String customContentString) {

    Log.d("Baidupush", "Clicked on the notification bar");
    String packageName = context.getApplicationContext().getPackageName();
    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    Log.d("Baidupush", packageName);
    context.startActivity(launchIntent);
    android.os.Handler handler = new android.os.Handler();
    //发送通知
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        Log.d("Baidupush","'postDelayed'");
        RNBssBaidupushModule.myPush.sendMsg(title,description,customContentString,RNBssBaidupushModule.DidOpenMessage);
      }
    }, 1000);

  }

  @Override
  public void onNotificationArrived(Context context, String title, String description, String customContentString) {
    Log.d("百度推送", "收到通知消息");
    if(!isAppIsInBackground(context)){
      //发送通知
      RNBssBaidupushModule.myPush.sendMsg(title,description,customContentString,RNBssBaidupushModule.DidReceiveMessage);

    }
  }

  //判断是否在后台
  private boolean isAppIsInBackground(Context context) {
    boolean isInBackground = true;
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
      List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
      for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
        //前台程序
        if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
          for (String activeProcess : processInfo.pkgList) {
            if (activeProcess.equals(context.getPackageName())) {
              isInBackground = false;
            }
          }
        }
      }
    } else {
      List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
      ComponentName componentInfo = taskInfo.get(0).topActivity;
      if (componentInfo.getPackageName().equals(context.getPackageName())) {
        isInBackground = false;
      }
    }

    return isInBackground;
  }


}
