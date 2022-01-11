import java.util.ArrayList;
import java.util.HashMap;

public class registerFile {

    double[] registerFileValue;
    String[] registerFileState;

    public registerFile()
    {
        registerFileValue =new double[32];
        // empty string clean value
        registerFileState =new String[32];
        for(int i = 0 ; i < registerFileState.length ; i++)
        {
            registerFileState[i] = "";
        }

    }

    public void setRegisterValue(int index , double value )
    {
        registerFileValue[index] = value ;
        registerFileState[index] = "" ;
    }
    public void setRegisterState(int index , String state )
    {
        registerFileState[index] = state ;
    }
    public String getRegisterState(int index  )
    {
        return registerFileState[index];
    }
    public double getRegisterValue(int index  )
    {
        return registerFileValue[index];
    }
    public String toString()
    {
        String result = "";
        for(int i = 0 ; i < registerFileState.length ; i++)
        {
            result=result+i+": "+registerFileState[i]+", ";
        }

        result+="\n";

        for(int i = 0 ; i < registerFileValue.length ; i++)
        {
            result=result+i+": "+registerFileValue[i]+", ";
        }

        return result;
    }

    public static void main (String[]args)
    {



    }

}
