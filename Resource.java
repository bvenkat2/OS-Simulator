import java.util.*;
public class Resource{
   private HashMap<Process, Integer> map;
   private HashMap<Integer, Integer> idMap;
   private int amount;
   private int number;
   private int max[];
   private int allocated[];
   
   public Resource(int a){
      amount = a;
      map = new HashMap<Process, Integer>();
      idMap = new HashMap<Integer , Integer>();
   
   }
   public Process request(int a, Process p){
      int i = 0;
      boolean done = false;
      if(a<amount){
         amount = amount-a;
         map.put(p, a);
         idMap.put(p.getPCB().getId(), a);
      }
      else{
         for(Process b: map.keySet()){
            if(b.getPCB().getPriority()>p.getPCB().getPriority()){
               i = b.getResources();
               if(i>=a){
                  b.releaseResources();
                  map.put(b,0);
                  amount = amount + i -a;
                  map.put(p, a);
                  idMap.put(p.getPCB().getId(), a);
                  done = true;
                  return b;
               }
            
            }
         }
         if(!done){
         return p;
         }
      }
      return null;
   
   }
   public void release(Process p){
      amount = amount + idMap.get(p.getPCB().getId());
      map.put(p, 0);
      idMap.put(p.getPCB().getId(), 0);
   
   }
}