# Github

Github中央仓库aar

# 1、build.gradle配置
```
apply plugin: 'com.android.library'
 
android {
    compileSdkVersion 26
    defaultConfig {
        minSdkVersion 21
        targetSdkVersion 26
        versionCode 1
        versionName "1.0"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
}
//////// 打包发布配置开始 ////////
apply plugin: 'maven'
ext {
    // 从Github上clone下来的项目的本地地址
    GITHUB_REPO_PATH = "F:\github\liblite"
    PUBLISH_GROUP_ID = 'com.keller'
    PUBLISH_ARTIFACT_ID = 'utils_lib'
    PUBLISH_VERSION = '1.0.1'
}
uploadArchives {
    repositories.mavenDeployer {
        def deployPath = file(project.GITHUB_REPO_PATH)
        repository(url: "file://${deployPath.absolutePath}")
        pom.project {
            groupId project.PUBLISH_GROUP_ID
            artifactId project.PUBLISH_ARTIFACT_ID
            version project.PUBLISH_VERSION
        }
    }
}
 
// 源代码一起打包
task androidSourcesJar(type: Jar) {
    classifier = 'sources'
    from android.sourceSets.main.java.sourceFiles
}
artifacts {
    archives androidSourcesJar
}
 
//////// 打包发布配置结束 ////////
————————————————
```

# 2、打包
```
gradlew uploadArchives
```

# 3、发布
```
git push
```

# 4、使用
project build.gradle加入
```
 maven { url "https://raw.githubusercontent.com/gionchat/liblite/master" }
```