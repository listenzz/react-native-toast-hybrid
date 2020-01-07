import React, { Component } from 'react'
import { NativeModules } from 'react-native'
const { ToastHybrid } = NativeModules
import hoistNonReactStatics from 'hoist-non-react-statics'

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

  static loading(text?: string) {
    return new Toast().loading(text)
  }

  private underlying: Promise<number> | null = null

  private async ensure(): Promise<number> {
    if (this.underlying !== null) {
      const key = await this.underlying
      const underlying = ToastHybrid.ensure(key)
      this.underlying = underlying
      return underlying
    }
    const underlying = ToastHybrid.create()
    this.underlying = underlying
    return underlying
  }

  private closed = false
  private timer: number | null = null

  loading(text?: string) {
    if (!this.closed) {
      this.clearTimeout()
      this.ensure().then(key => {
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

  text(text: string, duration = 2000) {
    return this.show(ToastHybrid.text, text, duration)
  }

  private show(fn: (key: number, text: string) => void, text: string, duration: number) {
    if (!this.closed) {
      this.clearTimeout()
      this.ensure().then(key => {
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

  shutdown() {
    this.closed = true
    this.hide()
  }
}

/**
 * HOC for Toast
 * @param WrappedComponent
 */
export function withToast(WrappedComponent: React.ComponentType<any>): React.ComponentType<any> {
  class ToastProvider extends Component<any> {
    toast = new Toast()

    constructor(props: any) {
      super(props)
    }

    componentWillUnmount() {
      this.toast.shutdown()
    }

    render() {
      const { forwardedRef, ...props } = this.props
      return <WrappedComponent toast={this.toast} {...props} ref={forwardedRef} />
    }
  }

  const ForwardedComponent = React.forwardRef((props, ref) => {
    return <ToastProvider {...props} forwardedRef={ref} />
  })
  const name = WrappedComponent.displayName || WrappedComponent.name
  ForwardedComponent.displayName = `withToast(${name})`
  hoistNonReactStatics(ForwardedComponent, WrappedComponent)
  return ForwardedComponent
}
