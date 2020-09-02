#如何使用静态代码检查
    1、在需要检查的模块的build.gradle文件中添加
    apply from: '../config/quality.gradle'
    2、执行命令
      ./gradlew :xxx:checkstyle
      ./gradlew :xxx:pmd
      ./gradlew :xxx:lint
      ./gradlew :xxx:findbugs
      由于findbugs检查的是字节码需要先编译再检查
