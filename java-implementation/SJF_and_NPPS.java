import java.awt.image.BufferStrategy;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

class Process {
    public int compareTo(Process otherProcess) {
        return Integer.compare(arrivalTime, otherProcess.arrivalTime);
    }
    int priority;
    int arrivalTime;
    int execTime;
    int burstTime;
    int PID;
    int completionTime;
    int turnAroundTime;
    int waitingTime;
    public Process(int pid, int priority, int at, int bt) {
        this.PID = pid;
        this.arrivalTime = at;
        this.burstTime = bt;
        this.priority = priority;
    }
}

public class SJF_and_NPPS {

    // ALL PROCESSES. FOR SORTING
    @SuppressWarnings("Convert2Diamond")
    static ArrayList<Process> allProcesses = new ArrayList<Process>();

    // UPPER NPPS QUEUE
    @SuppressWarnings("Convert2Diamond")
    static ArrayList<Process> upperQueue = new ArrayList<Process>();


    // LOWER SJF QUEUE
    @SuppressWarnings("Convert2Diamond")
    static ArrayList<Process> lowerQueue = new ArrayList<Process>();

    // READY QUEUE
    @SuppressWarnings("Convert2Diamond")
    static ArrayList<Process> readyQueue = new ArrayList<Process>();

    // PROCESSES THAT ARE DONE EXECUTING WILL BE PUT HERE
    @SuppressWarnings("Convert2Diamond")
    static ArrayList<Process> doneProcesses = new ArrayList<Process>();

    // sort processes
    static void processSorter(Process proc) {
        if(proc.priority <= 5) {
            upperQueue.add(proc);
        } else {
            lowerQueue.add(proc);
        }
    }

    static int ticker = 0;
    // exeucte process. expected to pop from a queue to here
    static void processExecute(Process proc) {
        proc.execTime = ticker;
        ticker += proc.burstTime;
        doneProcesses.add(proc); // ADD PROCESS TO THE LIST OF DONE PROCESSES
        proc.completionTime = ticker; // CALCULATE COMPLETION TIME
        proc.turnAroundTime = proc.completionTime - proc.arrivalTime; // CALCULATE TURNAROUND TIME
        proc.waitingTime = proc.turnAroundTime - proc.burstTime; // CALCULATE WAITING TIME
    }

    public static void main(String[] args) {

        Process P1 = new Process(1,5,9,1);
        Process P2 = new Process(2,5,8,5);
        Process P3 = new Process(3,6,5,2);
        Process P4 = new Process(4,3,1,3);
        Process P5 = new Process(5,1,8,8);
        Process P6 = new Process(6,6,15,6);
        Process[] testProcesses = {P1, P2, P3, P4, P5, P6};
        allProcesses.addAll(Arrays.asList(testProcesses));
        allProcesses.sort(new Comparator<Process>() {
            @Override
            public int compare(Process a, Process b)
            {
                return a.arrivalTime - b.arrivalTime;
            }
        });

        upperQueue.sort(new Comparator<Process>() {
            @Override
            public int compare(Process a, Process b)
            {
                return a.priority - b.priority;
            }
        });

        lowerQueue.sort(new Comparator<Process>() {
            @Override
            public int compare(Process a, Process b)
            {
                return a.burstTime - b.burstTime;
            }
        });

        // PLACE PROCESSES IN READY QUEUE WITH SET EXECUTION TIME.
        do { 
            if(allProcesses.isEmpty()){break;}
            // POP THE NEXT PROCESS
            Process nextProcess = allProcesses.getFirst();
            allProcesses.removeFirst();
        
            // SORT THE PROCESS
            if(nextProcess.priority <= 5) {
                upperQueue.add(nextProcess);
            } else {
                lowerQueue.add(nextProcess);

                
            }

            // 
            
            ticker++;
        } while (true);
        System.out.println(ticker);

        // DISPLAY OF GANTT CHART.
        String gantt = "";
        for (Process i : doneProcesses) {
            gantt += String.format("| P%s ", i.PID);
        }
        gantt += "|";
        System.out.println("GANTT CHART: \n" + gantt);        

        for (Process i : doneProcesses) {
            System.out.printf(
                "@%s P%s -- Pr. %s -- AT %s -- BT %s -- CT %s -- TAT %s -- WT %s%n",
                i.execTime, i.PID, i.priority, i.arrivalTime, i.burstTime, i.completionTime, i.turnAroundTime, i.waitingTime
            );
        }
    }
}
