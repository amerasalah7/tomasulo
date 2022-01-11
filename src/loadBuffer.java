public class loadBuffer {
    boolean busy ;
    int effectiveAddress ;
    Boolean execution ;
    Boolean finishExecution ;

    public loadBuffer()
    {
        busy = false ;
        effectiveAddress = 0 ;
        execution = false ;
        finishExecution = false ;
    }
    public String toString()
    {
        String result = "";
        result=result+"Busy : "+busy+", ";
        result=result+"Effective Address : "+effectiveAddress+", ";
        return result;
    }

}
