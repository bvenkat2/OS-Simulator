import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
public class Simulator extends Thread{
   static ArrayBlockingQueue<Process> memWait = new ArrayBlockingQueue<Process>(100, true);
   static final int gb = 1024;
   static char[] criticalSections = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
   static Semaphore[] semaphores = new Semaphore[criticalSections.length];
   static Memory mainMem = new Memory(2*gb);
   static double hardwareIO = Math.random();
   static Memory virtualMem = new Memory(16*gb);
   static Memory cacheMem = new Memory(512);
   static Memory cacheMemTwo = new Memory(16);
   static Memory cacheMemThree = new Memory(16);
   static Memory cacheMemFour = new Memory(16);
   static Semaphore getProcess = new Semaphore('p');
   static int cycles = 0;

   static NamedPipe namedPipe = null;
   static Resource resources = new Resource(50);
   static Semaphore writeMem = new Semaphore('m');
   static ArrayBlockingQueue<Process> resourceWait = new ArrayBlockingQueue<Process>(100, true);

   static Memory cache = new Memory(512);
   static Process one, two = null;
   static ArrayList<Process> childWait = new ArrayList<Process>();
   public static void main(int t, int o, String[] k, int cycle, int whichSchedule) throws Exception {
   
      cycles = cycle;
   // 
      // System.out.println("Type 0 for randomly generated processes, type 1 to read in from file");
      // Scanner in = new Scanner(System.in);
      int type = -1;
      // try{
         // type = Integer.parseInt(in.next());
      // }
      // catch(NumberFormatException e){
         // System.out.println("Not a valid option. Try again.");
      // 
      // }
      
      type = t;
      if(type == 0){
         MultilevelFeedbackQueue schedu= new MultilevelFeedbackQueue();
      
         int numProcesses = makeProcesses(o);

         
         for(int w = 0; w<semaphores.length;w++){
            semaphores[w] = new Semaphore(criticalSections[w]);
         }
         String filename;
         Scanner infile = null;
         ArrayList<String> commands = null;
         Scheduler schedule = new Scheduler();
         int mem = 0;
         int re = 0;
         for(int x = 0; x<numProcesses; x++){
            commands = new ArrayList<String>();
         
            filename = "process" + x +".txt";
            try{
               infile = new Scanner(new FileInputStream(filename));
            }
            catch(IOException e){
               System.out.println("file not found");
               System.exit(0);
            }
            String name = infile.nextLine();
            name = name.substring(0,name.length()-1);
            while(infile.hasNextLine()){
               commands.add(infile.nextLine());
            }
            mem = Integer.parseInt(commands.get(0));
            commands.remove(0);
            re = Integer.parseInt(commands.get(0));
            commands.remove(0);
            int priority = -1;
            for(int g = 0; g<commands.size(); g++){
               priority = getBurst(commands.get(g));
               if(priority != -1){
                  break;
               }
            
            }
            PCB control = new PCB("NEW", 0, x, priority);
            Process p = new Process(control, name, mem);
            int[] free = null;
            
            //
            
            
            int count = 0;
            p.addCommands(commands);
            int comsize = commands.size();
         
            PageTable pt = new PageTable((int)(Math.ceil(commands.size()/8.0)));               
            
         
            if(virtualMem.getFreePages().length*8 >= mem){
               free = virtualMem.getFreePages();
               for(int y = 0; y<pt.size(); y++){
                  pt.addFrameVM(count,free[count]);
                  count++;
               }
               // p.setPageTable(pt);
               
               virtualMem.allocatePages(p,pt.returnFramesVM());
               // p.fillStorage();
               
               p.setPageTable(pt);
               p.getPCB().setState("READY");
               Process s = resources.request(re, p);
               if(!(s == null)){
                  resourceWait.add(s);
               }
               else{
                  p.setResources(re);
               
                  if(whichSchedule == 0)
                     schedule.add(p);
                  if(whichSchedule == 1)
                     schedu.add(p);
               }
            
            }
            else{
               // free = virtualMem.getFreePages();
               // for(int y = 0; y<pt.size(); y++){
                  // pt.setFrameVM();
                  // count++;
               // }
               // p.setPageTable(pt);
               // virtualMem.allocatePages(pt);
               p.getPCB().setState("WAIT");
               memWait.add(p);
            }
         }
         if(whichSchedule == 0){   
            if(!schedule.isEmpty())
               one = schedule.getNextProcess();
            if(!schedule.isEmpty())
               two = schedule.getNextProcess();
            if(one != null && two != null){     
               namedPipe = new NamedPipe(one, two); 
               namedPipe.put(one, two, 10);
            }
            if(one != null)
               schedule.add(one);
            if(two != null)
               schedule.add(two);           
         
            schedule.printNames();
         
         // run(schedule);
         // schedu.addResources(resources);
         // schedu.RR1();
         // schedu.RR2();
         // schedu.FCFS();
         
            run(schedule);
            
            
            MultiThread m0 = new MultiThread("Thread 1", schedule);
            MultiThread m1 = new MultiThread("Thread 2", schedule);
            MultiThread m2 = new MultiThread("Thread 3", schedule);
            MultiThread m3 = new MultiThread("Thread 4", schedule);
            MultiThread m4 = new MultiThread("Thread 5", schedule);
            MultiThread m5 = new MultiThread("Thread 6", schedule);
            MultiThread m6 = new MultiThread("Thread 7", schedule);
            MultiThread m7 = new MultiThread("Thread 8", schedule);
         
            m0.start();
            m1.start();
            m2.start();
            m3.start();
            m4.start();
            m5.start();
            m6.start();
            m7.start();
         
         } 
         else if(whichSchedule == 1){
            schedu.addResources(resources);
            schedu.RR1();
            schedu.RR2();
            schedu.FCFS();
         }

         if(!resourceWait.isEmpty()){
            System.out.println("not empty");
         }
         infile.close();
      
      }
      else if(type == 1){
         ArrayList<String> filenames = new ArrayList<String>();
         MultilevelFeedbackQueue schedu= new MultilevelFeedbackQueue();
      
         Scheduler schedule = new Scheduler();
         int mem = 0;
         // System.out.println("Enter filenames one at a time, enter -1 when finished");
         // while(true){
            // String n = in.next();
            // if(!n.equals("-1"))
               // filenames.add(n);
            // else 
               // break;
         // }
            for(int w = 0; w<semaphores.length;w++){
            semaphores[w] = new Semaphore(criticalSections[w]);
         }

         for(int x = 0; x<k.length; x++){
            filenames.add(k[x]);
         
         }
         
         String filename;
         Scanner infile = null;
         int re = 0;
         for(int x = 0; x<filenames.size(); x++){
            ArrayList<String> commands = new ArrayList<String>();
         
            filename = filenames.get(x);
            try{
               infile = new Scanner(new FileInputStream(filename));
            }
            catch(IOException e){
               System.out.println("file not found");
               System.exit(0);
            }
            String name = infile.nextLine();
            name = name.substring(0,name.length()-1);
            while(infile.hasNextLine()){
               commands.add(infile.nextLine());
            }
            mem = Integer.parseInt(commands.get(0));
            commands.remove(0);
            re = Integer.parseInt(commands.get(0));
            commands.remove(0);
            int priority = -1;
            for(int g = 0; g<commands.size(); g++){
               priority = getBurst(commands.get(g));
               if(priority != -1){
                  break;
               }
            
            }
            PCB control = new PCB("NEW", 0, x, priority);
            Process p = new Process(control, name, mem);
            int[] free = null;
            
            //
            
            
            int count = 0;
            p.addCommands(commands);
            int comsize = commands.size();
         
            PageTable pt = new PageTable((int)(Math.ceil(commands.size()/8.0)));               
            
         
            if(virtualMem.getFreePages().length*8 >= mem){
               free = virtualMem.getFreePages();
               for(int y = 0; y<pt.size(); y++){
                  pt.addFrameVM(count,free[count]);
                  count++;
               }
               // p.setPageTable(pt);
               
               virtualMem.allocatePages(p,pt.returnFramesVM());
               // p.fillStorage();
               
               p.setPageTable(pt);
               p.getPCB().setState("READY");
               Process s = resources.request(re, p);
               
               if(!(s == null)){
                  resourceWait.add(s);
               }
               else{
                  p.setResources(re);
               
                  if(whichSchedule == 0)
                     schedule.add(p);
                  else if(whichSchedule == 1)
                     schedu.add(p);
               }
            
            }
            else{
               // free = virtualMem.getFreePages();
               // for(int y = 0; y<pt.size(); y++){
                  // pt.setFrameVM();
                  // count++;
               // }
               // p.setPageTable(pt);
               // virtualMem.allocatePages(pt);
               p.getPCB().setState("WAIT");
               memWait.add(p);
            }
         }
         if(whichSchedule == 0){  
            if(!schedule.isEmpty())
               one = schedule.getNextProcess();
            if(!schedule.isEmpty())
               two = schedule.getNextProcess();
            if(one != null && two != null){     
               namedPipe = new NamedPipe(one, two); 
               namedPipe.put(one, two, 10);
            }
            if(one != null)
               schedule.add(one);
            if(two != null)
               schedule.add(two);
         
            schedule.printNames();
         // run(schedule);
         // schedu.addResources(resources);
         // schedu.RR1();
         // schedu.RR2();
         // schedu.FCFS();
         
         
         try{
         
         
         
         
         
         
         
         
         
            MultiThread m0 = new MultiThread("Thread 1", schedule);
            MultiThread m1 = new MultiThread("Thread 2", schedule);
            MultiThread m2 = new MultiThread("Thread 3", schedule);
            MultiThread m3 = new MultiThread("Thread 4", schedule);
            MultiThread m4 = new MultiThread("Thread 5", schedule);
            MultiThread m5 = new MultiThread("Thread 6", schedule);
            MultiThread m6 = new MultiThread("Thread 7", schedule);
            MultiThread m7 = new MultiThread("Thread 8", schedule);
         
            m0.start();
            m1.start();
            m2.start();
            m3.start();
            m4.start();
            m5.start();
            m6.start();
            m7.start();
            
            
            
            
            
            
            }
            catch(NullPointerException n){
            
            }
         }
         else if(whichSchedule == 1){
            schedu.addResources(resources);
         
            schedu.RR1();
            schedu.RR2();
            schedu.FCFS();
         
         
         }
      
         if(!resourceWait.isEmpty()){
            System.out.println("not empty");
         }
         infile.close();
      }
      else{
         System.out.println("Not a valid option. Try again.");
      
      
      }
      // in.close();
      
   }
   public static int getBurst(String c){
      int param = -1;
   
      if(c.indexOf("(") != -1){
      
      
         String command = c.substring(0,c.indexOf("("));
      
         if(command.equals("CALCULATE")){
            param= Integer.parseInt(c.substring(c.indexOf("(")+1,c.indexOf(")")));
         }
      }
      return param;
   
   
   }
   public static int doCommand(String c, Process p){
      if(c.indexOf("(") != -1){
      
      
         String command = c.substring(0,c.indexOf("("));
         if(command.equals("YIELD")){
            p.yield();
            return -1;         
         }
         if(command.equals("OUT")){
            p.out();
         }
         int param = -1;
         if(command.equals("CALCULATE")){
            param= Integer.parseInt(c.substring(c.indexOf("(")+1,c.indexOf(")")));
         
            p.calculate(param);
         }
         if(command.equals("I/O")){ 
            param= Integer.parseInt(c.substring(c.indexOf("(")+1,c.indexOf(")")));
         
            System.out.println("I/O output is "+p.io(param));
         }
      }
      else{
         char critical= c.charAt(c.length()-1);
         int count = 0;
         for(char a : criticalSections){
            if(a == critical){
               semaphores[count].aquire(p);
               semaphores[count].release(p);
               break;
            }
            count++;
         }
      
      }
      return 1;
   }
   public static int makeProcesses(int num){
      // System.out.println("How many processes would you like?");
      // Scanner in = new Scanner(System.in);
      // int num = in.nextInt();
      String[] possibleNames = {"Music Player", "Text Editor", "Word Processor", "Video Player", "Angry Birds", "Doodle Jump", "Chess", "Messaging", "Facetime"};
      String name = "";
   
      FileWriter fr = null;
      String filename;
      for(int x = 0; x<num; x++){
         
         filename = "process" + x +".txt";
         File file = new File(filename);
      
         try {
            fr = new FileWriter(filename);
            int filelength = (int)(Math.random()*30 + 5);
            int child = (int)(Math.random()*(filelength-1));
            int grandchild = (int)(Math.random()*2);
            
            
            int crit = (int)(Math.random()*filelength);
         
            name = possibleNames[(int)(Math.random()*possibleNames.length)];
            fr.write(name + ":\n");
            fr.write((int)(Math.ceil(filelength/8)) + "\n");
            fr.write((int)(Math.random()*25) + "\n");
         
            for(int y = 0; y<filelength; y++)
            {
               if(y == crit){
                  fr.write("Critical Section:" + criticalSections[(int)(Math.random()*criticalSections.length)] + "\n");
               }
               else if(y == child){
                  fr.write("FORK()\n");
                  for(int e = 0; e<(int)(Math.random()*10 + 1); e++){
                     fr.write(randomCommand());
                  }
                  fr.write("END()\n");
               
               }
               else{
                  fr.write(randomCommand());
               }
            }
         } catch (IOException e) {
            e.printStackTrace();
         }finally{
            //close resources
            try {
               fr.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      
      }
      return num;
   }
   public static String randomCommand(){
      int rand = (int)(Math.random()*4);
      int randParam = (int)(Math.random()*100+1);
      if(rand == 0)
         return "CALCULATE("+randParam+")" + "\n";
      else if(rand == 1)
         return "I/O("+randParam+")" + "\n";
      else if(rand == 2)
         return "YIELD()"+ "\n";
      else if(rand == 3)
         return "OUT()" + "\n";
      return "error";
   }
   public static int lastEnd(ArrayList<String> com){
      for(int x = com.size()-1; x>0; x--){
         if(com.get(x).equals("END()")){
            return x;
         }
      
      }
      return -1;
   
   }
   public static void run(Scheduler schedule) {
      int cycleCount = 0;
   
      int i = 1;
      ArrayList<String> childCommands = new ArrayList<String>();
      Pipe pipe = null;
      PCB childPCB = null;
      int count = 0;
      Process peek = null;
      Process child = null;
      Process parent = null;
      int[] toSwap= null;
      PageTable pageTable = null;
      int endChild = -1;
      while(!schedule.isEmpty()){
         childCommands = new ArrayList<String>();
         try{
            if(hardwareIO > .95){
               throw new HardwareIOException("Hardware IO");
            }
         }
         catch(HardwareIOException e){
            System.out.println("handled io");
         }
         getProcess.aquire(null);
         Process active = schedule.getNextProcess();
         getProcess.release(active);
         pageTable = active.getPageTable();
         if(!pageTable.inMM()){
            toSwap = mainMem.getLeastRecentlyChanged(pageTable.size());
            mainMem.allocatePages(active,toSwap);
         }
         
         PCB activeController = active.getPCB();
         ArrayList<String> commands = active.getCommands();
         int current = activeController.getPointer();
         while(current<commands.size()){
            if(cycleCount>cycles){
               return;
            
            }
            cycleCount++;
            writeMem.aquire(active);
            int frameNum = current/8;
            boolean init = pageTable.frameInCM(frameNum);
            if(!init){
               int[] cacheFrame = cacheMem.getLeastRecentlyChanged(1);
               pageTable.addFrameCM(frameNum, cacheFrame[0]);
               cacheMem.allocatePages(active, cacheFrame);
            }
         
            writeMem.release(active);
         
            if(!activeController.getState().equals("RUN"))
               activeController.setState("RUN");
           //Child Process    
            if(commands.get(current).equals("FORK()")){
               endChild = lastEnd(commands);
               System.out.println("Child Created");
               current ++;
               while(current<endChild){
                  childCommands.add(commands.get(current));
                  current++;
               
               }
               current++;
               activeController.setPointer(current);
               childPCB = new PCB("NEW", 0, activeController.getId(),activeController.getPriority()-5 );
               child = new Process(childPCB, "child " +active.getName(), 8*childCommands.size() );
               child.addCommands(childCommands);
               child.getPCB().setState("READY");
               child.setChild(true);
               pipe = new Pipe(active, child);
               child.setPipe(pipe);
               active.setPipe(pipe);
               schedule.add(child);
               childWait.add(active);
               PageTable pt = new PageTable((int)(Math.ceil(childCommands.size()/8.0)));               
            
            
               if(virtualMem.getFreePages().length*8 >= childCommands.size()){
               
                  writeMem.aquire(child);
                  int counter = 0;
                  int[] free = virtualMem.getFreePages();
                  for(int y = 0; y<pt.size(); y++){
                     pt.addFrameVM(counter,free[count]);
                     count++;
                  }
               
                  virtualMem.allocatePages(child,pt.returnFramesVM());
                  writeMem.release(child);
                  child.setPageTable(pt);
                  schedule.add(child);
                  // schedu.add(p);
                                    
               }
               else{
               
                  child.getPCB().setState("WAIT");
                  memWait.add(child);
               }
            
               break;
            
            }
            
            i=doCommand(commands.get(current),active);
         
            current++;
            activeController.setPointer(current);
            //if yield
            if(i == -1){
               activeController.setState("READY");
               activeController.setPriority(activeController.getPriority()+15);
               active.setPCB(activeController);
               schedule.add(active);
               break;
            }
         }
         if(current == commands.size()&&(!active.getPCB().getState().equals("EXIT"))){
            active.getPCB().setState("EXIT");
            
            //Second Interprocess Communication
            if(namedPipe.get(active) != -1){
               System.out.println("Pipe accessed");
            
            }
            
            resources.release(active);
            active.releaseResources();
        
         
            while(!resourceWait.isEmpty()){
               Process q = resourceWait.peek();
               Process v = resources.request(q.getResources(), q);
               if(v== null)
                  schedule.add(resourceWait.poll());   
               else 
                  break;         
            }
           
            if(active.isChild()){
               child.getPipe().put(true, child);
               for(int x = 0; x<childWait.size(); x++){
                  if(childWait.get(x).getPipe().check()){
                     schedule.add(childWait.get(x));
                     childWait.remove(x);
                  }
               
               }
            
            
            }
            virtualMem.freePages(active.getPageTable().returnFramesVM());
            if(!memWait.isEmpty()){
               int[] free;
               int c1 = 0;
            
               pageTable = peek.getPageTable();
               peek = memWait.poll();
               free = virtualMem.getLeastRecentlyChanged(pageTable.size());
               for(int y = 0; y<pageTable.size(); y++){
                  pageTable.addFrameVM(c1, free[y]);
                  c1++;
               }
            //    
               // peek.setPageTable(pt);
               // mainMem.allocatePages(pt);
               peek.getPCB().setState("READY");
               schedule.add(peek);
               
            }
                       
         }
            
      }
   
   }

}