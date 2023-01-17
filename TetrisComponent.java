//Bogachan Arslan & Baran Abali
//9.11.17
//Maze Runner

import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//The component that draws and advances the game
public class TetrisComponent extends JComponent{
  public TetrisGame tetris; //the game class
  public Timer timer; 
  public TetrisTask task; //custom timer task (see class for more info)
  public int frequency=1000; //initial drop speed of block
  
  public TetrisComponent(){
    tetris=new TetrisGame();
    timer=new Timer();
    task=new TetrisTask(tetris,timer,frequency); //new TetrisTask instance
    
    //For getting the key codes of arrow keys, we have followed an instruction on here:
    //https://stackoverflow.com/questions/616924/how-to-check-if-the-key-pressed-was-an-arrow-key-in-java-keylistener
    this.addKeyListener(new KeyListener(){ //Keylistener is added to component
      public void keyPressed(KeyEvent e){ 
        int keyCode=e.getKeyCode();
        switch(keyCode){ // the keycode received from the event is checked
          case KeyEvent.VK_RIGHT: //if right arrow is pressed
            tetris.moveHorizontally(true); //calls this method indicating a move right will be performed if possible
            break;
          case KeyEvent.VK_DOWN:
            if(tetris.checkNextStep()) tetris.activeShape.shiftShape(0,1);//manually shifts the shape down if possible
            break;
          case KeyEvent.VK_LEFT:
            tetris.moveHorizontally(false);//calls this method indicating a move left will be performed if possible
            break;
          case KeyEvent.VK_UP: //if pressed up arrow
            tetris.safeRotate(); //calls the method to rotate shape
            break;
        }
      }
      //Other necessary methods to implement the interface
      public void keyReleased(KeyEvent e) {}
      public void keyTyped(KeyEvent e) {}
    });
    
    //Focuses on the component to register key presses
    setFocusable(true);
    setFocusTraversalKeysEnabled(false);   
  }
  
  //The method cancels the task & timer completely and rebuilds & reassigns 
  public void reassignTask(){
    timer.cancel();
    timer=new Timer();
    task.destroy(); //(see TetrisTask class)
    task=new TetrisTask(tetris,timer,frequency);  //builds new task
    timer.schedule(task,0,frequency); //assigns
  }
  
  //If game restarted, calls the method in game class
  public void runRestart(){
    reassignTask();
    tetris.restartGame();
  }

  //intructions to paint
  public void paintComponent(Graphics g) {
    Graphics2D g2=(Graphics2D) g;
    g2.drawRect(0,0,300,600); //frame for grid
    
    //Game Title
    Font font1 = new Font("Helvetica", Font.BOLD, 25); //instance of font class
    g2.setFont(font1); 
    g2.setColor(Color.RED);
    String text1 = "TETRIS GAME!";
    g2.drawString(text1, 315, 300); 
    
    //Score text
    Font font4 = new Font("Helvetica", Font.BOLD, 20); //instance of font class
    g2.setFont(font4); 
    g2.setColor(Color.BLACK);
    g2.drawString("SCORE: " + tetris.score, 340, 30);
          
    //Developers' names text
    Font font2 = new Font("Helvetica", Font.PLAIN, 15); //instance of font class
    g2.setFont(font2); 
    g2.setColor(Color.BLUE);
    String text2 = "By Bogachan and Baran.";
    g2.drawString(text2, 320, 320);
    
    //Start button
    Font font3 = new Font("Helvetica", Font.BOLD, 20); //instance of font class
    g2.setFont(font3); 
    g2.setColor(Color.GREEN);
    String text3 = "CLICK HERE";
    String text4 = "TO START";
    g2.drawString(text3, 340, 550);
    g2.drawString(text4, 350, 576);
    g2.setColor(Color.BLACK);
    g2.drawRect(305, 525, 190, 70); 
    
    //Stop button
    Font font5 = new Font("Helvetica", Font.BOLD, 20); //instance of font class
    g2.setFont(font5); 
    g2.setColor(Color.RED);
    String te = "CLICK HERE";
    String tex = "TO STOP";
    g2.drawString(te, 340, 470);
    g2.drawString(tex, 350, 496);
    g2.setColor(Color.GRAY);
    g2.drawRect(305, 440, 190, 70); 
    
    //Draws the empty grid
    for(ArrayList<Block> row:tetris.grid) {
      for(Block b: row){
        g2.setColor(Color.GRAY);
        g2.drawRect(b.x*30,b.y*30,30,30);
      }
    } 
    
    //For each shape currently present
    for(Shape s:tetris.shapesPresent){
      g2.setColor(s.getColor()); //gets the color of shape and sets it to brush
      for(Block b:s.blocks){
        g2.fillRect(b.x*30,b.y*30,30,30); //draws the shapes according to their relative positions to the grid
      }
    }
    
    //If game is completed
    //Shows a message with current score and instructions on how to restart
    if(tetris.gameComplete){
      g2.setColor(Color.BLACK);
      g2.drawRect(100,210,300,200);
      g2.setColor(Color.GREEN);
      g2.fillRect(100,210,300,200);
      g2.setColor(Color.BLACK);
      Font fo = new Font("Helvetica", Font.BOLD, 35);
      g2.setFont(fo);
      g2.drawString("Game Complete!",120,260);
      g2.setFont(new Font("Helvetica", Font.PLAIN, 30));
      g2.drawString("Final Score: "+tetris.score, 130, 320);
      g2.setFont(new Font("Helvetica", Font.PLAIN, 20));
      g2.drawString("Click the bottom right button",120,370);
      g2.drawString("to start again",190,400);
    }
  }
}