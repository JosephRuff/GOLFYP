package GOL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class optionWindow extends JFrame		// class to implement a popup window to change screen sizes
{
	private static final long serialVersionUID = 1L;		// default option to clear serialisation warning
	private worldPanel parent;								// the panel to which I am attached
	private JTextField widthField, heightField, cellSizeField;	// text fields for data entry

	optionWindow(worldPanel parent)		// constructor taking a parent parameter
	{
		this.parent = parent;
	}
	
	protected JOptionPane getOptionPane(JComponent parent) {
        JOptionPane pane = null;
        if (!(parent instanceof JOptionPane)) {
            pane = getOptionPane((JComponent)parent.getParent());
        } else {
            pane = (JOptionPane) parent;
        }
        return pane;
    }
	
	
	public void resize()	//initializes popup for a resize window and displays it, and applies results
	{
		final JButton okay = new JButton("Ok");
        okay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane pane = getOptionPane((JComponent)e.getSource());
                pane.setValue(okay);
            }
        });
        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane pane = getOptionPane((JComponent)e.getSource());
                pane.setValue(cancel);
            }
        });

		JPanel resizePanel = new JPanel();
		resizePanel.setLayout(new BoxLayout(resizePanel, BoxLayout.Y_AXIS));
		
		JPanel widthPanel = new JPanel();
		widthPanel.setLayout(new BoxLayout(widthPanel, BoxLayout.X_AXIS));
		JLabel xLabel = new JLabel("New Width: ");
		this.widthField = new JTextField(5);
		widthField.setText(Integer.toString(parent.getWorldWidth()));
		widthPanel.add(xLabel);
		widthPanel.add(widthField); 
		resizePanel.add(widthPanel);
		
		JPanel heightPanel = new JPanel();
		heightPanel.setLayout(new BoxLayout(heightPanel, BoxLayout.X_AXIS));
		JLabel yLabel = new JLabel("New Height: ");
		this.heightField = new JTextField(5);
		heightField.setText(Integer.toString(parent.getWorldHeight()));
		heightPanel.add(yLabel);
		heightPanel.add(heightField);
		resizePanel.add(heightPanel);
		
		JPanel cellSizePanel = new JPanel();
		cellSizePanel.setLayout(new BoxLayout(cellSizePanel, BoxLayout.X_AXIS));
		JLabel cellSizeLabel = new JLabel("New Size: ");
		this.cellSizeField = new JTextField(5);
		cellSizeField.setText(Integer.toString(parent.getCellSize()));
		cellSizePanel.add(cellSizeLabel);
		cellSizePanel.add(cellSizeField);
		resizePanel.add(cellSizePanel);
		
		//disable on invalid width inputs
		widthField.getDocument().addDocumentListener(new DocumentListener() 
		{
			public void changedUpdate(DocumentEvent e) 
			{
				disable();
			}
			public void removeUpdate(DocumentEvent e) 
			{
				disable();
			}
			public void insertUpdate(DocumentEvent e) 
			{
				disable();
			}
	
			public void disable() {
				if (isNumeric(widthField.getText()))
				{
					if (Integer.parseInt(widthField.getText()) <= 0)
					{
						okay.setEnabled(false);
					}
					else if (Integer.parseInt(widthField.getText()) > parent.getMaxWidth())
					{
						okay.setEnabled(false);
					}
					else
					{
						okay.setEnabled(true);
					}
				}
				else
				{
					okay.setEnabled(false);
				}
			}
		});
		
		//disable on invalid height inputs
		heightField.getDocument().addDocumentListener(new DocumentListener() 
		{
			public void changedUpdate(DocumentEvent e) 
			{
				disable();
			}
			public void removeUpdate(DocumentEvent e) 
			{
				disable();
			}
			public void insertUpdate(DocumentEvent e) 
			{
				disable();
			}
	
			public void disable() {
				if (isNumeric(heightField.getText()))
				{
					if (Integer.parseInt(heightField.getText()) <= 0)
					{
						okay.setEnabled(false);
					}
					else if (Integer.parseInt(heightField.getText()) > parent.getMaxHeight())
					{
						okay.setEnabled(false);
					}
					else
					{
						okay.setEnabled(true);
					}
				}
				else
				{
					okay.setEnabled(false);
				}
			}
		});
		
		//disable on invalid size inputs
		cellSizeField.getDocument().addDocumentListener(new DocumentListener() 
		{
			public void changedUpdate(DocumentEvent e) 
			{
				disable();
			}
			public void removeUpdate(DocumentEvent e) 
			{
				disable();
			}
			public void insertUpdate(DocumentEvent e) 
			{
				disable();
			}
	
			public void disable() {
				if (isNumeric(cellSizeField.getText()))
				{
					if (Integer.parseInt(cellSizeField.getText()) <= 0)
					{
						okay.setEnabled(false);
					}
					else if (Integer.parseInt(cellSizeField.getText()) > parent.getMaxCellSize())
					{
						okay.setEnabled(false);
					}
					else
					{
						okay.setEnabled(true);
					}
				}
				else
				{
					okay.setEnabled(false);
				}
			}
		});
		
		//show dialog
		int returnValue = JOptionPane.showOptionDialog(this, resizePanel, "Get", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{okay, cancel}, okay);
		if(returnValue == JOptionPane.OK_OPTION) // if options have been updated
		{
			this.parent.resizeWorldPanel(Integer.parseInt(cellSizeField.getText()), Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
		}
	}
	
	public void rules()	//initializes popup for a resize window and displays it, and applies results
	{
		final JButton okay = new JButton("Ok");
        okay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane pane = getOptionPane((JComponent)e.getSource());
                pane.setValue(okay);
            }
        });
        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane pane = getOptionPane((JComponent)e.getSource());
                pane.setValue(cancel);
            }
        });

		JPanel rulesPanel = new JPanel();
		rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
		
		JPanel bornPanel = new JPanel();
		bornPanel.setLayout(new BoxLayout(bornPanel, BoxLayout.X_AXIS));
		JLabel bornRulesLabel = new JLabel("Born: ");
		
		bornPanel.add(bornRulesLabel);
		JLabel bornLabel0 = new JLabel(" 0");
		bornPanel.add(bornLabel0);
		JCheckBox bornCheckBox0 = new JCheckBox();
		if (parent.getBornRules()[0])
		{
			bornCheckBox0.setSelected(true);
		}
		
		bornPanel.add(bornCheckBox0);
		JLabel bornLabel1 = new JLabel(" 1");
		bornPanel.add(bornLabel1);
		JCheckBox bornCheckBox1 = new JCheckBox();
		bornPanel.add(bornCheckBox1);
		if (parent.getBornRules()[1])
		{
			bornCheckBox1.setSelected(true);
		}
		
		JLabel bornLabel2 = new JLabel(" 2");
		bornPanel.add(bornLabel2);
		JCheckBox bornCheckBox2 = new JCheckBox();
		bornPanel.add(bornCheckBox2);
		if (parent.getBornRules()[2])
		{
			bornCheckBox2.setSelected(true);
		}
		
		JLabel bornLabel3 = new JLabel(" 3");
		bornPanel.add(bornLabel3);
		JCheckBox bornCheckBox3 = new JCheckBox();
		bornPanel.add(bornCheckBox3);
		if (parent.getBornRules()[3])
		{
			bornCheckBox3.setSelected(true);
		}
		
		JLabel bornLabel4 = new JLabel(" 4");
		bornPanel.add(bornLabel4);
		JCheckBox bornCheckBox4 = new JCheckBox();
		bornPanel.add(bornCheckBox4);
		if (parent.getBornRules()[4])
		{
			bornCheckBox4.setSelected(true);
		}
		
		JLabel bornLabel5 = new JLabel(" 5");
		bornPanel.add(bornLabel5);
		JCheckBox bornCheckBox5 = new JCheckBox();
		bornPanel.add(bornCheckBox5);
		if (parent.getBornRules()[5])
		{
			bornCheckBox5.setSelected(true);
		}
		
		JLabel bornLabel6 = new JLabel(" 6");
		bornPanel.add(bornLabel6);
		JCheckBox bornCheckBox6 = new JCheckBox();
		bornPanel.add(bornCheckBox6);
		if (parent.getBornRules()[6])
		{
			bornCheckBox6.setSelected(true);
		}
		
		JLabel bornLabel7 = new JLabel(" 7");
		bornPanel.add(bornLabel7);
		JCheckBox bornCheckBox7 = new JCheckBox();
		bornPanel.add(bornCheckBox7);
		if (parent.getBornRules()[7])
		{
			bornCheckBox7.setSelected(true);
		}
		
		JLabel bornLabel8 = new JLabel(" 8");
		bornPanel.add(bornLabel8);
		JCheckBox bornCheckBox8 = new JCheckBox();
		bornPanel.add(bornCheckBox8);
		if (parent.getBornRules()[8])
		{
			bornCheckBox8.setSelected(true);
		}
		
		rulesPanel.add(bornPanel);
		
		
		JPanel survivePanel = new JPanel();
		survivePanel.setLayout(new BoxLayout(survivePanel, BoxLayout.X_AXIS));
		JLabel surviveRulesLabel = new JLabel("Survive: ");
		
		survivePanel.add(surviveRulesLabel);
		JLabel surviveLabel0 = new JLabel(" 0");
		survivePanel.add(surviveLabel0);
		JCheckBox surviveCheckBox0 = new JCheckBox();
		if (parent.getSurviveRules()[0])
		{
			surviveCheckBox0.setSelected(true);
		}
		
		survivePanel.add(surviveCheckBox0);
		JLabel surviveLabel1 = new JLabel(" 1");
		survivePanel.add(surviveLabel1);
		JCheckBox surviveCheckBox1 = new JCheckBox();
		survivePanel.add(surviveCheckBox1);
		if (parent.getSurviveRules()[1])
		{
			surviveCheckBox1.setSelected(true);
		}
		
		JLabel surviveLabel2 = new JLabel(" 2");
		survivePanel.add(surviveLabel2);
		JCheckBox surviveCheckBox2 = new JCheckBox();
		survivePanel.add(surviveCheckBox2);
		if (parent.getSurviveRules()[2])
		{
			surviveCheckBox2.setSelected(true);
		}
		
		JLabel surviveLabel3 = new JLabel(" 3");
		survivePanel.add(surviveLabel3);
		JCheckBox surviveCheckBox3 = new JCheckBox();
		survivePanel.add(surviveCheckBox3);
		if (parent.getSurviveRules()[3])
		{
			surviveCheckBox3.setSelected(true);
		}
		
		JLabel surviveLabel4 = new JLabel(" 4");
		survivePanel.add(surviveLabel4);
		JCheckBox surviveCheckBox4 = new JCheckBox();
		survivePanel.add(surviveCheckBox4);
		if (parent.getSurviveRules()[4])
		{
			surviveCheckBox4.setSelected(true);
		}
		
		JLabel surviveLabel5 = new JLabel(" 5");
		survivePanel.add(surviveLabel5);
		JCheckBox surviveCheckBox5 = new JCheckBox();
		survivePanel.add(surviveCheckBox5);
		if (parent.getSurviveRules()[5])
		{
			surviveCheckBox5.setSelected(true);
		}
		
		JLabel surviveLabel6 = new JLabel(" 6");
		survivePanel.add(surviveLabel6);
		JCheckBox surviveCheckBox6 = new JCheckBox();
		survivePanel.add(surviveCheckBox6);
		if (parent.getSurviveRules()[6])
		{
			surviveCheckBox6.setSelected(true);
		}
		
		JLabel surviveLabel7 = new JLabel(" 7");
		survivePanel.add(surviveLabel7);
		JCheckBox surviveCheckBox7 = new JCheckBox();
		survivePanel.add(surviveCheckBox7);
		if (parent.getSurviveRules()[7])
		{
			surviveCheckBox7.setSelected(true);
		}
		
		JLabel surviveLabel8 = new JLabel(" 8");
		survivePanel.add(surviveLabel8);
		JCheckBox surviveCheckBox8 = new JCheckBox();
		survivePanel.add(surviveCheckBox8);
		if (parent.getSurviveRules()[8])
		{
			surviveCheckBox8.setSelected(true);
		}
		
		rulesPanel.add(survivePanel);
		
		
		
		
		//show dialog
		int returnValue = JOptionPane.showOptionDialog(this, rulesPanel, "Get", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{okay, cancel}, okay);
		if(returnValue == JOptionPane.OK_OPTION) // if options have been updated
		{
			boolean[] born = new boolean[9];
			boolean[] survive = new boolean[9];
			
			born[0] = bornCheckBox0.isSelected();
			born[1] = bornCheckBox1.isSelected();
			born[2] = bornCheckBox2.isSelected();
			born[3] = bornCheckBox3.isSelected();
			born[4] = bornCheckBox4.isSelected();
			born[5] = bornCheckBox5.isSelected();
			born[6] = bornCheckBox6.isSelected();
			born[7] = bornCheckBox7.isSelected();
			born[8] = bornCheckBox8.isSelected();
			
			survive[0] = surviveCheckBox0.isSelected();
			survive[1] = surviveCheckBox1.isSelected();
			survive[2] = surviveCheckBox2.isSelected();
			survive[3] = surviveCheckBox3.isSelected();
			survive[4] = surviveCheckBox4.isSelected();
			survive[5] = surviveCheckBox5.isSelected();
			survive[6] = surviveCheckBox6.isSelected();
			survive[7] = surviveCheckBox7.isSelected();
			survive[8] = surviveCheckBox8.isSelected();
			
			this.parent.setRules(survive, born);
		}
	}
	
	public static boolean isNumeric(String str)  
	{  
		try  
		{  
			int i = Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}
	
	private boolean resizeCheck()
	{
		return true;
	}
}
