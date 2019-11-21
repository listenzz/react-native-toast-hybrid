import { NativeModules, DeviceEventEmitter, Platform } from 'react-native'
const { HudHybrid } = NativeModules

export class LoadingHUD {
  private hud: HUD | null = null
  private loadingCount = 0

  show(text?: string) {
    if (this.loadingCount <= 0) {
      this.hud = new HUD()
      this.hud.onDismiss = () => {
        this.hideAll()
      }
      this.loadingCount = 0
    }
    this.hud!.spinner(text)
    this.loadingCount++
  }

  hide() {
    this.loadingCount--
    if (this.loadingCount <= 0) {
      this.hideAll()
    }
  }

  hideAll() {
    if (this.hud) {
      this.hud.hide()
      this.hud = null
      this.loadingCount = 0
    }
  }
}

export interface HUDConfig {
  backgroundColor?: string
  tintColor?: string
  cornerRadius?: number
  duration?: number
  graceTime?: number
  minShowTime?: number
  loadingText?: string
}

export default class HUD {
  static config(options: HUDConfig = {}) {
    HudHybrid.config(options)
  }

  static text(text: string) {
    new HUD().text(text).hideDelayDefault()
  }

  static info(text: string) {
    new HUD().info(text).hideDelayDefault()
  }

  static done(text: string) {
    new HUD().done(text).hideDelayDefault()
  }

  static error(text: string) {
    new HUD().error(text).hideDelayDefault()
  }

  static spinner(text?: string) {
    return new HUD().spinner(text)
  }

  private promise: Promise<string>
  onDismiss?: () => void

  constructor() {
    this.promise = HudHybrid.create()
    if (Platform.OS === 'android') {
      DeviceEventEmitter.addListener('ON_HUD_DISMISS', this.handleDismission)
    }
  }

  spinner(text?: string) {
    this.promise.then(hudKey => {
      HudHybrid.spinner(hudKey, text)
    })
    return this
  }

  text(text: string) {
    this.promise.then(hudKey => {
      HudHybrid.text(hudKey, text)
    })
    return this
  }

  info(text: string) {
    this.promise.then(hudKey => {
      HudHybrid.info(hudKey, text)
    })
    return this
  }

  done(text: string) {
    this.promise.then(hudKey => {
      HudHybrid.done(hudKey, text)
    })
    return this
  }

  error(text: string) {
    this.promise.then(hudKey => {
      HudHybrid.error(hudKey, text)
    })
    return this
  }

  hide() {
    this.promise.then(hudKey => {
      HudHybrid.hide(hudKey)
    })
  }

  hideDelay(delayMs = 0) {
    this.promise.then(hudKey => {
      HudHybrid.hideDelay(hudKey, delayMs)
    })
  }

  hideDelayDefault() {
    this.promise.then(hudKey => {
      HudHybrid.hideDelayDefault(hudKey)
    })
  }

  private handleDismission = (event: { hudKey: string }) => {
    this.promise.then(hudKey => {
      if (event.hudKey === hudKey) {
        DeviceEventEmitter.removeListener('ON_HUD_DISMISS', this.handleDismission)
        if (this.onDismiss) {
          this.onDismiss()
        }
      }
    })
  }
}
