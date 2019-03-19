import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
public class MultilevelFeedbackQueue{
   ArrayBlockingQueue<Process> memWait = new ArrayBlockingQueue<Process>(100, true);
   final int gb = 1024;
   boolean[] check;
   char[] criticalSections = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
   Semaphore[] semaphores = new Semaphore[criticalSections.length];
   Memory mainMem = new Memory(2*gb);
   double hardwareIO = Math.random();
   Memory virtualMem = new Memory(16*gb);
   NamedPipe namedPipe = null;
   Resource resources = new Resource(50);
   int nanoMultiplier = 1000000;
   ArrayBlockingQueue<Process> resourceWait = new ArrayBlockingQueue<Process>(100, true);

   Memory cache = new Memory(512);
   Process one, two = null;
   ArrayList<Process> childWait = new ArrayList<Process>();
   ArrayBlockingQueue<Process> q = null;
   ArrayBlockingQueue<Process> r1 = null;
   ArrayBlockingQueue<Process> r2 = null;

   public MultilevelFeedbackQueue(){
   
      q = new ArrayBlockingQueue<Process>(100);
      r1 = new ArrayBlockingQueue<Process>(100);
      r2 = new ArrayBlockingQueue<Process>(100);
   
   }
   public void addResources(Resource resources){
      this.resources = resources;
   
   }

