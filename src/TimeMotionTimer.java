/*package edu.columbia.dmi.TimeMotionTimer;*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.text.*;
import java.io.*;
import javax.swing.border.*;

public class TimeMotionTimer extends JApplet
    implements ActionListener, KeyListener, ComponentListener
{
    /* Local variables Needed for non gui part */

	long startTime=System.currentTimeMillis();
	String startTimeStr=new Date(startTime).toString();
	Vector dataSrc = new Vector();
	Vector processes = new Vector();
	Hashtable processNames = new Hashtable();
	Hashtable keymap = new Hashtable();
	FileIO fileChooser = new FileIO();
	TMTGUI tmt = null;
	SwingFactory sf = null;
	static final int COMMENTS = 0;
	static final int IDENTIFIERS = 1;
	static final int EVENTS = 2;	
	int errorCount = 0;

	public void init()
	{
		// This line prevents the "Swing: checked access to system event queue" message seen in some browsers.
		getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
		
		sf = new SwingFactory(null,null,this,this);
		tmt = new TMTGUI(sf);
		
		getContentPane().setLayout(new BorderLayout(0,0));
		getContentPane().add(BorderLayout.CENTER, tmt.getGUI());
		
        this.validate();
    }

	void getStartButton_actionPerformed(ActionEvent event)
	{
	    stopAndPrint();

    	startTime = System.currentTimeMillis();
    	Date date = new Date(startTime);

		startTimeStr = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(date);
		tmt.getEventEnterBox().requestFocus();
	}

	void getEventEnterBox_keyTyped(KeyEvent e)
	{
	    /* If key has not been defined, create new mapping */
	    long    time = System.currentTimeMillis();
	    String key = new Character(e.getKeyChar()).toString();
	    Object obj = keymap.get(key);
	    if (obj == null) {
	        tmt.getKeymapText().append(key + ",?\n");
	        EventCode event = addEvent(key,"?",null);
		    event.start(time);
		    displayEventButtons();
		    this.validate();	// to redraw
	    } else {
	        ((EventCode) obj).start(time);
	    }
	}              
	
	void getClearButton_actionPerformed(ActionEvent event)
	{
		tmt.getModelText().setText("");
		tmt.getKeymapText().setText("");
		tmt.getModelText().requestFocus();

	}

	void getRefreshButton_actionPerformed(ActionEvent event)
	{
		resetErrors();
		resetProcesses();
		parseKeymap();
		displayEventButtons();
		setFocusToRun();
	}

	void getStopButton_actionPerformed(ActionEvent event)
	{
        stopAndPrint();
	}

	private void stopAndPrint() {
	    long time = System.currentTimeMillis();
	    
	    for (int i=0;i<processes.size();++i) {
	    	((AProcess) processes.elementAt(i)).stop(time);
	   }
	}

	void getSubmitAllButton_actionPerformed(ActionEvent event)
	{
		resetErrors();
		parseModel();
		resetKeymap();
		parseKeymap();
		displayIdentifierFields();
		displayEventButtons();
		setFocusToRun();
	}
	
	private void resetErrors() {
		tmt.getErrorText().setText("");
		errorCount = 0;
	}
	
	private void setFocusToRun() {
		if (errorCount > 0)
			return;
			
		tmt.switchTo(TMTGUI.RUN);
		if (dataSrc != null && dataSrc.size() >= 1) {
			DataField df = (DataField) dataSrc.elementAt(0);
			if (df != null)
				df.requestFocus();
		}
  	}
	
	private void parseModel() {
		String label, line;
		StringTokenizer lines, st;
		int errorCount=0;

		lines = new StringTokenizer(tmt.getModelText().getText(),"\n");

		dataSrc = new Vector();

		tmt.getDataText().append("\"StartTime\"");	// so can list column titles
		for(int count=1;lines.hasMoreTokens();++count) {
		    line = lines.nextToken();
		    st = new StringTokenizer(line,",");
		    DataField df;

		    if (st.countTokens() != 2) {
		        tmt.getErrorText().append("[DataModel] Wrong format on line " + count + " '" + line + "'\n");
		        errorCount++;
		        continue;
		    }
		    label = st.nextToken();
		    df = new DataField(label, st.nextToken(), tmt.getStopButton());
		    if (df == null) {
		        tmt.getErrorText().append("[DataModel] Unable to add '" + line + "'");
		        ++errorCount;
		        continue;
		    }

		    dataSrc.addElement(df);

            tmt.getDataText().append(",\"" + label + "\"");
		}
		tmt.getDataText().append(",\"EventKey\",\"KeyMapping\",\"TimeSinceStart\",\"EventDuration\"\n");
	}

	private void displayIdentifierFields() {
	    sf.layoutFlowIntoBox(this, tmt.getDataOptionsPanel(), dataSrc);
	}
	

	private void displayEventButtons() {
		// enumerate over processes
		AProcess ap;
		
		tmt.getKeymapPanel().removeAll();	// remove components
		
		for (int i=1;i<processes.size();++i) {
			ap = (AProcess) processes.elementAt(i);
			
			if (ap.size() > 0) {
				// add new bordered box
				TitledBorder tb = new TitledBorder(new LineBorder(Color.black), ap.getName());
				tb.setTitleColor(Color.black);
				
				JPanel jp = sf.newJPanel(null,true);
				jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
				jp.setBorder(tb);
				
				tmt.getKeymapPanel().add(jp);
				
				sf.layoutFlowIntoBox(this,jp,(Vector) ap);
			}
		}
		// now add the un-grouped tasks
		ap = (AProcess) processes.elementAt(0);
		if (ap.size() > 0) {
			JPanel jp = sf.newJPanel(null,true);
			jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
			tmt.getKeymapPanel().add(jp);
			sf.layoutFlowIntoBox(this,jp,(Vector) ap);
		}
	}
	
