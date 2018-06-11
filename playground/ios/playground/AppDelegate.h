/**
 * Copyright (c) 2015-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import <UIKit/UIKit.h>

@protocol HostViewProvider;

@interface AppDelegate : UIResponder <UIApplicationDelegate, HostViewProvider>

@property (nonatomic, strong) UIWindow *window;

@end
