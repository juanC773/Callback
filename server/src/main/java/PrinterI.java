import Demo.CallbackPrx;
import Demo.Response;
import java.util.ArrayList;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;



public class PrinterI implements Demo.Printer
{
    private static ArrayList<Integer> fib = new ArrayList<Integer>();






    // Lista para almacenar los hostnames
      private static final ArrayList<String> userList = new ArrayList<>();
    
      // Mapa para almacenar los proxies de los usuarios
    private static final HashMap<String, CallbackPrx> userProxies = new HashMap<>();



    
     //Throughput
     private static final AtomicLong requestCount = new AtomicLong(0);

     //Jitter of response 

     private static final ArrayList<Long> latencyList = new ArrayList<>();
     private static final ArrayList<Long> jitterList = new ArrayList<>();

     
     // Contador de solicitudes procesadas con éxito
     private static final AtomicLong processedRequestCount = new AtomicLong(0);

     // Contador de solicitudes fallidas (no procesadas)
    private static final AtomicLong failedRequestCount = new AtomicLong(0);


  



    public void printString(String s, CallbackPrx callback, com.zeroc.Ice.Current current) {


        System.out.println("el mensaje recibido en el server es: "+s);



        long startTime = System.nanoTime();
    
        // Incrementar el contador de solicitudes
        requestCount.incrementAndGet();


        if (s.contains("to ")) {
            System.out.println("\nMensaje contiene 'to', llamando a handleUserMessage\n");
            handleUserMessage(s, callback);  // Envía el mensaje completo a handleUserMessage
            return;
        }

      
    
                System.out.println(s);
    
                String[] input = s.split(" ");
                String user = input[0];
                int number;
    
        try {

            
             
             if (!s.contains("list clients")) {
                // Almacenar el nombre de usuario en la lista si no está ya registrado
                if (!userList.contains(user)) {
                    userList.add(user);
                    userProxies.put(user, callback);  // Guardar el proxy del usuario
                    System.out.println("Usuario " + user + " registrado con su proxy.");



                }

    
                number = Integer.parseInt(input[1]);
                System.out.println(user + "\nFibonacci: " + fibonacci(number) + "\nPrime factors: " + primeFactor(number));
    
                long endtime = System.nanoTime();
                long latency = endtime - startTime;
    
                // Guardar la latencia y calcular jitter
                if (!latencyList.isEmpty()) {
                    long previousLatency = latencyList.get(latencyList.size() - 1);
                    long jitter = Math.abs(latency - previousLatency);
                    jitterList.add(jitter);
                }
    
                latencyList.add(latency);
                processedRequestCount.incrementAndGet();
    
                long quantityOfRequestServer = processedRequestCount.get() + failedRequestCount.get();
                System.out.println("Latency process: " + latency);
                System.out.println();
    
                // Usar callback cuando esté la respuesta
                callback.reportResponse(new Response(0, "Server response: " + s, quantityOfRequestServer));
                return;
            }
        } catch (NumberFormatException e) {
            failedRequestCount.incrementAndGet();
            System.out.println("Error: " + e.getMessage());
        }
    

        if (s.contains("list clients")) {
            System.out.println("List of connected users: funcionalidad nueva");

           
        
            
            //Clientes registrados
            if(userList.isEmpty()){
                System.out.println("No hay clientes registrados");
            }else{
                System.out.println("Lista de clientes conectados: ");

                for (int i = 0; i < getUserList().size(); i++) {

                    System.out.println(getUserList().get(i));
                    
                }

                    System.out.println("Fin de la lista de clientes registrados");
                
            }

        } 
        
  

        
        
        
        
        
        
        else if (input[1].contains("listifs")) {
            printNetworkInterfaces();
        } else if (input[1].contains("listports")) {
            runNmapOnIp(input[1]);
        } else if (input[1].contains("!")) {
            executeCommand(input[1]);
        }
    
        long endtime = System.nanoTime();
        long latency = endtime - startTime;
    
        if (!latencyList.isEmpty()) {
            long previousLatency = latencyList.get(latencyList.size() - 1);
            long jitter = Math.abs(latency - previousLatency);
            jitterList.add(jitter);
        }
    
        latencyList.add(latency);
    
        long quantityOfRequestServer = processedRequestCount.get() + failedRequestCount.get();
    
        System.out.println("Latency process: " + latency);
    
        callback.reportResponse(new Response(0, "Server response: " + s, quantityOfRequestServer));
    
        return;
    }
    



