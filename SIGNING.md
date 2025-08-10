# Android应用签名配置指南

## 生成密钥库

使用以下命令生成用于签名的密钥库文件：

```bash
keytool -genkey -v -keystore payment-monitor.keystore -alias payment_monitor -keyalg RSA -keysize 2048 -validity 10000
```

执行命令时会提示输入以下信息：
- 密钥库密码
- 密钥别名密码
- 姓名、组织单位、组织、城市、省份、国家代码

## 配置GitHub Secrets

在GitHub仓库的Settings → Secrets and variables → Actions中添加以下secrets：

1. `KEYSTORE_FILE`: 密钥库文件的Base64编码
2. `KEYSTORE_PASSWORD`: 密钥库密码
3. `KEY_ALIAS`: 密钥别名 (通常是: payment_monitor)
4. `KEY_PASSWORD`: 密钥别名密码

### 获取密钥库文件的Base64编码

```bash
# macOS/Linux
base64 -i payment-monitor.keystore | tr -d '\n' | pbcopy

# Windows (PowerShell)
[Convert]::ToBase64String([IO.File]::ReadAllBytes("payment-monitor.keystore"))
```

## 本地签名配置

如果要在本地构建Release版本，在`app`目录下创建`keystore.properties`文件：

```properties
storeFile=../payment-monitor.keystore
storePassword=你的密钥库密码
keyAlias=payment_monitor
keyPassword=你的密钥别名密码
```

**注意：** `keystore.properties`文件已被添加到`.gitignore`中，不会被提交到版本控制。

## 更新构建配置

修改`app/build.gradle.kts`文件以支持签名配置：

```kotlin
android {
    signingConfigs {
        create("release") {
            val keystorePropsFile = rootProject.file("keystore.properties")
            if (keystorePropsFile.exists()) {
                val keystoreProps = java.util.Properties()
                keystoreProps.load(java.io.FileInputStream(keystorePropsFile))
                
                storeFile = file(keystoreProps["storeFile"] as String)
                storePassword = keystoreProps["storePassword"] as String
                keyAlias = keystoreProps["keyAlias"] as String
                keyPassword = keystoreProps["keyPassword"] as String
            }
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

## 自动发布流程

当你推送带有版本标签的提交时，GitHub Actions会自动构建并发布签名的APK：

```bash
# 创建版本标签并推送
git tag v1.0.0
git push origin v1.0.0
```

或者使用GitHub界面手动触发release工作流。

## 安全注意事项

1. 永远不要将密钥库文件或密码提交到版本控制中
2. 定期备份密钥库文件
3. 使用强密码保护密钥库
4. 考虑使用Google Play App Signing来管理发布密钥