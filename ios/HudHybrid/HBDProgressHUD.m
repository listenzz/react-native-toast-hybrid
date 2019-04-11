//
//  ProgressHUD.m
//  HUD
//
//  Created by Listen on 2018/6/2.
//  Copyright © 2018年 Listen. All rights reserved.
//

#import "HBDProgressHUD.h"
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
        _cornerRadius = [HUDConfig defaultCornerRadius];
        _loadingText = [HUDConfig defaultLoadingText];
    }
    return self;
}

@end

@interface HBDProgressHUD()

@property(nonatomic, weak) MBProgressHUD *mbProgressHUD;

@end

@implementation HBDProgressHUD

@dynamic completionBlock;

- (instancetype)initWithView:(UIView *) view {
    if (self = [super init]) {
        _hostView = view;
    }
    return self;
}

- (void)setCompletionBlock:(HBDProgressHUDCompletionBlock)completionBlock {
    self.mbProgressHUD.completionBlock = completionBlock;
}

- (void)spinner:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbProgressHUD) {
        MBProgressHUD *hud = [[MBProgressHUD alloc] initWithView:self.hostView];
        hud.removeFromSuperViewOnHide = YES;
        [self.hostView addSubview:hud];
        hud.graceTime = [HUDConfig sharedConfig].graceTime;
        hud.minShowTime = [HUDConfig sharedConfig].minshowTime;
        [self configHUD:hud];
        self.mbProgressHUD = hud;
        [self.mbProgressHUD showAnimated:YES];
    }
    self.mbProgressHUD.mode = MBProgressHUDModeIndeterminate;
    self.mbProgressHUD.label.text = text;
}

- (void)hide {
    if (self.mbProgressHUD) {
        [self.mbProgressHUD hideAnimated:YES];
        self.mbProgressHUD = nil;
    }
}

- (void)hideDelay:(NSTimeInterval)interval {
    if (self.mbProgressHUD) {
        [self.mbProgressHUD hideAnimated:YES afterDelay:interval];
        self.mbProgressHUD = nil;
    }
}

- (void)hideDefaultDelay {
    [self hideDelay:[HUDConfig sharedConfig].duration];
}

- (void)text:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbProgressHUD) {
        self.mbProgressHUD =  [MBProgressHUD showHUDAddedTo: self.hostView animated:YES];
         [self configHUD:self.mbProgressHUD];
    }
    self.mbProgressHUD.mode = MBProgressHUDModeText;
    self.mbProgressHUD.label.text = text;
}

- (void)info:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbProgressHUD) {
        self.mbProgressHUD = [MBProgressHUD showHUDAddedTo:self.hostView animated:YES];
        [self configHUD:self.mbProgressHUD];
    }
    self.mbProgressHUD.mode = MBProgressHUDModeCustomView;
    self.mbProgressHUD.customView = [[UIImageView alloc] initWithImage:[self imageWithName:@"hud_info"]];
    self.mbProgressHUD.label.text = text;
}

- (void)done:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbProgressHUD) {
        self.mbProgressHUD =  [MBProgressHUD showHUDAddedTo:self.hostView animated:YES];
        [self configHUD:self.mbProgressHUD];
    }
    self.mbProgressHUD.mode = MBProgressHUDModeCustomView;
    self.mbProgressHUD.customView = [[UIImageView alloc] initWithImage:[self imageWithName:@"hud_done"]];
    self.mbProgressHUD.label.text = text;
}

- (void)error:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbProgressHUD) {
        self.mbProgressHUD =  [MBProgressHUD showHUDAddedTo:self.hostView animated:YES];
        [self configHUD:self.mbProgressHUD];
    }
    self.mbProgressHUD.mode = MBProgressHUDModeCustomView;
    self.mbProgressHUD.customView = [[UIImageView alloc] initWithImage:[self imageWithName:@"hud_error"]];
    self.mbProgressHUD.label.text = text;
}

- (void)configHUD:(MBProgressHUD *)hud {
    if ([HUDConfig sharedConfig].bezelColor) {
        hud.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
        hud.bezelView.color = [HUDConfig sharedConfig].bezelColor;
    }
    
    if ([HUDConfig sharedConfig].contentColor) {
        hud.contentColor = [HUDConfig sharedConfig].contentColor;
    }
    
    hud.completionBlock = ^{
        self.mbProgressHUD = nil;
    };
}

- (UIImage *)imageWithName:(NSString *)name {
    NSBundle *podBundle = [NSBundle bundleForClass:self.class];
    NSURL *bundleURL = [podBundle URLForResource:@"HudHybrid" withExtension:@"bundle"];
    NSBundle *bundle = [NSBundle bundleWithURL:bundleURL];
    UIImage *image = [[UIImage imageNamed:name inBundle:bundle compatibleWithTraitCollection:nil] imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    return image;
}

@end
