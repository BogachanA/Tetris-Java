//Bogachan Arslan & Baran Abali
//Tetris - Final Project

import java.util.*;


/* This class is to advance the active shape down automatically in each step
 * When an instance is cancelled it generates another instance with a smaller interval
 * to increase the falling speed of each block slightly until the end of game
 * */
public class TetrisTask extends TimerTask{
  public int frequency;
  public TetrisGame tetris;
  public Timer timer;
  
  //the consturctor gets the game instance, timer instance and initial speed
  public TetrisTask(TetrisGame t,Timer time, int freq){
    tetris=t;
    frequency=freq;
    timer=time;
  }
  
  //Instructions for each interval
  @Override
  public void run(){
    if(tetris.nextStep()){ //If next step returns true (ie activa shape changes)
      cancel(); //cancel method is called
    }
  }
  
  @Override
  //This custom cancel methos first cancels the current instance
  public boolean cancel(){
    super.cancel(); //with super class's cancelmethod
    frequency=(int)((double)frequency*0.99); //Then updates the speed
    TetrisTask task=new TetrisTask(tetris,timer,frequency); //and build 
    timer.schedule(task,0,frequency); //and assigns a new instance with new speed
    return true;
  }
  
  //The destroy method is to use the original function of the cancel method
  public void destroy(){
    super.cancel();
  }
}