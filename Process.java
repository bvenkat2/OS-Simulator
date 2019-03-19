import java.util.*;
import java.lang.*;
public class Process{
   private PCB control;
   private int minMemory;
   private String name;
   private final int pageSize = 8;
   private ArrayList<String> array;
   private Pipe pipe;
   private int resources;
   private PageTable pt;

   private boolean c = false;
   public Process(){
      control = new PCB();
      name = "";
      array = new ArrayList<String>();
      minMemory = 0;
      resources = 0;
   }
   public Process(PCB p, String a, int m){
      control = p;
      name = a;
      array = new ArrayList<String>();
      minMemory = m;
      pt = null;
      resources = 0;
      
   }
   public void setPageTable(PageTable p){
      pt = p;
   }
   public PageTable getPageTable(){
      return pt;
   }
   public void setResources(int i){
      resources = i;
   }
   public void releaseResources(){
      resources = 0;
   
   }
   public int getResources(){
      return resources;
   }

   public boolean isChild(){
      return c;
   
   }
   public void setChild(boolean b){
      c = b;
   
   }
   public void setPipe(Pipe p){
      pipe = p;
   
   }
   public Pipe getPipe(){
   
      return pipe;
   }
//    public void setPageTable(int[] a){
//       pageTable =a;
//    }
   // public HashMap<Integer, ArrayList<String>> getVirtualStorage(){
      // return storage;
   // 
   // }
   // public void fillStorage(){
      // int page = 0;
      // int offset = 0;
      // String address = "";
      // storage = new HashMap<Integer, ArrayList<String>>();
      // ArrayList<String> addresses = new ArrayList<String>();
      // int count = 1;
      // for(int x = 0; x<array.size(); x++){
         // address = "0x" + Integer.toHexString(page) + Integer.toHexString(offset);
         // addresses.add(address);
         // offset++;   
      //             
         // if(count == 8 || x == array.size()-1){
            // storage.put(page, addresses);
            // page++;
            // addresses = new ArrayList<String>();
            // count = 0;
            // offset = 0;
         // }
         // count++;
      // }
   //    
   
   
   // }
//    public int[] getPageTable(){
//       return pageTable;
//    }

   public PCB getPCB(){
      return control;
   
   }
   public int getMem(){
      return minMemory;
   }
   public void addCommands(ArrayList<String> commands){
      array = commands;
   
   }
   public ArrayList<String> getCommands(){
      return array;
   }
   public void calculate(int cycles){
      int count = 0;
      while(count<cycles)
         count++;
      System.out.println("Did calculate for " + cycles+ " cycles");
   }
   public int io(int param){
      int count = 0;
      while(count<param)
         count++;
      control.setState("WAIT");
      int value = (int)(Math.random()*50);
      return value;
   }
   public void yield(){
      System.out.println("Yielded");
   }
   public void out(){
      System.out.print("Process name is: " + name+ ", ");
      System.out.println(control.toString());         
   }
   public void setPCB(PCB p){
      control = p;
   }
   public String getName(){
      return name;
   }
}