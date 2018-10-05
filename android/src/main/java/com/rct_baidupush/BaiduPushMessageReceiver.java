package com.rct_baidupush;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushMessageReceiver;

import org.json.JSONObject;

import java.util.List;

public class BaiduPushMessageReceiver extends PushMessageReceiver {


  @Override
  public void onBind(Context context, int errorCode, String appid,
                     String userId, String channelId, String requestId) {

    RNBssBaidupushModule.channelId = channelId;

  }

  @Override
  public void onUnbind(Context context, int errorCode, String s) {

  }

  @Override
  public void onSetTags(Context context, int errorCode, List<String> list, List<String> list1, String s) {

  }

  @Override
  public void onDelTags(Context context, int errorCode, List<String> list, List<String> list1, String s) {
  }

  @Override
  public void onListTags(Context context, int i, List<String> list, String s) {

  }

  @Override
  public void onMessage(Context context, String message, String customContentString) {

  }
  @Override
  public void onNotificationClicked(Context context,final String title, final String description, final String customContentString) {

    String packageName = context.getApplicationContext().getPackageName();
    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity(launchIntent);
    android.os.Handler handler = new android.os.Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        RNBssBaidupushModule.myPush.sendMsg(title,description,customContentString,RNBssBaidupushModule.DidOpenMessage);
      }
    }, 1000);

  }

  @Override
  public void onNotificationArrived(Context context, String title, String description, String customContentString) {
    if(!isAppIsInBackground(context)){
      RNBssBaidupushModule.myPush.sendMsg(title,description,customContentString,RNBssBaidupushModule.DidReceiveMessage);

    }
  }

  private boolean isAppIsInBackground(Context context) {
    boolean isInBackground = true;
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
      List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
      for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
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
