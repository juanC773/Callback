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
        Response printString(string s, Callback* callback);

        void fact(long n, Callback* callback);
    }
}

