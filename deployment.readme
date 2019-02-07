Настройка сборочной машины:
- jdk/jre 1.8
- android sdk (https://developer.android.com/studio/) - command line tools only
- лицензия на sdk - (где взять?)
- ключи (где их взять?) - разместить в локальной папке сборочной машины

Получить исходники:
- Установить git
- git clone ssh://git@gitlabce.indev-group.eu:10022/profiles/profilesandroid.git

- cd profilesandroid/
- в local.properties установить путь к android sdk
- в gradle.properties установить переменные-пути: 
    Keys.repo=[путь к папке с ключами, размещёнными выше]
    service_account=[ имя json-файла ключа сервисаного аккаунта(лежит также в папке с ключами)]
    * profiles-upload-keystore.jks
    * profiles.properties
    * api-8539613066776331638-470215-b9a28f59aa25.json

Запустить сборку/размещение:
 - gradlew.bat [действие][flavour][config][packagetype]
    например: 
    gradlew.bat assembleDevelopmentDebug - соберёт debug сборку
    с переменным для development - конфигурации 
    
    gradlew.bat publishDevelopmentReleaseBundle
    (соберёт, подпишет и разместит в google play релизную сборку в формате app bundle
    с переменным для development - конфигурации -см. секцию  productFlavors  в app/build.gradle)


export ANDROID_HOME=android-sdk-linux
