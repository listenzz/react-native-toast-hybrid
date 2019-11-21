/**
 * Copyright (c) 2015-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import "AppDelegate.h"

#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>
#import <React/RCTBridgeModule.h>
#import <NavigationHybrid/NavigationHybrid.h>
#import <React/RCTBundleURLProvider.h>
#import <HudHybrid/HudHybrid.h>

@interface AppDelegate () <HBDReactBridgeManagerDelegate, HostViewProvider>

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    
    [HudConfig sharedConfig].hostViewProvider = self;
    
    NSURL *jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"example/index" fallbackResource:nil];
    [[HBDReactBridgeManager get] installWithBundleURL:jsCodeLocation launchOptions:launchOptions];
    [HBDReactBridgeManager get].delegate = self;
    
    UIStoryboard *storyboard =  [UIStoryboard storyboardWithName:@"LaunchScreen" bundle:nil];
    UIViewController *rootViewController = [storyboard instantiateInitialViewController];
    self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
    self.window.windowLevel = UIWindowLevelStatusBar + 1;
    self.window.rootViewController = rootViewController;
    [self.window makeKeyAndVisible];

    return YES;
}

- (void)reactModuleRegisterDidCompleted:(HBDReactBridgeManager *)manager {
    HBDViewController *vc1 = [manager controllerWithModuleName:@"HudHybrid" props:nil options:@{@"titleItem":@{@"title":@"Tab1"}}];
    HBDViewController *vc2 = [manager controllerWithModuleName:@"HudHybrid" props:nil options:@{@"titleItem":@{@"title":@"Tab2"}}];
    
    HBDNavigationController *nav1 = [[HBDNavigationController alloc] initWithRootViewController:vc1];
    nav1.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"Tab1" image:nil selectedImage:nil];
    HBDNavigationController *nav2 = [[HBDNavigationController alloc] initWithRootViewController:vc2];
    nav2.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"Tab2" image:nil selectedImage:nil];
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
