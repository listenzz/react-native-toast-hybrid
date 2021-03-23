#import "AppDelegate.h"

#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>
#import <React/RCTBridgeModule.h>
#import <HybridNavigation/HybridNavigation.h>
#import <React/RCTBundleURLProvider.h>
#import <ToastHybrid/ToastHybrid.h>
#import "ViewController.h"

@interface AppDelegate () <HBDReactBridgeManagerDelegate, HostViewProvider>

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // 设置 hud 的 hostView, 可以不设置
    [ToastConfig sharedConfig].hostViewProvider = self;
    
    NSURL *jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"example/index" fallbackResource:nil];
    [[HBDReactBridgeManager get] installWithBundleURL:jsCodeLocation launchOptions:launchOptions];
    [HBDReactBridgeManager get].delegate = self;
    // 注册原生页面
    [[HBDReactBridgeManager get] registerNativeModule:@"Tab2" forController:[ViewController class]];
    
    // 闪屏
    UIStoryboard *storyboard =  [UIStoryboard storyboardWithName:@"LaunchScreen" bundle:nil];
    UIViewController *rootViewController = [storyboard instantiateInitialViewController];
    self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
    self.window.windowLevel = UIWindowLevelStatusBar + 1;
    self.window.rootViewController = rootViewController;
    [self.window makeKeyAndVisible];

    return YES;
}

- (void)reactModuleRegisterDidCompleted:(HBDReactBridgeManager *)manager {
    
    // Tab1 是 RN 模块
    HBDViewController *vc1 = [manager controllerWithModuleName:@"Tab1" props:nil options:@{@"titleItem":@{@"title":@"React"}}];
    // Tab2 是原生模块
    HBDViewController *vc2 = [manager controllerWithModuleName:@"Tab2" props:nil options:@{@"titleItem":@{@"title":@"Native"}}];
    
    HBDNavigationController *nav1 = [[HBDNavigationController alloc] initWithRootViewController:vc1];
    nav1.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"React" image:nil selectedImage:nil];
    HBDNavigationController *nav2 = [[HBDNavigationController alloc] initWithRootViewController:vc2];
    nav2.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"Native" image:nil selectedImage:nil];
    HBDTabBarController *tabs = [[HBDTabBarController alloc] init];
    
    [tabs setViewControllers:@[nav1, nav2]];
    [manager setRootViewController:tabs];
}

- (UIView *)hostView {
    UIApplication *application = [[UIApplication class] performSelector:@selector(sharedApplication)];
    UIViewController *controller = application.keyWindow.rootViewController;
    return [self controller:controller].view;
}

- (UIViewController *)controller:(UIViewController *)controller {
    UIViewController *presentedController = controller.presentedViewController;
    if (presentedController && ![presentedController isBeingDismissed]) {
        return [self controller:presentedController];
    } else if ([controller isKindOfClass:[HBDDrawerController class]]) {
        HBDDrawerController *drawer = (HBDDrawerController *)controller;
        if ([drawer isMenuOpened]) {
            return drawer;
        } else {
            return [self controller:drawer.contentController];
        }
    } else if ([controller isKindOfClass:[HBDTabBarController class]]) {
        HBDTabBarController *tabs = (HBDTabBarController *)controller;
        return [self controller:tabs.selectedViewController];
    }
    return controller;
}

@end

