import Demo.Callback;
import Demo.Response;
import com.zeroc.Ice.Current;

public class MyCallback implements Callback {


    Response actualResponse=null;
   
    @Override
    public void reportResponse(Response response, Current current) {

        
        System.out.println("Respuesta del server con callback: " + response.value);

        //Actual response
        setActualResponse(response);

    }



    public Response getActualResponse(){

        return actualResponse;
    
    }

    public void setActualResponse(Response response){

        actualResponse=response;
        
    }

}
