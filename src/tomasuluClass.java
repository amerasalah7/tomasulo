import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class tomasuluClass {

    public static int cycle = 0 ;
    public static  reservationStation[] addAndSubtractStations  ;
    public static  reservationStation[]  multiplyAndDivideStations  ;
    public static  loadBuffer[]  loadBuffers  ;
    public static  storeBuffer[]  storeBuffers  ;
    public static  double[] memory ;
    public static  HashMap<Integer,Double> cache ;
    public static  registerFile registerFile ;
    public static  HashMap<String,Integer> latency ;
    public static Queue<String> instructions ;
    public static Queue<published> bus ;
    public static ArrayList<tableEntry> statusTable ;
    public static ArrayList<tableEntry> statusTableFinished ;
    public static Queue<String> toBeExecuted ;
    public static Queue<Integer> cacheFIFO ;
    public static Queue<String> toBeRemoved ;



    public tomasuluClass ()
    {
        //number of add stations
        addAndSubtractStations = new reservationStation[3];
        for(int i = 0 ; i < addAndSubtractStations.length ; i++)
        {
            addAndSubtractStations[i]= new reservationStation();
        }
        //number of multiply stations
        multiplyAndDivideStations = new reservationStation[2];
        for(int i = 0 ; i < multiplyAndDivideStations.length ; i++)
        {
            multiplyAndDivideStations[i]= new reservationStation();
        }
        //number load buffer
        loadBuffers = new loadBuffer[3];
        for(int i = 0 ; i < loadBuffers.length ; i++)
        {
            loadBuffers[i]= new loadBuffer();
        }
        //number store buffer
        storeBuffers = new storeBuffer[2];
        for(int i = 0 ; i < storeBuffers.length ; i++)
        {
            storeBuffers[i]= new storeBuffer();
        }
        //number memory
        memory = new double[2000];
        memory[25]=5;
        memory[40]=6;
        //number cache
        cache = new HashMap<>();
        //register file
        registerFile = new registerFile();
        registerFile.setRegisterValue(20,1.99);
        // instructions
        instructions = new LinkedList<>();
        // bus
        bus = new LinkedList<>();
        // status table
        statusTable = new ArrayList<>();
        // status table finished
        statusTableFinished = new ArrayList<>();
        // to be executed
        toBeExecuted=new LinkedList<>();
        // keep track of values inserted into cache
        cacheFIFO = new LinkedList<>();
        // to be removed
        toBeRemoved = new LinkedList<>();

    }

    public void execution ()
    {
        latency = new HashMap<>();
        // take input from user
        Scanner sc=new Scanner(System.in);

        //ADD latency
        System.out.println("Enter add instruction latency: ");
        int add = sc.nextInt();
        latency.put("ADD",add);
        //Subtract latency
        System.out.println("Enter subtract instruction latency: ");
        int sub = sc.nextInt();
        latency.put("SUBTRACT",sub);
        //MUL latency
        System.out.println("Enter multiply instruction latency: ");
        int mul = sc.nextInt();
        latency.put("MULTIPLY",mul);
        //DIV latency
        System.out.println("Enter division instruction latency: ");
        int div = sc.nextInt();
        latency.put("DIVIDE",div);
        //load latency
        System.out.println("Enter load instruction latency: ");
        int load = sc.nextInt();
        latency.put("LOAD",load);
        //store latency
        System.out.println("Enter store instruction latency: ");
        int store = sc.nextInt();
        latency.put("STORE",store);

//        // intialize the latency hash map
//        latency = new HashMap<>();
//        // reading from the file latency
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new FileReader("src/latency"));
//            }
//        catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        try {
//            while (true) {
//                assert br != null;
//                if (!br.ready()) break;
//                String x = br.readLine();
//
//                String[] arr = x.split(" ");
//
//                for(int j = 0 ; j < arr.length ; j++)
//                {
//                    latency.put(arr[0],Integer.parseInt(arr[1]));
//                }
//            }
//        }
//            catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

        // reading from the file assemblyCode
        BufferedReader br1 = null;
        try {
            br1 = new BufferedReader(new FileReader("src/assemblyCode"));
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            while (true) {
                assert br1 != null;
                if (!br1.ready()) break;

                String x = br1.readLine();

                instructions.add(x);

            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Tomasolu  process starts

        // while loop start here

        while(!statusTable.isEmpty() || cycle== 0)
        {

            // cycle
            cycle++;

            // Issue an instructions first
            issueInstruction();
            // check if there are instructions to be executed
            executeInstruction();
            // check if any finished execution
            finishExecution();
            // check if there are eny values to be written
            checkWrite();
            // write in the register file
            addNewValues();
            //Check All Reservation Stations to know which instructions will start execution next cycle
            checkExecute();
            // remove any written instruction
            removeWritten();

            // printing values each cycle
            System.out.println("Cycle : "+ cycle);
            System.out.println("----------------------------------------------------------------------");
            System.out.println("instructions");
            System.out.println(instructions);
            System.out.println("----------------------------------------------------------------------");
            System.out.println("Status Table Finished");
            for (tableEntry tableEntry : statusTableFinished) {
                System.out.println(tableEntry);
            }
            System.out.println("----------------------------------------------------------------------");
            System.out.println("Status Table");
            for (tableEntry tableEntry : statusTable) {
                System.out.println(tableEntry);
            }
            System.out.println("----------------------------------------------------------------------");
            System.out.println("Reqister File");
            System.out.println(registerFile);
            System.out.println("----------------------------------------------------------------------");
            System.out.println("ADD/SUB Stations");
            for (reservationStation addAndSubtractStation : addAndSubtractStations) {
                System.out.println(addAndSubtractStation);
            }
            System.out.println("----------------------------------------------------------------------");
            System.out.println("MUL/DIV Stations");
            for (reservationStation multiplyAndDivideStation : multiplyAndDivideStations) {
                System.out.println(multiplyAndDivideStation);
            }
            System.out.println("----------------------------------------------------------------------");
            System.out.println("LOAD Stations");
            for (loadBuffer loadBuffer : loadBuffers) {
                System.out.println(loadBuffer);
            }
            System.out.println("----------------------------------------------------------------------");
            System.out.println("STORE Stations");
            for (storeBuffer storeBuffer : storeBuffers) {
                System.out.println(storeBuffer);
            }
            System.out.println("----------------------------------------------------------------------");
            System.out.println("Memory");
            for (int c = 0 ; c < 50; c++) {
                System.out.print(c+":"+memory[c]+", ");
            }
            System.out.println("----------------------------------------------------------------------");
            System.out.println();
            System.out.println("Cache");
            System.out.println(cache);
            System.out.println("----------------------------------------------------------------------");

            System.out.println("-----------------Enter a number to proceed to the next cycle---------  ");

            int a= sc.nextInt();


        }

    }

    public static void issueInstruction()
    {
        // dequeue the instruction to be issued if there is one .
        if(!instructions.isEmpty())
        {
            String instruction = instructions.peek();
            String[] instructionArray = instruction.split(" ");
            // check the instruction type
            if(instructionArray[0].equals("ADD.D") || instructionArray[0].equals("SUB.D"))
            {
                // check if there is available spot in the add reservation station
                int stationNumber = -1 ;
                for(int i = 0 ; i < addAndSubtractStations.length ; i++)
                {
                    // a free reservation station is found
                    if(!addAndSubtractStations[i].busy)
                    {
                        // Add the instruction to the station
                        addAndSubtractStations[i].busy = true ;
                        addAndSubtractStations[i].instruction = instructionArray[0];
                        stationNumber = i ;
                        break;
                    }
                }

                if(stationNumber != -1) {
                    // check the availability of the data in the register file
                    instruction = instructions.remove();
                    // first operand
                    String operandOne = instructionArray[2];
                    String operandOneState = registerFile.getRegisterState(Integer.parseInt(operandOne.substring(1)));
                    if (operandOneState.equals("")) {
                        // clean value
                        addAndSubtractStations[stationNumber].Vj = registerFile.getRegisterValue(Integer.parseInt(operandOne.substring(1)));
                    } else {
                        // dirty value
                        addAndSubtractStations[stationNumber].Qj = operandOneState;
                    }
                    // Second operand
                    String operandTwo = instructionArray[3];
                    String operandTwoState = registerFile.getRegisterState(Integer.parseInt(operandTwo.substring(1)));
                    if (operandTwoState.equals("")) {
                        // clean value
                        addAndSubtractStations[stationNumber].Vk = registerFile.getRegisterValue(Integer.parseInt(operandTwo.substring(1)));
                    } else {
                        // dirty value
                        addAndSubtractStations[stationNumber].Qk = operandTwoState;
                    }

                    // Add the destination register to the register file
                    String destRegister = instructionArray[1];
                    String newState = "A"+(stationNumber+1);
                    registerFile.setRegisterState(Integer.parseInt(destRegister.substring(1)), newState);

                    // Add the entry to the status table
                    tableEntry aNumber = new tableEntry();
                    aNumber.issue = cycle;
                    aNumber.instruction = instructionArray[0];
                    aNumber.operandOne = operandOne;
                    aNumber.operandTwo = operandTwo;
                    aNumber.destination = destRegister;
                    aNumber.stationID = newState ;
                    statusTable.add(aNumber);
                }

            }
            else if(instructionArray[0].equals("MUL.D") || instructionArray[0].equals("DIV.D"))
            {
                // check if there is available spot in the add reservation station
                int stationNumber = -1 ;
                for(int i = 0 ; i < multiplyAndDivideStations.length ; i++)
                {
                    // a free reservation station is found
                    if(!multiplyAndDivideStations[i].busy)
                    {
                        // Add the instruction to the station
                        multiplyAndDivideStations[i].busy = true ;
                        multiplyAndDivideStations[i].instruction = instructionArray[0];
                        stationNumber = i ;
                        break;
                    }
                }

                if(stationNumber != -1) {
                    // check the availability of the data in the register file
                    instruction = instructions.remove();
                    // first operand
                    String operandOne = instructionArray[2];
                    String operandOneState = registerFile.getRegisterState(Integer.parseInt(operandOne.substring(1)));
                    if (operandOneState.equals("")) {
                        // clean value
                        multiplyAndDivideStations[stationNumber].Vj = registerFile.getRegisterValue(Integer.parseInt(operandOne.substring(1)));
                    } else {
                        // dirty value
                        multiplyAndDivideStations[stationNumber].Qj = operandOneState;
                    }
                    // Second operand
                    String operandTwo = instructionArray[3];
                    String operandTwoState = registerFile.getRegisterState(Integer.parseInt(operandTwo.substring(1)));
                    if (operandTwoState.equals("")) {
                        // clean value
                        multiplyAndDivideStations[stationNumber].Vk = registerFile.getRegisterValue(Integer.parseInt(operandTwo.substring(1)));
                    } else {
                        // dirty value
                        multiplyAndDivideStations[stationNumber].Qk = operandTwoState;
                    }

                    // Add the destination register to the register file
                    String destRegister = instructionArray[1];
                    String newState = "D"+(stationNumber+1);
                    registerFile.setRegisterState(Integer.parseInt(destRegister.substring(1)), newState);

                    // Add the entry to the status table
                    tableEntry aNumber = new tableEntry();
                    aNumber.issue = cycle;
                    aNumber.instruction = instructionArray[0];
                    aNumber.operandOne = operandOne;
                    aNumber.operandTwo = operandTwo;
                    aNumber.destination = destRegister;
                    aNumber.stationID = newState ;
                    statusTable.add(aNumber);
                }

            }
            else if(instructionArray[0].equals("L.D"))
            {
                // check if there is available spot in the add reservation station
                int stationNumber = -1 ;
                for(int i = 0 ; i < loadBuffers.length ; i++)
                {
                    // a free reservation station is found
                    if(!loadBuffers[i].busy)
                    {
                        // Add the instruction to the station
                        loadBuffers[i].busy = true ;
                        stationNumber = i ;
                        break;
                    }
                }

                if(stationNumber != -1) {

                    instruction = instructions.remove();
                    // Add the destination register to the register file
                    String destRegister = instructionArray[1];
                    String newState = "L"+(stationNumber+1);
                    registerFile.setRegisterState(Integer.parseInt(destRegister.substring(1)), newState);

                    // effective address
                    String effAddress = instructionArray[2];
                    loadBuffers[stationNumber].effectiveAddress = Integer.parseInt(effAddress);

                    // Add the entry to the status table
                    tableEntry aNumber = new tableEntry();
                    aNumber.issue = cycle;
                    aNumber.instruction = instructionArray[0];
                    aNumber.destination = destRegister;
                    aNumber.address = Integer.parseInt(effAddress);
                    aNumber.stationID = newState ;
                    statusTable.add(aNumber);
                }
            }
            else if(instructionArray[0].equals("S.D"))
            {
                // check if there is available spot in the add reservation station
                int stationNumber = -1 ;
                for(int i = 0 ; i < storeBuffers.length ; i++)
                {
                    // a free reservation station is found
                    if(!storeBuffers[i].busy)
                    {
                        // Add the instruction to the station
                        storeBuffers[i].busy = true ;
                        stationNumber = i ;
                        break;
                    }
                }

                if(stationNumber != -1) {
                    // check the availability of the data in the register file
                    instruction = instructions.remove();
                    // operand
                    String operand = instructionArray[1];
                    String operandState = registerFile.getRegisterState(Integer.parseInt(operand.substring(1)));
                    if (operandState.equals("")) {
                        // clean value
                        storeBuffers[stationNumber].Vj = registerFile.getRegisterValue(Integer.parseInt(operand.substring(1)));
                    } else {
                        // dirty value
                        storeBuffers[stationNumber].Qj = operandState;
                    }

                    // effective address
                    String address = instructionArray[2];
                    storeBuffers[stationNumber].effectiveAddress = Integer.parseInt(address);

                    // Add the entry to the status table
                    tableEntry aNumber = new tableEntry();
                    aNumber.issue = cycle;
                    aNumber.instruction = instructionArray[0];
                    aNumber.operandOne = operand;
                    aNumber.address = Integer.parseInt(address);
                    aNumber.stationID = "S"+(stationNumber+1) ;
                    statusTable.add(aNumber);
                }

            }

        }

    }

    public static void checkExecute()
    {
//        if(toBeExecuted.isEmpty()) {
//            // check Add/sub reservation stations
            for (int i = 0; i < addAndSubtractStations.length; i++) {
                if (Objects.equals(addAndSubtractStations[i].Qj, "") && Objects.equals(addAndSubtractStations[i].Qk, "") && !addAndSubtractStations[i].execution && addAndSubtractStations[i].busy) {
                    addAndSubtractStations[i].execution = true;
                    toBeExecuted.add("A" + (i + 1));
                }
            }
            // check mul/div reservation stations
            for (int i = 0; i < multiplyAndDivideStations.length; i++) {
                if (Objects.equals(multiplyAndDivideStations[i].Qj, "") && Objects.equals(multiplyAndDivideStations[i].Qk, "") && !multiplyAndDivideStations[i].execution && multiplyAndDivideStations[i].busy) {
                    multiplyAndDivideStations[i].execution = true;
                    toBeExecuted.add("D" + (i + 1));
                }
            }
            // check load buffer
            for (int i = 0; i < loadBuffers.length; i++) {
                if (!loadBuffers[i].execution && loadBuffers[i].busy) {
                    loadBuffers[i].execution = true;
                    toBeExecuted.add("L" + (i + 1));
                }
            }
            // check store buffer
            for (int i = 0; i < storeBuffers.length; i++) {
                if (Objects.equals(storeBuffers[i].Qj, "") && !storeBuffers[i].execution && storeBuffers[i].busy) {
                    storeBuffers[i].execution = true;
                    toBeExecuted.add("S" + (i + 1));
                }
            }
//        }
    }

    public static void executeInstruction()
    {
//        System.out.println("to be executed");
//        System.out.println(toBeExecuted);
        while(!toBeExecuted.isEmpty())
        {
            // dequeue the instruction to be executed
            String toBeExecutedInstruction = toBeExecuted.remove();
            // update the execution cycle of the instructions
            for (tableEntry tableEntry : statusTable) {
                if (tableEntry.stationID.equals(toBeExecutedInstruction)) {
                    tableEntry.execute = cycle;
                    break;
                }
            }
        }
    }

    public static void finishExecution()
    {
        for (tableEntry tableEntry : statusTable) {
            // check if it finished execution
            //ADD
            if (tableEntry.instruction.equals("ADD.D") && tableEntry.execute != 0) {
                int latencyVal = latency.get("ADD");
                if (tableEntry.execute + latencyVal - 1 == cycle) {
                    tableEntry.finish = true;
                    tableEntry.finishCycle = cycle;
                }
            }
            //Subtract
            if (tableEntry.instruction.equals("SUB.D") && tableEntry.execute != 0) {
                int latencyVal = latency.get("SUBTRACT");
                if (tableEntry.execute + latencyVal - 1 == cycle) {
                    tableEntry.finish = true;
                    tableEntry.finishCycle = cycle;
                }
            }
            //Multiply
            if (tableEntry.instruction.equals("MUL.D") && tableEntry.execute != 0) {
                int latencyVal = latency.get("MULTIPLY");
                if (tableEntry.execute + latencyVal - 1 == cycle) {
                    tableEntry.finish = true;
                    tableEntry.finishCycle = cycle;
                }
            }
            //DIVIDE
            if (tableEntry.instruction.equals("DIV.D") && tableEntry.execute != 0) {
                int latencyVal = latency.get("DIVIDE");
                if (tableEntry.execute + latencyVal - 1 == cycle) {
                    tableEntry.finish = true;
                    tableEntry.finishCycle = cycle;
                }
            }
            //LOAD
            if (tableEntry.instruction.equals("L.D") && tableEntry.execute != 0) {
                int latencyVal = latency.get("LOAD");
                if (tableEntry.execute + latencyVal - 1 == cycle) {
                    tableEntry.finish = true;
                    tableEntry.finishCycle = cycle;
                }
            }
            //Store
            if (tableEntry.instruction.equals("S.D") && tableEntry.execute != 0) {
                int latencyVal = latency.get("STORE");
                if (tableEntry.execute + latencyVal - 1 == cycle) {
                    tableEntry.finish = true;
                    tableEntry.finishCycle = cycle;
                }
            }
        }
    }

    public static void checkWrite()
    {
        for (tableEntry tableEntry : statusTable) {
            if (tableEntry.finish) {
                if (tableEntry.finishCycle != cycle && !tableEntry.inBuffer && bus.isEmpty()) {
                    // published object with attributes reservation station id and the value to be published
                    published toBePublished = new published();
                    if (!tableEntry.stationID.contains("S")) {
                        double toBeWritten = getResult(tableEntry.stationID);
                        toBePublished.value = toBeWritten;
                    }
                    toBePublished.reservationStation = tableEntry.stationID;
                    tableEntry.inBuffer = true;
                    tableEntry.write = cycle;
                    bus.add(toBePublished);
                }
            }
        }
    }

    public static double getResult(String id)
    {
        if(id.contains("A")) {
            // then sub or add
                int stationNumber = Integer.parseInt(id.substring(1));
                reservationStation station = addAndSubtractStations[stationNumber-1];
                double firstOperand = station.Vj;
                double secondOperand = station.Vk;
                if (station.instruction.equals("ADD.D")) {
                    return firstOperand + secondOperand;
                } else {
                        return firstOperand - secondOperand;
                }
        }
        if(id.contains("D")) {
            // MUL or DIV
            int stationNumber = Integer.parseInt(id.substring(1));
            reservationStation station = multiplyAndDivideStations[stationNumber-1];
            double firstOperand = station.Vj;
            double secondOperand = station.Vk;
            if (station.instruction.equals("DIV.D")) {
                return firstOperand / secondOperand;
            } else {
                return firstOperand * secondOperand;
            }
        }
        if(id.contains("L")){
            // Load
            int stationNumber = Integer.parseInt(id.substring(1));
            loadBuffer station = loadBuffers[stationNumber-1];
            int address = station.effectiveAddress;
            if (!cache.containsKey(address)) {
                if (cache.size() == 100) {
                    int fifo = cacheFIFO.remove();
                    cache.remove(fifo);
                }
                    cache.put(address, memory[address]);
                    cacheFIFO.add(address);
            }
            return cache.get(address);
        }

        return 0 ;
    }

    public static void addNewValues()
    {
//        System.out.println("BUS ");
//        System.out.println(bus);

        if(!bus.isEmpty()) {

            published toBeWritten = bus.remove();

            // check if register file need it
            for (int i = 0; i < 32; i++) {
                if (registerFile.getRegisterState(i).equals(toBeWritten.reservationStation))
                {
                    registerFile.setRegisterState(i,"");
                    registerFile.setRegisterValue(i,toBeWritten.value);
                }
            }
            // check if any of the reservation stations need them
            for (reservationStation addAndSubtractStation : addAndSubtractStations) {
                if (!addAndSubtractStation.execution && addAndSubtractStation.busy) {
                    if (addAndSubtractStation.Qj.equals(toBeWritten.reservationStation)) {
                        addAndSubtractStation.Qj = "";
                        addAndSubtractStation.Vj = toBeWritten.value;
                    }

                    if (addAndSubtractStation.Qk.equals(toBeWritten.reservationStation)) {
                        addAndSubtractStation.Qk = "";
                        addAndSubtractStation.Vk = toBeWritten.value;
                    }
                }
            }
            // check if any of the reservation stations need them
            for (reservationStation multiplyAndDivideStation : multiplyAndDivideStations) {

                if (!multiplyAndDivideStation.execution && multiplyAndDivideStation.busy) {

                    if (multiplyAndDivideStation.Qj.equals(toBeWritten.reservationStation)) {

                        multiplyAndDivideStation.Qj = "";
                        multiplyAndDivideStation.Vj = toBeWritten.value;
                    }

                    if (multiplyAndDivideStation.Qk.equals(toBeWritten.reservationStation)) {
                        multiplyAndDivideStation.Qk = "";
                        multiplyAndDivideStation.Vk = toBeWritten.value;
                    }
                }
            }
            // check if any of the store buffers need the value
            for (storeBuffer storeBuffer : storeBuffers) {
                if (!storeBuffer.execution && storeBuffer.busy) {
                    if (storeBuffer.Qj.equals(toBeWritten.reservationStation)) {
                        storeBuffer.Qj = "";
                        storeBuffer.Vj = toBeWritten.value;
                    }
                }
            }

            // handle removing from the table status and the stations
            handleRemove(toBeWritten.reservationStation);

        }
    }

    public static void handleRemove(String id)
    {
        if(id.contains("A"))
        {
            // then one of the add/sub instructions
            int station = Integer.parseInt(id.substring(1));
            reservationStation toBeRemoved = addAndSubtractStations[station-1];
            toBeRemoved.busy = false ;
            toBeRemoved.execution=false ;
            toBeRemoved.Vj=0;
            toBeRemoved.Vk=0;
            toBeRemoved.instruction="";
            toBeRemoved.finishExecution=false;
        }
        if(id.contains("D"))
        {
            // then one of the mul/div instructions
            int station = Integer.parseInt(id.substring(1));
            reservationStation toBeRemoved = multiplyAndDivideStations[station-1];
            toBeRemoved.busy = false ;
            toBeRemoved.execution=false ;
            toBeRemoved.Vj=0;
            toBeRemoved.Vk=0;
            toBeRemoved.instruction="";
            toBeRemoved.finishExecution=false;
        }
        if(id.contains("L"))
        {
            // then one of the load instructions
            int station = Integer.parseInt(id.substring(1));
            loadBuffer toBeRemoved = loadBuffers[station-1];
            toBeRemoved.busy = false ;
            toBeRemoved.execution=false ;
            toBeRemoved.finishExecution=false;
            toBeRemoved.effectiveAddress=0;
        }
        if(id.contains("S"))
        {
            // then one of the store instructions
            int station = Integer.parseInt(id.substring(1));
            storeBuffer toBeRemoved = storeBuffers[station-1];
            toBeRemoved.busy = false ;
            toBeRemoved.execution=false ;
            toBeRemoved.finishExecution=false;
            toBeRemoved.effectiveAddress=0;
        }

        toBeRemoved.add(id);
    }

    public static void removeWritten()
    {
        // remove from the status table
        if(!toBeRemoved.isEmpty()) {
            String id = toBeRemoved.remove();
            for (int i = 0; i < statusTable.size(); i++) {
                if (statusTable.get(i).stationID.equals(id)) {
                    tableEntry a = statusTable.remove(i);
                    statusTableFinished.add(a);
                    break;
                }
            }
        }
    }

    public static void main(String[]args)
    {
        tomasuluClass check = new tomasuluClass();
        check.execution();
    }
}
