//Bogachan Arslan & Baran Abali
//9.11.17
//Tetris Final Project

import java.util.*;
import java.awt.*;

public class Block{
  //variables of a block
  public int x; //column number in grid
  public int y; //row number in grid
  private boolean occupied; //boolean to check is the block is occupied by a block from shape
  
  //constructor
  //Receives the x and y positions to be placed
  public Block(int xpos,int ypos){
    x=xpos;
    y=ypos;
  }
  
  //changes the x position
  public void setX(int newX){
    x=newX;
  }
  
  //changes the y position
  public void setY(int newY){
    y=newY;
  }
  
  //changes the occupancy of the block
  public void changeFill(boolean isFilled){
    occupied=isFilled;
  }
  
  //checks if the block is occupied of available to place a shape
  public boolean isOccupied(){
    return occupied;
  }
}