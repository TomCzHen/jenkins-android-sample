# 项目说明

这是一个使用 `jenkinsfile` 构建 Android APK 的示例项目。

使用 docker-compose 可以快速的部署这个示例，由于 Android Build Tools 使用的 aapt 同时需要 32bit 和 64bit 运行环境，因此没有选择基于 Alpine 的 Jenkins 镜像。

项目分为 `master` `beta` `prod` 三个分支，分别对应开发环境、测试环境、生产环境，仅作为示例参考。

注：本示例仅在 Linux 下测试运行正常。

## docker-compose

修改项目中的 `.env` 文件的 `ANDROID_HOME` 值为你的 Android SDK 路径，然后执行 `docker-compose up -d` 启动容器后，可以通过 `http://ip:8080` 访问 Jenkins。

需要安装 [Blue Ocean Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Blue+Ocean+Plugin) 与 [Android Signing Plugin](https://wiki.jenkins.io/display/JENKINS/Android+Signing+Plugin) 插件。

在 Blue Ocean UI 中新建 Pipeline 添加本仓库即可。也可以先 fork 本项目，然后通过 GitHub Token 访问自己的帐号添加项目。

## Jenkinsfile

> 参考文档：

> [Blue Ocean ](https://jenkins.io/doc/book/blueocean/)

> [Pipeline Syntax](https://jenkins.io/doc/book/pipeline/syntax/)

> [Pipeline Steps Reference](https://jenkins.io/doc/pipeline/steps/)

所有的构建步骤都在 Jenkinsfile 中，不再通过 Web UI 添加，将 CI 也纳入版本控制。

注：Web Hook 仍然需要在 Web UI 中配置，但定时构建可以在 Jenkinsfile 中配置。

```
triggers {
    cron('H 4/* 0 0 1-5')
}
```

根据分支名作为 Stage 执行的条件：

```
when {
    branch 'prod'
}
```

并且加入了平台判断来执行不同的脚本：

```
if (isUnix()) {
    sh './gradlew clean assembleProd'
    } else {
    bat 'gradlew clean assembleProd'
}
```

### 参数输入

这里参数输入在构建中没有实际的作用，仅仅作为示例，可以根据实际需要修改构建脚本：

```
parameters {
    string(
            name: 'PERSON',
            defaultValue: 'Mr Jenkins',
            description: 'Who should I say hello to?'

    choice(
            name: 'BRANCH',
            choices: 'prod\ndev',
            description: 'Choice Branch'

    booleanParam(
            name: 'CAN_DANCE',
            defaultValue: true,
            description: 'Checkbox parameter'
    )
}
```

构建运行前的参数，手动执行时会提示输入参数，在 Stages 中可以通过 `params.PARAM_NAME` 的方式使用这些参数。

```gradle
stage('Initialize') {
    steps {
        echo 'Initialize...'
        echo "PERSON=${params.PERSON} BRANCH=${params.BRANCH} CAN_DANCE=${params.CAN_
        withEnv(['DISABLE_AUTH=true', 'DB_ENGINE=sqlite']) {
            echo "${env.DB_ENGINE} ${env.DISABLE_AUTH}"
            sh 'echo $DB_ENGINE $DISABLE_AUTH'
            echo getChangeString()
        }
    }
}
```

### Credentials

使用插件 [Credentials Plugin](https://wiki.jenkins.io/display/JENKINS/Credentials+Plugin) 来管理敏感配置信息。

**注意：由于本项目最终还是传入构建的 Android 代码中，最终仍然可以通过 Android 代码输出明文，因此实际上不具有保护意义的，仅作为演示使用。**

首先需要在 Credentials 中添加 ID 为 `BETA_SECRET_KEY` 与 `PROD_SECRET_KEY` 的 Secret Text。

在 Step 中通过 CredentialsID 可以读取 Jenkins 配置的 Credential 密文并赋值到变量 `SECRET_KEY`：

```gradle
steps {
    echo 'Building Beta APK...'
    withCredentials([string(credentialsId: 'BETA_SECRET_KEY', variable: 'SECRET_KEY')]) {
        script {
            if (isUnix()) {
                sh './gradlew clean assembleBetaDebug'
            } else {
                bat 'gradlew clean assembleBetaDebug'
            }
        }

}
```

从 Credentials 中获取值之后赋予环境变量 `SECRET_KEY`，然后在 Gradle 脚本中获取：

```
defaultConfig {
    applicationId "com.example.myfirstapp"
    minSdkVersion 23
    targetSdkVersion 26
    versionCode 1
    versionName "1.0"
    testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    buildConfigField "String", "SECRET_KEY", String.format("\"%s\"", System.getenv("SECRET_KEY") ?: "Develop Secret Key")
}
```

然后在 Android 项目代码中通过 `BuildConfig` 类来使用：

```java
secretKeyTextView.setText(BuildConfig.SECRET_KEY);
```

### Android Sign 签名证书

使用了 [Android Signing Plugin](https://wiki.jenkins.io/display/JENKINS/Android+Signing+Plugin) 来保护签名文件及密钥。

因为 Credentials Plugin 只支持 `PKCS#12` 格式的证书，因此先需要将生成好的 `JKS` 证书转换为 `PKCS#12` 格式：

```
keytool -importkeystore -srckeystore tomczhen.jks -srcstoretype JKS -deststoretype PKCS12 -destkeystore tomczhen.p12
```

将转换好的证书上传到 Credentials 中并配置好 ID，本项目中使用了 `ANDROID_SIGN_KEY_STORE` 作为 ID:

```gradle
steps {
    echo 'Sign APK'
    signAndroidApks(
            keyStoreId: "ANDROID_SIGN_KEY_STORE",
            keyAlias: "tomczhen",
            apksToSign: "**/*-prod-release-unsigned.apk",
            archiveSignedApks: false,
            archiveUnsignedApks: false
    )
}
```

## Gradle

> 参考文档：

> [Configure Build Variants](https://developer.android.com/studio/build/build-variants.html)

### Product Flavors

使用了 Product Flavors 来区分不同环境配置的包。

```gradle
productFlavors {
    dev {
        applicationIdSuffix ".dev"
        versionNameSuffix "-dev"
        resValue("string", "version_name_suffix", getVersionNameSuffix())
    }
    beta {
        applicationIdSuffix ".beta"
        versionNameSuffix "-beta"
        resValue("string", "version_name_suffix", getVersionNameSuffix())
    }
    prod {
        resValue("string", "version_name_suffix", "")
    }
}
```

同时还使用了 `resValue` 方法根据构建配置添加 string 资源，最终在 Android 代码中使用。

```java
versionNameSuffixTextView.setText(getString(R.string.version_name_suffix));
```

# TODO

* 测试环节
* 自动收集构建产物并通过 API 上传到测试平台
* 更优雅的处理构建失败问题