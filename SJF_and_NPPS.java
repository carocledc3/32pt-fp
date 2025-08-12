// CARLOS ROMEO CLEMENTE DEL CASTILLO III -- CC22-1H
// DALE COFULAN KERCHATEN -- CC22-1H
// UNIVERSITY OF THE CORDILLERAS
// CC22/INTRODUCTION TO PLATFORM TECHNOLOGIES
// [32PT-FP] Final Project
// 12 Aug. 2025 R

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

// QUEUE CLASS
class Queue<E> {
    
    // QUEUES ARE `arrayList`s AT THEIR CORE
    ArrayList<E> queueCore = new ArrayList<E>();

    // POP = REMOVE FROM FRONT
    E pop() {
        E popped = queueCore.getFirst();
        queueCore.removeFirst();
        return popped;
    }

    // PEEK = LOOK AT FRONT
    E peek() {return queueCore.getFirst();}
    void push(E obj) {
        queueCore.addLast(obj);
    }
    
    // GET SIZE
    int length() {return queueCore.size();}

    // IS EMPTY
    boolean isEmpty() {return queueCore.isEmpty();}

    // OVERRIDE FOR PRINTING
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < queueCore.size(); i++) {
            result += "| " + queueCore.get(i).toString() + " ";
        }
        result += "|";
        return result;
    }
}

// HI DALE I MANAGED THE OBJECT-ORIENTED APPROACH
class Process {
    // FOR COMPARING, SORT BY ARRIVAL TIME
    public int compareTo(Process otherProcess) {
        return Integer.compare(arrivalTime, otherProcess.arrivalTime);
    }

    // ATTRIBUTES
    int priority;
    int arrivalTime;
    int execTime;
    int burstTime;
    int PID;
    int completionTime;
    int turnAroundTime;
    int waitingTime;
    
    // CONSTRUCTOR
    public Process(int pid, int priority, int at, int bt) {
        this.PID = pid;
        this.arrivalTime = at;
        this.burstTime = bt;
        this.priority = priority;
    }

    // OVERRIDE FOR PRINTING
    @Override
    public String toString() { return "P" + PID;}
}

public class SJF_and_NPPS {

    // ALL PROCESSES. FOR SORTING
    static ArrayList<Process> allProcesses = new ArrayList<>();

    // UPPER NPPS QUEUE
    static ArrayList<Process> upperQueue = new ArrayList<>();

    // LOWER SJF QUEUE
    static ArrayList<Process> lowerQueue = new ArrayList<>();

    // READY QUEUE
    static Queue<Process> readyQueue = new Queue<Process>();

    // DONE PROCESSES
    static ArrayList<Process> doneProcesses = new ArrayList<>();

    // INITIALISE CURRENT PROCESS
    static Process currentProcess = null;
    static int burstTimeTicker = 0; // TICKER FOR CURRENT PROCESS'S BURST TIME
    static int CPUTime = 0; // TICKER FOR CPU TIME

    // splits String into String[] array with commas (and any amount of whitespace)
    // as delimiter
    private static String[] commaSeparate(String applejuice) {
        return applejuice.split(",\s*");
    }

    // method to check whether single string resolves to an int
    @SuppressWarnings("UseSpecificCatch")
    private static int intParser(String inp) {
        int res;
        try {
            res = Integer.parseInt(inp);
        } catch (Exception e) {
            return -1;
        }
        return res;
    }

    // method to check whether user input (of dimensions or of coordinates) is
    // indeed valid
    @SuppressWarnings("UseSpecificCatch")
    private static boolean intsParser(String orangejuice) {
        String[] orange = commaSeparate(orangejuice); // split input into String array based on commas
        for (String drop : orange) {
            try { // for each element in the split input,
                Integer.valueOf(drop); // check if it's an integer
            } catch (Exception e) {
                return false; // and return false if there's one that's not one
            }
        }
        // else, if it gets to this point, that means every part of the input is valid;
        // return true
        return true;
    }

    // input decoding. decodes a String input of comma-separated ints
    static int[] intsDecoder(String cheddar) {
        if (!intsParser(cheddar)) {
            int[] fail = { -1 }; // if input is not valid in the first place, return {-1}
            return fail;
        } else { // else, we know that the input is valid;
            String[] swissCheese = commaSeparate(cheddar); // separate input based on commas
            int[] parmesan = new int[swissCheese.length]; // and add each element of that to this new int[]
            for (int i = 0; i < swissCheese.length; i++) {
                parmesan[i] = Integer.parseInt(swissCheese[i]);
            }
            return parmesan; // return the int[]
        }
    }

