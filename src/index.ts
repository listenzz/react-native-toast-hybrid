import { useRef, useEffect } from 'react'
import { NativeModules, BackHandler } from 'react-native'
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

let defaultDuration = 2000

export default class Toast {
  static config(options: ToastConfig = {}) {
    defaultDuration = options.duration || defaultDuration
    ToastHybrid.config(options)
  }

  static text(text: string, duration = defaultDuration) {
    new Toast().text(text, duration)
  }

  static info(text: string, duration = defaultDuration) {
    new Toast().info(text, duration)
  }

  static done(text: string, duration = defaultDuration) {
    new Toast().done(text, duration)
  }

  static error(text: string, duration = defaultDuration) {
    new Toast().error(text, duration)
  }

  static loading(text?: string) {
    return new Toast().loading(text)
  }

  private underlying: Promise<number> | null = null

  private ensure(): Promise<number> {
    if (this.underlying !== null) {
      return this.underlying
    }
    const underlying = ToastHybrid.create()
    return underlying
  }

  private closed = false
  private timer: number | null = null

  loading(text?: string) {
    if (!this.closed) {
      this.clearTimeout()
      this.underlying = this.ensure()
      this.underlying.then((key) => {
        this.clearTimeout()
        ToastHybrid.loading(key, text)
      })
    }
    return this
  }

  private clearTimeout() {
    if (this.timer !== null) {
      clearTimeout(this.timer)
      this.timer = null
    }
  }

  text(text: string, duration = defaultDuration) {
    return this.show(ToastHybrid.text, text, duration)
  }

  private show(fn: (key: number, text: string) => void, text: string, duration: number) {
    if (!this.closed) {
      this.clearTimeout()
      this.underlying = this.ensure()
      this.underlying.then((key) => {
        if (!this.closed) {
          fn(key, text)
          this.clearTimeout()
          this.timer = <any>setTimeout(() => this.hide(), duration)
        } else {
          this.hide()
        }
      })
    }
    return this
  }

  info(text: string, duration = defaultDuration) {
    return this.show(ToastHybrid.info, text, duration)
  }

  done(text: string, duration = defaultDuration) {
    return this.show(ToastHybrid.done, text, duration)
  }

  error(text: string, duration = defaultDuration) {
    return this.show(ToastHybrid.error, text, duration)
  }

  hide() {
    this.clearTimeout()
    if (this.underlying !== null) {
      this.underlying.then((key) => {
        ToastHybrid.hide(key)
      })
      this.underlying = null
    }
  }

  private shutdown() {
    this.closed = true
    this.hide()
  }
}

export function useToast() {
  const toastRef = useRef(new Toast())
  useEffect(() => {
    const toast = toastRef.current
    ;(toast as any).closed = false
    return () => {
      ;(toast as any).shutdown()
    }
  }, [])

  useEffect(() => {
    function handleHardwareBack() {
      const toast = toastRef.current
      if ((toast as any).underlying !== null) {
        toast.hide()
        return true
      }
      return false
    }

    BackHandler.addEventListener('hardwareBackPress', handleHardwareBack)

    return () => {
      BackHandler.removeEventListener('hardwareBackPress', handleHardwareBack)
    }
  }, [])

  return toastRef.current
}
