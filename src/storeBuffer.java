public class storeBuffer {

    boolean busy ;
    int effectiveAddress ;
    String Qj ;
    double Vj ;
    Boolean execution ;
    Boolean finishExecution;

    public storeBuffer()
    {
        busy = false ;
        effectiveAddress = 0 ;
        Qj = "";
        Vj = 0 ;
        execution = false ;
        finishExecution = false ;
    }

    public String toString()
    {
        String result = "";
        result=result+"Busy : "+busy+", ";
        result=result+"Qj : "+Qj+", ";
        result=result+"Vj : "+Vj+", ";
        result=result+"Effective Address: "+effectiveAddress+", ";
        return result;
    }
}
