/*package edu.columbia.dmi.TimeMotionTimer;*/

import javax.swing.*;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.FlowLayout;
import java.awt.Color;

public class DataField extends JPanel implements FocusListener {
	private JLabel myLabel = new JLabel();
	private JTextField myText = new JTextField();
	private JButton stopButton = null;

	public DataField(String label, String width, JButton stopButton) {
		super(new FlowLayout(FlowLayout.CENTER,2,2));
		int columns = 5;	// a default value
		this.stopButton = stopButton;

		try { 
			columns = new Integer(width.trim()).intValue();
		}
		catch (NumberFormatException e) {
			System.err.println(width + " must be a number");
		}
		this.setRequestFocusEnabled(false);
		myLabel.setRequestFocusEnabled(false);
		myLabel.setText(label.trim());
		myLabel.setLabelFor(myText);
		myLabel.setOpaque(true);
		this.add(myLabel);
		myText.setColumns(columns);
		this.add(myText);
		myText.addFocusListener(this);
        myText.setBackground(Color.lightGray);
        myText.setForeground(Color.black);
        myLabel.setBackground(Color.lightGray);
        myLabel.setForeground(Color.black);
        this.setBackground(Color.lightGray);
        this.setForeground(Color.black);
	}

	public void focusGained(FocusEvent event)
	{
		myText.selectAll();	// so easily remove unwanted
	}

	public void focusLost(FocusEvent event) { }

	public String getText() {
		return myText.getText();
	}

	public void requestFocus() {
	    myText.requestFocus();
	    if (stopButton != null) {
	    	stopButton.setNextFocusableComponent(myText);
	    }
	}
}
