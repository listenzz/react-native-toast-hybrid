//
//  HudConfig.m
//  HudHybrid
//
//  Created by 李生 on 2019/11/21.
//

#import "HudConfig.h"

@implementation HudConfig

+ (instancetype)sharedConfig {
    static HudConfig *config;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        config = [[HudConfig alloc] init];
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
    return 0.8f;
}

+ (UIColor *)defaultBezelColor {
    return nil;
}

+ (UIColor *)defaultContentColor {
    return nil;
}

+ (CGFloat)defaultCornerRadius {
    return 10.f;
}

+ (NSString *)defaultLoadingText {
    return nil;
}

- (instancetype)init {
    if (self = [super init]) {
        _duration = [HudConfig defaultDuration];
        _graceTime = [HudConfig defaultGraceTime];
        _minshowTime = [HudConfig defaultMinShowTime];
        _bezelColor = [HudConfig defaultBezelColor];
        _contentColor = [HudConfig defaultContentColor];
        _cornerRadius = [HudConfig defaultCornerRadius];
        _loadingText = [HudConfig defaultLoadingText];
    }
    return self;
}

@end
