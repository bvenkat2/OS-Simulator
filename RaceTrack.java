import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.image.*;
import java.lang.*;
import javax.imageio.*;
public class RaceTrack extends JPanel{
   class RaceCar extends Thread{
      BufferedImage i = null;
      public RaceCar(int x, int y, Graphics g){
         try{
            i = ImageIO.read(new File("sportive-car.png"));
         }
         catch(IOException e){
            System.out.println("Error");
         
         }
         g.drawImage(i,0,0,null);
      }
      public void run(){
      
      
      
      
      
      }
   
   
   
   
   
   }






   public void paintComponent(Graphics g){
      super.paintComponent(g);
      g.setColor(Color.GRAY);
      g.fillRect(0,0,getWidth(),12);
      g.fillRect(0,getHeight()/4, getWidth(), 12);
      g.fillRect(0,getHeight()/2, getWidth(), 12);
   Thread t = new RaceCar(0,0,g);
   t.start();
   
   }



   public static void main(String[] args){
   
      JFrame frame = new JFrame("Race Track");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);
      FlowLayout layout = new FlowLayout();
      frame.setLayout(layout);
      
      
               
      JButton b1 = new JButton("Start");
      frame.add(b1);
      JButton b2 = new JButton("Pause");
      frame.add(b2);
      JButton b3 = new JButton("Reset");
      frame.add(b3);
      
      Dimension d = new Dimension(400,200);
      RaceTrack panel = new RaceTrack();
      panel.setPreferredSize(d);
      
   
   //    
      
      
      frame.add(panel); 
      
   
           
      frame.setSize(500,200);
      frame.setVisible(true);
      
      
      
      
      
   
   }


}