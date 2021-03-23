require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "ToastHybrid"
  s.version      = package["version"]
  s.summary      = package["description"]
 
  s.homepage     = "https://github.com/listenzz/react-native-toast-hybrid"
  s.license      = "MIT"
  s.authors      = { "listen" => "listenzz@163.com" }
  s.platforms    = { :ios => "10.0", :tvos => "10.0" }
  s.source       = { :git => "https://github.com/listenzz/react-native-toast-hybrid.git", :tag => "#{s.version}" }

  s.source_files = "ios/ToastHybrid/**/*.{h,m,swift}"
  s.resource_bundles = {
    'ToastHybrid' => ['ios/*.{storyboard,xib,xcassets,json,imageset,png}']
  }
  s.dependency "React"
  s.frameworks   = "CoreGraphics", "QuartzCore"
end