//
//  Hud.h
//  HudHybrid
//
//  Created by 李生 on 2019/11/21.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void (^HudCompletionBlock)(void);

@interface Hud : NSObject

/**
 * Called after the HUD is hiden.
 */
@property (copy, nullable) HudCompletionBlock completionBlock;

+ (void)text:(NSString *)text;

+ (void)info:(NSString *)text;

+ (void)done:(NSString *)text;

+ (void)error:(NSString *)text;

- (instancetype)initWithHostView:(UIView *)view;

- (void)spinner:(nullable NSString *)text;

- (void)hide;

- (void)hideDelay:(NSTimeInterval)interval;

- (void)hideDefaultDelay;

- (void)text:(NSString *)text;

- (void)info:(NSString *)text;

- (void)done:(NSString *)text;

- (void)error:(NSString *)text;

@end


NS_ASSUME_NONNULL_END
