import Demo.Callback;
import Demo.Response;
import com.zeroc.Ice.Current;

public class MyCallback implements Callback {
   
    @Override
    public void reportResponse(Response response, Current current) {
        System.out.println("Respuesta del server con callback: " + response.value);
    }
}
