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

@property(nonatomic, weak) UIView *hostView;
@property(nonatomic, weak) MBProgressHUD *mbHUD;

@end

@implementation Hud

@dynamic completionBlock;


+ (void)text:(NSString *)text {
    Hud *hud = [[Hud alloc] init];
    [hud text:text];
    [hud hideDefaultDelay];
}

+ (void)info:(NSString *)text {
    Hud *hud = [[Hud alloc] init];
    [hud info:text];
    [hud hideDefaultDelay];
}

+ (void)done:(NSString *)text {
    Hud *hud = [[Hud alloc] init];
    [hud done:text];
    [hud hideDefaultDelay];
}

+ (void)error:(NSString *)text {
    Hud *hud = [[Hud alloc] init];
    [hud error:text];
    [hud hideDefaultDelay];
}

- (instancetype)initWithHostView:(UIView *) view {
    if (self = [super init]) {
        _hostView = view;
    }
    return self;
}

- (instancetype)init {
    return [self initWithHostView:[[HudConfig sharedConfig].hostViewProvider hostView]];
}

- (void)setCompletionBlock:(HudCompletionBlock)completionBlock {
    self.mbHUD.completionBlock = completionBlock;
}

- (void)spinner:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbHUD) {
        MBProgressHUD *hud = [[MBProgressHUD alloc] initWithView:self.hostView];
        hud.removeFromSuperViewOnHide = YES;
        [self.hostView addSubview:hud];
        hud.graceTime = [HudConfig sharedConfig].graceTime;
        hud.minShowTime = [HudConfig sharedConfig].minshowTime;
        [self configHUD:hud];
        self.mbHUD = hud;
        [self.mbHUD showAnimated:YES];
    }
    self.mbHUD.mode = MBProgressHUDModeIndeterminate;
    self.mbHUD.label.text = text;
}

- (void)hide {
    if (self.mbHUD) {
        [self.mbHUD hideAnimated:YES];
        self.mbHUD = nil;
    }
}

- (void)hideDelay:(NSTimeInterval)interval {
    if (self.mbHUD) {
        [self.mbHUD hideAnimated:YES afterDelay:interval];
        self.mbHUD = nil;
    }
}

- (void)hideDefaultDelay {
    [self hideDelay:[HudConfig sharedConfig].duration];
}

- (void)text:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbHUD) {
        self.mbHUD =  [MBProgressHUD showHUDAddedTo: self.hostView animated:YES];
         [self configHUD:self.mbHUD];
    }
    self.mbHUD.mode = MBProgressHUDModeText;
    self.mbHUD.label.text = text;
}

- (void)info:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbHUD) {
        self.mbHUD = [MBProgressHUD showHUDAddedTo:self.hostView animated:YES];
        [self configHUD:self.mbHUD];
    }
    self.mbHUD.mode = MBProgressHUDModeCustomView;
    self.mbHUD.customView = [[UIImageView alloc] initWithImage:[self imageWithName:@"hud_info"]];
    self.mbHUD.label.text = text;
}

- (void)done:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbHUD) {
        self.mbHUD =  [MBProgressHUD showHUDAddedTo:self.hostView animated:YES];
        [self configHUD:self.mbHUD];
    }
    self.mbHUD.mode = MBProgressHUDModeCustomView;
    self.mbHUD.customView = [[UIImageView alloc] initWithImage:[self imageWithName:@"hud_done"]];
    self.mbHUD.label.text = text;
}

- (void)error:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbHUD) {
        self.mbHUD =  [MBProgressHUD showHUDAddedTo:self.hostView animated:YES];
        [self configHUD:self.mbHUD];
    }
    self.mbHUD.mode = MBProgressHUDModeCustomView;
    self.mbHUD.customView = [[UIImageView alloc] initWithImage:[self imageWithName:@"hud_error"]];
    self.mbHUD.label.text = text;
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
        self.mbHUD = nil;
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
