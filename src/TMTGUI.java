/*package edu.columbia.dmi.TimeMotionTimer;*/

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class TMTGUI {
	public final static int SETUP = 0;
	public final static int RUN = 1;
	
	private SwingFactory sf = null;
	private JPanel gui = null;
	private JTextArea modelText = null;
	private JTextArea keymapText = null;
	private JTextArea errorText = null;
	private JTextArea dataText = null;
	private JTextField eventEnterBox = null;
	
	private JPanel dataOptionsPanel = null;
	private JPanel keymapPanel = null;
	
	private JButton stopButton = null;
	private JButton startButton = null;
	private JButton clearButton = null;
	private JButton refreshButton = null;
	private JButton submitAllButton = null;
	
	private JTabbedPane tabbedPane = null;
	
	public TMTGUI(SwingFactory sf) {
		this.sf = sf;
		
		initText();
		initButtons();
		initGUI();

		modelText.requestFocus();
	}
	
	public JPanel getGUI() { return gui; }
	public JTextArea getModelText() { return modelText; }
	public JTextArea getKeymapText() { return keymapText; }
	public JTextArea getErrorText() { return errorText; }
	public JTextArea getDataText() { return dataText; }
	public JTextField getEventEnterBox() { return eventEnterBox; }
	
	public JPanel getDataOptionsPanel() { return dataOptionsPanel; }
	public JPanel getKeymapPanel() { return keymapPanel; }
	
	public JButton getStartButton() { return startButton; }
	public JButton getStopButton() { return stopButton; }
	public JButton getSubmitAllButton() { return submitAllButton; }
	public JButton getClearButton() { return clearButton; }
	public JButton getRefreshButton() { return refreshButton; }

/*	
	public void resetControls(JPanel dataOptionsPanel, JPanel keymapPanel) {
		if (dataOptionsPanel == null) {
			this.dataOptionsPanel = sf.newJPanel(null,true);
		}
		else {
			this.dataOptionsPanel = dataOptionsPanel;
		}
		if (keymapPanel == null) {
			this.keymapPanel = sf.newJPanel(null,true);
		}
		else {
			this.keymapPanel = keymapPanel;
		}
		this.validate();
	}
*/	
	
	public void switchTo(int screen) {
		if (screen == SETUP) {
			tabbedPane.setSelectedIndex(SETUP);
		}
		else if (screen == RUN) {
			tabbedPane.setSelectedIndex(RUN);
		}
		tabbedPane.validate();
	}	
	
	private void initText() {
		modelText = sf.newJTextArea(7,4,true);
		keymapText = sf.newJTextArea(7,4,true);
		errorText = sf.newJTextArea(2,4,false);
		dataText = sf.newJTextArea(18,4,false);
		eventEnterBox = sf.newJTextField(4);
		tabbedPane = sf.newJTabbedPane();
		dataOptionsPanel = sf.newJPanel(null,true);
		keymapPanel = sf.newJPanel(null,true);
	}
	
	private void initButtons() {
		startButton = sf.newJButton("StartTimer");
		stopButton = sf.newJButton("StopTimer");
		submitAllButton = sf.newJButton("Submit & Start");
		clearButton = sf.newJButton("Clear");
		refreshButton = sf.newJButton("Refresh Task Names");
	}
	
	private void initGUI() {
		gui = sf.newJPanel(null,true);
		gui.add(BorderLayout.NORTH, instructions());
		gui.add(BorderLayout.CENTER, tabbedPane());
	}

	private JTabbedPane tabbedPane() {
		JPanel dmp = dataModelPanel();
		
		tabbedPane.add(dmp);
		tabbedPane.add(dataEntryPanel());
		tabbedPane.setSelectedIndex(SETUP);
		tabbedPane.setSelectedComponent(dmp);
		tabbedPane.setTitleAt(SETUP,"Setup");
		tabbedPane.setTitleAt(RUN,"Run");		
		return tabbedPane;
	}
	
	private JPanel instructions() {
		JPanel jc = sf.newJPanel(null,true);
		
		jc.add(BorderLayout.NORTH, sf.newJLabel("Time-Motion Timer",null));
		jc.add(BorderLayout.CENTER, sf.newJLabel("Instructions:  Setup data to collect; Run timers to collect data; Cut&Paste data to save",null));
		return jc;
	}
	
	private JPanel dataModelPanel() {
		JPanel jc = sf.newJPanel(null,true);
		jc.add(BorderLayout.NORTH, modelIdentifiers());
		jc.add(BorderLayout.CENTER, modelTasks());
		jc.add(BorderLayout.SOUTH, modelBuildTools());
		return jc;
	}
	
	private JPanel modelIdentifiers() {
		JPanel jc = sf.newJPanel(null,true);
		JScrollPane jsp = sf.newJScrollPane();
		jsp.getViewport().add(modelText);
		
		jc.add(BorderLayout.NORTH, sf.newJLabel("Specify Identifying Data - on each line use the syntax:   Name,Field_Width",null));
		jc.add(BorderLayout.CENTER, jsp);
		return jc;
	}
	
	private JPanel modelTasks() {
		JPanel jc = sf.newJPanel(null,true);
		JScrollPane jsp = sf.newJScrollPane();
		jsp.getViewport().add(keymapText);
		
		jc.add(BorderLayout.NORTH, sf.newJLabel("Specify Timed Tasks - on each line use the syntax:  Key,Meaning.  See Help for grouping tasks.",null));
		jc.add(BorderLayout.CENTER, jsp);
		return jc;
	}	
	
	private JPanel dataEntryPanel() {
		JPanel jc = sf.newJPanel(null,true);
		jc.add(BorderLayout.NORTH, dataOptionsPanel);
		jc.add(BorderLayout.CENTER, dataLogger());
		jc.add(BorderLayout.SOUTH, keymapPanel);
		return jc;		
	}
	
	private JPanel dataLogger() {
		JPanel jc = sf.newJPanel(null,true);
		JScrollPane jsp = sf.newJScrollPane();
		jsp.getViewport().add(dataText);
		
		jc.add(BorderLayout.NORTH, dataEntryControls());
		jc.add(BorderLayout.CENTER, jsp);
		return jc;	
	}
	
	private JPanel dataEntryControls() {
		JPanel jc = sf.newJPanel(new FlowLayout(FlowLayout.CENTER,5,2),true);
		
		jc.add(startButton);
		jc.add(sf.newJLabel("Enter Single Character Events:",null));
		jc.add(eventEnterBox);
		jc.add(stopButton);
		return jc;
	}
	
	private JPanel modelControls() {
		JPanel jc = sf.newJPanel(new FlowLayout(FlowLayout.CENTER,5,2),true);
		
		jc.add(submitAllButton);
		jc.add(clearButton);
		jc.add(refreshButton);
		return jc;
	}
	
	private JPanel modelBuildTools() {
		JPanel jc = sf.newJPanel(null,true);
		JScrollPane jsp = sf.newJScrollPane();
		jsp.getViewport().add(errorText);
				
		jc.add(BorderLayout.NORTH, modelControls());
		jc.add(BorderLayout.CENTER, sf.newJLabel("Setup Errors:",null));
		jc.add(BorderLayout.SOUTH, jsp);
		return jc;
	}
}
