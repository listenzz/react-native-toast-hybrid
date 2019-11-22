# react-native-hud-hybrid

A progerss hud that can be used for react-native, while available for native android, ios

![ios-hud](./screenshot/ios-hud.gif)

## Installation

```
yarn add react-native-hud-hybrid
```

## Usage

详情请参考 example 这个项目

```js
// 在 js 中使用

import HUD, { LoadingHUD } from 'react-native-hud-hybrid'

text() {
    HUD.text('Hello World!!')
}

info() {
    HUD.info('有条消息要告诉你')
}

done() {
    HUD.done('任务已经完成啦！')
}

error() {
    HUD.error('可能什么地方出错了！')
}
```

正如 hybrid 所暗示的那样，也支持在原生中使用

```objc

// 在 iOS 中使用

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

```

```java
// 在 Android 中使用

root.findViewById(R.id.text).setOnClickListener(v -> {
    Hud.text(requireActivity(), "Hello Native!");
});

root.findViewById(R.id.info).setOnClickListener(v -> {
    Hud.info(requireActivity(), "一条好消息，一条坏消息，你要先听哪一条？");
});

root.findViewById(R.id.done).setOnClickListener(v -> {
    Hud.done(requireActivity(), "工作完成，提前下班");
});

root.findViewById(R.id.error).setOnClickListener(v -> {
    Hud.error(requireActivity(), "哈，你又写 BUG 了！");
});

```

强调一次，详情请参考 example 这个项目
