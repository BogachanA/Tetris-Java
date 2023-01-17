// Bogachan Arslan & Baran Abali
// Tetris - Final Project

import java.util.*;

public class TetrisGame{
  public ArrayList<Shape> shapesPresent=new ArrayList<Shape>(); //List that stores the shapes in the game
  public ArrayList<ArrayList<Block>> grid=new ArrayList<ArrayList<Block>>(); //The whole grid in which the game is played
  private Random rand=new Random();
  public Shape activeShape; //Current shape that will be moved for next step
  private Shape nextShape; //Next shape to display on the right
  public int score = 0;
  public boolean gameComplete=false; //To check if game is done
  
  //constructor
  public TetrisGame(){
    //Fills the grid by 20 rows and 10 colums with empty blocks
    for(int i=0;i<20;i++){
      ArrayList<Block> row=new ArrayList<Block>();
      for(int j=0;j<10;j++){
        Block b=new Block(j,i);
        row.add(b);
      }
      grid.add(row);
    }
    startGame(); //start the game
  }
   
  //starts the gane by
  public void startGame(){
    int shapeStyle=rand.nextInt(4-1)+1; //randomly picking a shape style (id in shape class)
    int nextShapeStyle=rand.nextInt(4-1)+1; //picks another for the next shape
    Shape firstShape=new Shape(4,0,shapeStyle); //generates them both and places first in the grid
    Shape next=new Shape(12,4,nextShapeStyle); //next to the right by setting their centers accordingly
    activeShape=firstShape; //asssigns them correspondingly
    nextShape=next;
    
    //adds them to the shapes of the game list
    shapesPresent.add(firstShape);
    shapesPresent.add(nextShape);
  }
  
  //restarts the game by
  public void restartGame(){
    grid.clear(); // clearing
    for(int i=0;i<20;i++){ //and re-filling the grid with empty blocks
      ArrayList<Block> row=new ArrayList<Block>();
      for(int j=0;j<10;j++){
        Block b=new Block(j,i);
        row.add(b);
      }
      grid.add(row);
    }
    //resets the game variables
    shapesPresent=new ArrayList<Shape>(); //clears out the shapes
    gameComplete=false;
    score=0;
    
    startGame(); //calls start game
  }
  
  //The method that advances the game a step
  //Returns true if the active shape changes and the next shape starts falling down
  //False if current shape is shifted only 
  public boolean nextStep(){
    if(checkNextStep()) activeShape.shiftShape(0,1); //checks if the shape could be moved down and does if so
    else{ // if nowhere to go down
      try{ //tries to
        for(Block b:activeShape.blocks){
          grid.get(b.y).get(b.x).changeFill(true); //fill all corresponding blocks from grid that shape occupies
        }
        nextShape.setCenter(4,0); //moves the next shape to the top of the grid
        activeShape=nextShape; //sets it to the active shape
        int shapeStyle=rand.nextInt(8-1)+1; //generates
        nextShape=new Shape(12,4,shapeStyle); //and assigns a new next shape to the right
        clearFullRows(); //calls the method to check and remove all filled rows of grid
        shapesPresent.add(nextShape); //registers the newly generated next shape
        score = score + 50; //increases the score by 50 for being able to place a block
        return true;
      }
      catch(IndexOutOfBoundsException e){ //If an error is received when placing the next shape to the grid
        gameComplete=true; //the game is now complete because there is nowhere to put the next shape to
      }
    }
    return false;
  }
  
  //Clears any rows that is filled
  private void clearFullRows(){
    //Checks for fully filled rows
    ArrayList<Integer> fullRowNumbers=new ArrayList<Integer>();
    for(int i=grid.size()-1;i>=0;i--){ //starting from top,
      ArrayList<Block> row=grid.get(i); //checks each row
      boolean allOccupied=true;
      for(Block b:row){
        if(!b.isOccupied()) allOccupied=false; //Unless a block is empty
      }
      if(allOccupied) fullRowNumbers.add(i); //registers a row
    }
    
    //If there are any full rows
    if(fullRowNumbers.size()>=1){
      for(int rowNo:fullRowNumbers){ //for each
        for(Block b:grid.get(rowNo)){ //Gets the block in each row
          b.changeFill(false); //empties it
          for(Shape s:shapesPresent){
            if(activeShape!=s){ // if it isn't the currently active shape (which is just put at the top before calling the method)
              Block correspondingBlockFromShape=s.getBlockOfLocation(b.x,b.y); //gets the block of shape that fills the current spot
              if(correspondingBlockFromShape!=null){
                s.removeBlock(correspondingBlockFromShape); //removes the block from the shape 
                score = score + 50; //adds 50 point for each block removed from board
                break;
              }
            }
          }
        }
      }
      
      //Then to slide the upper blocks down
      for(Shape s:shapesPresent){
        if(activeShape!=s){ //excluding the active shape Because it is currently at the top of board
          for(Block b:s.blocks){ //For each block of shapes
            if(b.y<fullRowNumbers.get(fullRowNumbers.size()-1)){ //if its y value is smaller than the y of the highest filled row
              Block currentCorrespondanceFromMaze=grid.get(b.y).get(b.x);
              currentCorrespondanceFromMaze.changeFill(false); //empties the current location of block on grid
              b.setY(b.y+fullRowNumbers.size()); //shifts the block down by the amount of filled rows
              Block newCorrespondance=grid.get(b.y).get(b.x); //finds its new location
              newCorrespondance.changeFill(true); //and fills that place on grid
            }
          }
        }
      }
    }
  }
  
