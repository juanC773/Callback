#!/bin/bash

# Define el número de clientes a ejecutar en paralelo
num_clients=50

# Número grande que será enviado a cada cliente para calcular Fibonacci
large_number=45

# Archivo de salida para los resultados
output_file="C:/Users/Admin/Videos/arquibash/client_results.txt"

# Limpiar el archivo de salida anterior
> "$output_file"

# Función para ejecutar el cliente y redirigir su salida
echo "Ejecutando cliente $1"

run_client() {
    java -jar "C:/Users/Admin/Desktop/Universidad/Sexto semestre/Arqui soft/Callback/client/build/libs/client.jar" >> "$output_file"
}




# Correr los clientes en paralelo
for ((i=1; i<=num_clients; i++)); do
    echo "Ejecutando cliente $i"
    run_client $large_number &
done

# Esperar a que todos los clientes terminen
wait

echo "Todos los clientes han terminado. Revisa el archivo $output_file para los resultados."
