#!/bin/bash

# Número de clientes a ejecutar
NUM_CLIENTES=10

# Archivo para registrar los resultados
RESULT_FILE="C:/Users/Admin/Videos/arquibash/client_results.txt"
echo "Resultados de concurrencia:" > "$RESULT_FILE"

# Rutas a los directorios de código fuente y clases compiladas
SRC_DIR="C:/Users/Admin/Desktop/Universidad/Sexto semestre/Arqui soft/Callback/client/src/main/java"
BIN_DIR="C:/Users/Admin/Desktop/Universidad/Sexto semestre/Arqui soft/Callback/client/bin/main"

# Compilar el código Java
javac -d "$BIN_DIR" "$SRC_DIR/Client.java"

# Función que lanza un cliente
run_client() {
  java -cp "$BIN_DIR" Client "$1" >> "$RESULT_FILE"
}

# Lanza múltiples clientes en paralelo
for ((i = 1; i <= NUM_CLIENTES; i++)); do
  run_client "$i" &
done

# Esperar a que terminen todos los clientes
wait

echo "Ejecución finalizada. Resultados en $RESULT_FILE"