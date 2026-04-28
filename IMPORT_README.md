---
AIGC:
    ContentProducer: Minimax Agent AI
    ContentPropagator: Minimax Agent AI
    Label: AIGC
    ProduceID: "00000000000000000000000000000000"
    PropagateID: "00000000000000000000000000000000"
    ReservedCode1: 304402207ee7c43f2a5e63ddce34a8b8946d13b238ef71312e44434faa83be5436a10112022049dbb49180396dfd93c4e126c5dc7fe292a10f8dc752ef44f82de9800e4e8bb2
    ReservedCode2: 30450220023a529896de6ab65fa6808d4a2befc816cfb63c2efa6193033ba8c9ae558dd1022100cf8b1baca75bbfadb4efdccecd6c756751af72ebd8e13d56e77a3e1547a4b896
---

# 极影桌面 - Android Studio 导入说明

## 导入步骤

### 1. 解压项目
将 `JiYingLauncher-AndroidStudio.zip` 解压到任意目录

### 2. 配置 SDK 路径
- 将 `local.properties.template` 重命名为 `local.properties`
- 填写您的 Android SDK 路径，例如：
  ```
  sdk.dir=/Users/xxx/Library/Android/sdk  # Mac
  sdk.dir=C:\\Users\\xxx\\AppData\\Local\\Android\\Sdk  # Windows
  sdk.dir=/home/xxx/Android/Sdk  # Linux
  ```

### 3. 用 Android Studio 打开
- 打开 Android Studio
- 选择 `File` -> `Open`
- 选择解压后的项目目录
- 等待 Gradle Sync 完成

### 4. 配置签名
项目已包含调试签名文件 `app/jiying.keystore`
- 密钥别名: jiying
- 密钥密码: jiying123
- 仓库密码: jiying123

## 项目结构

```
JiYingLauncher-AndroidStudio/
├── app/                          # 应用模块
│   ├── src/main/
│   │   ├── java/                 # Kotlin 源码
│   │   ├── res/                  # 资源文件
│   │   └── AndroidManifest.xml   # 清单文件
│   ├── build.gradle              # 模块构建配置
│   ├── proguard-rules.pro        # 混淆规则
│   └── jiying.keystore           # 签名文件
├── gradle/                       # Gradle Wrapper
├── build.gradle                  # 项目构建配置
├── settings.gradle               # 项目设置
└── README.md                     # 项目说明
```

## 主要功能模块

- **ui/main** - 主界面、桌面
- **ui/navigation** - 导航悬浮窗
- **ui/music** - 音乐播放器
- **ui/apps** - 应用商店、应用管理
- **service/** - 后台服务（音乐、导航保活等）
- **util/** - 工具类

## 编译要求

- Android Studio Arctic Fox 或更高版本
- Android SDK 34+
- JDK 11+
- Gradle 8.0+

## 常见问题

### Gradle Sync 失败
1. 检查网络连接
2. 配置国内镜像源（已在项目中配置）
3. 确保 JDK 版本正确

### 找不到 SDK
确保 `local.properties` 中的 `sdk.dir` 路径正确

### 签名配置
已在 `app/build.gradle` 中配置，如需修改请编辑该文件

## 联系方式

如有问题请查看 `README.md` 或联系开发者
