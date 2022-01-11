public class reservationStation {

    boolean busy ;
    String instruction ;
    String Qj;
    String Qk ;
    double Vj ;
    double Vk ;
    Boolean execution ;
    Boolean finishExecution ;

    public reservationStation ()
    {
        busy = false ;
        instruction = "";
        Qj = "" ;
        Qk = "" ;
        Vj = 0 ;
        Vk = 0 ;
        execution = false ;
        finishExecution = false ;

    }
    public String toString()
    {
        String result = "";
        result=result+"Busy : "+busy+", ";
        result=result+"Instruction : "+instruction+", ";
        result=result+"Qj : "+Qj+", ";
        result=result+"Qk : "+Qk+", ";
        result=result+"Vj : "+Vj+", ";
        result=result+"Vk : "+Vk+", ";
        return result;
    }

}
