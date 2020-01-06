# react-native-toast-hybrid

A toast that can be used for react-native, while available for native android, ios

![ios-hud](./screenshot/ios-hud.gif)

## Installation

```
yarn add react-native-toast-hybrid
```

## Usage

详情请参考 example 这个项目

```js
// 在 js 中使用

import Toast from 'react-native-toast-hybrid'

text() {
    Toast.text('Hello World!!')
}

info() {
    Toast.info('有条消息要告诉你')
}

done() {
    Toast.done('任务已经完成啦！')
}

error() {
    Toast.error('可能什么地方出错了！')
}
```

正如 hybrid 所暗示的那样，也支持在原生中使用

```objc

// 在 iOS 中使用

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

```

```java
// 在 Android 中使用

root.findViewById(R.id.text).setOnClickListener(v -> {
    Toast.text(requireActivity(), "Hello Native!");
});

root.findViewById(R.id.info).setOnClickListener(v -> {
    Toast.info(requireActivity(), "一条好消息，一条坏消息，你要先听哪一条？");
});

root.findViewById(R.id.done).setOnClickListener(v -> {
    Toast.done(requireActivity(), "工作完成，提前下班");
});

root.findViewById(R.id.error).setOnClickListener(v -> {
    Toast.error(requireActivity(), "哈，你又写 BUG 了！");
});

```

强调一次，详情请参考 example 这个项目
