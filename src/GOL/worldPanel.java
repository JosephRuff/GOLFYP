package GOL;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.Toolkit;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.concurrent.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class worldPanel extends JPanel
{
	private static final long serialVersionUID = 1L;  		// default option to clear serialisation warning
	
	private GUI parent;
	
	private int noOfAlive;
	private int noOfDead;
	private int noOfAliveMax;
	private int noOfDeadMax;
	private int noOfAliveMin;
	private int noOfDeadMin;
	private int noOfCells;
	private int currentGeneration; 
	private ArrayList<int[]> counterChangeList = new ArrayList<int[]>();
	
	private int cellSize;
	private int worldWidth;
	private int worldHeight; 
	private int maxCellSize = 100;
	private int maxWidth = 1000;
	private int maxHeight = 1000; 
	private int currentBufferSizeX; 
	private int currentBufferSizeY; 
	private int desiredBufferSize = 500; 
	private boolean xWrapMode = false;
	private boolean yWrapMode = false;
	
	private Graphics2D g;
	private BufferedImage worldImage;
	private boolean previewEnabled = false; 
	private ArrayList<Cell> dyingList = new ArrayList<Cell>();
	private ArrayList<Cell> newbornList = new ArrayList<Cell>();
	private Color aliveColour = new Color(0,0,0);
	private Color deadColour = new Color(255,255,255);
	private Color newbornColour = new Color(200,255,200);
	private Color dyingColour = new Color(175,0,0);
	private Color markerColour = new Color(255, 0, 255);
	private boolean resetFlag;
	
	private boolean stampModeFlag = false;
	private ArrayList<Cell> stampList = new ArrayList<Cell>();
	
	private boolean[][] cell;
	private boolean[][] cellBackup;
	private ArrayList<Cell> markerList = new ArrayList<Cell>();
	private ArrayList<Cell> changeList = new ArrayList<Cell>();
	private ArrayList<ArrayList<Cell>> pastChangeList = new ArrayList<ArrayList<Cell>>();
	private int noOfUndos = 10;
	
	private int PERIOD_INTERVAL = 200;
	private boolean running = false;
	private ScheduledExecutorService  timer;
	private ScheduledFuture<?> futureTask;
	private ScheduleTask timerTask;
	
	private boolean[] bornRules = new boolean[9];
	private boolean[] surviveRules = new boolean[9];
	
	public worldPanel(GUI parent, int size, int width, int height) 		// constructor
    {
		this.parent = parent;
		
		
		addMouseListener(new MouseAdapter()
		{
    		public void mouseReleased(MouseEvent me)
    		{
    			destroyMarker();
    			editBoard(me.getX(), me.getY());
    		}
    	});
		
		addMouseListener(new MouseAdapter()
		{
    		public void mousePressed(MouseEvent me)
    		{
    			destroyMarker();
    			createMarker(me.getX(), me.getY());
    		}
    	});
		
		addMouseMotionListener(new MouseAdapter()
		{
    		public void mouseDragged(MouseEvent me)
    		{
    			destroyMarker();
    			createMarker(me.getX(), me.getY());
    		}
    	});
		
		initWorldPanel(size, width, height);
    }

	private void initWorldPanel(int size, int width, int height)	//initializes world panel
	{
		setDoubleBuffered(true);
		
		bornRules[0] = false;
		bornRules[1] = false;
		bornRules[2] = false;
		bornRules[3] = true;
		bornRules[4] = false;
		bornRules[5] = false;
		bornRules[6] = false;
		bornRules[7] = false;
		bornRules[8] = false;
		surviveRules[0] = false;
		surviveRules[1] = false;
		surviveRules[2] = true;
		surviveRules[3] = true;
		surviveRules[4] = false;
		surviveRules[5] = false;
		surviveRules[6] = false;
		surviveRules[7] = false;
		surviveRules[8] = false;
		
		
		resizeWorldPanel(size, width, height);
        timer = Executors.newSingleThreadScheduledExecutor();
        timerTask = new ScheduleTask();
  	}
	
	public void resizeWorldPanel(int size, int width, int height)
	{
		destroyPreview(g);	//clear up any existing previews
		createBackup();		//store backup of current world
		
		//calculate buffersize
		this.currentBufferSizeX = this.desiredBufferSize;
		this.currentBufferSizeY = this.desiredBufferSize;
		if(xWrapMode)
		{
			this.currentBufferSizeX = 0;
		}
		if(yWrapMode)
		{
			this.currentBufferSizeY = 0;
		}
		boolean resizeFlag = true;
		if ((this.worldWidth * this.cellSize == width * size) && (this.worldHeight * this.cellSize == height * size))
		{
			resizeFlag = false;
		}
		this.cellSize = size;
		this.worldWidth = width;
		this.worldHeight = height;
			
		boolean[][] temp =  new boolean[this.worldWidth + (this.currentBufferSizeX * 2)][this.worldHeight + (this.currentBufferSizeY * 2)];
		for(int x = 0; x < (this.worldWidth + (this.currentBufferSizeX * 2)); x++)
		{
			for(int y = 0; y < (this.worldHeight + (this.currentBufferSizeY * 2)); y++)
			{
				temp[x][y] = false;
			}
		}
		this.cell = temp;
		int panelWidth = this.worldWidth * this.cellSize;
		int panelHeight = this.worldHeight * this.cellSize;
		if (resizeFlag)
		{
			setPreferredSize(new Dimension(panelWidth, panelHeight));
			this.worldImage = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
		}
		this.g = this.worldImage.createGraphics();
	    this.g.setColor(this.deadColour);
	    this.g.fillRect(0, 0, panelWidth, panelHeight);
	    Toolkit.getDefaultToolkit().sync();
	    restoreBackup();
	    updateImage(g);	
	    resetCounters();
	    repaint();
	    if (resizeFlag)
		{
	    	parent.pack();
		}
        if(this.previewEnabled)
        {
        	createPreview(g);
        }
        this.pastChangeList.clear();
	}
	
	public void createBackup()	//backup current world state
	{
		boolean[][] temp = new boolean[this.worldWidth][this.worldHeight];
		for(int x = 0; x < (this.worldWidth); x++)
		{
			for(int y = 0; y < (this.worldHeight); y++)
			{
				temp[x][y] = this.cell[x + this.currentBufferSizeX][y + this.currentBufferSizeY];
			}
		}
		this.cellBackup = temp;
	}
	
	public void restoreBackup()	//restore world state from backup
	{
		for(int x = 0; x < this.cellBackup.length; x++)
		{
			for(int y = 0; y < this.cellBackup[x].length; y++)
			{
				if (this.cellBackup[x][y])
				{
					this.changeList.add(new Cell(x + this.currentBufferSizeX,y + this.currentBufferSizeY));
				}
			}
		}
	}
	
	public void backupChanges()	//stores a copy of previous changes
	{
		this.pastChangeList.add((ArrayList<Cell>) this.changeList.clone());
		if (this.pastChangeList.size() > this.noOfUndos)
		{
			this.pastChangeList.remove(0);
		}
	}

	public void saveWorld()
    {
		//setup save dialog
    	String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
    	JFileChooser chooser = new JFileChooser(userDir);
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Game of life saves", "gol");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	System.out.println("You chose to save to this file: " + chooser.getSelectedFile().getName());
        	try{
        		//write to the file
        	    String saveFile = chooser.getSelectedFile().getName();
        	    if (!saveFile.endsWith(".gol"))
        	    {
        	    	saveFile += ".gol";
        	    }
        	    PrintWriter writer = new PrintWriter(saveFile, "UTF-8");
        	    writer.println("Size " + this.worldWidth + " " + this.worldHeight);
        	    writer.println("noOfAliveMax " + this.noOfAliveMax);
        	    writer.println("noOfDeadMax " + this.noOfDeadMax);
        	    writer.println("noOfAliveMin " + this.noOfAliveMin);
        	    writer.println("noOfDeadMin " + this.noOfDeadMin);
        	    writer.println("currentGeneration " + this.currentGeneration);
        	    writer.println("xWrapMode " + this.xWrapMode);
        	    writer.println("yWrapMode " + this.yWrapMode);
        	    for(int i = 0; i < this.worldWidth + (this.currentBufferSizeX * 2); i++)
        	    {
        	    	for(int j = 0; j < this.worldHeight + (this.currentBufferSizeY * 2); j++)
        	    	{
        	    		if(cell[i][j] == true){
        	    			writer.println((i - this.currentBufferSizeX) + " " + (j - this.currentBufferSizeY));
        	    		}
        	    	}
        	    }
        	    writer.close();
        	} 
        	catch (IOException e) {
        		System.out.println("Exception Reported by worldPanel.saveWorld(): " + e.getMessage());
        	}
        }
    	
	}
	
	public void saveWorldPacked()
    {
		if (this.noOfAlive > 0)
		{
			int leftGap = getLeftGap();
			int rightGap = getRightGap();
			int topGap = getTopGap();
			int bottomGap = getBottomGap();
			
			//setup save dialog
	    	String userDirLocation = System.getProperty("user.dir");
	        File userDir = new File(userDirLocation);
	    	JFileChooser chooser = new JFileChooser(userDir);
	    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Game of life stamps", "golx");
	        chooser.setFileFilter(filter);
	        int returnVal = chooser.showSaveDialog(parent);
	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	        	System.out.println("You chose to save to this file: " + chooser.getSelectedFile().getName());
	        	try{
	        		//write to the file
	        		String saveFile = chooser.getSelectedFile().getName();
	        	    if (!saveFile.endsWith(".golx"))
	        	    {
	        	    	saveFile += ".golx";
	        	    }
	        	    PrintWriter writer = new PrintWriter(saveFile, "UTF-8");
	        	    writer.println("Size " + (this.worldWidth - leftGap - rightGap) + " " + (this.worldHeight - topGap - bottomGap));
	        	    for(int i = 0; i < this.worldWidth + (this.currentBufferSizeX * 2); i++)
	        	    {
	        	    	for(int j = 0; j < this.worldHeight + (this.currentBufferSizeY * 2); j++)
	        	    	{
	        	    		if(cell[i][j] == true){
	        	    			writer.println((i - this.currentBufferSizeX - leftGap) + " " + (j - this.currentBufferSizeY - topGap));
	        	    		}
	        	    	}
	        	    }
	        	    writer.close();
	        	} 
	        	catch (IOException e) {
	        		System.out.println("Exception Reported by worldPanel.saveWorldPacked(): " + e.getMessage());
	        	}
	        }
		}
		else
		{
			JOptionPane.showMessageDialog(null, "There is nothing to save. ", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void loadWorld()
    {
		//setup load dialog
        destroyPreview(g);
    	String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
    	JFileChooser chooser = new JFileChooser(userDir);
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Game of life saves", "gol", "golx");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
        	try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
        		//read from the file
        		String line;
        	    clearBoard();
        	    while ((line = br.readLine()) != null) 
        	    {
        	    	String[] split = line.split(" ");
        	    	if(split.length == 3)
        	    	{
        	    		resizeWorldPanel(this.cellSize, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        	    	}
        	    	if(split.length == 2)
        	    	{
        	    		if(split[0].equals("noOfAliveMax"))
        	    		{
        	    			this.noOfAliveMax = Integer.parseInt(split[1]);
        	    			this.resetFlag = false;
        	    		}
        	    		else if(split[0].equals("noOfDeadMax"))
        	    		{
        	    			this.noOfDeadMax = Integer.parseInt(split[1]);
        	    			this.resetFlag = false;
        	    		}
        	    		else if(split[0].equals("noOfAliveMin"))
        	    		{
        	    			this.noOfAliveMin = Integer.parseInt(split[1]);
        	    			this.resetFlag = false;
        	    		}
        	    		else if(split[0].equals("noOfDeadMin"))
        	    		{
        	    			this.noOfDeadMin = Integer.parseInt(split[1]);
        	    			this.resetFlag = false;
        	    		}
        	    		else if(split[0].equals("currentGeneration"))
        	    		{
        	    			this.currentGeneration = Integer.parseInt(split[1]);
        	    			this.resetFlag = false;
        	    		}
        	    		else if(split[0].equals("xWrapMode"))
        	    		{
        	    			if (Boolean.parseBoolean(split[1]) == true)
        	    			{
        	    				if (this.xWrapMode == false)
        	    				{
        	    					toggleXWrap();
        	    					this.parent.xWrapMenuItem.setSelected(true);
        	    				}
        	    			}
        	    			else
        	    			{
        	    				if (this.xWrapMode == true)
        	    				{
        	    					toggleXWrap();
        	    					this.parent.xWrapMenuItem.setSelected(false);
        	    				}
        	    			}
        	    		}
        	    		else if(split[0].equals("yWrapMode"))
        	    		{
        	    			if (Boolean.parseBoolean(split[1]) == true)
        	    			{
        	    				if (this.yWrapMode == false)
        	    				{
        	    					toggleYWrap();
        	    					this.parent.yWrapMenuItem.setSelected(true);
        	    				}
        	    			}
        	    			else
        	    			{
        	    				if (this.yWrapMode == true)
        	    				{
        	    					toggleYWrap();
        	    					this.parent.yWrapMenuItem.setSelected(false);
        	    				}
        	    			}
        	    		}
        	    		else
        	    		{
        	    			this.changeList.add(new Cell(Integer.parseInt(split[0]) + this.currentBufferSizeX, Integer.parseInt(split[1]) + this.currentBufferSizeY));
        	    		}
        	    	}
        	    }
        	    updateImage(g);	
        	    resetCounters();
                repaint();
        	}
        	catch(IOException e)
        	{
        		System.out.println("Exception Reported by worldPanel.loadWorld(): " + e.getMessage());
        	}
        }
        if(this.previewEnabled)
        {
        	createPreview(g);
        }
	}
	
	public void loadWorldCentered()
    {
		//setup load dialog
        destroyPreview(g);
    	String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
    	JFileChooser chooser = new JFileChooser(userDir);
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Game of life saves", "golx");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
        	try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
        		//read from the file
        		String line;
        	    clearBoard();
        	    int x = 0;
    	    	int y = 0;
        	    while ((line = br.readLine()) != null) 
        	    {
        	    	String[] split = line.split(" ");
        	    	if(split.length == 3)
        	    	{
        	    		x = (this.worldWidth - Integer.parseInt(split[1]))/2;
        	    		y = (this.worldHeight - Integer.parseInt(split[2]))/2;
        	    		
        	    	}
        	    	if(split.length == 2)
        	    	{
        	    		this.changeList.add(new Cell(Integer.parseInt(split[0]) + this.currentBufferSizeX + x, Integer.parseInt(split[1]) + this.currentBufferSizeY + y));
        	    	}
        	    }
        	    updateImage(g);	
        	    resetCounters();
                repaint();
        	}
        	catch(IOException e)
        	{
        		System.out.println("Exception Reported by worldPanel.loadWorldCentered(): " + e.getMessage());
        	}
        }
        if(this.previewEnabled)
        {
        	createPreview(g);
        }
	}
	
	public void loadWorldStamp()
    {
		//setup load dialog
        destroyPreview(g);
    	String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
    	JFileChooser chooser = new JFileChooser(userDir);
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Game of life stamps", "golx");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
        	try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
        		//read from the file
        		String line;
        		this.stampList.clear();
        		int x = 0;
    	    	int y = 0;
        		while ((line = br.readLine()) != null) 
        	    {
        	    	String[] split = line.split(" ");
        	    	if(split.length == 3)
        	    	{
        	    		x = Integer.parseInt(split[1]);
        	    		y = Integer.parseInt(split[2]);
        	    	}
        	    	if(split.length == 2)
        	    	{
        	    		this.stampList.add(new Cell(Integer.parseInt(split[0]) - (x / 2), Integer.parseInt(split[1]) - (y / 2)));
        	    	}
        	    }
        	    this.stampModeFlag = true;
        	}
        	catch(IOException e)
        	{
        		System.out.println("Exception Reported by worldPanel.loadWorldStamp(): " + e.getMessage());
        	}
        }
        if(this.previewEnabled)
        {
        	createPreview(g);
        }
	}
	
	public void getInitialList()
	{
		
	}
	
	private void editBoard(int x, int y)	//toggle cell on click
	{
		if (this.running == false)
		{
			if(this.previewEnabled)
			{
				destroyPreview(g);
			}
			if (this.stampModeFlag)
			{
				for (int i = 0; i < this.stampList.size(); i++)
				{
					Cell c = this.stampList.get(i);
					this.changeList.add(new Cell((x / this.cellSize) + this.currentBufferSizeX + c.x, (y / this.cellSize) + this.currentBufferSizeY + c.y));
				}
			}
			else 
			{
				this.changeList.add(new Cell((x / this.cellSize) + this.currentBufferSizeX, (y / this.cellSize) + this.currentBufferSizeY));	
			}
			backupChanges();
			updateImage(g);	
			resetCounters();
	        if(this.previewEnabled)
			{
				createPreview(g);
			}
	        repaint();
	        this.pastChangeList.clear();
		}
	}
	
	private void createMarker(int x, int y)	
	{
		if (this.running == false)
		{
			this.g.setColor(this.markerColour);
			if (this.stampModeFlag)
			{
				for (int i = 0; i < this.stampList.size(); i++)
				{
					Cell c = this.stampList.get(i);
					if ((x / this.cellSize) + c.x >= 0 && (x / this.cellSize) + c.x < this.worldWidth)
					{
						if ((y / this.cellSize) + c.y >= 0 && (y / this.cellSize) + c.y < this.worldHeight)
						{
							this.markerList.add(new Cell((x / this.cellSize) + c.x, (y / this.cellSize) + c.y));
						}
					}
					
				}
				for (int i = 0; i < this.markerList.size(); i++)
				{
					Cell c = this.markerList.get(i);
					this.g.fillRect((c.x) * this.cellSize, (c.y) * this.cellSize, this.cellSize, this.cellSize);
					
				}
			}
			else
			{
				this.markerList.add(new Cell((x / this.cellSize), (y / this.cellSize)));
				Cell c = this.markerList.get(0);
				this.g.fillRect((c.x) * this.cellSize, (c.y) * this.cellSize, this.cellSize, this.cellSize);
			}
			repaint();
		}		
	}
	
	private void destroyMarker()	
	{
		for (int i = 0; i < this.markerList.size(); i++)
		{
			Cell c = this.markerList.get(i);
			if(cell[c.x + this.currentBufferSizeX][c.y + this.currentBufferSizeY])
			{
				this.g.setColor(this.aliveColour);
			}
			else
			{
				this.g.setColor(this.deadColour);
			}
			this.g.fillRect((c.x) * this.cellSize, (c.y) * this.cellSize, this.cellSize, this.cellSize);
		}
		this.markerList.clear();
		repaint();
	}
	
	public void clearBoard()
	{
		if(this.previewEnabled)
		{
			destroyPreview(g);
		}
		for(int x = 0; x < (this.worldWidth + (this.currentBufferSizeX * 2)); x++)
    	{
    		for(int y = 0; y < (this.worldHeight + (this.currentBufferSizeY * 2)); y++)
    		{
    			if (cell[x][y])
    			{
        			this.changeList.add(new Cell(x,y));
    			}
    		}
    	}
		backupChanges();
		updateImage(g);
		resetCounters();
		if(this.previewEnabled)
		{
			createPreview(g);
		}
	}
	
	public void calculateNextStep()
	{
		for(int x = 0; x < (this.worldWidth + (this.currentBufferSizeX * 2)); x++)
    	{
    		for(int y = 0; y < (this.worldHeight + (this.currentBufferSizeY * 2)); y++)
    		{
    			int adjacent = 0;
    			for(int i = -1; i < 2; i++)
    			{
    				for(int j = -1; j < 2; j++)
    				{
    					boolean checkFlag = false; 
    					int xcheck = x + i;
    					int ycheck = y + j;
    					if (!(i == 0 && j == 0))
    					{
    						if (!((xcheck < 0) || (ycheck < 0) || (xcheck >= (this.worldWidth + (this.currentBufferSizeX * 2))) || (ycheck >= (this.worldHeight + (this.currentBufferSizeY * 2)))))
        					{
        						checkFlag = true; 
        					}
    						
    						if (xcheck < 0)
        					{
    							xcheck = xcheck + (this.worldWidth + (this.currentBufferSizeX * 2));
    							if (this.xWrapMode)
    							{
    								checkFlag = true; 
    							}
        					}
    						else if (xcheck >= (this.worldWidth + (this.currentBufferSizeX * 2)))
        					{
    							xcheck = xcheck - (this.worldWidth + (this.currentBufferSizeX * 2));
    							if (this.xWrapMode)
    							{
    								checkFlag = true; 
    							}
        					}
    						
    						if (ycheck < 0)
        					{
    							ycheck = ycheck + (this.worldHeight + (this.currentBufferSizeY * 2));
    							if (this.yWrapMode)
    							{
    								checkFlag = true; 
    							}
        					}
    						else if (ycheck >= (this.worldHeight + (this.currentBufferSizeY * 2)))
        					{
    							ycheck = ycheck - (this.worldHeight + (this.currentBufferSizeY * 2));
    							if (this.yWrapMode)
    							{
    								checkFlag = true; 
    							}
        					}
    					}
    					if (checkFlag)
    					{
    						
    						if (cell[xcheck][ycheck] == true)
    						{
    							adjacent = adjacent + 1;
    						}
    					}
    				}
    			}
    			if (cell[x][y]==false)
    			{
    				if (checkRulesBorn(adjacent))
    				{
    					this.changeList.add(new Cell(x,y));
    				}
    			}
    			else
    			{
    				if (!checkRulesSurvive(adjacent))
    				{
    					this.changeList.add(new Cell(x,y));
    				}
    			}
       		}
    	}
	}
	
	private boolean checkRulesBorn(int adjacent)
	{
		switch (adjacent) 
		{
		case 0: if (this.bornRules[0])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 1: if (this.bornRules[1])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 2: if (this.bornRules[2])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 3: if (this.bornRules[3])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 4: if (this.bornRules[4])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 5: if (this.bornRules[5])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 6: if (this.bornRules[6])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 7: if (this.bornRules[7])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 8: if (this.bornRules[8])
				{
					return true;
				}
				else
				{
					return false;
				}
		default: return false;
		}
	}
	
	private boolean checkRulesSurvive(int adjacent)
	{
		switch (adjacent) 
		{
		case 0: if (this.surviveRules[0])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 1: if (this.surviveRules[1])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 2: if (this.surviveRules[2])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 3: if (this.surviveRules[3])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 4: if (this.surviveRules[4])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 5: if (this.surviveRules[5])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 6: if (this.surviveRules[6])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 7: if (this.surviveRules[7])
				{
					return true;
				}
				else
				{
					return false;
				}
		case 8: if (this.surviveRules[8])
				{
					return true;
				}
				else
				{
					return false;
				}
		default: return false;
		}
	}
	
	private void updateImage(Graphics g)	//apply and clear change list
	{
		for(int i = 0; i < this.changeList.size(); i++)
		{
			Cell c = this.changeList.get(i);
			if(c.x >= 0 && c.x < (this.worldWidth + (this.currentBufferSizeX * 2)) && c.y >= 0 && c.y < (this.worldHeight + (this.currentBufferSizeY * 2)))
			{
				if (this.cell[c.x][c.y] == true)
				{
					this.g.setColor(this.deadColour);
					this.cell[c.x][c.y] = false;
				}
				else
				{
					this.g.setColor(this.aliveColour);
					this.cell[c.x][c.y] = true;
				}
				if(c.x >= this.currentBufferSizeX && c.x < this.worldWidth + this.currentBufferSizeX)
				{
					if(c.y >= this.currentBufferSizeY && c.y < this.worldHeight + this.currentBufferSizeY)
					{
						this.g.fillRect((c.x - this.currentBufferSizeX) * this.cellSize, (c.y - this.currentBufferSizeY) * this.cellSize, this.cellSize, this.cellSize);
					}
				}
			}
		}
		this.changeList.clear();
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void createPreview(Graphics g)
	{
		calculateNextStep();	//find pending changes
		//draw and track preview cells
		for(int i = 0; i < this.changeList.size(); i++)
		{
			Cell c = this.changeList.get(i);
			if(c.x >= 0 && c.x < (this.worldWidth + (this.currentBufferSizeX * 2)) && c.y >= 0 && c.y < (this.worldHeight + (this.currentBufferSizeY * 2)))
			{
				if (this.cell[c.x][c.y] == true)
				{
					this.g.setColor(this.dyingColour);
					this.dyingList.add(new Cell(c.x, c.y));
				}
				else
				{
					this.g.setColor(this.newbornColour);
					this.newbornList.add(new Cell(c.x, c.y));
				}
				if(c.x >= this.currentBufferSizeX && c.x < this.worldWidth + this.currentBufferSizeX)
				{
					if(c.y >= this.currentBufferSizeY && c.y < this.worldHeight + this.currentBufferSizeY)
					{
						this.g.fillRect((c.x - this.currentBufferSizeX) * this.cellSize, (c.y - this.currentBufferSizeY) * this.cellSize, this.cellSize, this.cellSize);
					}
				}
			}
		}
		this.changeList.clear();
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void destroyPreview(Graphics g)	//clear up any existing preview cells
	{
		for(int i = 0; i < this.dyingList.size(); i++)
		{
			Cell c = this.dyingList.get(i);
			if(c.x >= 0 && c.x < (this.worldWidth + (this.currentBufferSizeX * 2)) && c.y >= 0 && c.y < (this.worldHeight + (this.currentBufferSizeY * 2)))
			{
				this.g.setColor(this.aliveColour);
				if(c.x >= this.currentBufferSizeX && c.x < this.worldWidth + this.currentBufferSizeX)
				{
					if(c.y >= this.currentBufferSizeY && c.y < this.worldHeight + this.currentBufferSizeY)
					{
						this.g.fillRect((c.x - this.currentBufferSizeX) * this.cellSize, (c.y - this.currentBufferSizeY) * this.cellSize, this.cellSize, this.cellSize);
					}
				}
			}
		}
		this.dyingList.clear();
		for(int i = 0; i < this.newbornList.size(); i++)
		{
			Cell c = this.newbornList.get(i);
			if(c.x >= 0 && c.x < (this.worldWidth + (this.currentBufferSizeX * 2)) && c.y >= 0 && c.y < (this.worldHeight + (this.currentBufferSizeY * 2)))
			{
				this.g.setColor(this.deadColour);
				if(c.x >= this.currentBufferSizeX && c.x < this.worldWidth + this.currentBufferSizeX)
				{
					if(c.y >= this.currentBufferSizeY && c.y < this.worldHeight + this.currentBufferSizeY)
					{
						this.g.fillRect((c.x - this.currentBufferSizeX) * this.cellSize, (c.y - this.currentBufferSizeY) * this.cellSize, this.cellSize, this.cellSize);
					}
				}
			}
		}
		this.newbornList.clear();
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void previewOff()
	{
		this.previewEnabled = false; 
		destroyPreview(g);
		repaint();
	}
	
	public void previewOn()
	{
		this.previewEnabled = true; 
		createPreview(g);
		repaint();
	}
	
	public void nextStep()
	{
		repaint();

		if(this.previewEnabled)
		{
			destroyPreview(g);
		}
		calculateNextStep();
		backupChanges();
		updateImage(g);
		updateCounters();
		if(this.previewEnabled)
		{
			createPreview(g);
		}
		//repaint();
	}
	
	public void prevStep()
	{
		if (this.pastChangeList != null && !(this.pastChangeList.isEmpty())) 
		{
			if(this.previewEnabled)
			{
				destroyPreview(g);
			}
			//reapply previous changelist
			this.changeList = (ArrayList<Cell>) this.pastChangeList.get(this.pastChangeList.size() - 1).clone();
			
			this.pastChangeList.remove(this.pastChangeList.size() - 1);
			updateImage(g);
			undoCounters();
			if(this.previewEnabled)
			{
				createPreview(g);
			}
			repaint();
		}
	}
	
	
	
	public void createResizeWindow()
	{
		// create an option window to get new dimensions
    	optionWindow reSizer = new optionWindow(this);
    	// display the popup and get the new dimensions
    	reSizer.resize();
	}
	
	public void createRulesWindow()
	{
		// create an option window to get new dimensions
    	optionWindow ruleChanger = new optionWindow(this);
    	// display the popup and get the new dimensions
    	ruleChanger.rules();
	}
	
	public void togglePause()
	{
		if (this.running == true)
		{
			this.futureTask.cancel(true);
			this.running = false;
		}
		else
		{
			changeInterval(PERIOD_INTERVAL);
			this.running = true;
		}
	}
	
	public void toggleXWrap()
	{
		if (this.xWrapMode)
		{
			this.xWrapMode = false;
			resizeWorldPanel(this.cellSize, this.worldWidth, this.worldHeight);
		}
		else
		{
			this.xWrapMode = true;
			resizeWorldPanel(this.cellSize, this.worldWidth, this.worldHeight);
		}
	}
	
	public void toggleYWrap()
	{
		if (this.yWrapMode)
		{
			this.yWrapMode = false;
			resizeWorldPanel(this.cellSize, this.worldWidth, this.worldHeight);
		}
		else
		{
			this.yWrapMode = true;
			resizeWorldPanel(this.cellSize, this.worldWidth, this.worldHeight);
		}
	}
	
	public void changeInterval(long time)	//change timer interval
	{
		if(time > 0)
		{
			if (this.futureTask != null)
			{
				this.futureTask.cancel(true);
			}
			
			this.futureTask = this.timer.scheduleAtFixedRate(this.timerTask, time, time, TimeUnit.MILLISECONDS);
		}
	}
	
	public void setInterval(int time)
	{
		this.PERIOD_INTERVAL = time;
		if(this.running == true)
		{
			changeInterval(PERIOD_INTERVAL);
		}
	}
	
	private class ScheduleTask extends TimerTask
	{
		public void run()
		{
			nextStep();
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(this.worldImage, 0, 0, this);
	}

	private void showCounters()
	{
		this.parent.aliveLabel.setText("   Alive: " + Integer.toString(this.noOfAlive) + "   ");
		this.parent.aliveMaxLabel.setText("   Max Alive: " + Integer.toString(this.noOfAliveMax) + "   ");
		this.parent.aliveMinLabel.setText("   Min Alive: " + Integer.toString(this.noOfAliveMin) + "   ");
		this.parent.deadLabel.setText("   Dead: " + Integer.toString(this.noOfDead) + "   ");
		this.parent.deadMaxLabel.setText("   Max Dead: " + Integer.toString(this.noOfDeadMax) + "   ");
		this.parent.deadMinLabel.setText("   Min Dead: " + Integer.toString(this.noOfDeadMin) + "   ");
		this.parent.generationLabel.setText("   Generation: " + Integer.toString(this.currentGeneration) + "   ");
	}
	
	private void backupCounters()
	{
		int[] temp = new int[4];
		
		temp[0] = this.noOfAliveMax;
		temp[1] = this.noOfAliveMin;
		temp[2] = this.noOfDeadMax;
		temp[3] = this.noOfDeadMin;
		
		this.counterChangeList.add(temp);
		
		if (this.counterChangeList.size() > this.noOfUndos)
		{
			this.counterChangeList.remove(0);
		}
	}
	
	private void undoCounters()
	{
		if (counterChangeList.size() > 0)
		{
			this.noOfAlive = 0;
			for (int x = 0; x < this.worldWidth + (this.currentBufferSizeX * 2); x++)
			{
				for (int y = 0; y < this.worldHeight + (this.currentBufferSizeY * 2); y++)
				{
					if (cell[x][y])
					{
						this.noOfAlive = this.noOfAlive + 1;
					}
				}
			}
			this.noOfDead = this.noOfCells - this.noOfAlive;
					
			this.noOfAliveMax = this.counterChangeList.get(this.counterChangeList.size() - 1)[0];
			this.noOfAliveMin = this.counterChangeList.get(this.counterChangeList.size() - 1)[1];
			this.noOfDeadMax = this.counterChangeList.get(this.counterChangeList.size() - 1)[2];
			this.noOfDeadMin = this.counterChangeList.get(this.counterChangeList.size() - 1)[3];
			
			this.counterChangeList.remove(this.counterChangeList.size() - 1);
			
			this.currentGeneration--;
			showCounters();
		}
	}
	
	private void updateCounters()
	{
		this.noOfAlive = 0;
		for (int x = 0; x < this.worldWidth + (this.currentBufferSizeX * 2); x++)
		{
			for (int y = 0; y < this.worldHeight + (this.currentBufferSizeY * 2); y++)
			{
				if (cell[x][y])
				{
					this.noOfAlive = this.noOfAlive + 1;
				}
			}
		}
		this.noOfDead = this.noOfCells - this.noOfAlive;
		backupCounters();
		if (this.noOfAlive > this.noOfAliveMax)
		{
			
			this.noOfAliveMax = this.noOfAlive;
		}
		if (this.noOfAlive < this.noOfAliveMin)
		{
			this.noOfAliveMin = this.noOfAlive; 
		}
		if (this.noOfDead > this.noOfDeadMax)
		{
			this.noOfDeadMax = this.noOfDead;
		}
		if (this.noOfDead < this.noOfDeadMin)
		{
			this.noOfDeadMin = this.noOfDead;
		}
		this.currentGeneration++;
		showCounters();
	}
	
	private void resetCounters() //calculates the counters based on he worlds setup
	{
		this.noOfCells = (this.worldWidth + (this.currentBufferSizeX * 2)) * (this.worldHeight + (this.currentBufferSizeY * 2));
		
		this.noOfAlive = 0;
		for (int x = 0; x < this.worldWidth + (this.currentBufferSizeX * 2); x++)
		{
			for (int y = 0; y < this.worldHeight + (this.currentBufferSizeY * 2); y++)
			{
				if (cell[x][y])
				{
					this.noOfAlive = this.noOfAlive + 1;
				}
			}
		}
		this.noOfDead = this.noOfCells - this.noOfAlive;
		if (this.resetFlag)
		{
			this.noOfAliveMax = this.noOfAlive;
			this.noOfAliveMin = this.noOfAlive;
			this.noOfDeadMax = this.noOfDead;
			this.noOfDeadMin = this.noOfDead;
			this.currentGeneration = 0;
			this.resetFlag = false;
		}
		this.resetFlag = true;
		showCounters();
	}
	
	public void stampModeOff()
	{
		this.stampModeFlag = false;
	}
	
	public void setRules(boolean[] survive, boolean[] born)
	{
		destroyPreview(g);
		this.surviveRules = survive;
		this.bornRules = born;
		System.out.print("B");
		if (this.bornRules[0])
		{
			System.out.print(0);
		}
		if (this.bornRules[1])
		{
			System.out.print(1);
		}
		if (this.bornRules[2])
		{
			System.out.print(2);
		}
		if (this.bornRules[3])
		{
			System.out.print(3);
		}
		if (this.bornRules[4])
		{
			System.out.print(4);
		}
		if (this.bornRules[5])
		{
			System.out.print(5);
		}
		if (this.bornRules[6])
		{
			System.out.print(6);
		}
		if (this.bornRules[7])
		{
			System.out.print(7);
		}
		if (this.bornRules[8])
		{
			System.out.print(8);
		}
		System.out.print("/S");
		if (this.surviveRules[0])
		{
			System.out.print(0);
		}
		if (this.surviveRules[1])
		{
			System.out.print(1);
		}
		if (this.surviveRules[2])
		{
			System.out.print(2);
		}
		if (this.surviveRules[3])
		{
			System.out.print(3);
		}
		if (this.surviveRules[4])
		{
			System.out.print(4);
		}
		if (this.surviveRules[5])
		{
			System.out.print(5);
		}
		if (this.surviveRules[6])
		{
			System.out.print(6);
		}
		if (this.surviveRules[7])
		{
			System.out.print(7);
		}
		if (this.surviveRules[8])
		{
			System.out.print(8);
		}
		System.out.println("");
		if (this.previewEnabled)
		{
			createPreview(g);
		}
	}
	
	public boolean[] getBornRules()
	{
		return this.bornRules;
	}
	
	public boolean[] getSurviveRules()
	{
		return this.surviveRules;
	}
	
	public int getLeftGap()
	{
		int leftGap = -1;
		boolean searchFlag = true;
		int x = this.currentBufferSizeX;
		while (searchFlag)
		{
			for (int y = this.currentBufferSizeY; y < this.currentBufferSizeY + this.worldHeight; y++)
			{
				if(cell[x][y])
				{
					searchFlag = false;
				}
			}
			if (x >= this.currentBufferSizeX + this.worldWidth)
			{
				searchFlag = false;
			}
			leftGap++;
			x++; 
		}
		return leftGap;
	}
	
	public int getRightGap()
	{
		int rightGap = -1;
		boolean searchFlag = true;
		int x = this.currentBufferSizeX + this.worldWidth - 1;
		while (searchFlag)
		{
			for (int y = this.currentBufferSizeY; y < this.currentBufferSizeY + this.worldHeight; y++)
			{
				if(cell[x][y])
				{
					searchFlag = false;
				}
			}
			if (x <= this.currentBufferSizeX)
			{
				searchFlag = false;
			}
			rightGap++;
			x--; 
		}
		return rightGap;
	}
	
	public int getTopGap()
	{
		int topGap = -1;
		boolean searchFlag = true;
		int y = this.currentBufferSizeY;
		while (searchFlag)
		{
			for (int x = this.currentBufferSizeX; x < this.currentBufferSizeX + this.worldWidth; x++)
			{
				if(cell[x][y])
				{
					searchFlag = false;
				}
			}
			if (y >= this.currentBufferSizeY + this.worldHeight)
			{
				searchFlag = false;
			}
			topGap++;
			y++; 
		}
		return topGap;
	}
	
	public int getBottomGap()
	{
		int bottomGap = -1;
		boolean searchFlag = true;
		int y = this.currentBufferSizeY + this.worldHeight - 1;
		while (searchFlag)
		{
			for (int x = this.currentBufferSizeX; x < this.currentBufferSizeX + this.worldWidth; x++)
			{
				if(cell[x][y])
				{
					searchFlag = false;
				}
			}
			if (y <= this.currentBufferSizeY)
			{
				searchFlag = false;
			}
			bottomGap++;
			y--; 
		}
		return bottomGap;
	}
	
	public boolean getRunning()
	{
		return this.running;
	}
	
	public int getCellSize()
	{
		return this.cellSize;
	}
	
	public int getWorldWidth()
	{
		return this.worldWidth;
	}
	
	public int getWorldHeight()
	{
		return this.worldHeight;
	}
	
	public int getMaxCellSize()
	{
		return this.maxCellSize;
	}
	
	public int getMaxWidth()
	{
		return this.maxWidth;
	}
	
	public int getMaxHeight()
	{
		return this.maxHeight;
	}
	
	public boolean getPreviewMode()
	{
		return this.previewEnabled;
	}
}