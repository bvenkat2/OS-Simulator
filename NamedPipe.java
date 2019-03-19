public class NamedPipe {
   private int data;
   private int id1, id2;

   public NamedPipe(Process a, Process b){
      id1 = a.getPCB().getId();
      id2 = b.getPCB().getId();
   
   
   }
   public void put(Process a, Process b, int c){
      int temp1 = a.getPCB().getId();
      int temp2 = b.getPCB().getId();
      if((temp1 == id1 &temp2 == id2)||(temp1 == id2 && temp2 == id1)){
         data = c;
      
      }
      else System.out.println("Error");
   
   
   }
   public int get(Process a){
      if(a.getPCB().getId() == id1 || a.getPCB().getId() == id2){
      
         return data;
      }
      else 
         return -1;
   
   }




}