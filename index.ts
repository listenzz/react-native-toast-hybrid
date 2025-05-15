import Navigation, {  BarStyleDarkContent } from 'hybrid-navigation';
import App from './App';

Navigation.setDefaultOptions({
  screenBackgroundColor: '#F8F8F8',
  topBarStyle: BarStyleDarkContent,
});

Navigation.startRegisterComponent();
Navigation.registerComponent('Tab1', () => App);
Navigation.endRegisterComponent();

// boostrap from native, so we don't need to call Navigator.setRoot here.
