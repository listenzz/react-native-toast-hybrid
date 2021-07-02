//
//  Toast.m
//  ToastHybrid
//
//  Created by 李生 on 2019/11/21.
//
#import "Toast.h"
#import "ToastConfig.h"
#import "MBProgressHUD.h"

@interface Toast()

@property(nonatomic, weak) UIView *hostView;
@property(nonatomic, weak) MBProgressHUD *mbHUD;

@end

@implementation Toast

@dynamic completionBlock;


+ (void)text:(NSString *)text {
    Toast *hud = [[Toast alloc] init];
    [hud text:text];
    [hud hideDefaultDelay];
}

+ (void)info:(NSString *)text {
    Toast *hud = [[Toast alloc] init];
    [hud info:text];
    [hud hideDefaultDelay];
}

+ (void)done:(NSString *)text {
    Toast *hud = [[Toast alloc] init];
    [hud done:text];
    [hud hideDefaultDelay];
}

+ (void)error:(NSString *)text {
    Toast *hud = [[Toast alloc] init];
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
    return [self initWithHostView:[[ToastConfig sharedConfig].hostViewProvider hostView]];
}

- (void)setCompletionBlock:(ToastCompletionBlock)completionBlock {
    self.mbHUD.completionBlock = completionBlock;
}

- (void)loading:(NSString *)text {
    if (!self.hostView) {
        return;
    }
    
    if (!self.mbHUD) {
        MBProgressHUD *hud = [[MBProgressHUD alloc] initWithView:self.hostView];
        hud.removeFromSuperViewOnHide = YES;
        [self.hostView addSubview:hud];
        hud.graceTime = [ToastConfig sharedConfig].graceTime;
        hud.minShowTime = [ToastConfig sharedConfig].minshowTime;
        [self configHUD:hud];
        self.mbHUD = hud;
        [self.mbHUD showAnimated:YES];
    }
    self.mbHUD.mode = MBProgressHUDModeIndeterminate;
    self.mbHUD.label.text = text;
    if ([ToastConfig sharedConfig].fontSize > 0) {
        self.mbHUD.label.font = [UIFont systemFontOfSize:[ToastConfig sharedConfig].fontSize];
    }
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
    [self hideDelay:[ToastConfig sharedConfig].duration];
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
    if ([ToastConfig sharedConfig].fontSize > 0) {
        self.mbHUD.label.font = [UIFont systemFontOfSize:[ToastConfig sharedConfig].fontSize];
    }
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
    if ([ToastConfig sharedConfig].fontSize > 0) {
        self.mbHUD.label.font = [UIFont systemFontOfSize:[ToastConfig sharedConfig].fontSize];
    }
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
    if ([ToastConfig sharedConfig].fontSize > 0) {
        self.mbHUD.label.font = [UIFont systemFontOfSize:[ToastConfig sharedConfig].fontSize];
    }
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
    if ([ToastConfig sharedConfig].fontSize > 0) {
        self.mbHUD.label.font = [UIFont systemFontOfSize:[ToastConfig sharedConfig].fontSize];
    }
}

- (void)configHUD:(MBProgressHUD *)hud {
    if ([ToastConfig sharedConfig].bezelColor) {
        hud.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
        hud.bezelView.color = [ToastConfig sharedConfig].bezelColor;
    }
    
    if ([ToastConfig sharedConfig].contentColor) {
        hud.contentColor = [ToastConfig sharedConfig].contentColor;
    }
    
    hud.completionBlock = ^{
        self.mbHUD = nil;
    };
}

- (UIImage *)imageWithName:(NSString *)name {
    NSBundle *podBundle = [NSBundle bundleForClass:self.class];
    NSURL *bundleURL = [podBundle URLForResource:@"ToastHybrid" withExtension:@"bundle"];
    NSBundle *bundle = [NSBundle bundleWithURL:bundleURL];
    UIImage *image = [[UIImage imageNamed:name inBundle:bundle compatibleWithTraitCollection:nil] imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    return image;
}

@end
