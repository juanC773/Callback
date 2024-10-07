module Demo
{
    class Response{
        long responseTime;
        string value;
        long quantityOfRequestServer;
    }

    interface Callback{
        void reportResponse(string response);
    }

    interface Printer
    {
        Response printString(string s);

        void fact(long n, Callback* callback);
    }
}

