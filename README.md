# 收款监听器 (Payment Monitor)

一个基于Android NotificationListenerService的收款通知监听应用，支持监听支付宝和微信收款通知，提供现代化的Material Design 3界面。

## ✨ 功能特性

- 📱 **实时监听** - 自动监听支付宝和微信收款通知
- 💰 **收款记录** - 记录所有收款信息，包括金额、时间和来源
- 🎨 **现代界面** - 基于Material Design 3的现代化UI设计
- 📊 **滚动显示** - 流畅的收款记录列表滚动展示
- 🔄 **应用跳转** - 点击记录可跳转到对应的支付应用
- ⚡ **后台常驻** - 前台服务保证应用持续工作
- 🌙 **主题适配** - 支持浅色/深色主题自动切换

## 🛠️ 技术栈

- **UI框架**: Jetpack Compose + Material Design 3
- **架构模式**: MVVM + Repository Pattern
- **依赖注入**: Hilt
- **数据库**: Room Database
- **异步处理**: Kotlin Coroutines
- **核心服务**: NotificationListenerService + Foreground Service

## 📋 系统要求

- Android 8.0 (API level 26) 或更高版本
- 支持通知访问权限
- 建议加入电池优化白名单以确保后台运行

## 🚀 快速开始

### 安装应用
1. 从 [Releases](../../releases) 下载最新的APK文件
2. 在Android设备上安装APK
3. 首次启动应用

### 配置权限
1. **通知访问权限**: 
   - 设置 → 应用 → 特殊应用访问权限 → 通知使用权
   - 找到"收款监听器"并授权
2. **后台运行权限**:
   - 设置 → 电池 → 后台应用管理
   - 将应用设置为"允许"
3. **自启动权限** (部分厂商):
   - 设置 → 应用启动管理
   - 允许应用自启动

### 开始使用
- 完成权限配置后，应用将自动开始监听收款通知
- 状态栏会显示"收款监听服务"正在运行
- 收到支付宝或微信收款时，应用会自动记录

## 📱 界面预览

*界面截图将在UI开发完成后添加*

## 🔧 开发环境

### 云端构建 (推荐)
本项目配置了GitHub Actions自动构建：
- 推送代码到仓库自动触发构建
- 构建完成后可下载APK文件
- 支持自动发布Release版本

### 本地开发
如需本地开发，请安装：
- Android Studio (最新版本)
- JDK 17
- Android SDK (API 34)

## 📝 开发计划

- [x] ✅ 项目基础架构搭建
- [x] ✅ GitHub Actions自动构建配置
- [ ] 🔄 NotificationListenerService核心服务实现
- [ ] 📊 收款信息解析逻辑开发
- [ ] 💾 Room数据库和数据模型创建
- [ ] 🎨 Jetpack Compose现代化UI构建
- [ ] 📋 收款记录列表界面实现
- [ ] 🔗 应用跳转功能添加
- [ ] ⚙️ 前台服务和权限处理
- [ ] 🚀 应用优化和发布准备

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/新功能`)
3. 提交更改 (`git commit -am '添加新功能'`)
4. 推送分支 (`git push origin feature/新功能`)
5. 创建 Pull Request

## 📄 开源协议

本项目采用 MIT 协议 - 详情请查看 [LICENSE](LICENSE) 文件

## ⚠️ 免责声明

- 本应用仅用于个人收款记录管理，不涉及任何资金操作
- 请确保在合法合规的前提下使用本应用
- 开发者不承担因使用本应用导致的任何损失

## 📞 支持与反馈

如果您在使用过程中遇到问题或有改进建议，请：
- 提交 [Issue](../../issues)
- 发起 [Discussion](../../discussions)

---
*使用GitHub Actions云端构建 | 无需本地Android开发环境*