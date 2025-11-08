package com.reactnative.toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.BaseReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.module.model.ReactModuleInfoProvider;

import java.util.HashMap;
import java.util.Map;

public class ToastHybridPackage extends BaseReactPackage {

    @Nullable
    @Override
    public NativeModule getModule(@NonNull String name, @NonNull ReactApplicationContext reactApplicationContext) {
        if (ToastHybridModule.NAME.equals(name)) {
            return new ToastHybridModule(reactApplicationContext);
        }
        return null;
    }

    @NonNull
    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return new ReactModuleInfoProvider() {
            @NonNull
            @Override
            public Map<String, ReactModuleInfo> getReactModuleInfos() {
                Map<String, ReactModuleInfo> map = new HashMap<>();
                map.put(ToastHybridModule.NAME, new ReactModuleInfo(
                    ToastHybridModule.NAME,       // name
                    ToastHybridModule.NAME,       // className
                    false, // canOverrideExistingModule
                    false, // needsEagerInit
                    false, // isCXXModule
                    true   // isTurboModule
                ));

                return map;
            }
        };
    }
}