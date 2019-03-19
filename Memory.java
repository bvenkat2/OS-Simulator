//If in cache, run, otherwise look in main, if in main swap into cache, otherwise look in virtual, if in virtual, swap into cache, otherwise invalid reference
import java.util.*;
public class Memory{
   private final int totalMem;
   private final int pageSize = 8;
   private Page[] pages;
   private int freePages;
   private int count;
   private int[] accessed;
   public Memory(int size){
      totalMem = size;
      pages = new Page[totalMem/pageSize];
      freePages = pages.length;
      fill();
      count = 0;
      accessed = new int[pages.length];
   
   
   }
   public void fill(){
      for(int x = 0; x<pages.length; x++){
         pages[x] = new Page();
      }
   }


   
   // public void allocateProcess(Process p){
      // int numPages = (int)(Math.ceil(p.getMem()/pageSize));
      // if(freePages > numPages){
      //   for(int x = 0; x<
      // 
      // }
   // 
   // }
   // public int getUsed(){
      // return usedMem;
   // 
   // }
   // public int getFree(){
      // return freeMem;
   // }
  
   
   public void allocatePages(Process p, int[] a){
   
      int count = 0;
      Page temp = null;
      ArrayList<String> commands = p.getCommands();
      for(int x = 0; x<a.length; x++){
      if(a[x] == -1){
      return;
      
      }
         temp = new Page();
         
         for(int y = 0; y<8; y++){
            if(count<commands.size()){
               temp.add(commands.get(count));
               count++;
            }
         }
         pages[a[x]] = temp;
         accessed[a[x]] = count;
         count++;
         temp = null;
      }
   }
   public void freePages(int[] a){
      for(int x = 0; x<a.length; x++){
         pages[a[x]] = new Page();
      
      }
   }
   public int[] getFreePages(){
      int count = 0;
      for(int x = 0; x<pages.length; x++){
         if(pages[x].isEmpty())
            count++;
      }
      int[] temp = new int[count];
      count = 0;
      for(int y = 0; y<pages.length-1; y++){
         if(pages[y].isEmpty()){
            temp[count] = y;
            count++;
         }
      }
      return temp;
   }
   public int indexLowest(int[] t){
      int index = 0;
      for(int x = 0; x<t.length; x++){
         if(t[x] < t[index]){
            index = x;
         
         }
      
      }
      return index;
   
   }
   public int[] getLeastRecentlyChanged(int n){
      int[] free = getFreePages();
      int[] temp = new int[n];
   
      int counter = 0;
      if(free.length>n){
         for(int x = 0; x<n; x++){
            temp[x] = free[x];
         }
      }
      else{
         for(int y = 0; y<n; y++){
            int i = indexLowest(accessed);
            temp[counter] = i;
            accessed[i] = count;
            count++;
            counter++;
         }   
      }
      return temp;
   
   }
// 
}