//
//  ProgressHUDModule.m
//  HUD
//
//  Created by Listen on 2018/6/2.
//  Copyright © 2018年 Listen. All rights reserved.
//

#import "ProgressHUDModule.h"
#import <React/RCTLog.h>
#import "ProgressHUD.h"
#import "MBProgressHUD.h"

@interface ProgressHUDModule()

@property(nonatomic, strong) ProgressHUD *progressHUD;

@end

@implementation ProgressHUDModule

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
    
    if (self.progressHUD.hostView && self.progressHUD.hostView != RCTPresentedViewController().view) {
        [self.progressHUD forceHide];
        self.progressHUD = [self createHud];
    }
    [self.progressHUD show:nil];
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
        
    }
    if (options[@"tintColor"]) {
        
    }
    if (options[@"cornerRadius"]) {
        
    }
    if (options[@"duration"]) {
        
    }
    if (options[@"graceTime"]) {
        
    }
    if (options[@"minShowTime"]) {
        
    }
}

- (ProgressHUD *)createHud {
    return [[ProgressHUD alloc] initWithView:RCTPresentedViewController().view];
}

@end
