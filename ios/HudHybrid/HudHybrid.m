#import "HudHybrid.h"
#import <React/RCTLog.h>
#import <React/RCTBridge.h>

@interface HudHybrid()

@property(nonatomic, assign) NSInteger hudKeyGenerator;
@property(nonatomic, strong) NSMutableDictionary *huds;

@end

@implementation HudHybrid

RCT_EXPORT_MODULE()

- (instancetype)init {
    if (self = [super init]) {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleReload) name:RCTBridgeWillReloadNotification object:nil];
    }
    return self;
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:RCTBridgeWillReloadNotification object:nil];
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(create:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    if (!_huds) {
        _huds = [[NSMutableDictionary alloc] init];
    }
    UIView *hostView = [self currentHostView];
    if (hostView) {
        Hud *hud = [self createHud];
        NSNumber *hudKey = @(++self.hudKeyGenerator);
        [self.huds setObject:hud forKey:hudKey];
        resolve(hudKey);
    } else {
        resolve(@(-1));
    }
}

RCT_EXPORT_METHOD(spinner:(NSNumber * __nonnull)hudKey text:(NSString *)text) {
    Hud *hud = [self.huds objectForKey:hudKey];
    if (hud) {
        [hud spinner:text ?: [HudConfig sharedConfig].loadingText];
    }
}

RCT_EXPORT_METHOD(hide:(NSNumber* __nonnull)hudKey) {
    Hud *hud = [self.huds objectForKey:hudKey];
    if (hud) {
        [self.huds removeObjectForKey:hudKey];
        [hud hide];
    }
}

RCT_EXPORT_METHOD(hideDelay:(NSNumber* __nonnull)hudKey delay:(NSNumber * __nonnull)ms) {
    Hud *hud = [self.huds objectForKey:hudKey];
    if (hud) {
        [self.huds removeObjectForKey:hudKey];
        [hud hideDelay:[ms floatValue] / 1000.f];
    }
}

RCT_EXPORT_METHOD(hideDelayDefault:(NSNumber* __nonnull)hudKey) {
    Hud *hud = [self.huds objectForKey:hudKey];
    if (hud) {
        [self.huds removeObjectForKey:hudKey];
        [hud hideDefaultDelay];
    }
}

RCT_EXPORT_METHOD(text:(NSNumber * __nonnull)hudKey text:(NSString *)text) {
    Hud *hud = [self.huds objectForKey:hudKey];
    if (hud) {
        [hud text:text];
    }
}

RCT_EXPORT_METHOD(info:(NSNumber * __nonnull)hudKey text:(NSString *)text) {
    Hud *hud = [self.huds objectForKey:hudKey];
    if (hud) {
        [hud info:text];
    }
}

RCT_EXPORT_METHOD(done:(NSNumber * __nonnull)hudKey text:(NSString *)text) {
    Hud *hud = [self.huds objectForKey:hudKey];
    if (hud) {
        [hud done:text];
    }
}

RCT_EXPORT_METHOD(error:(NSNumber * __nonnull)hudKey text:(NSString *)text) {
    Hud *hud = [self.huds objectForKey:hudKey];
    if (hud) {
        [hud error:text];
    }
}

- (void)handleReload {
    if (self.huds) {
        for (NSNumber *key in self.huds) {
            Hud *hud = self.huds[key];
            [hud hide];
        }
        [self.huds removeAllObjects];
    }
}

RCT_EXPORT_METHOD(config:(NSDictionary *)options) {
    if (options[@"backgroundColor"]) {
        [HudConfig sharedConfig].bezelColor = [HudHybrid colorWithHexString:options[@"backgroundColor"]];
    } else {
        [HudConfig sharedConfig].bezelColor = [HudConfig defaultBezelColor];
    }
    
    if (options[@"tintColor"]) {
        [HudConfig sharedConfig].contentColor = [HudHybrid colorWithHexString:options[@"tintColor"]];
    } else {
        [HudConfig sharedConfig].contentColor = [HudConfig defaultContentColor];
    }
    
    if (options[@"cornerRadius"]) {
        [HudConfig sharedConfig].cornerRadius = [options[@"cornerRadius"] floatValue];
    } else {
        [HudConfig sharedConfig].cornerRadius = [HudConfig defaultCornerRadius];
    }
    
    if (options[@"duration"]) {
        [HudConfig sharedConfig].duration = [options[@"duration"] floatValue] / 1000.0f;
    } else {
        [HudConfig sharedConfig].duration = [HudConfig defaultDuration];
    }
    
    if (options[@"graceTime"]) {
        [HudConfig sharedConfig].graceTime = [options[@"graceTime"] floatValue] / 1000.0f;
    } else {
        [HudConfig sharedConfig].graceTime = [HudConfig defaultGraceTime];
    }
    
    if (options[@"minShowTime"]) {
        [HudConfig sharedConfig].minshowTime = [options[@"minShowTime"] floatValue] / 1000.0f;
    } else {
        [HudConfig sharedConfig].minshowTime = [HudConfig defaultMinShowTime];
    }
    
    if (options[@"loadingText"]) {
        [HudConfig sharedConfig].loadingText = options[@"loadingText"];
    } else {
        [HudConfig sharedConfig].loadingText = [HudConfig defaultLoadingText];
    }
}

- (Hud *)createHud {
    return [[Hud alloc] initWithView:[self currentHostView]];
}

- (UIView *)currentHostView {
    UIView *hostView;
    if ([HudConfig sharedConfig].hostViewProvider) {
        hostView = [[HudConfig sharedConfig].hostViewProvider hostView];
    } else {
        UIApplication *application = [[UIApplication class] performSelector:@selector(sharedApplication)];
        UIViewController *controller = application.keyWindow.rootViewController;
        while (controller.presentedViewController && !controller.presentedViewController.isBeingDismissed) {
            controller = controller.presentedViewController;
        }
        hostView = controller.view;
    }
    return hostView;
}

+ (UIColor *) colorWithHexString: (NSString *) hexString {
    NSString *colorString = [[hexString stringByReplacingOccurrencesOfString:@"#" withString:@""] uppercaseString];
    CGFloat alpha, red, green, blue;
    switch ([colorString length]) {
        case 6: // #RRGGBB
            alpha = 1.0f;
            red   = [self colorComponentFrom: colorString start: 0 length: 2];
            green = [self colorComponentFrom: colorString start: 2 length: 2];
            blue  = [self colorComponentFrom: colorString start: 4 length: 2];
            break;
        case 8: // #AARRGGBB
            alpha = [self colorComponentFrom: colorString start: 0 length: 2];
            red   = [self colorComponentFrom: colorString start: 2 length: 2];
            green = [self colorComponentFrom: colorString start: 4 length: 2];
            blue  = [self colorComponentFrom: colorString start: 6 length: 2];
            break;
        default:
            alpha = 1.0f;
            red   = 0.0f;
            green = 0.0f;
            blue  = 0.0f;
            [NSException raise:@"Invalid color value" format: @"Color value %@ is invalid.  It should be a hex value of the form #RRGGBB, or #AARRGGBB", hexString];
            break;
    }
    return [UIColor colorWithRed: red green: green blue: blue alpha: alpha];
}

+ (CGFloat) colorComponentFrom: (NSString *) string start: (NSUInteger) start length: (NSUInteger) length {
    NSString *substring = [string substringWithRange: NSMakeRange(start, length)];
    NSString *fullHex = length == 2 ? substring : [NSString stringWithFormat: @"0%@", substring];
    unsigned hexComponent;
    [[NSScanner scannerWithString: fullHex] scanHexInt: &hexComponent];
    return hexComponent / 255.0;
}

@end
