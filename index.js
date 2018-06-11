import { NativeModules } from 'react-native';

const HUDModule = NativeModules.HUD;

export default class HUD {
  /**
   * ```backgroundColor: '#BB000000',
   * tintColor: '#FFFFFF',
   * cornerRadius: 5, // only for android
   * duration: 2000,
   * graceTime: 3000,
   * minShowTime: 800,
   * dimAmount: 0.0, // only for andriod
   * loadingText: '加载中...',```
   * @param {*} options
   */
  static config(options = {}) {
    HUDModule.config(options);
  }

  static showLoading() {
    return new HUD(HUDModule.showLoading());
  }

  static text(text) {
    HUDModule.text(text);
  }

  static info(text) {
    HUDModule.info(text);
  }

  static done(text) {
    HUDModule.done(text);
  }

  static error(text) {
    HUDModule.error(text);
  }

  constructor(promise) {
    this.promise = promise;
  }

  // instance methoed
  hideLoading() {
    this.promise
      .then(hudKey => {
        HUDModule.hideLoading(hudKey);
      })
      .catch(e => {
        /*swallow*/
      });
  }
}
