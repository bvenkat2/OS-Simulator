import java.util.*;
import java.util.*;
public class RoundRobin{
   private int q = 0;
   private int nanoMultiplier = 1000000;
   private ArrayList<Process> processes = null;
   
   public RoundRobin(int a){
      q = a;
      processes = new ArrayList<Process>();
   }
   public void add(Process p){
      processes.add(p);
   }
   public boolean isEmpty(){
      return processes.isEmpty();
   }
   public void run(){
      int i = 1;
      ArrayList<String> childCommands = new ArrayList<String>();
      int[] free = null;
      Pipe pipe = null;
      int [] pt = null;
      PCB childPCB = null;
      int count = 0;
      Process peek = null;
      Process child = null;
      Process parent = null;
      int endChild = -1;
   
   
   
   
   }
}