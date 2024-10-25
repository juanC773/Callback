#!/bin/bash
./gradlew clean build
scp -r -o StrictHostKeyChecking=no -r ./server/build/libs/. swarch@xhgrid20:~/Callback
swarch
ssh -o StrictHostKeyChecking=no swarch@xhgrid20
swarch
cd Callback
mkdir server
unzip server.jar -d ./server
rm -r server.jar
cd server
sed -i 's/localhost/'192.168.131.140'/g' config.server
cd ..
jar cfm server.jar server/META-INF/MANIFEST.MF -C server .
rm -r server
java -jar server 