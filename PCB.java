public class PCB{

   private String state;
   private int pointer;
   private int id;
   private int priority;
   
   public PCB(){
      state = "NEW";
      pointer = 0;
      id = -1;
      priority = 1;  
   }
   public PCB(String s1,int p1, int b, int pri){
      if(isState(s1))
         state = s1;
      pointer = p1; 
      id = b;
      priority = pri;
   }
   private boolean isState(String s){
      if(s.equals("NEW")||s.equals("READY")||s.equals("RUN")||s.equals("WAIT")||s.equals("EXIT"))
         return true;
      return false;
   }
   public void setState(String s){
      System.out.println("Process ID: " + id+ "'s state has been changed from " + state + " to " +s);
      state = s;
   
   }
   public void setPointer(int p){
      pointer = p;
   }
   public void setId(int i){
      id = i;
   }
   public void setPriority(int p){
      priority = p;
   }
   public String getState(){
      return state;
   }
   
   public int getPointer(){
      return pointer;
   }
   public int getId(){
      return id;
   }
   public int getPriority(){
      return priority;
   }
   
   public String toString(){
      int ln = pointer + 1;
      return "ID is: " + id + ", State is: "+state+", command number is: " +ln+", Priority is: "  + priority;
   }
   public void setID(int i ){
      id = i;
   
   }
}