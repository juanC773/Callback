
import Demo.Response;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {

    // Contador de solicitudes enviadas
    private static final AtomicLong sentRequestCount = new AtomicLong(0);

    // Contador de solicitudes perdidas
    private static final AtomicLong missedRequest = new AtomicLong(0);


    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        // Iniciar el monitor
       // MonitorClient.startMonitoring();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client",
                extraArgs)) {
            Response response = null;
            Demo.PrinterPrx service = Demo.PrinterPrx
                    .checkedCast(communicator.propertyToProxy("Printer.Proxy"));

            ObjectAdapter adapter = communicator.createObjectAdapter("callback");
            Demo.Callback callback = new MyCallback();

            ObjectPrx prx = adapter.add(callback, Util.stringToIdentity("callback"));
            Demo.CallbackPrx callbackPrx = Demo.CallbackPrx.checkedCast(prx);
            adapter.activate();

            if (service == null) {
                throw new Error("Invalid proxy");
            }

            //String message;
            boolean execute = true;
            String user = getUserHostString();

            System.out.println("Bienvenido"
                    + "\nIngrese:"
                    + "\n-Un numero n, para saber el fibonacci de ese numero y sus factores primos"
                    + "\n-La palabra listifs para ver las interfaces logicas del server"
                    + "\n-La palabra listports junto con una direccion ip para ver los servicios y los puertos en los que se ofrecen estos servicios en la ip dada. Ejemplo de cadena listports192.158.1.38"
                    + "\n-El caracter ! junto con un comando para ver el resultado de correr ese comando en el server. Ejemplo de string !comando"
                    + "\n-Escribe la palabra list clients para ver la lista de clientes registrados"
                    + "\n-Enviar un mensaje a otro usuario, ejemplo : 'to username-  mensaje para enviar'"
                    + "\n-Enviar un mensaje a todos, ejemplo : 'BC mensaje para enviar'"
                    + "\n-Exit para cerrar el programa");

           



            while (execute) {while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                sentRequestCount.incrementAndGet();
                
                // Send the request asynchronously
                CompletableFuture.runAsync(() -> {
                    try {
                        service.printString(user + message, callbackPrx);
                    } catch (Exception e) {
                        System.err.println("Error sending request: " + e.getMessage());
                    }
                }, executorService);

                System.out.println("Solicitud enviada. Puedes seguir enviando solicitudes.");
            }
        } 
    }




                
                

                
                /*
                 MyCallback myCallback = new MyCallback();

                Response response2 = myCallback.getActualResponse();

                if (response2 != null) {

                    // Calcular y mostrar la diferencia entre solicitudes enviadas y atendidas
                    long quantityOfRequestServer = response2.quantityOfRequestServer;
                    long sentRequests = sentRequestCount.get();
                    long missedRequestww = sentRequests - quantityOfRequestServer;

                    missedRequest.set(missedRequestww);
                }
                 */
               

           
        }
    










    private static String getUserHostString() {
        String username = System.getProperty("user.name");
        String hostname = getHostname();
        return username + ":" + hostname + " ";
    }

    private static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown-host";
        }
    }

    public static long getMissedRequest() {

        return missedRequest.get();

    }

    public static void resetMissRequest() {
        missedRequest.set(0);
    }

}