  //Performs movement of of shape to right or left according to boolean
  public void moveHorizontally(boolean toRight){
    int xShift= toRight? 1:-1; //if right, shifts x by +1, or else -1
    if(checkMoveHorizontally(toRight)) activeShape.shiftShape(xShift,0); //checks if move available, then does if so
  }
  
  //Checks if a horizontal move is available
  //Either right of left accroding to the boolean parameter
  private boolean checkMoveHorizontally(boolean toRight){
    ArrayList<Block> outerBlocks=activeShape.findMostRightOrLeft(toRight); //Gets the rightest or leftest blocks of shape (see method in shape class)
    int indexDifference= toRight? 1:-1; //if right, differnce is +1, or else -1
    for(Block b:outerBlocks){
      try{ //tries to
        Block next=grid.get(b.y).get(b.x+indexDifference); //get the block to the right or left by the distance
        if(next.isOccupied()) return false; //if occupied returns false
      } catch (IndexOutOfBoundsException e){ //if block doesn't exist (ie location is out of the grid)
        return false; //returns false
      }
    }
    return true; //otherwise returns true
  }
  
  //Helper method for saferotate
  //Checks if the current positions of blocks of active shape corresponds to empty places to place them
  private boolean checkIfPlaceable(){
    for(Block b:activeShape.blocks){
      Block corresponding=grid.get(b.y).get(b.x); //gets each corresponding block from grid
      if(corresponding.isOccupied()) return false; //if occupied returns false
    }
    return true;
  }
  
  //This method rotates the block per user request and places it somewhere that it can
  //So for example if when rotated the shape is out of bounds, this method shifts it until it 
  //is in a place that it can be put in the grid
  public void safeRotate(){
    activeShape.rotateShape(); //First rotates the shape (see shape class)
    boolean shapeNotPlaceable=true; //flag for placement
    int carryDistance=1; //First carrying distance
    int direction=0; //this variable keeps track of the current direction to shift to
    while(shapeNotPlaceable){ //until it is placeable
      try{ //tries to
        if(checkIfPlaceable()){ //check the corresponding blocks from grid and see their occupancies
          shapeNotPlaceable=false;
          break;
        }
      } catch (IndexOutOfBoundsException e){} //If shape out of the grid (no block corresponds) then moves on 
      
      //Each direction is tried one by one
      //And each shift in dirrection is the same amount
      //If all directions fail, then increments the shifting distance
      switch(direction%4){
        case 0: //try to shift to right and place it
          activeShape.shiftShape(carryDistance,0);
          break;
        case 1: //then revert and try left
          activeShape.shiftShape(carryDistance*-2,0); //Negative sign is to go left and x2 is to also revert the shift of case 1
          break;
        case 2: //then try down. This is usually for safely rotating when the shape first enters the grid to prevent going up above it
          activeShape.shiftShape(carryDistance,carryDistance); //shifting in x direction is to revert case 1
          break;
        case 3: //if both fails, revert case 2 and raise the shape up a block
          activeShape.shiftShape(0,-carryDistance*2); //x2 is to revert case 2
          carryDistance++; //Increment the distance to try shifting further away when all directions fail
          break;
      }
      direction++; //increments the direction to try the next case
    }
  }
  
  //This method checks if the block can be shifted down for next step
  public boolean checkNextStep(){
    ArrayList<Block> lowestBlocks=activeShape.findLowest(); //gets the lowest block in each column of shape
    for(Block b:lowestBlocks){
      try{
        Block next=grid.get(b.y+1).get(b.x); //gets the next block (y+1) for each lowest
        if(next.isOccupied()) return false; //if corresponding block is filled already returns false, can't go down
      } catch (IndexOutOfBoundsException e){ //if the next blocks are out of the grid
        return false;//also can't go there
      }
    }
    return true; //otherwise return true;
  }
  
  
}