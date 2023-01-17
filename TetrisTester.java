//Bogachan Arslan
//9.11.17
//Maze Runner

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;

public class TetrisTester{
  private static boolean gamePaused=true; //To check if the game is paused
  
  public static void main(String[] args){
    
    JFrame frame = new JFrame("Tetris");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //default size
    frame.setSize(500, 620);
    
    final TetrisComponent tc=new TetrisComponent(); //Tetris component is put into frame
    frame.add(tc);
    
    //Listener for clicks
    frame.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent event) { //when clicked
        int x=event.getX(); //gets x and y of click position
        int y=event.getY();
        if (x > 350 && y > 550) { // if in start button area
          if(gamePaused) { // if game is paused
            tc.reassignTask(); //Calls the reassign task method to continue
            gamePaused=false; //changes the boolean to indicate unpause
          } else { //otherwise
            tc.runRestart(); //restars the game
          }
        } else if(x>350 && (y>440 && y<510)){ //if in stop button area
          if(!gamePaused){ //and if paused
            tc.task.destroy(); //destroys the current task tostop the shape
            gamePaused=true; //indicates pause
          }
        }
        
        
      }
    });
    
    frame.setVisible(true);
    frame.setResizable(false); //unresizable
    frame.setLocationRelativeTo(null); //centered
    
    //Timer to redraw game each millisecond
    Timer timer=new Timer();
    TimerTask task=new TimerTask(){
      public void run(){
        tc.repaint();
      }
    };
    timer.schedule(task,0,1);

  }
}