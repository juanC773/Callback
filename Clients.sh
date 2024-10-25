#!/usr/bin/expect -f

# Configuración de variables
set timeout -1
set password "swarch"
set hosts {hgrid2 hgrid3 hgrid4 hgrid5 hgrid6 hgrid7 hgrid8 hgrid9 hgrid10 hgrid11 hgrid12 hgrid13 hgrid14 hgrid15 hgrid16 hgrid17 hgrid18 hgrid19}

# Copiar inicialmente a xhgrid1
spawn scp -o StrictHostKeyChecking=no -r . swarch@xhgrid1:~/Callback
expect {
    "password:" { send "$password\r" }
    timeout { puts "Timeout en SCP inicial"; exit 1 }
}
expect eof

# Conectar a xhgrid20 y compilar
spawn ssh -o StrictHostKeyChecking=no swarch@xhgrid1
expect "password:" { send "$password\r" }
expect "$ " { send "cd Callback\r" }
expect "$ " { send "chmod +x ./gradlew\r" }
expect "$ " { send "./gradlew build\r" }
expect "$ "

# Iterar sobre cada host
foreach host $hosts {
    # Copiar archivos al host
    spawn scp -o StrictHostKeyChecking=no -r . swarch@$host:~/Callback
    expect {
        "password:" { send "$password\r" }
        timeout { puts "Timeout en SCP para $host"; continue }
    }
    expect eof
    
    # Conectar y ejecutar comandos en el host
    spawn ssh -o StrictHostKeyChecking=no swarch@$host
    expect "password:" { send "$password\r" }
    
    # Preparar el cliente
    expect "$ " { send "cd ./client/build/libs\r" }
    expect "$ " { send "mkdir client\r" }
    expect "$ " { send "unzip client.jar -d ./client\r" }
    expect "$ " { send "rm -r client.jar\r" }
    expect "$ " { send "cd client\r" }
    expect "$ " { send "sed -i 's/localhost/$host/g' config.client\r" }
    expect "$ " { send "cd ..\r" }
    expect "$ " { send "jar cfm client.jar client/META-INF/MANIFEST.MF -C client .\r" }
    expect "$ " { send "rm -r client\r" }
    expect "$ " { send "java -jar client.jar\r" }
    expect "222222"
    expect "$ " { send "exit\r" }
    expect eof
}

# Limpiar Callback local
spawn rm -r Callback

# Esperar un tiempo prudencial para las pruebas
puts "Esperando 5 minutos para que finalicen las pruebas..."
sleep 300

# Limpiar Callback en hosts remotos y matar los procesos java
foreach host $hosts {
    spawn ssh -o StrictHostKeyChecking=no swarch@$host
    expect "password:" { send "$password\r" }
    expect "$ " { send "pkill -f 'java -jar client.jar'\r" }
    expect "$ " { send "rm -r Callback\r" }
    expect "$ " { send "exit\r" }
    expect eof
}