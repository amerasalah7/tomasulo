public class tableEntry {
    int issue ;
    int execute ;
    int write ;
    String instruction ;
    String operandOne ;
    String operandTwo ;
    String destination ;
    int address ;
    String stationID ;
    Boolean finish ;
    boolean inBuffer ;
    int finishCycle ;

    public tableEntry()
    {

        issue = 0 ;
        execute = 0 ;
        write = 0 ;
        instruction = "";
        operandOne = "";
        operandTwo= "" ;
        destination = ""  ;
        address = 0 ;
        stationID = "";
        finish = false ;
    }
    public String toString()
    {
        String result = "";
        result=result+"ID : "+stationID+", ";
        result=result+"Instruction : "+instruction+", ";
        result=result+"Destination : "+destination+", ";
        result=result+"Operand One : "+operandOne+", ";
        result=result+"Operand Two : "+operandTwo+", ";
        result=result+"Address : "+address+", ";
        result=result+"Issue : "+issue+", ";
        result=result+"Execute : "+execute+", ";
        result=result+"Finish Execute : "+finishCycle+", ";
        result=result+"Write : "+write+", ";
        return result;
    }
}
