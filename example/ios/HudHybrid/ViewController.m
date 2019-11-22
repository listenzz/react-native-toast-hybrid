//
//  ViewController.m
//  HudHybrid
//
//  Created by 李生 on 2019/11/22.
//  Copyright © 2019 李生. All rights reserved.
//

#import "ViewController.h"
#import <HudHybrid/Hud.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (IBAction)showLoading:(UIButton *)sender {
    Hud *loading = [[Hud alloc] init];
    [loading spinner:@"正在加载..."];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [loading text:@"资料已经下载完成"];
        [loading hideDefaultDelay];
    });
}

- (IBAction)showText:(UIButton *)sender {
    [Hud text:@"Hello Native!"];
}

- (IBAction)showInfo:(UIButton *)sender {
    [Hud info:@"一条好消息，一条坏消息，你要先听哪一条？"];
}

- (IBAction)showDone:(UIButton *)sender {
    [Hud done:@"工作完成，提前下班"];
}

- (IBAction)showError:(UIButton *)sender {
    [Hud error:@"哈，你又写 BUG 了！"];
}

@end
