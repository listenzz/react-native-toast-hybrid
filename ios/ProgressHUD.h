//
//  ProgressHUD.h
//  HUD
//
//  Created by Listen on 2018/6/2.
//  Copyright © 2018年 Listen. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@protocol HostViewProvider;

@interface HUDConfig : NSObject

+ (instancetype)sharedConfig;

+ (NSTimeInterval)defaultDuration;

+ (NSTimeInterval)defaultGraceTime;

+ (NSTimeInterval)defaultMinShowTime;

+ (UIColor *)defaultBezelColor;

+ (UIColor *)defaultContentColor;

+ (CGFloat)defaultCornerRadius;

+ (CGFloat)defaultDimAmount;

+ (NSString *)defaultLoadingText;

@property(nonatomic, assign) NSTimeInterval duration;

@property(nonatomic, assign) NSTimeInterval graceTime;

@property(nonatomic, assign) NSTimeInterval minshowTime;

@property(nonatomic, assign) CGFloat dimAmount;

@property(nonatomic, strong) UIColor *bezelColor;

@property(nonatomic, strong) UIColor *contentColor;

@property(nonatomic, assign) CGFloat cornerRadius;

@property(nonatomic, strong) NSString *loadingText;

@property(nonatomic, strong) id<HostViewProvider> hostViewProvider;

@end

@interface ProgressHUD : NSObject

@property(nonatomic, weak) UIView *hostView;

- (instancetype)initWithView:(UIView *) view;

- (void)show:(NSString *)text;

- (NSInteger)hide;

- (void)forceHide;

- (void)text:(NSString *)text;

- (void)info:(NSString *)text;

- (void)done:(NSString *)text;

- (void)error:(NSString *)text;

@end

@protocol HostViewProvider <NSObject>

- (UIView *)hostView;

@end

