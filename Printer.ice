module Demo
{
    class Response{
        long responseTime;
        string value;
        long quantityOfRequestServer;
    }
    interface Printer
    {
        Response printString(string s);
    }
}