   public void add(Process p){
      r1.add(p);
   
   
   }
   public void RR1(){
      check = new boolean[r1.size()];
      boolean processDone = false;;
      int runtime = 8;
      Process active = null;
   
   
      while(!r1.isEmpty()){
         int endChild = -1;
         int time = 0;
      
         int[] free = null;
         Pipe pipe = null;
         int [] pt = null;
         PCB childPCB = null;
         int count = 0;
         Process peek = null;
         Process child = null;
         Process parent = null;
         active = r1.poll();
      
         long starttime = System.nanoTime();
         boolean finished = false;
         processDone = false;
         int i = 1;
         ArrayList<String> childCommands = new ArrayList<String>();
         
         childCommands = new ArrayList<String>();
         PCB activeController = active.getPCB();
         ArrayList<String> commands = active.getCommands();
         int current = activeController.getPointer();
         while(current<commands.size()){
            if(!activeController.getState().equals("RUN"))
               activeController.setState("RUN");
               //Child Process    
            if(commands.get(current).equals("FORK()")){
               endChild = Simulator.lastEnd(commands);
               System.out.println("Child Created");
               current ++;
               while(current<endChild){
                  childCommands.add(commands.get(current));
                  current++;
                     
               }
               current++;
               activeController.setPointer(current);
               childPCB = new PCB("NEW", 0, activeController.getId(),activeController.getPriority() );
               child = new Process(childPCB, "child " +active.getName(), 8*childCommands.size() );
               child.addCommands(childCommands);
               child.getPCB().setState("READY");
               child.setChild(true);
               pipe = new Pipe(active, child);
               child.setPipe(pipe);
               active.setPipe(pipe);
               r1.add(child);
               childWait.add(active);
               break;
                  
            }
               
            i=Simulator.doCommand(commands.get(current),active);
               
            current++;
            time++;
            activeController.setPointer(current);
               //if yield
            if(i == -1){
               activeController.setState("READY");
               activeController.setPriority(activeController.getPriority()+15);
               active.setPCB(activeController);
               r2.add(active);
               break;
            }
            finished = (time >= runtime);
            if(finished){
        
               r2.add(active);
               break;
       
                  
            }
            
         }
         if(current == commands.size()&&(!active.getPCB().getState().equals("EXIT"))){
            processDone = true;
            active.getPCB().setState("EXIT");
               

            resources.release(active);
            active.releaseResources();
               
               
            while(!resourceWait.isEmpty()){
               Process q = resourceWait.peek();
               Process v = resources.request(q.getResources(), q);
               if(v== null)
                  r1.add(resourceWait.poll());   
               else 
                  break;         
            }
               
            if(active.isChild()){
               active.getPipe().put(true, active);
               for(int x = 0; x<childWait.size(); x++){
                  if(childWait.get(x).getPipe().check()){
                     r1.add(childWait.get(x));
                     childWait.remove(x);
                  }
                     
               }
                  
                  
            }
                                     
         }
            
         
            
         
            
      }
   
   }
   public void RR2(){
      boolean processDone = false;;
      int runtime = 8*nanoMultiplier;
      Process active = null;
   
   
      while(!r2.isEmpty()){
         int endChild = -1;
         int time = 0;
         int[] free = null;
         Pipe pipe = null;
         int [] pt = null;
         PCB childPCB = null;
         int count = 0;
         Process peek = null;
         Process child = null;
         Process parent = null;
         active = r2.poll();
      
         boolean finished = false;
         processDone = false;
         int i = 1;
         ArrayList<String> childCommands = new ArrayList<String>();
         
         childCommands = new ArrayList<String>();
         PCB activeController = active.getPCB();
         ArrayList<String> commands = active.getCommands();
         int current = activeController.getPointer();
         while(current<commands.size()){
            if(!activeController.getState().equals("RUN"))
               activeController.setState("RUN");
               //Child Process    
            if(commands.get(current).equals("FORK()")){
               endChild = Simulator.lastEnd(commands);
               System.out.println("Child Created");
               current ++;
               while(current<endChild){
                  childCommands.add(commands.get(current));
                  current++;
                     
               }
               current++;
               activeController.setPointer(current);
               childPCB = new PCB("NEW", 0, activeController.getId(),activeController.getPriority() );
               child = new Process(childPCB, "child " +active.getName(), 8*childCommands.size() );
               child.addCommands(childCommands);
               child.getPCB().setState("READY");
               child.setChild(true);
               pipe = new Pipe(active, child);
               child.setPipe(pipe);
               active.setPipe(pipe);
               r2.add(child);
               childWait.add(active);
               break;
                  
            }
               
            i=Simulator.doCommand(commands.get(current),active);
               
            current++;
            time++;
            activeController.setPointer(current);
            finished = (time >= runtime);
         
               //if yield
            if(i == -1){
               activeController.setState("READY");
               activeController.setPriority(activeController.getPriority()+15);
               active.setPCB(activeController);
               q.add(active);
               break;
            }
            
            
          
            if(finished){
               q.add(active);
               break;
            }
            
            
         }
         if(current == commands.size()&&(!active.getPCB().getState().equals("EXIT"))){
            check[active.getPCB().getId()] = true;
            active.getPCB().setState("EXIT");
               
         
            resources.release(active);
            active.releaseResources();
               
               
            while(!resourceWait.isEmpty()){
               Process q = resourceWait.peek();
               Process v = resources.request(q.getResources(), q);
               if(v== null)
                  r2.add(resourceWait.poll());   
               else 
                  break;         
            }
               
            if(active.isChild()){
               active.getPipe().put(true, active);
               for(int x = 0; x<childWait.size(); x++){
                  if(childWait.get(x).getPipe().check()){
                     r2.add(childWait.get(x));
                     childWait.remove(x);
                  }
                     
               }
                  
                  
            }
                                     
         }
            
         
            
         
            
      }
   
   }
   public void FCFS(){
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
      while(!q.isEmpty()){
         childCommands = new ArrayList<String>();
         try{
            if(hardwareIO > .95){
               throw new HardwareIOException("Hardware IO");
            }
         }
         catch(HardwareIOException e){
            System.out.println("handled io");
         }
         Process active = q.poll();
         PCB activeController = active.getPCB();
         ArrayList<String> commands = active.getCommands();
         int current = activeController.getPointer();
         while(current<commands.size()){
            if(!activeController.getState().equals("RUN"))
               activeController.setState("RUN");
           //Child Process    
            if(commands.get(current).equals("FORK()")){
               endChild = Simulator.lastEnd(commands);
               System.out.println("Child Created");
               current ++;
               while(current<endChild){
                  childCommands.add(commands.get(current));
                  current++;
               
               }
               current++;
               activeController.setPointer(current);
               childPCB = new PCB("NEW", 0, activeController.getId(),activeController.getPriority() );
               child = new Process(childPCB, "child " +active.getName(), 8*childCommands.size() );
               child.addCommands(childCommands);
               child.getPCB().setState("READY");
               child.setChild(true);
               pipe = new Pipe(active, child);
               child.setPipe(pipe);
               active.setPipe(pipe);
               q.add(child);
               childWait.add(active);
               break;
            
            }
            
            i=Simulator.doCommand(commands.get(current),active);
         
            current++;
            activeController.setPointer(current);
            //if yield
            if(i == -1){
               activeController.setState("READY");
               activeController.setPriority(activeController.getPriority()+15);
               active.setPCB(activeController);
               q.add(active);
               break;
            }
         }
         if(current == commands.size()&&(!active.getPCB().getState().equals("EXIT"))){
            check[active.getPCB().getId()] = true;
            active.getPCB().setState("EXIT");
            
         
            resources.release(active);
            active.releaseResources();
            
         
            while(!resourceWait.isEmpty()){
               Process f = resourceWait.peek();
               Process v = resources.request(f.getResources(), f);
               if(v== null)
                  q.add(resourceWait.poll());   
               else 
                  break;         
            }
           
            if(active.isChild()){
               child.getPipe().put(true, child);
               for(int x = 0; x<childWait.size(); x++){
                  if(childWait.get(x).getPipe().check()){
                     q.add(childWait.get(x));
                     childWait.remove(x);
                  }
               
               }
            
            
            }
         }
            
      }
   
   
   
   
   
   }
   
   
}