   private void handleUserMessage(String s, CallbackPrx callback) {

    // Imprimir mensaje para debug
    System.out.println("\nLlegó al método handleUserMessage");
    System.out.println("Mensaje de entrada al método: " + s);
    
    // Eliminar todo el prefijo hasta la palabra "to" (incluyendo Admin:Juan)
    if (s.contains("to")) {
        // Encontrar el índice de la palabra "to"
        int toIndex = s.toLowerCase().indexOf("to");
        if (toIndex != -1) {
            s = s.substring(toIndex + 2).trim();  // Eliminar todo lo que está antes y el "to"
        }
    }

    // Separar el destinatario y el contenido del mensaje
    String[] parts = s.split("-", 2);

    // Verificar que haya un destinatario y un mensaje
    if (parts.length == 2) {
        String recipient = parts[0].trim();  // Destinatario
        String message = parts[1].trim();    // Mensaje

        // Imprimir destinatario y mensaje para debug
        System.out.println("\nRecipient: " + recipient + " | Message: " + message);

        // Verificar si el destinatario está registrado en el mapa de proxies
        if (userProxies.containsKey(recipient)) {
            CallbackPrx recipientProxy = userProxies.get(recipient);
            System.out.println("Enviando mensaje a " + recipient + ": " + message);
            recipientProxy.reportResponse(new Response(0, "Mensaje de " + recipient + ": " + message, requestCount.get()));
        } else {
            System.out.println("El usuario " + recipient + " no está registrado.");
            callback.reportResponse(new Response(1, "Error: El usuario " + recipient + " no está registrado.", requestCount.get()));
        }
    } else {
        // Si el formato del mensaje es incorrecto
        callback.reportResponse(new Response(1, "Formato inválido de mensaje.", requestCount.get()));
    }
}

    





    private int fibonacci(int n){
        if (fib.isEmpty()){
            fib.add(1);
            fib.add(1);
        }
        
        if(n <= 1){
            return 1;
        } else if (fib.size() < n){
            
            for (int i = 2; i <= n; i++){
                fib.add(i, fibonacci(i-1) + fibonacci(i-2));
            }

        }
        return fib.get(n);
    }

    private String primeFactor (int n){
        String message = "";

        int pf = 2;
        boolean firstPF = true;

        while (n > 1) {
            if (n%pf == 0){
                if (firstPF){
                    message = pf + "";
                }else {
                    message += " + " + pf;
                }
                n /= pf;
            } else {
                pf++;
            }
        }

        return message;
    }

    private static void printNetworkInterfaces (){
        try{

            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                System.out.println(netint.getDisplayName());
            }

            
        }catch (SocketException e){
            // Incrementar el contador de solicitudes fallidas
            failedRequestCount.incrementAndGet();
            System.out.println("Error: " + e.getMessage());

        }
    }
    
    private static void runNmapOnIp(String input) {
        String ip = extractIp(input);
        if (ip != null) {
            System.out.println("Extracted IP: " + ip);
            runNmap(ip);
        } else {
            System.out.println("Can not extract a valid ip address");
        }
    }

    private static String extractIp(String input) {
        Pattern pattern = Pattern.compile("listports\\s*(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static void runNmap(String ip) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("nmap", "-sn", ip);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
           // Incrementar el contador de solicitudes fallidas
           failedRequestCount.incrementAndGet();
           System.out.println("Error: " + e.getMessage());

        }
    }
    

    //

    private static void executeCommand(String input) {
        String command = extractCommand(input);
        if (command != null) {
            System.out.println("Extracted command: " + command);
            runCommand(command);
        } else {
            System.out.println("can not extract command.");
        }
    }

    private static String extractCommand(String input) {
        Pattern pattern = Pattern.compile("^\\s*!(\\S.*)\\s*$");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private static void runCommand(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                processBuilder.command("cmd.exe", "/c", command);
            } else {
                processBuilder.command("sh", "-c", command);
            }
            
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println("Result");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

        } catch (Exception e) {
            // Incrementar el contador de solicitudes fallidas
            failedRequestCount.incrementAndGet();
            System.out.println("Error: " + e.getMessage());

        }
    }




    //Throuhput

    public static long getRequestCount() {
        return requestCount.get();
    }
    
    public static void resetRequestCount() {
        requestCount.set(0);
    }



    //Jitter

    public static ArrayList<Long> getLatencyList(){

        return latencyList;

    }

    public static ArrayList<Long> getJitterList() {
        return jitterList;
    }


    public static long getRequestNotProcess(){

        return failedRequestCount.get();

    }


    public static void resetFailedRequestCount() {
        failedRequestCount.set(0);
    }


    public static long getRequestProcess(){

        return processedRequestCount.get();

    }


    public static void ProcessedRequestCount() {
        processedRequestCount.set(0);
    }




    public static ArrayList<String> getUserList() {
        return userList;
    }


   
    


}