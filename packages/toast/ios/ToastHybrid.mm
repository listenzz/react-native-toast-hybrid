#import "ToastHybrid.h"

#import <React/RCTLog.h>
#import <React/RCTInvalidating.h>

@interface ToastHybrid() <RCTInvalidating>

@property(nonatomic, assign) NSInteger toastKeyGenerator;
@property(nonatomic, strong) NSMutableDictionary *toasts;

@end

@implementation ToastHybrid

+ (NSString *)moduleName { 
    return @"ToastHybrid";
}


+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

- (void)invalidate {
    if (self.toasts) {
        for (NSNumber *key in self.toasts) {
            Toast *toast = self.toasts[key];
            [toast hide];
        }
        [self.toasts removeAllObjects];
    }
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params { 
    return std::make_shared<facebook::react::NativeToastSpecJSI>(params);
}

- (void)createToast:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject { 
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

- (void)ensure:(double)key resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject { 
    Toast *toast = [self.toasts objectForKey:@(key)];
    if (toast) {
        resolve(@(key));
    } else {
        [self createToast:resolve reject:reject];
    }
}

- (void)config:(nonnull NSDictionary *)options { 
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
    
    if (options[@"fontSize"]) {
        [ToastConfig sharedConfig].fontSize = [options[@"fontSize"] floatValue];
    } else {
        [ToastConfig sharedConfig].fontSize = [ToastConfig defaultFontSize];
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


- (void)done:(double)key text:(nonnull NSString *)text { 
    Toast *toast = [self.toasts objectForKey:@(key)];
    if (toast) {
        [toast done:text];
    }
}


- (void)error:(double)key text:(nonnull NSString *)text { 
    Toast *toast = [self.toasts objectForKey:@(key)];
    if (toast) {
        [toast error:text];
    }
}


- (void)hide:(double)key { 
    Toast *toast = [self.toasts objectForKey:@(key)];
    if (toast) {
        [self.toasts removeObjectForKey:@(key)];
        [toast hide];
    }
}


- (void)info:(double)key text:(nonnull NSString *)text { 
    Toast *toast = [self.toasts objectForKey:@(key)];
    if (toast) {
        [toast info:text];
    }
}


- (void)loading:(double)key text:(nonnull NSString *)text { 
    Toast *toast = [self.toasts objectForKey:@(key)];
    if (toast) {
        [toast loading:text ?: [ToastConfig sharedConfig].loadingText];
    }
}


- (void)text:(double)key text:(nonnull NSString *)text { 
    Toast *toast = [self.toasts objectForKey:@(key)];
    if (toast) {
        [toast text:text];
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
