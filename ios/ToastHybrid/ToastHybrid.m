#import "ToastHybrid.h"
#import <React/RCTLog.h>
#import <React/RCTBridge.h>

@interface ToastHybrid()

@property(nonatomic, assign) NSInteger toastKeyGenerator;
@property(nonatomic, strong) NSMutableDictionary *toasts;

@end

@implementation ToastHybrid

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
    if (!_toasts) {
        _toasts = [[NSMutableDictionary alloc] init];
    }
    UIView *hostView = [self hostView];
    if (hostView) {
        Toast *toast = [self createToast];
        NSNumber *key = @(++self.toastKeyGenerator);
        [self.toasts setObject:toast forKey:key];
        resolve(key);
    } else {
        resolve(@(-1));
    }
}

RCT_EXPORT_METHOD(ensure:(NSNumber* __nonnull)key resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    Toast *toast = [self.toasts objectForKey:key];
    if (toast) {
        resolve(key);
    } else {
        [self create:resolve rejecter:reject];
    }
}

RCT_EXPORT_METHOD(loading:(NSNumber * __nonnull)key text:(NSString *)text graceTime:(NSInteger)graceTime) {
    Toast *toast = [self.toasts objectForKey:key];
    if (toast) {
        [toast loading:text ?: [ToastConfig sharedConfig].loadingText graceTime:graceTime];
    }
}

RCT_EXPORT_METHOD(hide:(NSNumber* __nonnull)key) {
    Toast *toast = [self.toasts objectForKey:key];
    if (toast) {
        [self.toasts removeObjectForKey:key];
        [toast hide];
    }
}

RCT_EXPORT_METHOD(text:(NSNumber * __nonnull)key text:(NSString *)text) {
    Toast *toast = [self.toasts objectForKey:key];
    if (toast) {
        [toast text:text];
    }
}

RCT_EXPORT_METHOD(info:(NSNumber * __nonnull)key text:(NSString *)text) {
    Toast *toast = [self.toasts objectForKey:key];
    if (toast) {
        [toast info:text];
    }
}

RCT_EXPORT_METHOD(done:(NSNumber * __nonnull)key text:(NSString *)text) {
    Toast *toast = [self.toasts objectForKey:key];
    if (toast) {
        [toast done:text];
    }
}

RCT_EXPORT_METHOD(error:(NSNumber * __nonnull)key text:(NSString *)text) {
    Toast *toast = [self.toasts objectForKey:key];
    if (toast) {
        [toast error:text];
    }
}

- (void)handleReload {
    if (self.toasts) {
        for (NSNumber *key in self.toasts) {
            Toast *toast = self.toasts[key];
            [toast hide];
        }
        [self.toasts removeAllObjects];
    }
}

RCT_EXPORT_METHOD(config:(NSDictionary *)options) {
    if (options[@"backgroundColor"]) {
        [ToastConfig sharedConfig].bezelColor = [ToastHybrid colorWithHexString:options[@"backgroundColor"]];
    } else {
        [ToastConfig sharedConfig].bezelColor = [ToastConfig defaultBezelColor];
    }
    
    if (options[@"tintColor"]) {
        [ToastConfig sharedConfig].contentColor = [ToastHybrid colorWithHexString:options[@"tintColor"]];
    } else {
        [ToastConfig sharedConfig].contentColor = [ToastConfig defaultContentColor];
    }
    
    if (options[@"cornerRadius"]) {
        [ToastConfig sharedConfig].cornerRadius = [options[@"cornerRadius"] floatValue];
    } else {
        [ToastConfig sharedConfig].cornerRadius = [ToastConfig defaultCornerRadius];
    }
    
    if (options[@"duration"]) {
        [ToastConfig sharedConfig].duration = [options[@"duration"] floatValue] / 1000.0f;
    } else {
        [ToastConfig sharedConfig].duration = [ToastConfig defaultDuration];
    }
    
    if (options[@"graceTime"]) {
        [ToastConfig sharedConfig].graceTime = [options[@"graceTime"] floatValue] / 1000.0f;
    } else {
        [ToastConfig sharedConfig].graceTime = [ToastConfig defaultGraceTime];
    }
    
    if (options[@"minShowTime"]) {
        [ToastConfig sharedConfig].minshowTime = [options[@"minShowTime"] floatValue] / 1000.0f;
    } else {
        [ToastConfig sharedConfig].minshowTime = [ToastConfig defaultMinShowTime];
    }
    
    if (options[@"loadingText"]) {
        [ToastConfig sharedConfig].loadingText = options[@"loadingText"];
    } else {
        [ToastConfig sharedConfig].loadingText = [ToastConfig defaultLoadingText];
    }
}

- (Toast *)createToast {
    return [[Toast alloc] initWithHostView:[self hostView]];
}

- (UIView *)hostView {
    return [[ToastConfig sharedConfig].hostViewProvider hostView];
}

+ (UIColor *)colorWithHexString: (NSString *) hexString {
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

+ (CGFloat)colorComponentFrom: (NSString *) string start: (NSUInteger) start length: (NSUInteger) length {
    NSString *substring = [string substringWithRange: NSMakeRange(start, length)];
    NSString *fullHex = length == 2 ? substring : [NSString stringWithFormat: @"0%@", substring];
    unsigned hexComponent;
    [[NSScanner scannerWithString: fullHex] scanHexInt: &hexComponent];
    return hexComponent / 255.0;
}

@end
