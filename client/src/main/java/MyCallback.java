import Demo.Callback;
import Demo.Response;
import com.zeroc.Ice.Current;

public class MyCallback implements Callback {
    @Override
    public void reportResponse(String response, Current current) {
        // Maneja la respuesta del servidor
        System.out.println("Respuesta del servidor (callback): " + response);
    }
}
