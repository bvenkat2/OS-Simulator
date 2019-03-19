public class Semaphore{
   private int count = -1;
   private char name;
   public Semaphore(char a){
      name = a;
   }
   public void aquire(Process p){
      count++;
      while(count>0){
      
      }
   }
   public void release(Process p){
      count--;
   
   }
}