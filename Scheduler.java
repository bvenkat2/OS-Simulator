import java.util.*;
public class Scheduler{
   private ArrayList<Object[]> pq;
   
   
   public Scheduler(){
      pq = new ArrayList<Object[]>();
   }
   public void add(Process p){
      Object[] array = new Object[2];
      array[0] = p.getPCB().getPriority();
      array[1] = p;
      pq.add(array);
   }

   public Process getNextProcess(){
      int low = (int)(pq.get(0))[0];
      int index = -1;
      for(int x =0; x< pq.size(); x++){
         if(low>=((int)(pq.get(x))[0])){
            index = x;
            low = (int)(pq.get(x))[0];
         }
      
      }
      Process p= (Process)pq.get(index)[1];
      pq.remove(index);
      return p;
   }
   public boolean isEmpty(){
      if(pq.size() == 0)
         return true;
      return false;
   }
   public int size(){
      return pq.size();
   
   }
   public void printNames(){
      Process p = null;
      PCB b = null;
      for(int x = 0; x<pq.size(); x++){
         p = (Process)pq.get(x)[1];
         b = p.getPCB();
         System.out.println("Process ID " + b.getId() + " has name " + p.getName());
      }
   
   
   }
}