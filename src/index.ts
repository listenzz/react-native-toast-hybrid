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

export default class Toast {
  static config(options: ToastConfig = {}) {
    ToastHybrid.config(options)
  }

  static text(text: string, duration = 2000) {
    new Toast().text(text, duration)
  }

  static info(text: string) {
    new Toast().info(text)
  }

  static done(text: string) {
    new Toast().done(text)
  }

  static error(text: string) {
    new Toast().error(text)
  }

  static loading(text?: string, graceTime?: number) {
    return new Toast().loading(text, graceTime)
  }

  private underlying: Promise<number> | null = null

  private async ensure(): Promise<number> {
    if (this.underlying !== null) {
      const key = await this.underlying
      const underlying = ToastHybrid.ensure(key)
      return underlying
    }
    const underlying = ToastHybrid.create()
    return underlying
  }

  private closed = false
  private timer: number | null = null

  loading(text?: string, graceTime = -1) {
    if (!this.closed) {
      this.clearTimeout()
      this.underlying = this.ensure()
      this.underlying.then(key => {
        this.clearTimeout()
        ToastHybrid.loading(key, text, graceTime)
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

  text(text: string, duration = 2000) {
    return this.show(ToastHybrid.text, text, duration)
  }

  private show(fn: (key: number, text: string) => void, text: string, duration: number) {
    if (!this.closed) {
      this.clearTimeout()
      this.underlying = this.ensure()
      this.underlying.then(key => {
        if (!this.closed) {
          fn(key, text)
          this.clearTimeout()
          this.timer = setTimeout(() => this.hide(), duration)
        } else {
          this.hide()
        }
      })
    }
    return this
  }

  info(text: string, duration = 2000) {
    return this.show(ToastHybrid.info, text, duration)
  }

  done(text: string, duration = 2000) {
    return this.show(ToastHybrid.done, text, duration)
  }

  error(text: string, duration = 2000) {
    return this.show(ToastHybrid.error, text, duration)
  }

  hide() {
    this.clearTimeout()
    if (this.underlying !== null) {
      this.underlying.then(key => {
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
