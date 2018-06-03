//
//  ProgressHUD.m
//  HUD
//
//  Created by Listen on 2018/6/2.
//  Copyright © 2018年 Listen. All rights reserved.
//

#import "HUD.h"
#import "MBProgressHUD.h"

@implementation HUDConfig

+ (instancetype)sharedConfig {
    static HUDConfig *config;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        config = [[HUDConfig alloc] init];
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

+ (CGFloat)defaultDimAmount {
    return 0.0f;
}

+ (NSString *)defaultLoadingText {
    return nil;
}

- (instancetype)init {
    if (self = [super init]) {
        _duration = [HUDConfig defaultDuration];
        _graceTime = [HUDConfig defaultGraceTime];
        _minshowTime = [HUDConfig defaultMinShowTime];
        _bezelColor = [HUDConfig defaultBezelColor];
        _contentColor = [HUDConfig defaultContentColor];
        _dimAmount = [HUDConfig defaultDimAmount];
        _cornerRadius = [HUDConfig defaultCornerRadius];
        _loadingText = [HUDConfig defaultLoadingText];
    }
    return self;
}

@end

@interface HUD()

@property(nonatomic, weak) MBProgressHUD *mbHUD;
@property(nonatomic, assign) NSInteger loadingCount;

@end

@implementation HUD

- (instancetype)initWithView:(UIView *) view {
    if (self = [super init]) {
        _hostView = view;
    }
    return self;
}

- (void)show:(NSString *)text {
    if (self.loadingCount == 0) {
        MBProgressHUD *hud = [[MBProgressHUD alloc] initWithView:self.hostView];
        hud.removeFromSuperViewOnHide = YES;
        [self.hostView addSubview:hud];
        hud.graceTime = [HUDConfig sharedConfig].graceTime;
        hud.minShowTime = [HUDConfig sharedConfig].minshowTime;
        hud.completionBlock = ^{
            self.mbHUD = nil;
            self.loadingCount = 0;
        };
        hud.label.text = text;
        [self configHUD:hud];
        self.mbHUD = hud;
        [hud showAnimated:YES];
    }
     self.loadingCount ++;
}

- (NSInteger)hide {
    self.loadingCount --;
    if (self.loadingCount <=0) {
        [self forceHide];
    }
    return self.loadingCount;
}

- (void)forceHide {
    if (self.mbHUD) {
        [self.mbHUD hideAnimated:YES];
        self.mbHUD = nil;
    }
}

- (void)text:(NSString *)text {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo: self.hostView animated:YES];
    hud.label.text = text;
    hud.mode = MBProgressHUDModeText;
    [self configHUD:hud];
    [hud hideAnimated:YES afterDelay:[HUDConfig sharedConfig].duration];
}

- (void)info:(NSString *)text {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.hostView animated:YES];
    hud.mode = MBProgressHUDModeCustomView;
    hud.customView = [[UIImageView alloc] initWithImage:[self imageWithName:@"hud_info"]];
    hud.label.text = text;
    [self configHUD:hud];
    [hud hideAnimated:YES afterDelay:[HUDConfig sharedConfig].duration];
}

- (void)done:(NSString *)text {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.hostView animated:YES];
    hud.mode = MBProgressHUDModeCustomView;
    hud.customView = [[UIImageView alloc] initWithImage:[self imageWithName:@"hud_done"]];
    hud.label.text = text;
    [self configHUD:hud];
    [hud hideAnimated:YES afterDelay:[HUDConfig sharedConfig].duration];
}

- (void)error:(NSString *)text {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.hostView animated:YES];
    hud.mode = MBProgressHUDModeCustomView;
    hud.customView = [[UIImageView alloc] initWithImage:[self imageWithName:@"hud_error"]];
    hud.label.text = text;
    [self configHUD:hud];
    [hud hideAnimated:YES afterDelay:[HUDConfig sharedConfig].duration];
}

- (void)configHUD:(MBProgressHUD *)hud {
    if ([HUDConfig sharedConfig].bezelColor) {
        hud.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
        hud.bezelView.color = [HUDConfig sharedConfig].bezelColor;
    }
    
    if ([HUDConfig sharedConfig].contentColor) {
        hud.contentColor = [HUDConfig sharedConfig].contentColor;
    }
}

- (UIImage *)imageWithName:(NSString *)name {
    NSBundle *podBundle = [NSBundle bundleForClass:self.class];
    NSURL *bundleURL = [podBundle URLForResource:@"ProgressHUD" withExtension:@"bundle"];
    NSBundle *bundle = [NSBundle bundleWithURL:bundleURL];
    UIImage *image = [[UIImage imageNamed:name inBundle:bundle compatibleWithTraitCollection:nil] imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    return image;
}

@end
