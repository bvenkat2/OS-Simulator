import java.util.*;
public class PageTable{
//for each frame needed by process
//first column is location in vm
//second column is location in mm
//third column is location in cache
//-1 if not present
   private int[][] table;

   public PageTable(int frames){
      table = new int[frames][3];
      for(int x = 0; x<table.length; x++){
         table[x][0] = -1;
         table[x][1] = -1;
         table[x][2] = -1;
         
      }
   }
   public int[] returnFramesVM(){
      int[] temp = new int[table.length];
      for(int x = 0; x<table.length; x++){
         temp[x] = table[x][0];
      }
      return temp;
   
   }
   public void addFrameVM(int frame, int page){
      table[frame][0] = page;
   }
   public void addFrameMM(int frame, int page){
      table[frame][1] = page;
   }
   public void addFrameCM(int frame, int page){
      table[frame][2] = page;
   }
   public int getPageNumberVM(int frame, int page){
      return table[frame][0];
   }
   public int getPageNumberMM(int frame, int page){
      return table[frame][1];
   }

   public int getPageNumberCM(int frame, int page){
      return table[frame][2];
   }
   public int size(){
      return table.length;
   }
   public boolean inMM(){
      for(int x = 0; x<table.length; x++){
         if(table[x][1] != -1)
            return true;
      }
      return false;
   }
   public boolean frameInCM(int frame){
      if(table[frame][2] != -1)
         return true;
      return false;
   }



}