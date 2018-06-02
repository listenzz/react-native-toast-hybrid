//
//  ProgressHUD.h
//  HUD
//
//  Created by Listen on 2018/6/2.
//  Copyright © 2018年 Listen. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface HUDConfig : NSObject

+ (instancetype)sharedConfig;

@property(nonatomic, assign) NSTimeInterval duration;

@property(nonatomic, assign) NSTimeInterval graceTime;

@property(nonatomic, assign) NSTimeInterval minshowTime;

@property(nonatomic, assign) CGFloat dim;

@property(nonatomic, strong) UIColor *bezelColor;

@property(nonatomic, strong) UIColor *tintColor;

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
