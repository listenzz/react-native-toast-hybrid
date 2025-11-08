#import "AppDelegate.h"
#import "ViewController.h"

#import <React-RCTAppDelegate/RCTDefaultReactNativeFactoryDelegate.h>
#import <React-RCTAppDelegate/RCTReactNativeFactory.h>
#import <ReactAppDependencyProvider/RCTAppDependencyProvider.h>

#import <React/RCTLinkingManager.h>
#import <React/RCTBundleURLProvider.h>
#import <React/RCTLog.h>

#import <HybridNavigation/HybridNavigation.h>
#import <ToastHybrid/ToastHybrid.h>


@interface ReactNativeDelegate : RCTDefaultReactNativeFactoryDelegate
@end

@implementation ReactNativeDelegate

- (NSURL *)bundleURL {
#if DEBUG
    return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
    return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

@end

@interface AppDelegate () <HBDReactBridgeManagerDelegate, HostViewProvider>

@property (strong, nonatomic) RCTRootViewFactory *rootViewFactory;
@property (strong, nonatomic) id<RCTReactNativeFactoryDelegate> reactNativeDelegate;
@property (strong, nonatomic) RCTReactNativeFactory *reactNativeFactory;

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // 设置 hud 的 hostView, 可以不设置
    [ToastConfig sharedConfig].hostViewProvider = self;
    
    ReactNativeDelegate *delegate = [[ReactNativeDelegate alloc] init];
    RCTReactNativeFactory *factory = [[RCTReactNativeFactory alloc] initWithDelegate:delegate];
    delegate.dependencyProvider = [[RCTAppDependencyProvider alloc] init];
    
    
    self.reactNativeDelegate = delegate;
    self.reactNativeFactory = factory;
    self.rootViewFactory = factory.rootViewFactory;
    
    [self.rootViewFactory initializeReactHostWithLaunchOptions:launchOptions];
    [[HBDReactBridgeManager get] installWithReactHost:self.rootViewFactory.reactHost];
    
    [HBDReactBridgeManager get].delegate = self;
    // 注册原生页面
    [[HBDReactBridgeManager get] registerNativeModule:@"Tab2" forViewController:[ViewController class]];
    
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
    HBDViewController *vc1 = [manager viewControllerWithModuleName:@"Tab1" props:nil options:@{@"titleItem":@{@"title":@"React"}}];
    // Tab2 是原生模块
    HBDViewController *vc2 = [manager viewControllerWithModuleName:@"Tab2" props:nil options:@{@"titleItem":@{@"title":@"Native"}}];
    
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
    if ([controller isKindOfClass:[UITabBarController class]]) {
        UITabBarController *tabs = (UITabBarController *)controller;
        return tabs.selectedViewController;
    }
    return controller;
}

@end

