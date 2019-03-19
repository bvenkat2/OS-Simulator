 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.lang.*;
import java.io.*;
 
public class Display extends JFrame{
   private JFrame commandLine;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JTextField inputCL;
   private JTextArea outputCL;
   private JDialog inputDialog;
   private JTextField fileInput;
   private String[] filenames;
   private int val = -1;
   private int numFiles = 0;
   private int cycles = 10000;
   private int which;
   private JDialog pickScheduler;
    

   public Display(){
      prepareGUI();
   }
   public static void main(String[] args){
      Display  gui = new Display();      
      // gui.runCommandLine();
   }
   private void prepareGUI(){
   
   //Overall Frame
      commandLine = new JFrame("Operating System Command Line");
      commandLine.setSize(400,400);
      BoxLayout boxLayout = new BoxLayout(commandLine.getContentPane(), BoxLayout.Y_AXIS); 
      commandLine.setLayout(boxLayout);
      
      commandLine.addWindowListener(
         new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
               System.exit(0);
            }        
         });    
      
   // Console Output  
      JScrollPane j = new JScrollPane(outputCL);       
      outputCL = new JTextArea();
      outputCL.setSize(300,300);
      JScrollPane r = new JScrollPane(outputCL);       
      redirectSystemStreams();
   
   //    // outputCL.setBackground(Color.BLUE);
   //    JTextField which = new JTextField();
   //    which.addActionListener(
   //       new ActionListener() {
   //          public void actionPerformed(ActionEvent e) {
   //             outputCL.append(inputCL.getText());
   //             
   //          }
   //       
   //       });
   //    which.setVisible(true);
   // //    
   // // 
   // //    
      JButton user = new JButton("User Provided");
      JButton generated = new JButton("Generated");
      user.addActionListener ( 
         new ActionListener()  
         {  
            public void actionPerformed( ActionEvent e )  
            {  
               getVal(1); 
               JDialog prompt = new JDialog(commandLine, "Enter processes filenames separated by commas", true);
               JTextField f = new JTextField(20);
               f.addActionListener(
                  new ActionListener() {
                  
                     public void actionPerformed(ActionEvent e) {
                        filenames = parseNames(f.getText());  
                     }
                  });                  
               prompt.setSize(300,300);
               prompt.add(f);
               prompt.setVisible(true);
            }  
         });  
         
      generated.addActionListener ( 
         new ActionListener()  
         {  
            public void actionPerformed( ActionEvent e )  
            {  
               getVal(0); 
               JDialog prompt = new JDialog(commandLine, "Enter number of files", true);
               JTextField f = new JTextField(20);
               f.addActionListener(
                  new ActionListener() {
                  
                     public void actionPerformed(ActionEvent e) {
                        numFiles = Integer.parseInt(f.getText());  
                     }
                  });                  
               prompt.setSize(300,300);
               prompt.add(f);
               prompt.setVisible(true);
            
            }  
         });  
      
   
      inputDialog = new JDialog(commandLine, "Select User Provided or Randomly Generated Program", true);
      inputDialog.setLayout(new FlowLayout());
      inputDialog.setSize(400, 400);
   //    inputDialog.add(which);
      inputDialog.add(user);
      inputDialog.add(generated);
      inputDialog.setVisible(true);
      
      
      
      JButton sjf = new JButton("SJF");
      JButton mfb = new JButton("Multi Level Feedback Queue");
            
      sjf.addActionListener ( 
         new ActionListener()  
         {  
            public void actionPerformed( ActionEvent e )  
            {  
               getScheduler(0);
            }  
         }); 
                 mfb.addActionListener ( 
         new ActionListener()  
         {  
            public void actionPerformed( ActionEvent e )  
            {  
               getScheduler(1);
            }  
         });   
   
      
      
      pickScheduler = new JDialog(commandLine, "Pick which scheduler you want", true);
      pickScheduler.setLayout(new FlowLayout());
      pickScheduler.setSize(400,400);
      pickScheduler.add(sjf);
      pickScheduler.add(mfb);
      pickScheduler.setVisible(true);
      
      
      JDialog getCycles = new JDialog(commandLine, "Input how many cycles you want", true);
      getCycles.setSize(300,300);
      JTextField cycleIn = new JTextField(20);
       cycleIn.addActionListener(
                  new ActionListener() {
                  
                     public void actionPerformed(ActionEvent e) {
                        getCycle(Integer.parseInt(cycleIn.getText()));  
                     }
                  });     
      
      getCycles.add(cycleIn);
      
      
      getCycles.setVisible(true);
      
      
      
      
      
      // inputDialog.add(which);
      // inputDialog.setSize(400,400);
      // inputDialog.setVisible(true);
   // 
   //    // commandLine.add(inputDialog);
      commandLine.add(r);
      // commandLine.add(inputDialog);
   // //    
   // //    
   // //    
   // //    
   // //    
   // // 
      commandLine.setVisible(true); 
      
      try{
         Simulator.main(val, numFiles, filenames, cycles, which);
      }
      catch(Exception t){
      
      
      }
   
   
   }
   public void getCycle(int i){
   cycles = i;
   }
   public void getScheduler(int i){
      which = i;
   }
   public String[] parseNames(String n){
      String[] temp = n.split(",");
      
      return temp;
   
   }
   public void getVal(int i){
      val = i;
   }

   private void updateTextArea(final String text) {
      SwingUtilities.invokeLater(
         new Runnable() {
            public void run() {
               outputCL.append(text);
            }
         });
   }
 
   private void redirectSystemStreams() {
      OutputStream out = 
         new OutputStream() {
            public void write(int b) throws IOException {
               updateTextArea(String.valueOf((char) b));
            }
         
            public void write(byte[] b, int off, int len) throws IOException {
               updateTextArea(new String(b, off, len));
            }
         
            public void write(byte[] b) throws IOException {
               write(b, 0, b.length);
            }
         };
   
      System.setOut(new PrintStream(out, true));
      System.setErr(new PrintStream(out, true));
   }
}