

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.util.*;  // Needed for Scanner
import java.io.*; // file IO
import javax.imageio.*; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.*;
import java.io.*;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class pacmanSummative extends JFrame implements MouseListener, ActionListener
{
    Map map = new Map (40,30,20,20);
    JComboBox itemBox;
    boolean key_right, key_left, key_down, key_up; // Input booleans
    int orientation = 2; // Orientation 1 is up, 2 is right, 3 is down
    
    
    //======================================================== constructor
    public pacmanSummative () 
    {
    // 1... Create/initialize components
    JButton searchBtn = new JButton ("Search");
    searchBtn.addActionListener (this);
    JButton saveBtn = new JButton ("Save");
    saveBtn.addActionListener (this);
    JButton loadBtn = new JButton ("Load");
    loadBtn.addActionListener (this);
    String list[] = {"Space","Pellet","Wall"};
    itemBox = new JComboBox(list);

    // 2... Create content pane, set layout
    JPanel content = new JPanel ();        // Create a content pane
    content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
    JPanel north = new JPanel (); // where the buttons, etc. will be
    north.setLayout (new FlowLayout ()); // Use FlowLayout for input area

    DrawArea board = new DrawArea (800, 600); // Area for cards to be displayed

    // 3... Add the components to the input area.
    north.add (searchBtn);
    north.add (saveBtn);
    north.add (loadBtn);
    north.add (itemBox);
    content.add (north, "North"); // Input area
    content.add (board, "Center"); // Deck display area

    content.addMouseListener(this);

    // 4... Set this window's attributes.
    setContentPane (content);
    pack ();
    setTitle ("Map Demo");
    setSize (850, 700);
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo (null);           // Center window.
    }

    public void actionPerformed (ActionEvent e)
    {
        if (e.getActionCommand ().equals ("Search"))
        
        {int found = map.search((String)itemBox.getSelectedItem());    //Returns number of items found
            JOptionPane.showMessageDialog(null, found+" "+(String)itemBox.getSelectedItem()+"s found.");}

    else if (e.getActionCommand ().equals ("Load"))  //Loads map
    {
        
        map.load("SaveMap.txt");
    }

    repaint (); // do after each action taken to update deck
    }
    
    public void GameInput (KeyEvent e)
    {
        
    }
        
    class DrawArea extends JPanel
    {
    public DrawArea (int width, int height)
    {
        this.setPreferredSize (new Dimension (width, height)); // size
    }

    public void paintComponent (Graphics g)
    {
        map.print (g);
    }
    }

    // Must implement all methods from listener
    public void mousePressed(MouseEvent e) {
    
    char chara = ' ';
    
    if (itemBox.getSelectedItem().equals("Space"))   
      chara = ' ';
      
    if (itemBox.getSelectedItem().equals("Wall"))
      chara = 'W';
      
    if (itemBox.getSelectedItem().equals("Pellet"))
      chara = 'P';
      
    map.add(e.getX(),e.getY(),chara); //Adds tile 
    repaint();
    
    }
       
    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}    

    //======================================================== method main
    public static void main (String[] args)
    {
    pacmanSummative window = new pacmanSummative ();
    window.setVisible (true);
    }
}

class pacman
{
    protected char map [] [];
    protected int width, height;
    private Image wall,floor,pellet, pacmanUp, pacmanRight, pacmanDown, pacmanLeft;
     boolean key_right, key_left, key_down, key_up; // Input booleans

    public pacman (int cols, int rows, int blockwidth, int blockheight) // set up default map
    {
    width = blockwidth;  height = blockheight;
    map = new char [rows] [cols]; // define 2-d array size

    for (int row = 0 ; row < rows ; row++)
        for (int col = 0 ; col <cols ; col++)
        {
        if (row == 0 || row == rows-1 || col == 0 || col == cols-1)   //Sets up map
            map [row] [col] = 'W'; // put up a wall
        else
            map [row] [col] = ' '; // blank space
    }
    
    try
        {
            wall = ImageIO.read (new File ("Wall.jpg"));    //Load images
            floor = ImageIO.read (new File ("Floor.jpg")); 
            pellet = ImageIO.read (new File ("Pellet.jpg"));  
            pacmanUp = ImageIO.read (new File ("Up.jpg"));
            pacmanRight = ImageIO.read (new File ("Right.jpg"));
            pacmanDown = ImageIO.read (new File ("Down.jpg"));
            pacmanLeft = ImageIO.read (new File ("Left.jpg"));
        }
        catch (IOException e)
        {
            System.out.println ("File not found");
        }
        
    addKeyListener(new GameInput()); // Add it to the JPanel
   
    }

    public void print (Graphics g)  // displays the map on the screen
    {
      for (int row = 0 ; row < map.length; row++)// number of rows
      {
        for (int col = 0 ; col < map[0].length; col++)// length of first row
        {
        if (map [row] [col] == 'W') // wall
            g.drawImage (wall, col*20, row*20, null);
        else if (map [row] [col] == 'P') // door
            g.drawImage (pellet, col*20, row*20, null);
        else if (map [row] [col] == ' ') // space will erase what was there
            g.drawImage (floor, col*20, row*20, null);
        }
      }  
       
      g.drawImage (pacmanRight, col / 2, row / 2, null);
    }    
    
    class GameInput implements KeyListener 
    {
            public void keyTyped(KeyEvent e) {}

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == e.VK_DOWN) key_down = false;
                if (e.getKeyCode() == e.VK_UP) key_up = false;
                if (e.getKeyCode() == e.VK_RIGHT) key_right = false;
                if (e.getKeyCode() == e.VK_LEFT) key_left = false;
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == e.VK_DOWN) key_down = true;
                if (e.getKeyCode() == e.VK_UP) key_up = true;
                if (e.getKeyCode() == e.VK_RIGHT) key_right = true;
                if (e.getKeyCode() == e.VK_LEFT) key_left = true;
            }
        }

    public void play (String fileName)
    {
      BufferedReader reader = null;  
        try
        {
        reader = new BufferedReader(new FileReader(fileName));
        }
        catch(FileNotFoundException f){}
        String line = "";
        int row = 0;
        try {
            while((line = reader.readLine()) != null)
            {
               String[] cols = line.split(",");
               int col = 0;
               for(String  c : cols)
               {
                  map[row][col] = c.charAt(0);   
                  col++;
               }
               row++;
            }
        reader.close();
        } catch (IOException e) {}  
    }  
    
    public void pacmanMove ()
    {
            int xCoordinate, yCoordinate;
            
            if (key_down) 
            { 
                 loadImage("Down.jpg"); 
                 xCoordinate = col / 2;
                 yCoordinate = row / 2;
                 g.drawImage (pacmanDown, col / 2, row / 2, null);
            }

            
            if (key_up) 
            {
                loadImage("Up.jpg"); yCoordinate--; 
            }

          
            if (key_right) 
            { 
                loadImage("Right.jpg"); xCoordinate++; 
            }

           
            if (key_left) 
            { 
                loadImage("Left.jpg"); xCoordinate--; 
            }
    }

}

