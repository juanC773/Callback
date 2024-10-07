module Demo
{
    class Response{
        long responseTime;
        string value;
        long quantityOfRequestServer;
    }

    interface Callback{
        void reportResponse(Response response);
    }

    interface Printer
    {
        void printString(string s, Callback* callback);

        
    }
}

