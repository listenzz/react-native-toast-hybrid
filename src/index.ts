import { NativeModules } from 'react-native'
const { ToastHybrid } = NativeModules

export interface ToastConfig {
  backgroundColor?: string
  tintColor?: string
  cornerRadius?: number
  duration?: number
  graceTime?: number
  minShowTime?: number
  loadingText?: string
}

export default class Toast {
  static config(options: ToastConfig = {}) {
    ToastHybrid.config(options)
  }

  static text(text: string) {
    new Toast().text(text).hideDelayDefault()
  }

  static info(text: string) {
    new Toast().info(text).hideDelayDefault()
  }

  static done(text: string) {
    new Toast().done(text).hideDelayDefault()
  }

  static error(text: string) {
    new Toast().error(text).hideDelayDefault()
  }

  static loading(text?: string) {
    return new Toast().loading(text)
  }

  private promise: Promise<string>

  constructor() {
    this.promise = ToastHybrid.create()
  }

  loading(text?: string) {
    this.promise.then(key => {
      ToastHybrid.loading(key, text)
    })
    return this
  }

  text(text: string) {
    this.promise.then(key => {
      ToastHybrid.text(key, text)
    })
    return this
  }

  info(text: string) {
    this.promise.then(key => {
      ToastHybrid.info(key, text)
    })
    return this
  }

  done(text: string) {
    this.promise.then(key => {
      ToastHybrid.done(key, text)
    })
    return this
  }

  error(text: string) {
    this.promise.then(key => {
      ToastHybrid.error(key, text)
    })
    return this
  }

  hide() {
    this.promise.then(key => {
      ToastHybrid.hide(key)
    })
  }

  hideDelay(delayMs = 0) {
    this.promise.then(key => {
      ToastHybrid.hideDelay(key, delayMs)
    })
  }

  hideDelayDefault() {
    this.promise.then(key => {
      ToastHybrid.hideDelayDefault(key)
    })
  }
}
