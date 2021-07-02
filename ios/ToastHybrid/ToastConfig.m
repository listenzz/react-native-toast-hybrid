//
//  ToastConfig.m
//  ToastHybrid
//
//  Created by 李生 on 2019/11/21.
//

#import "ToastConfig.h"


@interface DefaultHostViewProvider : NSObject <HostViewProvider>

@end

@implementation DefaultHostViewProvider

- (UIView *)hostView {
    UIApplication *application = [[UIApplication class] performSelector:@selector(sharedApplication)];
    UIViewController *controller = application.keyWindow.rootViewController;
    while (controller.presentedViewController && !controller.presentedViewController.isBeingDismissed) {
        controller = controller.presentedViewController;
    }
    return controller.view;
}

@end


@implementation ToastConfig

+ (instancetype)sharedConfig {
    static ToastConfig *config;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        config = [[ToastConfig alloc] init];
    });
    return config;
}

+ (NSTimeInterval)defaultDuration {
    return 2.0f;
}

+ (NSTimeInterval)defaultGraceTime {
    return 0.3f;
}

+ (NSTimeInterval)defaultMinShowTime {
    return 0.5f;
}

+ (UIColor *)defaultBezelColor {
    return nil;
}

+ (UIColor *)defaultContentColor {
    return nil;
}

+ (CGFloat)defaultFontSize {
    return 0;
}

+ (CGFloat)defaultCornerRadius {
    return 10.f;
}

+ (NSString *)defaultLoadingText {
    return nil;
}

- (instancetype)init {
    if (self = [super init]) {
        _duration = [ToastConfig defaultDuration];
        _graceTime = [ToastConfig defaultGraceTime];
        _minshowTime = [ToastConfig defaultMinShowTime];
        _bezelColor = [ToastConfig defaultBezelColor];
        _contentColor = [ToastConfig defaultContentColor];
        _fontSize = [ToastConfig defaultFontSize];
        _cornerRadius = [ToastConfig defaultCornerRadius];
        _loadingText = [ToastConfig defaultLoadingText];
        _hostViewProvider = [[DefaultHostViewProvider alloc] init];
    }
    return self;
}

@end
