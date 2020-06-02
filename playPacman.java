import java.awt.*;
import java.io.*; // allows file access
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.*; // file IO
import javax.imageio.*; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.*;
import javax.swing.JOptionPane;
import java.util.Random;
import sun.audio.*; 
import java.io.File;
import java.io.IOException; 


class playPacman extends JFrame implements KeyListener
{
    static Map map = new Map (30, 40); // create map, block size
    static Timer t;
    public static int pacmanDirection = 0 ; // Direction 1 is up, 2 is right, 3 is down, 4 is left

    
    
    //======================================================== constructor
    public playPacman ()
    {

    // 1... Enable key listener for movement
    addKeyListener (this);
    setFocusable (true);
    setFocusTraversalKeysEnabled (false);

    // 2... Create content pane, set layout
    JPanel content = new JPanel ();        // Create a content pane



    DrawArea board = new DrawArea (1200, 800); // Area for map to be displayed

    content.add (board); // map display area

    // 4... Set this window's attributes.
    setContentPane (content);
    pack ();
    setTitle ("RPG Demo");
    setSize (1200, 800);
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo (null);           // Center window.

    board.addKeyListener (this);

    Movement randomStuff = new Movement (); // ActionListener for timer
    t = new Timer (200, randomStuff); // set up Movement to run every 250 milliseconds
    t.start (); // start the timer
  
    pacmanMovement move = new pacmanMovement ();
    t = new Timer (200,move);
    t.start();
    

    }

    public void actionPerformed (ActionEvent e)
    {
        
    }
    
    public void keyTyped (KeyEvent e)
    {
    // nothing
    }


    public void keyReleased (KeyEvent e)
    {
    // nothing
    }


    public void keyPressed (KeyEvent e) // handle cursor keys and enter
    {
    int key = e.getKeyCode ();
    
    
    switch (key)
    {
        case KeyEvent.VK_UP:    
        pacmanDirection = 1;  
        map.starter = true;

        break;
        
        case KeyEvent.VK_DOWN:
        pacmanDirection = 3;        
        map.starter = true;                        // ActionListener for timer
    
        break;
        
        case KeyEvent.VK_LEFT:
        pacmanDirection = 4;  
        map.starter = true;                     // ActionListener for timer
    
        break;
        
        case KeyEvent.VK_RIGHT:
        pacmanDirection = 2;                        // ActionListener for timer
        map.starter = true;
        
        break;
        
    
    }
    repaint ();
    }



    class DrawArea extends JPanel // draw board
    {
    public DrawArea (int width, int height)
    {
        this.setPreferredSize (new Dimension (width, height)); // size
    }


    public void paintComponent (Graphics g) // draw game board
    {
        map.print (g);
    }
    }


    class Movement implements ActionListener // executed according to timer delay
    {
    public void actionPerformed (ActionEvent event)
    {
        repaint (); // refresh
    }
    } 

    class pacmanMovement implements ActionListener // timer for pacman moving up
    {
        public void actionPerformed (ActionEvent event)
        {
            if (pacmanDirection == 1)
            map.move (0, -1, pacmanDirection);    
            
            if (pacmanDirection == 2)
             map.move (1, 0, pacmanDirection);  
             
            if (pacmanDirection == 3)
             map.move (0, 1, pacmanDirection);  
             
            if (pacmanDirection == 4)
             map.move (-1, 0, pacmanDirection);  
            
        }
     }  
    
  
    //======================================================== method main
    public static void main (String[] args)
    {
    playPacman window = new playPacman ();
    window.setVisible (true);
    }
}


// --------------------------------------  ------------------------------------------------------

class Map
{
    private char map[] [];                                                                                     //Declare variables 
    private int  posx, posy, points = 0, lives = 3, level = 1,ghost1x,ghost1y,ghost2x,ghost2y,ghost3x,ghost3y,ghost4x,ghost4y,spawnx,spawny;
    public Image wall,floor,pellet, pacmanUp, pacmanRight, start,pacmanDown,gameover, pacmanLeft, pacmanCurrent,cherry,ghost;
    int pDirection = playPacman.pacmanDirection;   
    boolean starter = false;
    

    
    public Map (int rows, int cols)             
    {
    
    map = new char [rows] [cols]; // define 2-d array size

      ghost1x = 1;  //Sets ghost spawn point
    ghost1y = 1;
    ghost2x = 1;
    ghost2y = 28;
    ghost3x = 38;
    ghost3y = 1;
    ghost4x = 38;
    ghost4y = 28;
    
    BufferedReader reader = null;                                 //Reads map from file, creates 2D array
        try
        {
        reader = new BufferedReader(new FileReader("SaveMap.txt"));
        }
        catch(FileNotFoundException f){}
        String line = "";
        int row = 0;
        try {
            while((line = reader.readLine()) != null)
            {
               String[] col = line.split(",");
               int coll = 0;
               for(String  c : col)
               {
                  map[row][coll] = c.charAt(0);   
                  coll++;
               }
               row++;
            }
        reader.close();
        } catch (IOException e) {}
   
    try
        {
            wall = ImageIO.read (new File ("Wall.jpg"));    //Load images
            floor = ImageIO.read (new File ("Floor.jpg")); 
            pellet = ImageIO.read (new File ("Pellet.jpg"));
            pacmanCurrent = ImageIO.read (new File ("Up.jpg"));
            cherry = ImageIO.read (new File ("Cherry.jpg"));
            ghost = ImageIO.read (new File ("Ghost.jpg"));
            gameover = ImageIO.read (new File ("Gameover.jpg"));
            start = ImageIO.read (new File ("Start.jpg"));
        }
        
        catch (IOException e)
        {
            System.out.println ("File not found");
        }    
        
    for (int x = 0 ; x < 30 ; x ++)    //Finds where pacman is, sets coordinates 
    { 
      for (int y = 0; y < 40 ; y++)
      {
        if (map[x][y] == 'M')   //Finds pacman, replaces with blank
        {
          posx = y;
          posy = x;
          map[x][y] = ' ';
          
          spawnx = posx;
          spawny = posy;
        }
      }
    }
    
    }

