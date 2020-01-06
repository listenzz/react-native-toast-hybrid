//
//  ToastConfig.h
//  ToastHybrid
//
//  Created by 李生 on 2019/11/21.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@protocol HostViewProvider <NSObject>

- (UIView *)hostView;

@end

@interface ToastConfig : NSObject

+ (instancetype)sharedConfig;

+ (NSTimeInterval)defaultDuration;

+ (NSTimeInterval)defaultGraceTime;

+ (NSTimeInterval)defaultMinShowTime;

+ (UIColor *)defaultBezelColor;

+ (UIColor *)defaultContentColor;

+ (CGFloat)defaultCornerRadius;

+ (NSString *)defaultLoadingText;

@property(nonatomic, assign) NSTimeInterval duration;

@property(nonatomic, assign) NSTimeInterval graceTime;

@property(nonatomic, assign) NSTimeInterval minshowTime;

@property(nonatomic, strong) UIColor *bezelColor;

@property(nonatomic, strong) UIColor *contentColor;

@property(nonatomic, assign) CGFloat cornerRadius;

@property(nonatomic, strong) NSString *loadingText;

@property(nonatomic, strong) id<HostViewProvider> hostViewProvider;

@end

NS_ASSUME_NONNULL_END
