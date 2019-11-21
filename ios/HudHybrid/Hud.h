//
//  Hud.h
//  HudHybrid
//
//  Created by 李生 on 2019/11/21.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void (^HBDProgressHUDCompletionBlock)(void);

@interface Hud : NSObject

@property(nonatomic, weak) UIView *hostView;

/**
 * Called after the HUD is hiden.
 */
@property (copy, nullable) HBDProgressHUDCompletionBlock completionBlock;

- (instancetype)initWithView:(UIView *) view;

- (void)spinner:(NSString *)text;

- (void)hide;

- (void)hideDelay:(NSTimeInterval)interval;

- (void)hideDefaultDelay;

- (void)text:(NSString *)text;

- (void)info:(NSString *)text;

- (void)done:(NSString *)text;

- (void)error:(NSString *)text;

@end

@protocol HostViewProvider <NSObject>

- (UIView *)hostView;

@end

NS_ASSUME_NONNULL_END
