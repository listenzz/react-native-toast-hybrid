//
//  Hud.m
//  HudHybrid
//
//  Created by 李生 on 2019/11/21.
//
#import "Hud.h"
#import "HudConfig.h"
#import "MBProgressHUD.h"

@interface Hud()

@property(nonatomic, weak) MBProgressHUD *mbProgressHUD;

@end

@implementation Hud

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
        hud.graceTime = [HudConfig sharedConfig].graceTime;
        hud.minShowTime = [HudConfig sharedConfig].minshowTime;
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
    [self hideDelay:[HudConfig sharedConfig].duration];
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
    if ([HudConfig sharedConfig].bezelColor) {
        hud.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
        hud.bezelView.color = [HudConfig sharedConfig].bezelColor;
    }
    
    if ([HudConfig sharedConfig].contentColor) {
        hud.contentColor = [HudConfig sharedConfig].contentColor;
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