    public void newLevel()    //When old level is beaten, new level
    {
      level++;  //Adds 1 to level counter 
      ghost1x = 1;  //Sets ghost spawn point
    ghost1y = 1;
    ghost2x = 1;
    ghost2y = 28;
    ghost3x = 38;
    ghost3y = 1;
    ghost4x = 38;
    ghost4y = 28;

      BufferedReader reader = null;                                         //Repeats what is found in constructor 
        try
        {
        reader = new BufferedReader(new FileReader("SaveMap.txt"));
        }
        catch(FileNotFoundException f){}
        String line = "";
        int row = 0;
        try {
            while((line = reader.readLine()) != null)
            {
               String[] col = line.split(",");
               int coll = 0;
               for(String  c : col)
               {
                  map[row][coll] = c.charAt(0);   
                  coll++;
               }
               row++;
            }
        reader.close();
        } catch (IOException e) {}
        
        for (int x = 0 ; x < 30 ; x ++)
    { 
      for (int y = 0; y < 40 ; y++)
      {
        if (map[x][y] == 'M')
        {
          posx = y;
          posy = x;
          map[x][y] = ' ';
        }
      }
    }
      
    }

    public void print (Graphics g)    // displays the map on the screen
    {
    for (int row = 0 ; row < map.length ; row++) // number of rows
    {
        for (int col = 0 ; col < map [0].length ; col++) // length of first row
        {
        if (map [row] [col] == 'W') // wall
            g.drawImage (wall, col*20, row*20, null);
        else if (map [row] [col] ==   'P') // pellet
            g.drawImage (pellet, col*20, row*20, null);
        else if (map[row][col] == 'C')
            g.drawImage(cherry,col*20 , row*20,null);
            
        else if (map [row] [col] == ' ') // space will erase what was there
            g.drawImage (floor, col*20, row*20, null);
            
        g.drawImage(ghost,ghost1x*20,ghost1y*20,null);   
        g.drawImage(ghost,ghost2x*20,ghost2y*20,null);  
        g.drawImage(ghost,ghost3x*20,ghost3y*20,null);  
        g.drawImage(ghost,ghost4x*20,ghost4y*20,null);  
        
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));    //Displays stats 
        g.drawString("Points: " + points, 810,30 );
        g.drawString("Lives: " + lives, 810, 60);
        g.drawString("Level: " + level , 810, 90);
        
       
            
        }
    }
    
        
        g.drawImage (pacmanCurrent,posx * 20, posy * 20, null); // draw character
         if (lives == 0)
          g.drawImage(gameover,0,0,null);
          
         if (starter== false)
          g.drawImage(start,0,0,null);
    }

    

 

    public void move (int dx, int dy, int direction) // moves character if possible (no obstruction)
    {
       
        if (lives!= 0)
        moveTheGhost();   
        
    if (search() == 0)
      newLevel();
        
    if (map [posy + dy] [posx + dx] == ' ' ||map [posy + dy] [posx + dx] == 'P'||map [posy + dy] [posx + dx] == 'C' ) // empty space, pellet, or cherry
    {       
       try
        {          
            if (direction == 0)
                pacmanCurrent = ImageIO.read (new File ("Right.jpg"));    //Image of pacman depending on his orientation 
            if (direction == 1)
                pacmanCurrent = ImageIO.read (new File ("Up.jpg"));
            if (direction == 2)
                pacmanCurrent = ImageIO.read (new File ("Right.jpg"));
            if (direction == 3)
                pacmanCurrent = ImageIO.read (new File ("Down.jpg"));
            if (direction == 4)
                pacmanCurrent = ImageIO.read (new File ("Left.jpg"));       
        }
        catch (IOException e)
        {
            System.out.println ("File not found");
        }
        if (lives != 0)
        {posx += dx;     //Changes pacman's location 
        posy += dy;}
        
        if (map[posy][posx] == 'P')   //If pacman eats something, gains points
          points = points + 10;
        
        if (map[posy][posx] == 'C')
          points = points + 100;
          
        map[posy][posx] = ' ';   //Object is eaten, replaced with blank
      
          
    }
    
    }

    public int search()   //Counts number of pellets. If zero, next level 
    {
      char item;
        int count=0;

            
        for (char[] a: map)//go through array and find number of matches
            for (char b: a)
                if (b=='P' || b == 'C')
                    count++;
                    
        return count;
    }
    
    public void moveTheGhost()  //Ghost moves
    {
      int x,originalx,originaly;
      
      Random rand = new Random();   
      
      
      do{                        //Move first ghost
      x = rand.nextInt(4);

      originalx = ghost1x;   //Remember original location
      originaly = ghost1y;
    
      
  
       if (x == 0 && ghost1x != 0)   //RNG will move ghost
        ghost1x--;
        
      if (x == 1 && ghost1x != 40)
        ghost1x++;
        
      if (x == 2 && ghost1y != 0)
        ghost1y--;
        
      if (x == 3 && ghost1y != 30)
        ghost1y++;
        
      if (map[ghost1y][ghost1x] != 'W')   //If ghost enters a wall, return to original location 
      {
        
        break;
      }
      
      ghost1x = originalx;
      ghost1y = originaly;
    }
    while (true);
    
      if (posx == ghost1x && posy == ghost1y)  //If touches pacman, he dies 
        youDied();
      
      else if (originalx == posx && originaly == posy)
        
        youDied();
        
     do{                           //Move second ghost
      x = rand.nextInt(4);

      originalx = ghost2x;   //Remember original location
      originaly = ghost2y;
    
      
  
       if (x == 0 && ghost2x != 0)   //RNG will move ghost
        ghost2x--;
        
      if (x == 1 && ghost2x != 40)
        ghost2x++;
        
      if (x == 2 && ghost2y != 0)
        ghost2y--;
        
      if (x == 3 && ghost2y != 30)
        ghost2y++;
        
      if (map[ghost2y][ghost2x] != 'W')   //If ghost enters a wall, return to original location 
      {
        
        break;
      }
      
      ghost2x = originalx;
      ghost2y = originaly;
    }
    while (true);
    
      if (posx == ghost2x && posy == ghost2y)  //If touches pacman, he dies 
        youDied();
      
      else if (originalx == posx && originaly == posy)
        
        youDied();
        
        
      do{                         //Third ghost
      x = rand.nextInt(4);

      originalx = ghost3x;   //Remember original location
      originaly = ghost3y;
    
      
  
       if (x == 0 && ghost3x != 0)   //RNG will move ghost
        ghost3x--;
        
      if (x == 1 && ghost3x != 40)
        ghost3x++;
        
      if (x == 2 && ghost3y != 0)
        ghost3y--;
        
      if (x == 3 && ghost3y != 30)
        ghost3y++;
        
      if (map[ghost3y][ghost3x] != 'W')   //If ghost enters a wall, return to original location 
      {
        
        break;
      }
      
      ghost3x = originalx;
      ghost3y = originaly;
    }
    while (true);
    
      if (posx == ghost3x && posy == ghost3y)  //If touches pacman, he dies 
        youDied();
      
      else if (originalx == posx && originaly == posy)
        
        youDied();
        
        
       do{                            //Ghost 4
      x = rand.nextInt(4);

      originalx = ghost4x;   //Remember original location
      originaly = ghost4y;
    
      
  
       if (x == 0 && ghost4x != 0)   //RNG will move ghost
        ghost4x--;
        
      if (x == 1 && ghost4x != 40)
        ghost4x++;
        
      if (x == 2 && ghost4y != 0)
        ghost4y--;
        
      if (x == 3 && ghost4y != 30)
        ghost4y++;
        
      if (map[ghost4y][ghost4x] != 'W')   //If ghost enters a wall, return to original location 
      {
        
        break;
      }
      
      ghost4x = originalx;
      ghost4y = originaly;
    }
    while (true);
    
      if (posx == ghost4x && posy == ghost4y)  //If touches pacman, he dies 
        youDied();
      
      else if (originalx == posx && originaly == posy)
        
        youDied();
    }
    
    public void youDied()  
    {
      posx = spawnx;   //Returned to spawn
      posy = spawny;
      lives --;
      
      
     ghost1x = 1;  //Sets ghost spawn point
    ghost1y = 1;
    ghost2x = 1;
    ghost2y = 28;
    ghost3x = 38;
    ghost3y = 1;
    ghost4x = 38;
    ghost4y = 28;
      
    }
}

