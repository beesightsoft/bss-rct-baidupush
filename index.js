import { DeviceEventEmitter, NativeAppEventEmitter, NativeModules, Platform } from 'react-native'

const { RNBssBaidupush } = NativeModules
class BaiDuPush {
  constructor () {
    this.ReceiveMessageObj = null
    this.BackstageMessageObj = null
  }
  monitorReceiveMessage (callBack) {
    if (Platform.OS === 'ios') {
      this.ReceiveMessageObj = NativeAppEventEmitter.addListener(
        RNBssBaidupush.DidReceiveMessage, callBack)
    } else {
      this.ReceiveMessageObj = DeviceEventEmitter.addListener(
        RNBssBaidupush.DidReceiveMessage, (data) => {
          let obj = {}
          obj.title = data.title
          obj.description = data.description
          obj.customContentString = JSON.parse(data.customContentString)
          callBack(obj)
        })
    }
  }

  monitorBackstageOpenMessage (callBack) {
    if (Platform.OS === 'ios') {
      this.BackstageMessageObj = NativeAppEventEmitter.addListener(
        RNBssBaidupush.DidOpenMessage, callBack)
    } else {
      this.BackstageMessageObj = DeviceEventEmitter.addListener(
        RNBssBaidupush.DidOpenMessage, (data) => {
          let obj = {}
          obj.title = data.title
          obj.description = data.description
          obj.customContentString = JSON.parse(data.customContentString)
          callBack(obj)
        })
    }
  }

  monitorMessageCancel () {
    if (this.ReceiveMessageObj) {
      this.ReceiveMessageObj.remove()
    }

    if (this.BackstageMessageObj) {
      this.BackstageMessageObj.remove()
    }
  }

  async getChannelId () {
    try {
      return await RNBssBaidupush.getChannelId()
    } catch (e) {
      return null
    }
  }

  testSend () {
    RNBssBaidupush.testPrint('hello world')
  }
}

export default new BaiDuPush()
