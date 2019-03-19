public class MultiThread implements Runnable{
   private Thread t;
   private String name;
   private Scheduler s;
   public MultiThread(String name, Scheduler s){
      this.name = name;
      this.s = s;
   
   }


   public void run(){
      try{
            Simulator.run(s);
            }
            catch(NullPointerException e){
            Simulator.run(s);
            }
    
   
   }
   public void start(){
      if(t == null){
         t = new Thread(this, name);
         t.start();
      }
   
   }


}