public class Pipe{
   private int id;
   private boolean terminated;
   public Pipe(){
   id = -1;
   terminated = false;
   
   }
   public Pipe(Process a, Process b){
      terminated = false;
      if(a.getPCB().getId() == b.getPCB().getId()){
         id = a.getPCB().getId();
      }
      else id = -1;
   
   }

   public void put(boolean b, Process p){
      if(p.getPCB().getId() == id){
         terminated = b;
      
      }      
   }
   public boolean check(){
      return terminated;
   }

}