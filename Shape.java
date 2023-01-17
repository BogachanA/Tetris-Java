// Bogachan Arslan & Baran Abali
// Tetris Final Project

import java.util.*;
import java.awt.*;

public class Shape{
  private int size=4;
  public int xCenter; //x position of center of the shape 
  public int yCenter; //y position of center of the shape. One block is placed here, the others are placed relatively.
  private Color c; //Each shape has its own color
  private Random rand=new Random(); //Random class instance to generate random integers
  public ArrayList<Block> blocks; //The list of blocks that make up the shape
  
  //The class constructor
  //Parameters:
  //  -x: the x position to assign to the center of shape
  //  -y: y to assign to center
  //  -id: The id's of shapes keep track of the possible 4-block combinations that forms a shape. The shape is constructed
  //       according to the id received. Hence, differently shaped shapes are formed.
  public Shape(int x,int y, int id){
    xCenter=x;
    yCenter=y;
    blocks=new ArrayList<Block>();
    
    c=new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)); //A random color is generated and assigned to shape
    
    //Generating the 4 blocks that make up the shape
    Block b1=new Block(xCenter,yCenter); //First is placed to the center of the shape
    Block b2=new Block(0,0); //All the others will be placed according to the center and the id
    Block b3=new Block(0,0);
    Block b4=new Block(0,0);
    
    //Different id's create different shapes.
    switch (id){
      case 1:
        b2=new Block(xCenter,yCenter+1);       //   _
        b3= new Block(xCenter+1,yCenter+1);    //  | |___
        b4=new Block(xCenter+2,yCenter+1);     //  |__c__|
        yCenter++; xCenter++; //Shifts the center to b3's position (c) for proper rotation around it
        break;
      case 2:
        b2=new Block(xCenter+1,yCenter);       //   _______
        b3= new Block(xCenter+2,yCenter);      //  |__ c __|
        b4=new Block(xCenter+1,yCenter+1);     //     |_|
        xCenter++; //Shifts the center to b2's position (c) for proper rotation around it
        break;
      case 3:
        b2=new Block(xCenter+1,yCenter);      //   __________
        b3= new Block(xCenter+2,yCenter);     //  |___c______|
        b4=new Block(xCenter+3,yCenter);      //
        xCenter++; //Shifts the center to b2's position (c) for proper rotation around it
        break;
      case 4:                                 //  _
        b2=new Block(xCenter,yCenter+1);      // | |__
        b3= new Block(xCenter+1,yCenter+1);   // |c_  |
        b4=new Block(xCenter+1,yCenter+2);    //    |_|
        yCenter++;//Shifts the center to b2's position (c) for proper rotation around it
        break;
      case 5:
        b2=new Block(xCenter+1,yCenter);     //  ____
        b3= new Block(xCenter,yCenter+1);    // |c   |
        b4=new Block(xCenter+1,yCenter+1);   // |____|
        break; 
      case 6:
        b2=new Block(xCenter+1,yCenter);     //   ______
        b3= new Block(xCenter+2,yCenter);    //  |  _c__|
        b4=new Block(xCenter,yCenter+1);     //  |_|
        xCenter++; //Shifts the center to b2's position (c) for proper rotation around it
        break;
      case 7:
        b2=new Block(xCenter+1,yCenter);     //  ___
        b3= new Block(xCenter+1,yCenter+1);  // |_  |_ 
        b4=new Block(xCenter+2,yCenter+1);   //   |c__|
        xCenter++;
        yCenter++; //Shifts the center to b3's position (c) for proper rotation around it
        break;
    }
    blocks.add(b1);blocks.add(b2);blocks.add(b3);blocks.add(b4); //Adds the created blocks to the block list
    
    //The shape is then rotated n amount of times (0<=n<=3, n is randomly picked)
    //So that each different rotation could appear on the board
    int rotationCount=rand.nextInt(3);
    for(int i=0;i<rotationCount;i++){
      this.rotateShape(); //method to rotate shape
    }
    
  }
  
  //Method to remove given Block object from the block list if it exists.
  public void removeBlock(Block b){
    blocks.remove(b);
  }
  
  //Method to change the coordinates of center to given parameters
  public void setCenter(int x, int y){
    int xChange=x-xCenter; //Finds the distsance between current x and destination x
    int yChange=y-yCenter; //Same for y
    this.shiftShape(xChange,yChange); //Utilizes the shiftShape method to shift the shape by calculated distance
  }
  
  //Shifts the shape by given x and y units
  public void shiftShape(int xNew,int yNew){
    //Shifts the center
    xCenter+=xNew; 
    yCenter+=yNew;
    
    //Then applies the shift to each block of shape
    for(Block b:blocks){
      b.setX(b.x+xNew);
      b.setY(b.y+yNew);
    }
  }
  
  //Rotates the shape around the center point
  public void rotateShape(){
    //We have tried on paper and observed how each location and (x,y) pair change when a block is rotated
    //around a certain point. Then we came up with this method
    for(Block b:blocks){ //for each block
      //The distance from center is calculated for each direction
      int xDifference=b.x-xCenter;
      int yDifference=b.y-yCenter;
      b.setX(xCenter+yDifference*-1); //The new x distance from center becomes the previous negative y distance
      b.setY(yCenter+xDifference); //The new y distance from center becomes the previous x distance
    }
  }
  
  //Returns the block with the sought location values,
  public Block getBlockOfLocation(int x,int y){
    for(Block b:blocks){
      if(b.x==x && b.y==y) return b;
    }
    return null; //returns null if no block in shape has those values
  }
  
  //Returns the color (mainly for the tetris component's paint method)
  public Color getColor(){
    return c;
  }
  
  //This method returns all the blocks with the lowest y values in each column (meaning the lowest blocks for each x value the shape holds)
  //This method is called in tetrisgame class to get the blocks to check if the shape can go down a step further
  public ArrayList<Block> findLowest(){
    ArrayList<Block> lowestOfColumn=new ArrayList<Block>(); //empty array list (will be filled and returned)
    HashSet<Integer> columns=new HashSet<Integer>(); //Non-repeating 'list' to store all x values (column #) to find the lowest in
    for(Block b:blocks){ //Looks through each block in shape
      if(!columns.contains(b.x)) columns.add(b.x); //And adds its x value if it hasn't already been added to the list
    }
    for(int colNo:columns){ //for each x in the list
      Block lowestOfCurrentCol=new Block(0,-10);  //This is an examplary block to compare initially
      for(Block b: blocks){ //for each block
        //if the block has the current x value that is being iterated, and has a higher y value (meaning that it is lower in the grid)
        if(b.x==colNo && b.y>lowestOfCurrentCol.y) lowestOfCurrentCol=b; //Sets the lowest of the current row to the block
      }
      lowestOfColumn.add(lowestOfCurrentCol); //then adds the lowest to the arraylist
    }
    return lowestOfColumn; //when done for each row, returns the list
  }
  
  //The same method but gets the blocks with either the greatest (meaning the most right) or the smallest (most left) x value
  //for each row. The right or left check is determined by the boolean (if true=searches most right and vice versa)
  //to check for a step right or left instead of down
  public ArrayList<Block> findMostRightOrLeft(boolean searchRight){
    ArrayList<Block> outerBlocks=new ArrayList<Block>();
    HashSet<Integer> rows=new HashSet<Integer>();
    for(Block b:blocks){
      if(!rows.contains(b.y)) rows.add(b.y); //This time stores each unique y value (row number)
    }
    for(int rowNo:rows){ //for each stored
      if (searchRight){ //if boolean indicates a request for a search for the most right
        Block rightOfRow=new Block(-10,-5); //examplary to start comparing
        for(Block b: blocks){
          if(b.y==rowNo && b.x>rightOfRow.x) rightOfRow=b; //if greater x value, sets the block as rightest of current row
        }
        outerBlocks.add(rightOfRow); //adds it to the list
      } else { //if booolean indicates a request for a search for the most left
        Block leftOfRow=new Block(20,-10);
        for(Block b: blocks){
          if(b.y==rowNo && b.x<leftOfRow.x) leftOfRow=b; //if smaller x value, sets the block as the most left of current row
        }
        outerBlocks.add(leftOfRow); //adds to the list
      }
    }
    
    return outerBlocks; //returns the list 
  }
  
}