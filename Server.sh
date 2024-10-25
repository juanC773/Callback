#!/usr/bin/expect -f

# Configura el tiempo de espera y la contraseña
set timeout -1
set password "swarch"

# Enviar el directorio Callback al host xhgrid20
spawn scp -o StrictHostKeyChecking=no -r . swarch@xhgrid20:~/Callback
expect "password:" { send "$password\r" }
expect eof

# Conectarse a xhgrid20
spawn ssh -o StrictHostKeyChecking=no swarch@xhgrid20
expect "password:" { send "$password\r" }
expect "$ " { send "cd Callback && chmod +x ./gradlew && ./gradlew build\r" }
expect "$ " { send "cd ./server/build/libs && mkdir server && unzip server.jar -d ./server && rm -r server.jar\r" }
expect "$ " { send "cd server && sed -i 's/localhost/192.168.131.140/g' config.server && cd ..\r" }
expect "$ " { send "jar cfm server.jar server/META-INF/MANIFEST.MF -C server . && rm -r server\r" }
expect "$ " { send "java -jar server.jar\r" }
expect eof
