import { NativeModules, DeviceEventEmitter, Platform } from "react-native";
const HUDModule = NativeModules.HUD;

export class LoadingHUD {
  private hud: HUD | null = null;
  private loadingCount = 0;

  show(text?: string) {
    if (this.loadingCount <= 0) {
      this.hud = new HUD();
      this.hud.onDismiss = () => {
        this.hide();
      };
      this.loadingCount = 0;
    }
    this.hud!.spinner(text);
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

export interface HUDConfig {
  backgroundColor?: string;
  tintColor?: string;
  cornerRadius?: number;
  duration?: number;
  graceTime?: number;
  minShowTime?: number;
  loadingText?: string;
}

export default class HUD {
  static config(options: HUDConfig = {}) {
    HUDModule.config(options);
  }

  private promise: Promise<string>;
  onDismiss?: () => void;

  constructor() {
    this.promise = HUDModule.create();
    if (Platform.OS === "android") {
      DeviceEventEmitter.addListener("ON_HUD_DISMISS", this.handleDismission);
    }
  }

  spinner(text?: string) {
    this.promise
      .then(hudKey => {
        HUDModule.spinner(hudKey, text);
      })
      .catch(e => {
        /*swallow*/
      });
    return this;
  }

  text(text: string) {
    this.promise
      .then(hudKey => {
        HUDModule.text(hudKey, text);
      })
      .catch(e => {
        /*swallow*/
      });
    return this;
  }

  info(text: string) {
    this.promise
      .then(hudKey => {
        HUDModule.info(hudKey, text);
      })
      .catch(e => {
        /*swallow*/
      });
    return this;
  }

  done(text: string) {
    this.promise
      .then(hudKey => {
        HUDModule.done(hudKey, text);
      })
      .catch(e => {
        /*swallow*/
      });
    return this;
  }

  error(text: string) {
    this.promise
      .then(hudKey => {
        HUDModule.error(hudKey, text);
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

  handleDismission = (event: { hudKey: string }) => {
    this.promise.then(hudKey => {
      if (event.hudKey === hudKey) {
        DeviceEventEmitter.removeListener(
          "ON_HUD_DISMISS",
          this.handleDismission
        );
        if (this.onDismiss) {
          this.onDismiss();
        }
      }
    });
  };
}
