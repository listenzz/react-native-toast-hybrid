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
    [toast loading:@"Downloading..."];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [toast text:@"Download is done."];
        [toast hideDefaultDelay];
    });
}

- (IBAction)showText:(UIButton *)sender {
    [Toast text:@"Hello Native!"];
}

- (IBAction)showInfo:(UIButton *)sender {
    [Toast info:@"A long long message to tell you."];
}

- (IBAction)showDone:(UIButton *)sender {
    [Toast done:@"Work is done!"];
}

- (IBAction)showError:(UIButton *)sender {
    [Toast error:@"Maybe somthing is wrong!"];
}

@end
