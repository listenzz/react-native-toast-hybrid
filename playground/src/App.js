import React, { Component } from "react";
import { StyleSheet, Text, View, TouchableOpacity } from "react-native";
import HUD, { LoadingHUD } from "react-native-hud-hybrid";

export default class App extends Component {
  constructor(props) {
    super(props);
    this.push = this.push.bind(this);
    this.loading = this.loading.bind(this);
    this.loadingHUD = new LoadingHUD();
    this.timeoutHandle = undefined;
  }

  componentDidMount() {
    HUD.config({
      // backgroundColor: '#BB000000',
      // tintColor: '#FFFFFF',
      // cornerRadius: 5, // only for android
      // duration: 2000,
      // graceTime: 3000,
      // minShowTime: 800,
      // dimAmount: 0.0, // only for andriod
      loadingText: "加载中..."
    });
    this.props.navigation.isRoot().then(isRoot => {
      if (!isRoot) {
        setTimeout(() => {
          this.props.navigation.pop();
        }, 2000);
      }
    });
  }

  componentWillUnmount() {
    if (this.loadingHUD) {
      this.loadingHUD.hideAll();
    }
    if (this.timeoutHandle) {
      clearTimeout(this.timeoutHandle);
    }
  }

  loading() {
    this.loadingHUD.show();
    this.timeoutHandle = setTimeout(() => {
      this.loadingHUD.show("祝你好运");
      this.timeoutHandle = setTimeout(() => {
        this.loadingHUD.show("");
        this.timeoutHandle = setTimeout(() => {
          this.timeoutHandle = undefined;
          this.loadingHUD.hideAll();
        }, 2000);
      }, 2000);
    }, 2000);
  }

  text() {
    new HUD().text("Hello World!!").hideDelayDefault();
  }

  info() {
    new HUD().info("有条消息要告诉你").hideDelayDefault();
  }

  done() {
    new HUD().done("任务已经完成啦！").hideDelayDefault();
  }

  error() {
    new HUD().error("可能什么地方出错了！").hideDelayDefault();
  }

  push() {
    this.props.navigation.push("playground");
  }

  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity
          onPress={this.loading}
          activeOpacity={0.2}
          style={styles.button}
        >
          <Text style={styles.buttonText}> loading </Text>
        </TouchableOpacity>

        <TouchableOpacity
          onPress={this.text}
          activeOpacity={0.2}
          style={styles.button}
        >
          <Text style={styles.buttonText}> text </Text>
        </TouchableOpacity>

        <TouchableOpacity
          onPress={this.info}
          activeOpacity={0.2}
          style={styles.button}
        >
          <Text style={styles.buttonText}> info </Text>
        </TouchableOpacity>

        <TouchableOpacity
          onPress={this.done}
          activeOpacity={0.2}
          style={styles.button}
        >
          <Text style={styles.buttonText}> done </Text>
        </TouchableOpacity>

        <TouchableOpacity
          onPress={this.error}
          activeOpacity={0.2}
          style={styles.button}
        >
          <Text style={styles.buttonText}> error </Text>
        </TouchableOpacity>

        <TouchableOpacity
          onPress={this.push}
          activeOpacity={0.2}
          style={styles.button}
        >
          <Text style={styles.buttonText}> push and auto pop back </Text>
        </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "flex-start",
    alignItems: "stretch",
    paddingTop: 16
  },

  button: {
    alignItems: "center",
    justifyContent: "center",
    height: 40
  },

  buttonText: {
    backgroundColor: "transparent",
    color: "rgb(34,88,220)"
  },

  text: {
    backgroundColor: "transparent",
    fontSize: 16,
    alignSelf: "flex-start",
    textAlign: "left",
    margin: 8
  }
});
