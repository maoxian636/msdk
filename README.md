# XZABSdk

## 项目介绍
AB合集sdk

## 依赖

### 1. 添加仓库  
```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
### 2. 添加依赖
```
dependencies {
	        implementation 'com.github.maoxian636:msdk:Tag'
	}   
```

## 使用教程

### 1. 初始化
XConfigData传参数据

| 参数名             | 含义              | 条件 |
|-----------------|-----------------|----|
| requestPath     | 后台输入的接口路径     |  必须  |
| username        | 后台创建的用户名      | 必须   |
| language        | 后台的语言         |  可选  |
| naturalQuantity | 监测自然量         | 可选  |
```
     try {
            MSDKSingleton.getInstance(this).setMSDKConfig(XConfigData("xxxx", "aaaaaa"))
            MSDKSingleton.setDebug(true)
            MSDKSingleton.getInstance(this).initConfig()
        } catch (e: XException) {
            throw RuntimeException(e)
        }
```
### 2. 后台数据格式
数据需要转义，然后去掉空格

| 返回key      | 含义                      |
|--------------|-------------------------|
| monitorType  | true 表示Adjust           |
| ----------- | false 表示Appsflyer       |
| monitorName | 表示监控名称                  |
| monitorKey  | 表示监控key                 |
| monitorPath | 表示js交互路径                |
| adjust      | 表示需要监控的点击事件（使用adjust填入） |
|----------- | 偶数是事件名                  |
| ----------- | 奇数是对应的事件上报key           |
```
{
	"monitorType": true,
	"monitorName": "adjust",
	"monitorKey": "so5p7fwr25mo",
	"monitorPath": "jsBridge",
	"adjust": ["recharge","az5acd","register","k4ujxh","rechargeClick","ro2yy7","firstrecharge","gpe2wi"]
}
```