        // lines for æsthetic purposes
    final static void line() {
        System.out.println("------------------------------------------------------------");
    }

    final static void doubleLine() {
        System.out.println("============================================================");
    }
    
    @SuppressWarnings({ "deprecation", "UseSpecificCatch" })
    public final static void clearConsole() { // method to clear console, thank you
        // https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                System.out.println("\u001b[2J" + "\u001b[H");
            }
        } catch (Exception E) {
            // je m'en câlisse
        }
    }
    
    // AVERAGES
    static double averageOfIntAL(ArrayList<Integer> input) {
        double res = 0;
        for (Integer i : input) { res += i / input.size();}
        return res;
    }

    // COMPUTATION OF AVERAGES

    public static void main(String[] args) {
        clearConsole();

        // INTRODUCTION
        doubleLine();
        System.out.println("CARLOS ROMEO CLEMENTE DEL CASTILLO III");
        System.out.println("DALE COFULAN KERCHATEN");
        System.out.println("CC22-1H: INTRODUCTION TO PLATFORM TECHNOLOGIES");
        System.out.println("FINAL PROJECT: MULTILEVEL QUEUE PROCESS SCHEDULING SIMULATOR");
        System.out.println("- UPPER QUEUE: NPPS (NON-PREEMPTIVE PRIORITY SCHEDULING)");
        System.out.println("- LOWER QUEUE: SJF (SHORTEST JOB FIRST)");
        doubleLine();

        // INITIALISE VARIABLES
        int numProcesses;
        String temp = "";
        Scanner inpnum;

        // HAVE USER INPUT NUMBER OF PROCESSES
        do {
            System.out.print("[?] How many processes do you want to simulate? >> ");
            inpnum = new Scanner(System.in);
            temp = inpnum.nextLine();
            numProcesses = intParser(temp);
        } while (intParser(temp) == -1);
        
        // CREATE PROCESSES ARRAY
        Process[] testProcesses = new Process[numProcesses];

        // ASK FOR PROCESS INFO FOR EVERY PROCESS...
        System.out.print("""
        [i] FOR EACH PROCESS:
        [i] Separated by commas, input in order:
        [i] The Priority Level, the Arrival Time, and the Burst Time.
        [i] Example: "3, 5, 6"
        [i] For Priority 3, Arrival Time t=5, Burst Time 6.
        """);
        for (int i = 1; i <= numProcesses; i++) {
        
        // STORE INPUT DECODE FAIL STATE
        int[] decodeFail = {-1};

        // UNTIL INPUT IS VALID, LOOP
            do {
            line();
            System.out.printf("""
            [[[ FOR PROCESS P%s ]]]
            """, i);
            System.out.print(" >> ");
            inpnum = new Scanner(System.in);
            temp = inpnum.nextLine();
            } while ( intsDecoder(temp) == decodeFail || intsDecoder(temp).length != 3 || temp.endsWith(","));

        // ARRAY TO STORE CURRENT PROCESS INFO
        int[] rawPI = intsDecoder(temp);
        // NEW PROCESS
        testProcesses[i-1] = new Process(i, rawPI[0], rawPI[1], rawPI[2]);

        }

        Process[] processes = testProcesses;
        ArrayList<Integer> TATs = new ArrayList<>();
        ArrayList<Integer> WTs = new ArrayList<>();

        allProcesses.addAll(Arrays.asList(testProcesses));

        // MAIN LOOP. THIS WILL RUN FOR EVERY ITERATION OF THE TICKER
        do { 
            // IF ALL PROCESSES ARE DONE, EXIT LOOP
            if(doneProcesses.size() == processes.length){
                break;
            }

            // SORT PROCESSES BY PRIORITY THEN ARRIVAL TIME
            allProcesses.sort(new Comparator<Process>() {
            @Override
            public int compare(Process a, Process b)
            {
                return a.priority - b.priority;
            }
        });
            allProcesses.sort(new Comparator<Process>() {
            @Override
            public int compare(Process a, Process b)
            {
                return a.arrivalTime - b.arrivalTime;
            }
        });

        // SORT UPPER (NPPS) QUEUE BY PRIORITY
        upperQueue.sort(new Comparator<Process>() {
            @Override
            public int compare(Process a, Process b)
            {
                return a.priority - b.priority;
            }
        });

        // SORT LOWER (SJF) QUEUE BY BURST TIME
        lowerQueue.sort(new Comparator<Process>() {
            @Override
            public int compare(Process a, Process b)
            {
                return a.burstTime - b.burstTime;
            }
        });

        // INITIALISE NEXT PROCESS TO BE PROCESSED
        Process nextProcess = null;

        if(!allProcesses.isEmpty() && (allProcesses.getFirst().arrivalTime <= CPUTime)) {
            // POP FRONT OF allProcesses to nextProcess
            nextProcess = allProcesses.getFirst();
            allProcesses.removeFirst();
        }

        // SORT THE PROCESS
        if(nextProcess != null) {
            // PROCESS GOES TO UPPER QUEUE IF PRIORITY IS LESS THAN 5. ELSE, LOWER QUEUE
            if(nextProcess.priority <= 5) {
                upperQueue.add(nextProcess);
            } else {
                lowerQueue.add(nextProcess);
            }
        }
    
            // ENQUEUE THE PROCESSES INTO THE READY QUEUE.
            if(upperQueue.isEmpty()) {
                // IF THE UPPER QUEUE IS EMPTY...
                if(!lowerQueue.isEmpty()) {
                    // BUT THE LOWER QUEUE IS NOT, POP FROM THE LOWER QUEUE
                    readyQueue.push(lowerQueue.getFirst());
                    lowerQueue.removeFirst();
                }
            } else {
                // ELSE, POP FROM THE UPPER QUEUE
                readyQueue.push(upperQueue.getFirst());
                upperQueue.removeFirst();
            }

            // EXECUTE THE PROCESSES.
            if(!readyQueue.isEmpty() && currentProcess == null) {
                // POP FROM THE READY QUEUE INTO currentProcess
                currentProcess = readyQueue.pop();
                // SET ITS EXECUTION BEGINNING TIME TO THE CURRENT CPU TIME
                currentProcess.execTime = CPUTime;
            }

            // IF THERE'S A CURRENT PROCESS AND IT'S NOT DONE...
            if(currentProcess != null && !(burstTimeTicker == currentProcess.burstTime)) {
                // TICK THE BURST TIME FORWARD
                burstTimeTicker++;
                // TICK CPU TIME FORWARD TOO
                CPUTime++;
            } else if (currentProcess != null) {
                // ELSE, IF THERE'S A CURRENT PROCESS AND ITS DONE...
                // COMPUTE COMPLETION TIME
                currentProcess.completionTime = CPUTime;
                
                // COMPUTE TURNAROUND TIME
                currentProcess.turnAroundTime = currentProcess.completionTime - currentProcess.arrivalTime;

                // COMPUTE WAITING TIME
                currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;

                // FOR COMPUTATION OF AVERAGES
                TATs.add(currentProcess.turnAroundTime);
                WTs.add(currentProcess.waitingTime);

                // ADD PROCESS TO THE LIST OF doneProcesses
                doneProcesses.add(currentProcess);
                burstTimeTicker = 0;
                currentProcess = null;

                // IF THE READY QUEUE IS NOT EMPTY, IMMEDIATELY EXECUTE THE NEXT PROCESS
                if(!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.pop();
                    currentProcess.execTime = CPUTime;
                    burstTimeTicker++;
                }

                // TICK CPU TIME FORWARD
                CPUTime++;
            } else { // ELSE, IF THERE'S NO CURRENT PROCESS...
                // TICK CPU TIME FORWARD
                CPUTime++;
            }
            
        } while (true);

        // DISPLAY OF GANTT CHART. NOT INCLUDING IDLE TIME (NOT SURE HOW TO INCLUDE THAT)
        line();
        String gantt = "";
        for (Process i : doneProcesses) {
            gantt += String.format("| P%s ", i.PID);
        }
        gantt += "|";
        System.out.println("\nGANTT CHART: \n" + gantt);        

        System.out.println("""

        LEGEND:
        Pri. == Process Priority
        AT == Arrival Time
        BT == Burst Time
        EBT == Execution Beginning Time
        CT == Completion Time
        TAT == Turnaround Time
        WT == Waiting Time
        """);
        for (Process i : doneProcesses) {
            System.out.printf(
                "P%s -- Pri. %s -- AT %s -- BT %s -- EBT %s -- CT %s -- TAT %s -- WT %s%n",
                i.PID, i.priority, i.arrivalTime, i.burstTime, i.execTime, i.completionTime, i.turnAroundTime, i.waitingTime
            );
        }
        line();
        System.out.printf("AVERAGE TURNAROUND TIME: %.2f units %n", averageOfIntAL(TATs));
        System.out.printf("AVERAGE WAITING TIME: %.2f units %n", averageOfIntAL(WTs));
    }
}
