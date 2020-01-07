import { ReactRegistry, Garden, BarStyleDarkContent } from 'react-native-navigation-hybrid'
import App from './App'
import { withToast } from 'react-native-toast-hybrid'

Garden.setStyle({
  screenBackgroundColor: '#F8F8F8',
  topBarStyle: BarStyleDarkContent,
})

ReactRegistry.startRegisterComponent(withToast)
ReactRegistry.registerComponent('Tab1', () => App)
ReactRegistry.endRegisterComponent()

// boostrap from native, so we don't need to call Navigator.setRoot here.
