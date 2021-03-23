import { ReactRegistry, Garden, BarStyleDarkContent } from 'hybrid-navigation'
import App from './App'

Garden.setStyle({
  screenBackgroundColor: '#F8F8F8',
  topBarStyle: BarStyleDarkContent,
})

ReactRegistry.startRegisterComponent()
ReactRegistry.registerComponent('Tab1', () => App)
ReactRegistry.endRegisterComponent()

// boostrap from native, so we don't need to call Navigator.setRoot here.
