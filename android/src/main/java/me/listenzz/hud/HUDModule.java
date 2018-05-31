package me.listenzz.hud;

import android.os.Handler;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.kaopiz.kprogresshud.KProgressHUD;

public class HUDModule extends ReactContextBaseJavaModule {

    private KProgressHUD hud;

    public HUDModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "HUD";
    }

    @ReactMethod
    public void show() {

        if (getCurrentActivity() != null) {
            Log.i("HUD", "Hello HUD!!!");
            hud = KProgressHUD.create(getCurrentActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
            hud.show();
            scheduleDismiss();
        }
    }


    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 2000);
    }

}
