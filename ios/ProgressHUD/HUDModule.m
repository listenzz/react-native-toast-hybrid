//
//  ProgressHUDModule.m
//  HUD
//
//  Created by Listen on 2018/6/2.
//  Copyright © 2018年 Listen. All rights reserved.
//

#import "HUDModule.h"
#import <React/RCTLog.h>
#import "HUD.h"
#import "MBProgressHUD.h"

@interface HUDModule()

@property(nonatomic, strong) HUD *progressHUD;

@end

@implementation HUDModule

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

RCT_EXPORT_MODULE(HUD);

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(text:(NSString *)text) {
    [[self createHud] text:text];
}

RCT_EXPORT_METHOD(info:(NSString *)text) {
    [[self createHud] info:text];
}

RCT_EXPORT_METHOD(done:(NSString *)text) {
    [[self createHud] done:text];
}

RCT_EXPORT_METHOD(error:(NSString *)text) {
    [[self createHud] error:text];
}

RCT_EXPORT_METHOD(show) {
    if (!self.progressHUD) {
        self.progressHUD = [self createHud];
    }
    
    if (self.progressHUD.hostView && self.progressHUD.hostView != [self currentHostView]) {
        [self.progressHUD forceHide];
        self.progressHUD = [self createHud];
    }
    [self.progressHUD show:[HUDConfig sharedConfig].loadingText];
}

RCT_EXPORT_METHOD(hide) {
    if (self.progressHUD) {
        NSInteger result = [self.progressHUD hide];
        if (result == 0) {
            self.progressHUD = nil;
        }
    }
}

RCT_EXPORT_METHOD(config:(NSDictionary *)options) {
    if (options[@"backgroundColor"]) {
        [HUDConfig sharedConfig].bezelColor = [HUDModule colorWithHexString:options[@"backgroundColor"]];
    } else {
        [HUDConfig sharedConfig].bezelColor = [HUDConfig defaultBezelColor];
    }
    
    if (options[@"tintColor"]) {
        [HUDConfig sharedConfig].contentColor = [HUDModule colorWithHexString:options[@"tintColor"]];
    } else {
        [HUDConfig sharedConfig].contentColor = [HUDConfig defaultContentColor];
    }
    
    if (options[@"cornerRadius"]) {
        [HUDConfig sharedConfig].cornerRadius = [options[@"cornerRadius"] floatValue];
    } else {
        [HUDConfig sharedConfig].cornerRadius = [HUDConfig defaultCornerRadius];
    }
    
    if (options[@"duration"]) {
        [HUDConfig sharedConfig].duration = [options[@"duration"] floatValue] / 1000.0f;
    } else {
        [HUDConfig sharedConfig].duration = [HUDConfig defaultDuration];
    }
    
    if (options[@"graceTime"]) {
        [HUDConfig sharedConfig].graceTime = [options[@"graceTime"] floatValue] / 1000.0f;
    } else {
        [HUDConfig sharedConfig].graceTime = [HUDConfig defaultGraceTime];
    }
    
    if (options[@"minShowTime"]) {
        [HUDConfig sharedConfig].minshowTime = [options[@"minShowTime"] floatValue] / 1000.0f;
    } else {
        [HUDConfig sharedConfig].minshowTime = [HUDConfig defaultMinShowTime];
    }
    
    if (options[@"dimAmount"]) {
        [HUDConfig sharedConfig].dimAmount = [options[@"dimAmount"] floatValue];
    } else {
        [HUDConfig sharedConfig].dimAmount = [HUDConfig defaultDimAmount];
    }
    
    if (options[@"loadingText"]) {
        [HUDConfig sharedConfig].loadingText = options[@"loadingText"];
    } else {
        [HUDConfig sharedConfig].loadingText = [HUDConfig defaultLoadingText];
    }
}

- (HUD *)createHud {
    return [[HUD alloc] initWithView:[self currentHostView]];
}

- (UIView *)currentHostView {
    UIView *hostView;
    if ([HUDConfig sharedConfig].hostViewProvider) {
        hostView = [[HUDConfig sharedConfig].hostViewProvider hostView];
    } else {
        UIApplication *application = [[UIApplication class] performSelector:@selector(sharedApplication)];
        UIViewController *controller = application.keyWindow.rootViewController;
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
