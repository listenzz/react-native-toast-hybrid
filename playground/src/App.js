import React, { Component } from 'react';

import { Platform, StyleSheet, Text, View, TouchableOpacity } from 'react-native';

import HUD from 'react-native-hud';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' + 'Shake or press menu button for dev menu',
});

export default class App extends Component {
  componentDidMount() {}

  _onPressButton = () => {
    HUD.show();
  };

  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity onPress={this._onPressButton} activeOpacity={0.2} style={styles.button}>
          <Text style={styles.buttonText}> 点我 </Text>
        </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'stretch',
    backgroundColor: '#F5FCFF',
    paddingTop: 16 + 88,
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    height: 40,
  },

  buttonText: {
    backgroundColor: 'transparent',
    color: 'rgb(34,88,220)',
  },
});
