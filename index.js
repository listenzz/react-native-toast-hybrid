import { NativeModules, DeviceEventEmitter, Platform } from 'react-native';

const HUDModule = NativeModules.HUD;

export class LoadingHUD {
  hud = null;
  loadingCount = 0;

  show(text) {
    if (this.loadingCount <= 0) {
      this.hud = new HUD();
      this.hud.onDismiss = () => {
        this.hide();
      };
      this.loadingCount = 0;
    }
    this.hud.show(text);
    this.loadingCount++;
  }

  hide() {
    this.loadingCount--;
    if (this.loadingCount <= 0) {
      this.hideAll();
    }
  }

  hideAll() {
    if (this.hud) {
      this.hud.hide();
      this.hud = null;
      this.loadingCount = 0;
    }
  }
}

export default class HUD {
  /**
   * ```backgroundColor: '#BB000000',
   * tintColor: '#FFFFFF',
   * cornerRadius: 5, // only for android
   * duration: 2000,
   * graceTime: 3000,
   * minShowTime: 800,
   * loadingText: '加载中...',```
   * @param {*} options
   */
  static config(options = {}) {
    HUDModule.config(options);
  }
  constructor() {
    this.promise = HUDModule.create();
    if (Platform.OS === 'android') {
      DeviceEventEmitter.addListener('ON_HUD_DISMISS', this.handleDismission);
    }
  }

  handleDismission = event => {
    this.promise.then(hudKey => {
      if (event.hudKey === hudKey) {
        DeviceEventEmitter.removeListener('ON_HUD_DISMISS', this.handleDismission);
        if (this.onDismiss) {
          this.onDismiss();
        }
      }
    });
  };

  show(text) {
    this.promise
      .then(hudKey => {
        HUDModule.show(hudKey, text);
      })
      .catch(e => {
        /*swallow*/
      });
    return this;
  }

  hide() {
    this.promise
      .then(hudKey => {
        HUDModule.hide(hudKey);
      })
      .catch(e => {
        /*swallow*/
      });
  }

  hideDelay(delayMs = 0) {
    this.promise
      .then(hudKey => {
        HUDModule.hideDelay(hudKey, delayMs);
      })
      .catch(e => {
        /*swallow*/
      });
  }

  hideDelayDefault() {
    this.promise
      .then(hudKey => {
        HUDModule.hideDelayDefault(hudKey);
      })
      .catch(e => {
        /*swallow*/
      });
  }

  text(text) {
    this.promise
      .then(hudKey => {
        HUDModule.text(hudKey, text);
      })
      .catch(e => {
        /*swallow*/
      });
    return this;
  }

  info(text) {
    this.promise
      .then(hudKey => {
        HUDModule.info(hudKey, text);
      })
      .catch(e => {
        /*swallow*/
      });
    return this;
  }

  done(text) {
    this.promise
      .then(hudKey => {
        HUDModule.done(hudKey, text);
      })
      .catch(e => {
        /*swallow*/
      });
    return this;
  }

  error(text) {
    this.promise
      .then(hudKey => {
        HUDModule.error(hudKey, text);
      })
      .catch(e => {
        /*swallow*/
      });
    return this;
  }
}
