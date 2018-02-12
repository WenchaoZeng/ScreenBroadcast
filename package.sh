mvn clean
mvn package

# 拷贝文件到目标目录
rm -rdf ./target/release
mkdir ./target/release
cp ./target/screenbroadcast*.jar ./target/release/ScreenbBroadcast.jar
cp -R ./web ./target/release/

# mac app打包: http://centerkey.com/mac/java/
jdk=$(/usr/libexec/java_home)
$jdk/bin/javapackager \
   -deploy \
   -native image \
   -name ScreenBroadcast \
   -BappVersion=0.0.0 \
   -Bicon=app.icns \
   -srcdir ./target/release \
   -srcfiles ScreenbBroadcast.jar:web/index.html \
   -appclass com.zwc.screenbroadcast.Main \
   -outdir target/release/result \
   -outfile ScreenBroadcast \
   -nosign \
   -v