/*
	private void displayEventButtons() {
		sf.layoutFlowIntoBox(this, tmt.getKeymapPanel(), getEventButtons());
	}
	
	private Vector getEventButtons() {
		// enumerate over processes
		Vector components = new Vector();
		
		AProcess ap;
		
		for (int i=1;i<processes.size();++i) {
			ap = (AProcess) processes.elementAt(i);
			
			if (ap.size() > 0) {
				// add new bordered box
				TitledBorder tb = new TitledBorder(new LineBorder(Color.black), ap.getName());
				tb.setTitleColor(Color.black);
				
				JPanel jp = sf.newJPanel(null,true);
				jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
				jp.setBorder(tb);
				
				components.add(jp);
			}
		}
		// now add the un-grouped tasks
		ap = (AProcess) processes.elementAt(0);
		if (ap.size() > 0) {
			JPanel jp = sf.newJPanel(null,true);
			jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
			
			components.add(jp);
		}
		
		return components;
	}
*/	
	
	private void resetKeymap() {
		keymap.clear();
		resetProcesses();
	}
	
	private void parseKeymap() {
		String label, line;
		StringTokenizer lines, st;
		String process = null; 
		
        lines = new StringTokenizer(tmt.getKeymapText().getText(),"\n");

        for(int count=1;lines.hasMoreTokens();++count) {
            line = lines.nextToken();
            
            if (line.startsWith("<process name=\"")) {
            	st = new StringTokenizer(line,"\"");
            	if (st.countTokens() != 3) {
		        		tmt.getErrorText().append("[EventCode] invalid format:  line " + count + " '" + line + "'\n");
			        errorCount++;
			        continue;
            	}
            	st.nextToken();
            	process = st.nextToken().trim();
            }
            else if (line.equals("</process>")) {
            	process = null;
            }
            else {
	          st = new StringTokenizer(line,",");
			    if (st.countTokens() != 2) {
			        tmt.getErrorText().append("[EventCode] Expect 2 args on line " + count + " '" + line + "'\n");
			        errorCount++;
			        continue;
			    }
			    String src, dst;
	
			    src = st.nextToken().trim();
			    if (src.length() != 1) {
			        tmt.getErrorText().append("[EventCode] Keys should be one character:  line " + count + " '" + line + "'\n");
			        errorCount++;
			        continue;
			    }
			    dst = st.nextToken().trim();
				
				addEvent(src,dst,process);

			}
		}
	}
	
	EventCode addEvent(String hotkey, String meaning, String process) {
		// ultimately need to check for existing events
		EventCode event = null;
		AProcess ap;
		
		// create a new process, if needed
		if (process != null) {
			if (processNames.containsKey(process)) {
				ap = (AProcess) processNames.get(process);
			}
			else {
				ap = new AProcess(process);
				processes.addElement(ap);
				processNames.put(process,ap);
			}
		}
		else {
			ap = (AProcess) processes.elementAt(0);
		}
		
		// create, or rename new event
		if (keymap.containsKey(hotkey)) {
			event = (EventCode) keymap.get(hotkey);
			event.setTranslation(meaning);
		}
		else {
			event = new EventCode(hotkey,meaning.trim(),process,tmt.getEventEnterBox());
			keymap.put(hotkey,event);
		}
		
		ap.addElement(event);
		
		if (process != null) {
			event.setProcess(ap);	// so that stopOthers() only applies to process sets
		}
		else {
			event.setProcess(null);
		}
		
		return event;
	}
	
	void resetProcesses() {
		processes = new Vector();
		processes.addElement(new AProcess(null));
		processNames = new Hashtable();
	}
	
	/* Code for ActionListener */

	public void actionPerformed(ActionEvent event)
	{
		Object obj = event.getSource();
    	if (obj instanceof JButton) {
		    if (obj == tmt.getStartButton()) {
			    getStartButton_actionPerformed(event);
		    } else if (obj == tmt.getClearButton()) {
			    getClearButton_actionPerformed(event);
		   }  else if (obj == tmt.getRefreshButton()) {
		   		getRefreshButton_actionPerformed(event);
		    } else if (obj == tmt.getStopButton()) {
			    getStopButton_actionPerformed(event);
		    } else if (obj == tmt.getSubmitAllButton()) {
			    getSubmitAllButton_actionPerformed(event);
		   }
    	}
/*    	
    	else if (obj instanceof JMenuItem) {
        	if (obj == loadModel) {
        		String src = fileChooser.loadFile("Load Experiment Definition File");
        		parseCSVModel(src);
        	}
        	else if (obj == saveModel) {
        		String src = "[Identifiers]\n" +
        						tmt.getModelText().getText() +
        						"\n[Events]\n" +
        						tmt.getKeymapText().getText();
        						
        		fileChooser.saveFile("Save Experiment Definition File", src);
        	}
        	else if (obj == saveData) {
        		fileChooser.saveFile("Save Data to File", tmt.getDataText().getText());
        	}
        	else if (obj == exitItem) {
				try {
			    	// Beep
			    	Toolkit.getDefaultToolkit().beep();
			    	// Show a confirmation dialog
			    	int reply = JOptionPane.showConfirmDialog(this, 
			    	                                          "Do you really want to exit?", 
			    	                                          "Time Motion Timer - Exit" , 
			    	                                          JOptionPane.YES_NO_OPTION, 
			    	                                          JOptionPane.QUESTION_MESSAGE);
					// If the confirmation was affirmative, handle exiting.
					if (reply == JOptionPane.YES_OPTION)
					{
				    	this.setVisible(false);    // hide the Frame
				    	System.exit(0);            // close the application
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}        		
        	}
        	else if (obj == aboutItem) {
				try {
					// JDialog Create with owner and show as modal
						JDialog JDialog1 = new JDialog();
						JDialog1.setModal(false);
						JDialog1.show();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}        		
        	}
      }
*/
	}


	public boolean parseCSVModel(String src) {
		BufferedReader br = new BufferedReader(new StringReader(src));
		String s;
		StringBuffer ids = new StringBuffer();
		StringBuffer evs = new StringBuffer();
		StringBuffer comments = new StringBuffer();
		int section = COMMENTS;
		boolean ok = false;
				
		try {
			for (int line=0;(s = br.readLine()) != null;++line) {
				if ("[Identifiers]".equals(s)) {
					section = IDENTIFIERS;
					continue;
				}
				if ("[Events]".equals(s)) {
					section = EVENTS;
					continue;
				}
				s = s.trim();
				if (s.length() > 0) {
					if (section == IDENTIFIERS) {
						ids.append(s + "\n");
					}
					else if (section == EVENTS) {
						evs.append(s + "\n");
					}
					else {
						comments.append(s + "\n");
					}
				}
			}
			tmt.getModelText().setText(ids.toString());
			tmt.getKeymapText().setText(evs.toString());
			ok = true;
		}
		catch (IOException e) { 
			System.out.println(e.getMessage());
			ok = false;
		}
		try {
			br.close();
		}
		catch (Exception e) { }
		return ok;
	}
	
	/* Code for EventListener */
	public void keyTyped(KeyEvent event)
	{
	    Object obj = event.getSource();
	    if (obj == tmt.getEventEnterBox()) {
			getEventEnterBox_keyTyped(event);
	    }
	}

	public void keyPressed(KeyEvent event) { }
	public void keyReleased(KeyEvent event) { }

	/* Code for ComponentListener */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentShown(ComponentEvent e) { }

	public void componentResized(ComponentEvent e) {
		displayIdentifierFields();
		displayEventButtons();
	    this.validate();    // else doesn't fully resize everything
	}
}
