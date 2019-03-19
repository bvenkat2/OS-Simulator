public class Page{
   String[] commands;
   int maxSize = 8;
   int count = 0;
   public Page(){
      commands = new String[maxSize];
   
   }
   public Page(String[] commands){
      this.commands = commands;
   }
   public void add(String command){
      if(count <8){
         commands[count] = command;
         count++;
      }
      else{
         System.out.println("Error!");
      
      }
   
   }
   public String get(int index){
      return commands[index];
   
   }
   public boolean isEmpty(){
      for(int x = 0; x<commands.length; x++){
         if(commands[x] != null)
            return false;
      
      }
      return true;
   }

}