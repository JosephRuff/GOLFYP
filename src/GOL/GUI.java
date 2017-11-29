package GOL;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GUI extends JFrame		//implements GUI
{
	private static final long serialVersionUID = 1L;  		// default option to clear serialisation warning

	JLabel aliveLabel = new JLabel("");
	JLabel aliveMaxLabel = new JLabel("");
	JLabel aliveMinLabel = new JLabel("");
	JLabel deadLabel = new JLabel("");
	JLabel deadMaxLabel = new JLabel("");
	JLabel deadMinLabel = new JLabel("");
	JLabel generationLabel = new JLabel("");
	
	JCheckBoxMenuItem xWrapMenuItem = new JCheckBoxMenuItem("Horizontal Wrap");
	JCheckBoxMenuItem yWrapMenuItem = new JCheckBoxMenuItem("Vertical Wrap");
	
	public GUI()			//class constructor
	{
		initUI(); 			//call the class initialiser
	}
	
	private void initUI() 	//the class initialiser
	{
		//create and add a worldPanel
		worldPanel gameWorld = new worldPanel(this, 10, 80, 80);
		getContentPane().add(gameWorld, BorderLayout.CENTER);
		
		//create and set a menuBar
		JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        //create and fileMenu and attach to menuBar
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        //create a save option and add to fileMenu
        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);
        
        //create a save packed option and add to fileMenu
        JMenuItem savePackedMenuItem = new JMenuItem("Save as Stamp");
        fileMenu.add(savePackedMenuItem);
        
        //create a load option and add to fileMenu
        JMenuItem loadMenuItem = new JMenuItem("Load");
        fileMenu.add(loadMenuItem);
        
        //create a load centered option and add to fileMenu
        JMenuItem loadCenterMenuItem = new JMenuItem("Load Centered");
        fileMenu.add(loadCenterMenuItem);
        
        
        //create a load as stamp option and add to fileMenu
        JMenuItem loadStampMenuItem = new JMenuItem("Load as Stamp");
        fileMenu.add(loadStampMenuItem);
        
        //create a cancel stamp option and add to fileMenu
        JMenuItem cancelStampMenuItem = new JMenuItem("Stamp mode off");
        fileMenu.add(cancelStampMenuItem);
        cancelStampMenuItem.setVisible(false);
        
        //add action listeners to the file menu items
        saveMenuItem.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.saveWorld();
        	}
        });
        
        //add action listeners to the file menu items
        savePackedMenuItem.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.saveWorldPacked();
        	}
        });
        
        loadMenuItem.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.loadWorld();
        	}
        });
        
        loadCenterMenuItem.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.loadWorldCentered();
        	}
        });
        
        loadStampMenuItem.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.loadWorldStamp();
        		loadStampMenuItem.setVisible(false);
        		cancelStampMenuItem.setVisible(true);
        	}
        });
        
        cancelStampMenuItem.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.stampModeOff();
        		loadStampMenuItem.setVisible(true);
        		cancelStampMenuItem.setVisible(false);
        	}
        });
        
        
        //create and option menu and attach to menuBar
        JMenu optionsMenu = new JMenu("Options");
        menuBar.add(optionsMenu);
        
        //create a resizeMenu and add to optionsMenu
        JMenu resizeMenu = new JMenu("Resize");
        optionsMenu.add(resizeMenu);
        
        //create a resizeMenu and add to optionsMenu
        JMenu boardSizeMenu = new JMenu("Board Size");
        resizeMenu.add(boardSizeMenu);
        
        //create several size menuItem and add to resizeMenu
        JRadioButtonMenuItem boardSizeMenuItem01 = new JRadioButtonMenuItem("25x25");
        boardSizeMenu.add(boardSizeMenuItem01);
        JRadioButtonMenuItem boardSizeMenuItem02 = new JRadioButtonMenuItem("40x40");
        boardSizeMenu.add(boardSizeMenuItem02);
        JRadioButtonMenuItem boardSizeMenuItem03 = new JRadioButtonMenuItem("60x40");
        boardSizeMenu.add(boardSizeMenuItem03);
        JRadioButtonMenuItem boardSizeMenuItem04 = new JRadioButtonMenuItem("75x40");
        boardSizeMenu.add(boardSizeMenuItem04);
        boardSizeMenu.addSeparator();
        JRadioButtonMenuItem boardSizeMenuItem05 = new JRadioButtonMenuItem("50x50");
        boardSizeMenu.add(boardSizeMenuItem05);
        JRadioButtonMenuItem boardSizeMenuItem06 = new JRadioButtonMenuItem("80x80");
        boardSizeMenu.add(boardSizeMenuItem06);
        boardSizeMenuItem06.setSelected(true);
        JRadioButtonMenuItem boardSizeMenuItem07 = new JRadioButtonMenuItem("120x80");
        boardSizeMenu.add(boardSizeMenuItem07);
        JRadioButtonMenuItem boardSizeMenuItem08 = new JRadioButtonMenuItem("150x80");
        boardSizeMenu.add(boardSizeMenuItem08);
        boardSizeMenu.addSeparator();
        JRadioButtonMenuItem boardSizeMenuItem09 = new JRadioButtonMenuItem("100x100");
        boardSizeMenu.add(boardSizeMenuItem09);
        JRadioButtonMenuItem boardSizeMenuItem10 = new JRadioButtonMenuItem("160x160");
        boardSizeMenu.add(boardSizeMenuItem10);
        JRadioButtonMenuItem boardSizeMenuItem11 = new JRadioButtonMenuItem("240x160");
        boardSizeMenu.add(boardSizeMenuItem11);
        JRadioButtonMenuItem boardSizeMenuItem12 = new JRadioButtonMenuItem("300x160");
        boardSizeMenu.add(boardSizeMenuItem12);
        boardSizeMenu.addSeparator();
        JRadioButtonMenuItem boardSizeMenuItemCustom = new JRadioButtonMenuItem("Custom");
        boardSizeMenu.add(boardSizeMenuItemCustom);

        
        //groups the boardSizeMenuItems 
        ButtonGroup boardSizeButtonGroup = new ButtonGroup();
        boardSizeButtonGroup.add(boardSizeMenuItem01);
        boardSizeButtonGroup.add(boardSizeMenuItem02);
        boardSizeButtonGroup.add(boardSizeMenuItem03);
        boardSizeButtonGroup.add(boardSizeMenuItem04);
        boardSizeButtonGroup.add(boardSizeMenuItem05);
        boardSizeButtonGroup.add(boardSizeMenuItem06);
        boardSizeButtonGroup.add(boardSizeMenuItem07);
        boardSizeButtonGroup.add(boardSizeMenuItem08);
        boardSizeButtonGroup.add(boardSizeMenuItem09);
        boardSizeButtonGroup.add(boardSizeMenuItem10);
        boardSizeButtonGroup.add(boardSizeMenuItem11);
        boardSizeButtonGroup.add(boardSizeMenuItem12);
        boardSizeButtonGroup.add(boardSizeMenuItemCustom);
        
        
        //add action listeners to the resize menu items
        boardSizeMenuItem01.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 25, 25);
        	}
        });
        
        boardSizeMenuItem02.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 40, 40);
        	}
        });
        
        boardSizeMenuItem03.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 60, 40);
        	}
        });
        
        boardSizeMenuItem04.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 75, 40);
        	}
        });
        boardSizeMenuItem05.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 50, 50);
        	}
        });
        
        boardSizeMenuItem06.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 80, 80);
        	}
        });
        
        boardSizeMenuItem07.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 120, 80);
        	}
        });
        
        boardSizeMenuItem08.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 150, 80);
        	}
        });
        boardSizeMenuItem09.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 100, 100);
        	}
        });
        
        boardSizeMenuItem10.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 160, 160);
        	}
        });
        
        boardSizeMenuItem11.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 240, 160);
        	}
        });
        
        boardSizeMenuItem12.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.resizeWorldPanel(gameWorld.getCellSize(), 300, 160);
        	}
        });
        
        boardSizeMenuItemCustom.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.createResizeWindow();
        	}
        });
        
        
        
        //create a cellSizeMenu and add to resizeMenu
        JMenuItem cellSizeMenu = new JMenu("Cell Size");
        cellSizeMenu.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        	}
        });
        resizeMenu.add(cellSizeMenu);
        
        
        //create several size cellSizeMenuItems and add to cellSizeMenu
        JRadioButtonMenuItem cellSizeMenuItem01 = new JRadioButtonMenuItem("5px");
        cellSizeMenu.add(cellSizeMenuItem01);
        JRadioButtonMenuItem cellSizeMenuItem02 = new JRadioButtonMenuItem("10px");
        cellSizeMenu.add(cellSizeMenuItem02);
        cellSizeMenuItem02.setSelected(true);
        JRadioButtonMenuItem cellSizeMenuItem03 = new JRadioButtonMenuItem("20px");
        cellSizeMenu.add(cellSizeMenuItem03);
        JRadioButtonMenuItem cellSizeMenuItemCustom = new JRadioButtonMenuItem("Custom");
        cellSizeMenu.add(cellSizeMenuItemCustom);
        
        
        //groups cellSizeMenuItems
        ButtonGroup cellSizeButtonGroup = new ButtonGroup();
        cellSizeButtonGroup.add(cellSizeMenuItem01);
        cellSizeButtonGroup.add(cellSizeMenuItem02);
        cellSizeButtonGroup.add(cellSizeMenuItem03);
        cellSizeButtonGroup.add(cellSizeMenuItemCustom);
        
        //add action listeners to the cellSizeMenu items
        cellSizeMenuItem01.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if(gameWorld.getCellSize() != 5)
        		{
            		gameWorld.resizeWorldPanel(5, gameWorld.getWorldWidth(), gameWorld.getWorldHeight());
        		}
        		
        	}
        });
        cellSizeMenuItem02.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if(gameWorld.getCellSize() != 10)
        		{
	        		gameWorld.resizeWorldPanel(10, gameWorld.getWorldWidth(), gameWorld.getWorldHeight());
        		}
        	}
        });
        
        cellSizeMenuItem03.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if(gameWorld.getCellSize() != 20)
        		{
	        		gameWorld.resizeWorldPanel(20, gameWorld.getWorldWidth(), gameWorld.getWorldHeight());
        		}
        	}
        });

        cellSizeMenuItemCustom.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.createResizeWindow();
        	}
        });
		
        //create a speedMenu and add to optionsMenu
        JMenu speedMenu = new JMenu("Speed");
        optionsMenu.add(speedMenu);
		
        //create several speed menuItem and add to speedMenu
        JRadioButtonMenuItem speedMenuItem1 = new JRadioButtonMenuItem("Extra Slow");
        speedMenu.add(speedMenuItem1);
        JRadioButtonMenuItem speedMenuItem2 = new JRadioButtonMenuItem("Slow");
        speedMenu.add(speedMenuItem2);
        JRadioButtonMenuItem speedMenuItem3 = new JRadioButtonMenuItem("Standard");
        speedMenu.add(speedMenuItem3);
        speedMenuItem3.setSelected(true);
        JRadioButtonMenuItem speedMenuItem4 = new JRadioButtonMenuItem("Quick");
        speedMenu.add(speedMenuItem4);
        JRadioButtonMenuItem speedMenuItem5 = new JRadioButtonMenuItem("Extra Quick");
        speedMenu.add(speedMenuItem5);
        
        
        //group speedMenuItems
        ButtonGroup speedSettingsButtonGroup = new ButtonGroup();
        speedSettingsButtonGroup.add(speedMenuItem1);
        speedSettingsButtonGroup.add(speedMenuItem2);
        speedSettingsButtonGroup.add(speedMenuItem3);
        speedSettingsButtonGroup.add(speedMenuItem4);
        speedSettingsButtonGroup.add(speedMenuItem5);
        
        //add action listeners to the speed menu items
        speedMenuItem1.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.setInterval(1000);
        	}
        });
        speedMenuItem2.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.setInterval(500);
        	}
        });
        
        speedMenuItem3.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.setInterval(200);
        	}
        });
        
        speedMenuItem4.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.setInterval(100);
        	}
        });
        
        speedMenuItem5.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.setInterval(50);
        	}
        });
        
        
        
        //create a preview mode check box and add it to the menubar
        JCheckBoxMenuItem previewMenuItem = new JCheckBoxMenuItem("Preview Mode");
        //optionsMenu.add(previewMenuItem);
        menuBar.add(previewMenuItem);
        
        //add action listeners to the preview mode check box
        previewMenuItem.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if(gameWorld.getPreviewMode())
        		{
        			gameWorld.previewOff();
        		}
        		else
        		{
        			gameWorld.previewOn();
        		}
        	}
        });
        
        
        //create a wrapMenu and add to optionsMenu
        JMenu wrapMenu = new JMenu("Wrapping");
        optionsMenu.add(wrapMenu);
        
        //add the x axis wrap mode check box to the wrapMenu
        wrapMenu.add(this.xWrapMenuItem);
        
        //add action listeners to the preview mode check box
        xWrapMenuItem.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.toggleXWrap();
        	}
        });
        
        //add the y axis wrap mode check box to the wrapMenu
        wrapMenu.add(this.yWrapMenuItem);
        
        //add action listeners to the preview mode check box
        yWrapMenuItem.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.toggleYWrap();
        	}
        });
        
        //create a counterMenu and add to optionsMenu
        JMenu counterMenu = new JMenu("Counters");
        optionsMenu.add(counterMenu);
        
        //create counterOption checkboxs and add them to the wrapMenu
        JCheckBoxMenuItem counterOption1 = new JCheckBoxMenuItem("Alive");
        counterMenu.add(counterOption1);
        counterOption1.setSelected(true);
        JCheckBoxMenuItem counterOption2 = new JCheckBoxMenuItem("Max Alive");
        counterMenu.add(counterOption2);
        JCheckBoxMenuItem counterOption3 = new JCheckBoxMenuItem("Min Alive");
        counterMenu.add(counterOption3);
        JCheckBoxMenuItem counterOption4 = new JCheckBoxMenuItem("Dead");
        counterMenu.add(counterOption4);
        JCheckBoxMenuItem counterOption5 = new JCheckBoxMenuItem("Max Dead");
        counterMenu.add(counterOption5);
        JCheckBoxMenuItem counterOption6 = new JCheckBoxMenuItem("Min Dead");
        counterMenu.add(counterOption6);
        JCheckBoxMenuItem counterOption7 = new JCheckBoxMenuItem("Generation");
        counterMenu.add(counterOption7);
        counterOption7.setSelected(true);
        
        //create action listeners for counterOption checkboxs
        counterOption1.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if (counterOption1.isSelected())
        		{
        			aliveLabel.setVisible(true);
        			pack();
        		}
        		else
        		{
        			aliveLabel.setVisible(false);
        			pack();
        		}
        	}
        });
        counterOption2.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if (counterOption2.isSelected())
        		{
        			aliveMaxLabel.setVisible(true);
        			pack();
        		}
        		else
        		{
        			aliveMaxLabel.setVisible(false);
        			pack();
        		}
        	}
        });
        counterOption3.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if (counterOption3.isSelected())
        		{
        			aliveMinLabel.setVisible(true);
        			pack();
        		}
        		else
        		{
        			aliveMinLabel.setVisible(false);
        			pack();
        		}
        	}
        });
        counterOption4.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if (counterOption4.isSelected())
        		{
        			deadLabel.setVisible(true);
        			pack();
        		}
        		else
        		{
        			deadLabel.setVisible(false);
        			pack();
        		}
        	}
        });
        counterOption5.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if (counterOption5.isSelected())
        		{
        			deadMaxLabel.setVisible(true);
        			pack();
        		}
        		else
        		{
        			deadMaxLabel.setVisible(false);
        			pack();
        		}
        	}
        });
        counterOption6.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if (counterOption6.isSelected())
        		{
        			deadMinLabel.setVisible(true);
        			pack();
        		}
        		else
        		{
        			deadMinLabel.setVisible(false);
        			pack();
        		}
        	}
        });
        counterOption7.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		if (counterOption7.isSelected())
        		{
        			generationLabel.setVisible(true);
        			pack();
        		}
        		else
        		{
        			generationLabel.setVisible(false);
        			pack();
        		}
        	}
        });
        
        
        //create a rulesMenu and add to optionsMenu
        JMenu rulesMenu = new JMenu("Rules");
        optionsMenu.add(rulesMenu);
        
        //create several size menuItem and add to resizeMenu
        JRadioButtonMenuItem rulesMenuItem01 = new JRadioButtonMenuItem("Conway's Life: B3/S23");
        rulesMenu.add(rulesMenuItem01);
        rulesMenuItem01.setSelected(true);
        JRadioButtonMenuItem rulesMenuItem02 = new JRadioButtonMenuItem("Fredkin: B1357/S02468");
        rulesMenu.add(rulesMenuItem02);
        JRadioButtonMenuItem rulesMenuItem03 = new JRadioButtonMenuItem("Replicator: B1357/S1357");
        rulesMenu.add(rulesMenuItem03);
        JRadioButtonMenuItem rulesMenuItem04 = new JRadioButtonMenuItem("Seeds: B2/S");
        rulesMenu.add(rulesMenuItem04);
        JRadioButtonMenuItem rulesMenuItem05 = new JRadioButtonMenuItem("Live Free or Die: B2/S0");
        rulesMenu.add(rulesMenuItem05);
        JRadioButtonMenuItem rulesMenuItem06 = new JRadioButtonMenuItem("Life without death: B3/S012345678");
        rulesMenu.add(rulesMenuItem06);
        JRadioButtonMenuItem rulesMenuItem07 = new JRadioButtonMenuItem("DotLife: B3/S023");
        rulesMenu.add(rulesMenuItem07);
        JRadioButtonMenuItem rulesMenuItem08 = new JRadioButtonMenuItem("Flock: B3/S12");
        rulesMenu.add(rulesMenuItem08);
        JRadioButtonMenuItem rulesMenuItem09 = new JRadioButtonMenuItem("Mazectric: B3/S1234");
        rulesMenu.add(rulesMenuItem09);
        JRadioButtonMenuItem rulesMenuItem10 = new JRadioButtonMenuItem("Maze: B3/S12345");
        rulesMenu.add(rulesMenuItem10);
        JRadioButtonMenuItem rulesMenuItem11 = new JRadioButtonMenuItem("EightLife: B3/S238");
        rulesMenu.add(rulesMenuItem11);
        JRadioButtonMenuItem rulesMenuItem12 = new JRadioButtonMenuItem("Long Life: B345/S5");
        rulesMenu.add(rulesMenuItem12);
        JRadioButtonMenuItem rulesMenuItem13 = new JRadioButtonMenuItem("Gems: B3457/S4568");
        rulesMenu.add(rulesMenuItem13);
        JRadioButtonMenuItem rulesMenuItem14 = new JRadioButtonMenuItem("2x2: B36/S125");
        rulesMenu.add(rulesMenuItem14);
        JRadioButtonMenuItem rulesMenuItem15 = new JRadioButtonMenuItem("HighLife: B36/S23");
        rulesMenu.add(rulesMenuItem15);
        JRadioButtonMenuItem rulesMenuItem16 = new JRadioButtonMenuItem("Stains: B3678/S235678");
        rulesMenu.add(rulesMenuItem16);
        JRadioButtonMenuItem rulesMenuItem17 = new JRadioButtonMenuItem("Day & Night: B3678/S34678");
        rulesMenu.add(rulesMenuItem17);
        JRadioButtonMenuItem rulesMenuItem18 = new JRadioButtonMenuItem("LowDeath: B368/S238");
        rulesMenu.add(rulesMenuItem18);
        JRadioButtonMenuItem rulesMenuItem19 = new JRadioButtonMenuItem("Move: B368/S245");
        rulesMenu.add(rulesMenuItem19);
        JRadioButtonMenuItem rulesMenuItem20 = new JRadioButtonMenuItem("DryLife: B37/S23");
        rulesMenu.add(rulesMenuItem20);
        JRadioButtonMenuItem rulesMenuItem21 = new JRadioButtonMenuItem("Pedestrian Life: B38/S23");
        rulesMenu.add(rulesMenuItem21);
        JRadioButtonMenuItem rulesMenuItem22 = new JRadioButtonMenuItem("HoneyLife: B38/S238");
        rulesMenu.add(rulesMenuItem22);
        JRadioButtonMenuItem rulesMenuItemCustom = new JRadioButtonMenuItem("Custom");
        rulesMenu.add(rulesMenuItemCustom);

        
        //groups the rulesMenuItems 
        ButtonGroup rulesButtonGroup = new ButtonGroup();
        rulesButtonGroup.add(rulesMenuItem01);
        rulesButtonGroup.add(rulesMenuItem02);
        rulesButtonGroup.add(rulesMenuItem03);
        rulesButtonGroup.add(rulesMenuItem04);
        rulesButtonGroup.add(rulesMenuItem05);
        rulesButtonGroup.add(rulesMenuItem06);
        rulesButtonGroup.add(rulesMenuItem07);
        rulesButtonGroup.add(rulesMenuItem08);
        rulesButtonGroup.add(rulesMenuItem09);
        rulesButtonGroup.add(rulesMenuItem10);
        rulesButtonGroup.add(rulesMenuItem11);
        rulesButtonGroup.add(rulesMenuItem12);
        rulesButtonGroup.add(rulesMenuItem13);
        rulesButtonGroup.add(rulesMenuItem14);
        rulesButtonGroup.add(rulesMenuItem15);
        rulesButtonGroup.add(rulesMenuItem16);
        rulesButtonGroup.add(rulesMenuItem17);
        rulesButtonGroup.add(rulesMenuItem18);
        rulesButtonGroup.add(rulesMenuItem19);
        rulesButtonGroup.add(rulesMenuItem20);
        rulesButtonGroup.add(rulesMenuItem21);
        rulesButtonGroup.add(rulesMenuItem22);
        rulesButtonGroup.add(rulesMenuItemCustom);
        
        rulesMenuItem01.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem02.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = true; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = true; 
        		born[6] = false; 
        		born[7] = true; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = true; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = false; 
        		survive[4] = true; 
        		survive[5] = false; 
        		survive[6] = true; 
        		survive[7] = false; 
        		survive[8] = true; 
        		gameWorld.setRules(survive, born);
        	}
        });

        rulesMenuItem03.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = true; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = true; 
        		born[6] = false; 
        		born[7] = true; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = true; 
        		survive[2] = false; 
        		survive[3] = true; 
        		survive[4] = false; 
        		survive[5] = true; 
        		survive[6] = false; 
        		survive[7] = true; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem04.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = true; 
        		born[3] = false; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = false; 
        		survive[3] = false; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem05.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = true; 
        		born[3] = false; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = true; 
        		survive[1] = false; 
        		survive[2] = false; 
        		survive[3] = false; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem06.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = true; 
        		survive[1] = true; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = true; 
        		survive[5] = true; 
        		survive[6] = true; 
        		survive[7] = true; 
        		survive[8] = true; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem07.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = true; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem08.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = true; 
        		survive[2] = true; 
        		survive[3] = false; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem09.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = true; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = true; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem10.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = true; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = true; 
        		survive[5] = true; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem11.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = true; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem12.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = true; 
        		born[5] = true; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = false; 
        		survive[3] = false; 
        		survive[4] = false; 
        		survive[5] = true; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem13.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = true; 
        		born[5] = true; 
        		born[6] = false; 
        		born[7] = true; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = false; 
        		survive[3] = false; 
        		survive[4] = true; 
        		survive[5] = true; 
        		survive[6] = true; 
        		survive[7] = false; 
        		survive[8] = true; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem14.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = true; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = true; 
        		survive[2] = true; 
        		survive[3] = false; 
        		survive[4] = false; 
        		survive[5] = true; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem15.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = true; 
        		born[7] = false; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem16.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = true; 
        		born[7] = true; 
        		born[8] = true; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = false; 
        		survive[5] = true; 
        		survive[6] = true; 
        		survive[7] = true; 
        		survive[8] = true; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem17.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = true; 
        		born[7] = true; 
        		born[8] = true; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = false; 
        		survive[3] = true; 
        		survive[4] = true; 
        		survive[5] = false; 
        		survive[6] = true; 
        		survive[7] = true; 
        		survive[8] = true; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem18.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = true; 
        		born[7] = false; 
        		born[8] = true; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = true; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem19.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = true; 
        		born[7] = false; 
        		born[8] = true; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = false; 
        		survive[4] = true; 
        		survive[5] = true; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem20.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = true; 
        		born[8] = false; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem21.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = true; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = false; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItem22.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		boolean[] born = new boolean[9]; 
        		born[0] = false; 
        		born[1] = false; 
        		born[2] = false; 
        		born[3] = true; 
        		born[4] = false; 
        		born[5] = false; 
        		born[6] = false; 
        		born[7] = false; 
        		born[8] = true; 
        		boolean[] survive = new boolean[9]; 
        		survive[0] = false; 
        		survive[1] = false; 
        		survive[2] = true; 
        		survive[3] = true; 
        		survive[4] = false; 
        		survive[5] = false; 
        		survive[6] = false; 
        		survive[7] = false; 
        		survive[8] = true; 
        		gameWorld.setRules(survive, born);
        	}
        });
        
        rulesMenuItemCustom.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.createRulesWindow();
        	}
        });
        
        
        //create a clear board option and add to optionsMenu
        JMenuItem clearBoardOption = new JMenuItem("Clear Board");
        optionsMenu.add(clearBoardOption);
        
        //add action listeners to the resize menu items
        clearBoardOption.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		gameWorld.clearBoard();
        		gameWorld.repaint();
        	}
        });

        //add the counter labels to the menuBar
        menuBar.add(this.aliveLabel);
        menuBar.add(this.aliveMaxLabel);
        this.aliveMaxLabel.setVisible(false);
        menuBar.add(this.aliveMinLabel);
        this.aliveMinLabel.setVisible(false);
        menuBar.add(this.deadLabel);
        this.deadLabel.setVisible(false);
        menuBar.add(this.deadMaxLabel);
        this.deadMaxLabel.setVisible(false);
        menuBar.add(this.deadMinLabel);
        this.deadMinLabel.setVisible(false);
        menuBar.add(this.generationLabel);
        
        
        
        
      
        //create and add a controls panel
        JPanel controlPanel = new JPanel();
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        
        //create and add a previous button
        Button prevButton = new Button("Prev");
        controlPanel.add(prevButton);
        prevButton.setPreferredSize(new Dimension(50, 25));
        
        //create and add a pause button
        Button pauseButton = new Button("Play");
        controlPanel.add(pauseButton);
        pauseButton.setPreferredSize(new Dimension(70, 25));
        
        //create and add a next button
        Button nextButton = new Button("Next");
        controlPanel.add(nextButton);
        nextButton.setPreferredSize(new Dimension(50, 25));

        //add listener for pauseButton event
        pauseButton.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		gameWorld.togglePause();
        		if (gameWorld.getRunning() == false)
        		{
        			resizeMenu.setEnabled(true);
        			wrapMenu.setEnabled(true);
        			rulesMenu.setEnabled(true);
        			clearBoardOption.setEnabled(true);
        			fileMenu.setEnabled(true);
        			prevButton.setVisible(true);
        			nextButton.setVisible(true);
        			pauseButton.setLabel("Play");
        		}
        		else
        		{
        			resizeMenu.setEnabled(false);
        			wrapMenu.setEnabled(false);
        			rulesMenu.setEnabled(false);
        			clearBoardOption.setEnabled(false);
        			fileMenu.setEnabled(false);
        			prevButton.setVisible(false);
        			nextButton.setVisible(false);
            		pauseButton.setLabel("Pause");
        		}
        		//System.out.println(gameWorld.getRunning());
        	}
        });
        
        //add listener for prevButton event
        prevButton.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		gameWorld.prevStep();
        	}
        });
        
        //add listener for nextButton event
        nextButton.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		gameWorld.nextStep();
        	}
        });
        
        
        
        setResizable(false);	        //lock window size
        pack();							//set frame size to match the components
        setTitle("Conway");				//set title to "Conway"
        setLocationRelativeTo(null);	//set window to middle of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) 		//program start execution point
	{
		EventQueue.invokeLater(new Runnable() {
			//@Override
			public void run()
			{
				JFrame game = new GUI();		//create the user interface
				game.setVisible(true);			//display it
			}
		});
	}
}