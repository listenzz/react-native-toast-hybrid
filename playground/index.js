import { ReactRegistry, Garden } from 'react-native-navigation-hybrid';
import App from './src/App';

// 配置全局样式
Garden.setStyle({
  topBarStyle: 'dark-content',
});

ReactRegistry.startRegisterComponent();
ReactRegistry.registerComponent('playground', () => App);
ReactRegistry.endRegisterComponent();
