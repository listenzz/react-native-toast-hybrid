require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = "ProgressHUD"
  s.version      = package['version']
  s.summary      = "progress hud for react native app"

  s.authors      = { "listen" => "listenzz@163.com" }
  s.homepage     = "https://github.com/listenzz/react-native-hud"
  s.license      = package['license']
  s.platform     = :ios, "8.0"

  s.module_name  = 'ProgressHUD'

  s.source       = { :git => "https://github.com/listenzz/react-native-hud.git", :tag => "v#{s.version}" }
  s.source_files  = "ios/*.{h,m,swift}"

  s.dependency 'React'
  s.frameworks = 'UIKit'
end