//
//  ViewController.m
//  HudHybrid
//
//  Created by 李生 on 2019/11/22.
//  Copyright © 2019 李生. All rights reserved.
//

#import "ViewController.h"
#import <ToastHybrid/ToastHybrid.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (IBAction)showLoading:(UIButton *)sender {
    Toast *toast = [[Toast alloc] init];
    [toast loading:@"正在加载..."];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [toast text:@"资料已经下载完成"];
        [toast hideDefaultDelay];
    });
}

- (IBAction)showText:(UIButton *)sender {
    [Toast text:@"Hello Native!"];
}

- (IBAction)showInfo:(UIButton *)sender {
    [Toast info:@"一条好消息，一条坏消息，你要先听哪一条？"];
}

- (IBAction)showDone:(UIButton *)sender {
    [Toast done:@"工作完成，提前下班"];
}

- (IBAction)showError:(UIButton *)sender {
    [Toast error:@"哈，你又写 BUG 了！"];
}

@end
