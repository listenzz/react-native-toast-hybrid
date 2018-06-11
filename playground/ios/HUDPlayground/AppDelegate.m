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
#import <ProgressHUD/HBDProgressHUD.h>

@interface AppDelegate () <HostViewProvider>

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    
    [HUDConfig sharedConfig].hostViewProvider = self;
    
    NSURL *jsCodeLocation;
    
    jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"playground/index" fallbackResource:nil];
    
    [[HBDReactBridgeManager sharedInstance] installWithBundleURL:jsCodeLocation launchOptions:launchOptions];
    
    self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
    
    HBDViewController *vc1 = [[HBDReactBridgeManager sharedInstance] controllerWithModuleName:@"playground" props:nil options:nil];
    HBDViewController *vc2 = [[HBDReactBridgeManager sharedInstance] controllerWithModuleName:@"playground" props:nil options:nil];
    HBDNavigationController *nav1 = [[HBDNavigationController alloc] initWithRootViewController:vc1];
    nav1.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"Tab1" image:nil selectedImage:nil];
    HBDNavigationController *nav2 = [[HBDNavigationController alloc] initWithRootViewController:vc2];
    nav2.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"Tab2" image:nil selectedImage:nil];
    HBDTabBarController *tabs = [[HBDTabBarController alloc] init];
    
    [tabs setViewControllers:@[nav1, nav2]];
    
    
    self.window.rootViewController = tabs;
    [self.window makeKeyAndVisible];
    return YES;
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
