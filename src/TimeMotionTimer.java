/*
A basic extension of the JApplet class
*/

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
	static final int COMMENTS = 0;
	static final int IDENTIFIERS = 1;
	static final int EVENTS = 2;	
	int errorCount = 0;

	public void init()
	{
		// This line prevents the "Swing: checked access to system event queue" message seen in some browsers.
		getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
		
		setJMenuBar(JMenuBar1);

		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(new BorderLayout(0,0));
		getContentPane().setBackground(java.awt.Color.lightGray);
		getContentPane().setForeground(java.awt.Color.black);
		getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 12));
		setSize(630,470);
		instructionsPanel.setRequestFocusEnabled(false);
		instructionsPanel.setDoubleBuffered(false);
		instructionsPanel.setLayout(new BorderLayout(0,0));
		getContentPane().add(BorderLayout.NORTH, instructionsPanel);
		instructionsPanel.setBackground(java.awt.Color.lightGray);
		instructionsPanel.setForeground(java.awt.Color.black);
		instructionsPanel.setBounds(0,0,630,30);
		JLabel1.setRequestFocusEnabled(false);
		JLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		JLabel1.setText("Time-Motion Timer");
		JLabel1.setOpaque(true);
		instructionsPanel.add(BorderLayout.NORTH, JLabel1);
		JLabel1.setBackground(java.awt.Color.lightGray);
		JLabel1.setForeground(java.awt.Color.black);
		JLabel1.setBounds(0,0,630,15);
		JLabel2.setRequestFocusEnabled(false);
		JLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		JLabel2.setText("Instructions:  Setup data to collect; Run timers to collect data; Cut&Paste data to save");
		JLabel2.setOpaque(true);
		instructionsPanel.add(BorderLayout.CENTER, JLabel2);
		JLabel2.setBackground(java.awt.Color.lightGray);
		JLabel2.setForeground(java.awt.Color.black);
		JLabel2.setBounds(0,15,630,15);
		tabbedPanel.setRequestFocusEnabled(false);
		tabbedPanel.setOpaque(true);
		getContentPane().add(BorderLayout.CENTER, tabbedPanel);
		tabbedPanel.setBackground(java.awt.Color.lightGray);
		tabbedPanel.setForeground(java.awt.Color.black);
		tabbedPanel.setBounds(0,30,630,440);
		dataModelPanel.setRequestFocusEnabled(false);
		dataModelPanel.setDoubleBuffered(false);
		dataModelPanel.setLayout(new BorderLayout(0,0));
		tabbedPanel.add(dataModelPanel);
		dataModelPanel.setBackground(java.awt.Color.lightGray);
		dataModelPanel.setForeground(java.awt.Color.black);
		dataModelPanel.setBounds(2,27,625,410);
		dataModelPanel.setVisible(false);
		JPanel8.setRequestFocusEnabled(false);
		JPanel8.setDoubleBuffered(false);
		JPanel8.setLayout(new BorderLayout(0,2));
		dataModelPanel.add(BorderLayout.NORTH, JPanel8);
		JPanel8.setBackground(java.awt.Color.lightGray);
		JPanel8.setForeground(java.awt.Color.black);
		JPanel8.setBounds(0,0,625,125);
		JLabel3.setRequestFocusEnabled(false);
		JLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		JLabel3.setText("Specify Identifying Data - on each line use the syntax:   Name,Field_Width");
		JLabel3.setLabelFor(modelText);
		JLabel3.setAlignmentY(0.0F);
		JLabel3.setOpaque(true);
		JPanel8.add(BorderLayout.NORTH, JLabel3);
		JLabel3.setBackground(java.awt.Color.lightGray);
		JLabel3.setForeground(java.awt.Color.black);
		JLabel3.setBounds(0,0,625,15);
		JScrollPane2.setRequestFocusEnabled(false);
		JScrollPane2.setOpaque(true);
		JPanel8.add(BorderLayout.CENTER, JScrollPane2);
		JScrollPane2.getViewport().setBackground(java.awt.Color.lightGray);
		JScrollPane2.getViewport().setForeground(java.awt.Color.black);
		JScrollPane2.setBounds(0,17,625,108);
		modelText.setRows(7);
		modelText.setTabSize(4);
		JScrollPane2.getViewport().add(modelText);
		modelText.setBackground(java.awt.Color.lightGray);
		modelText.setForeground(java.awt.Color.black);
		modelText.setBounds(0,0,622,105);
		JPanel3.setRequestFocusEnabled(false);
		JPanel3.setDoubleBuffered(false);
		JPanel3.setLayout(new BorderLayout(0,2));
		dataModelPanel.add(BorderLayout.CENTER, JPanel3);
		JPanel3.setBackground(java.awt.Color.lightGray);
		JPanel3.setForeground(java.awt.Color.black);
		JPanel3.setBounds(0,125,625,204);
		JLabel6.setRequestFocusEnabled(false);
		JLabel6.setText("Specify Timed Tasks - on each line use the syntax:  Key,Meaning.  See Help for grouping tasks.");
		JLabel6.setLabelFor(keymapText);
		JLabel6.setOpaque(true);
		JPanel3.add(BorderLayout.NORTH, JLabel6);
		JLabel6.setBackground(java.awt.Color.lightGray);
		JLabel6.setForeground(java.awt.Color.black);
		JLabel6.setBounds(0,0,625,15);
		JScrollPane4.setRequestFocusEnabled(false);
		JScrollPane4.setOpaque(true);
		JPanel3.add(BorderLayout.CENTER, JScrollPane4);
		JScrollPane4.getViewport().setBackground(java.awt.Color.lightGray);
		JScrollPane4.getViewport().setForeground(java.awt.Color.black);
		JScrollPane4.setBounds(0,17,625,187);
		keymapText.setRows(7);
		keymapText.setTabSize(4);
		JScrollPane4.getViewport().add(keymapText);
		keymapText.setBackground(java.awt.Color.lightGray);
		keymapText.setForeground(java.awt.Color.black);
		keymapText.setBounds(0,0,622,184);
		JPanel7.setRequestFocusEnabled(false);
		JPanel7.setDoubleBuffered(false);
		JPanel7.setLayout(new BorderLayout(0,2));
		dataModelPanel.add(BorderLayout.SOUTH, JPanel7);
		JPanel7.setBackground(java.awt.Color.lightGray);
		JPanel7.setForeground(java.awt.Color.black);
		JPanel7.setBounds(0,329,625,81);
		JPanel1.setRequestFocusEnabled(false);
		JPanel1.setDoubleBuffered(false);
		JPanel1.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
		JPanel7.add(BorderLayout.NORTH, JPanel1);
		JPanel1.setBackground(java.awt.Color.lightGray);
		JPanel1.setForeground(java.awt.Color.black);
		JPanel1.setBounds(0,0,625,29);
		submitAll.setText("Submit & Start");
		submitAll.setActionCommand("Submit");
		JPanel1.add(submitAll);
		submitAll.setBackground(java.awt.Color.lightGray);
		submitAll.setForeground(java.awt.Color.black);
		submitAll.setBounds(219,2,117,25);
		clearButton.setText("Clear");
		clearButton.setActionCommand("Clear");
		JPanel1.add(clearButton);
		clearButton.setBackground(java.awt.Color.lightGray);
		clearButton.setForeground(java.awt.Color.black);
		clearButton.setBounds(341,2,65,25);
		refreshButton.setText("Refresh Task Names");
		JPanel1.add(refreshButton);
		refreshButton.setBackground(java.awt.Color.lightGray);
		refreshButton.setForeground(java.awt.Color.black);
		JLabel7.setRequestFocusEnabled(false);
		JLabel7.setText("Setup Errors:");
		JLabel7.setOpaque(true);
		JPanel7.add(BorderLayout.CENTER, JLabel7);
		JLabel7.setBackground(java.awt.Color.lightGray);
		JLabel7.setForeground(java.awt.Color.black);
		JLabel7.setBounds(0,31,625,15);
		JScrollPane3.setRequestFocusEnabled(false);
		JScrollPane3.setOpaque(true);
		JPanel7.add(BorderLayout.SOUTH, JScrollPane3);
		JScrollPane3.getViewport().setBackground(java.awt.Color.lightGray);
		JScrollPane3.getViewport().setForeground(java.awt.Color.black);
		JScrollPane3.setBounds(0,48,625,33);
		errorText.setRows(2);
		errorText.setRequestFocusEnabled(false);
		errorText.setTabSize(4);
		errorText.setEditable(false);
		JScrollPane3.getViewport().add(errorText);
		errorText.setBackground(java.awt.Color.lightGray);
		errorText.setForeground(java.awt.Color.black);
		errorText.setBounds(0,0,622,30);
		dataEntryPanel.setRequestFocusEnabled(false);
		dataEntryPanel.setDoubleBuffered(false);
		dataEntryPanel.setLayout(new BorderLayout(0,0));
		tabbedPanel.add(dataEntryPanel);
		dataEntryPanel.setBackground(java.awt.Color.lightGray);
		dataEntryPanel.setForeground(java.awt.Color.black);
		dataEntryPanel.setBounds(2,27,625,410);
		dataEntryPanel.setVisible(false);
		dataOptionsPanel.setRequestFocusEnabled(false);
		dataOptionsPanel.setAlignmentX(0.0F);
		dataOptionsPanel.setDoubleBuffered(false);
		dataOptionsPanel.setLayout(new BoxLayout(dataOptionsPanel,BoxLayout.Y_AXIS));
		dataEntryPanel.add(BorderLayout.NORTH, dataOptionsPanel);
		dataOptionsPanel.setBackground(java.awt.Color.lightGray);
		dataOptionsPanel.setForeground(java.awt.Color.black);
		dataOptionsPanel.setBounds(0,0,625,0);
		JPanel2.setLayout(new BorderLayout(0,0));
		dataEntryPanel.add(BorderLayout.CENTER, JPanel2);
		JPanel2.setBackground(java.awt.Color.lightGray);
		JPanel2.setForeground(java.awt.Color.black);
		JPanel2.setBounds(0,0,625,410);
		JPanel6.setRequestFocusEnabled(false);
		JPanel6.setDoubleBuffered(false);
		JPanel6.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		JPanel2.add(BorderLayout.NORTH, JPanel6);
		JPanel6.setBackground(java.awt.Color.lightGray);
		JPanel6.setForeground(java.awt.Color.black);
		JPanel6.setBounds(0,0,625,35);
		startButton.setText("StartTimer");
		startButton.setActionCommand("StartTimer");
		JPanel6.add(startButton);
		startButton.setBackground(java.awt.Color.lightGray);
		startButton.setForeground(java.awt.Color.black);
		startButton.setBounds(101,5,95,25);
		JLabel4.setRequestFocusEnabled(false);
		JLabel4.setText("Enter Single Character Events:");
		JLabel4.setOpaque(true);
		JPanel6.add(JLabel4);
		JLabel4.setBackground(java.awt.Color.lightGray);
		JLabel4.setForeground(java.awt.Color.black);
		JLabel4.setBounds(201,10,172,15);
		eventEnterBox.setColumns(4);
		JPanel6.add(eventEnterBox);
		eventEnterBox.setBackground(java.awt.Color.lightGray);
		eventEnterBox.setForeground(java.awt.Color.black);
		eventEnterBox.setBounds(378,8,44,19);
		stopButton.setText("Stop Timer");
		stopButton.setActionCommand("Stop Timer");
		JPanel6.add(stopButton);
		stopButton.setBackground(java.awt.Color.lightGray);
		stopButton.setForeground(java.awt.Color.black);
		stopButton.setBounds(427,5,97,25);
		JScrollPane1.setRequestFocusEnabled(false);
		JScrollPane1.setOpaque(true);
		JPanel2.add(BorderLayout.CENTER, JScrollPane1);
		JScrollPane1.getViewport().setBackground(java.awt.Color.lightGray);
		JScrollPane1.getViewport().setForeground(java.awt.Color.black);
		JScrollPane1.setBounds(0,35,625,375);
		dataText.setRows(18);
		dataText.setTabSize(4);
		dataText.setEditable(false);
		JScrollPane1.getViewport().add(dataText);
		dataText.setBackground(java.awt.Color.lightGray);
		dataText.setForeground(java.awt.Color.black);
		dataText.setBounds(0,0,622,372);
		keymapPanel.setAlignmentX(0.0F);
		keymapPanel.setLayout(new BoxLayout(keymapPanel,BoxLayout.Y_AXIS));
		dataEntryPanel.add(BorderLayout.SOUTH, keymapPanel);
		keymapPanel.setBackground(java.awt.Color.lightGray);
		keymapPanel.setForeground(java.awt.Color.black);
		keymapPanel.setBounds(0,410,625,0);
		tabbedPanel.setSelectedIndex(0);
		tabbedPanel.setSelectedComponent(dataModelPanel);
		tabbedPanel.setTitleAt(0,"Setup");
		tabbedPanel.setTitleAt(1,"Run");
		
		//$$ JMenuBar1.move(168,312);
		fileMenu.setText("File");
		fileMenu.setActionCommand("File");
		fileMenu.setMnemonic((int)'F');
		JMenuBar1.add(fileMenu);
		
		if (fileChooser.isAvailable()) {
			loadModel.setText("Load Experiment Model...");
			loadModel.addActionListener(this);
			fileMenu.add(loadModel);
			saveModel.setText("Save Experiment Model...");
			saveModel.addActionListener(this);
			fileMenu.add(saveModel);
			saveData.setText("Save Data...");
			saveData.addActionListener(this);
			fileMenu.add(saveData);
		}
		fileMenu.add(JSeparator1);
		exitItem.setText("Exit");
		exitItem.setActionCommand("Exit");
		exitItem.setMnemonic((int)'X');
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);
		helpMenu.setText("Help");
		helpMenu.setActionCommand("Help");
		helpMenu.setMnemonic((int)'H');
		JMenuBar1.add(helpMenu);
		aboutItem.setText("About...");
		aboutItem.setActionCommand("About...");
		aboutItem.setMnemonic((int)'A');
		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);
		//}}

		//{{REGISTER_LISTENERS
		//}}

		/* Add custon controls, avoiding re-writes by Symantec */
		startButton.addActionListener(this);
		clearButton.addActionListener(this);
		refreshButton.addActionListener(this);
		stopButton.addActionListener(this);
		submitAll.addActionListener(this);
        eventEnterBox.addKeyListener(this);
        this.addComponentListener(this);

        this.validate();
        modelText.requestFocus();
    }

	//{{DECLARE_CONTROLS
	javax.swing.JPanel instructionsPanel = new javax.swing.JPanel();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JTabbedPane tabbedPanel = new javax.swing.JTabbedPane();
	javax.swing.JPanel dataModelPanel = new javax.swing.JPanel();
	javax.swing.JPanel JPanel8 = new javax.swing.JPanel();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JScrollPane JScrollPane2 = new javax.swing.JScrollPane();
	javax.swing.JTextArea modelText = new javax.swing.JTextArea();
	javax.swing.JPanel JPanel3 = new javax.swing.JPanel();
	javax.swing.JLabel JLabel6 = new javax.swing.JLabel();
	javax.swing.JScrollPane JScrollPane4 = new javax.swing.JScrollPane();
	javax.swing.JTextArea keymapText = new javax.swing.JTextArea();
	javax.swing.JPanel JPanel7 = new javax.swing.JPanel();
	javax.swing.JPanel JPanel1 = new javax.swing.JPanel();
	javax.swing.JButton submitAll = new javax.swing.JButton();
	javax.swing.JButton clearButton = new javax.swing.JButton();
	javax.swing.JButton refreshButton = new javax.swing.JButton();
	javax.swing.JLabel JLabel7 = new javax.swing.JLabel();
	javax.swing.JScrollPane JScrollPane3 = new javax.swing.JScrollPane();
	javax.swing.JTextArea errorText = new javax.swing.JTextArea();
	javax.swing.JPanel dataEntryPanel = new javax.swing.JPanel();
	javax.swing.JPanel dataOptionsPanel = new javax.swing.JPanel();
	javax.swing.JPanel JPanel2 = new javax.swing.JPanel();
	javax.swing.JPanel JPanel6 = new javax.swing.JPanel();
	javax.swing.JButton startButton = new javax.swing.JButton();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	javax.swing.JTextField eventEnterBox = new javax.swing.JTextField();
	javax.swing.JButton stopButton = new javax.swing.JButton();
	javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
	javax.swing.JTextArea dataText = new javax.swing.JTextArea();
	javax.swing.JPanel keymapPanel = new javax.swing.JPanel();

	javax.swing.JMenuBar JMenuBar1 = new javax.swing.JMenuBar();
	javax.swing.JMenu fileMenu = new javax.swing.JMenu();
	javax.swing.JMenuItem loadModel = new javax.swing.JMenuItem();
	javax.swing.JMenuItem saveModel = new javax.swing.JMenuItem();
	javax.swing.JMenuItem saveData = new javax.swing.JMenuItem();
	javax.swing.JSeparator JSeparator1 = new javax.swing.JSeparator();
	javax.swing.JMenuItem exitItem = new javax.swing.JMenuItem();
	javax.swing.JMenu helpMenu = new javax.swing.JMenu();
	javax.swing.JMenuItem aboutItem = new javax.swing.JMenuItem();
	//}}

	void startButton_actionPerformed(ActionEvent event)
	{
	    stopAndPrint();

    	startTime = System.currentTimeMillis();
    	Date date = new Date(startTime);

		startTimeStr = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(date);
		eventEnterBox.requestFocus();
	}

	void eventEnterBox_keyTyped(KeyEvent e)
	{
	    /* If key has not been defined, create new mapping */
	    long    time = System.currentTimeMillis();
	    String key = new Character(e.getKeyChar()).toString();
	    Object obj = keymap.get(key);
	    if (obj == null) {
	        keymapText.append(key + ",?\n");
	        EventCode event = addEvent(key,"?",null);
		    event.start(time);
		    displayEventButtons();
		    this.validate();	// to redraw
	    } else {
	        ((EventCode) obj).start(time);
	    }
	}              
	
	void clearButton_actionPerformed(ActionEvent event)
	{
		modelText.setText("");
		keymapText.setText("");
		modelText.requestFocus();

	}

	void refreshButton_actionPerformed(ActionEvent event)
	{
		resetErrors();
		resetProcesses();
		parseKeymap();
		displayEventButtons();
		setFocusToRun();
	}

	void stopButton_actionPerformed(ActionEvent event)
	{
        stopAndPrint();
	}

	private void stopAndPrint() {
	    long time = System.currentTimeMillis();
	    
	    for (int i=0;i<processes.size();++i) {
	    	((AProcess) processes.elementAt(i)).stop(time);
	   }
	}

	void submitAll_actionPerformed(ActionEvent event)
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
		errorText.setText("");
		errorCount = 0;
	}
	
	private void setFocusToRun() {
		if (errorCount > 0)
			return;
			
      tabbedPanel.setSelectedIndex(1);
		if (dataSrc != null) {
			DataField df = (DataField) dataSrc.elementAt(0);
			if (df != null)
				df.requestFocus();
		}
  	}
	
	private void parseModel() {
		String label, line;
		StringTokenizer lines, st;
		int errorCount=0;

		lines = new StringTokenizer(modelText.getText(),"\n");

		dataSrc = new Vector();

		dataText.append("\"StartTime\"");	// so can list column titles
		for(int count=1;lines.hasMoreTokens();++count) {
		    line = lines.nextToken();
		    st = new StringTokenizer(line,",");
		    DataField df;

		    if (st.countTokens() != 2) {
		        errorText.append("[DataModel] Wrong format on line " + count + " '" + line + "'\n");
		        errorCount++;
		        continue;
		    }
		    label = st.nextToken();
		    df = new DataField(label, st.nextToken());
		    if (df == null) {
		        errorText.append("[DataModel] Unable to add '" + line + "'");
		        ++errorCount;
		        continue;
		    }

		    dataSrc.addElement(df);

            dataText.append(",\"" + label + "\"");
		}
		dataText.append(",\"EventKey\",\"KeyMapping\",\"TimeSinceStart\",\"EventDuration\"\n");
	}

	private void displayIdentifierFields() {
	    layoutFlowIntoBox(this, dataOptionsPanel, dataSrc);
	}
	
	private void displayEventButtons() {
		// enumerate over processes
		AProcess ap;
		
		keymapPanel.removeAll();	// remove components
		
		for (int i=1;i<processes.size();++i) {
			ap = (AProcess) processes.elementAt(i);
			
			if (ap.size() > 0) {
				// add new bordered box
				TitledBorder tb = new TitledBorder(new LineBorder(Color.black), ap.getName());
				tb.setTitleColor(Color.black);
				
				JPanel jp = coloredJPanel();
				jp.setBorder(tb);
				
				keymapPanel.add(jp);
				
				layoutFlowIntoBox(this,jp,(Vector) ap);
			}
		}
		// now add the un-grouped tasks
		ap = (AProcess) processes.elementAt(0);
		if (ap.size() > 0) {
			JPanel jp = coloredJPanel();
			keymapPanel.add(jp);
			layoutFlowIntoBox(this,jp,(Vector) ap);
		}
	}
	
	private void resetKeymap() {
		keymap.clear();
		resetProcesses();
	}
	
	private void parseKeymap() {
		String label, line;
		StringTokenizer lines, st;
		String process = null; 
		
        lines = new StringTokenizer(keymapText.getText(),"\n");

        for(int count=1;lines.hasMoreTokens();++count) {
            line = lines.nextToken();
            
            if (line.startsWith("<process name=\"")) {
            	st = new StringTokenizer(line,"\"");
            	if (st.countTokens() != 3) {
		        		errorText.append("[EventCode] invalid format:  line " + count + " '" + line + "'\n");
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
			        errorText.append("[EventCode] Expect 2 args on line " + count + " '" + line + "'\n");
			        errorCount++;
			        continue;
			    }
			    String src, dst;
	
			    src = st.nextToken().trim();
			    if (src.length() != 1) {
			        errorText.append("[EventCode] Keys should be one character:  line " + count + " '" + line + "'\n");
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
			event = new EventCode(hotkey,meaning.trim(),process);
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
	
	class AProcess extends Vector {
		String name;
		
		AProcess(String name) {
			this.name = name;
		}
		
		void stop(long time) { 
			for (int i=0;i<this.size();++i) {
				((EventCode) this.elementAt(i)).stop(time);
			}
		}
		
		void stopOthers(EventCode current, long time) {
			for (int i=0;i<this.size();++i) {
				EventCode ev = (EventCode) this.elementAt(i);
				if (ev != current) {
					ev.stop(time);
				}
			}
		}
	
	   String getName() { return name; }
	}
	
	private void layoutFlowIntoBox(Container bounds, JComponent box, Vector components) {
	    /* Assumes that width of dst is same as master */
	    int count,x,maxWidth;
        Enumeration enum = components.elements();
        FlowLayout flow = new FlowLayout(FlowLayout.CENTER,2,2);
        JPanel jp = coloredJPanel(flow);
        Insets insets = jp.getInsets(); // will always be the same:  same FlowLayout

        maxWidth = bounds.getSize().width - (bounds.getInsets().left + bounds.getInsets().right);

        /* Clear previous sub-elements of box */
        box.removeAll();

        for (x=0,count=0;enum.hasMoreElements();++count) {
            JComponent jc = (JComponent) enum.nextElement();
            Dimension d = jc.getPreferredSize();

            if (d.width + x > maxWidth) {
                if (d.width > maxWidth) {
                    /* avoid infinite loop in trying to add:  Won't fit in
                    current or new panel, so add new one. */
                    if (count > 0) {
                        box.add(jp);
                        jp = coloredJPanel(new FlowLayout(FlowLayout.LEFT,2,2));
                    }
                    jp.add(jc); // make new panel for it, and fill it even though won't fit
                    box.add(jp);
                    jp = coloredJPanel(flow);
                    x = 0;
                }
                else {
                    if (count > 0)
                        box.add(jp);
                    jp = coloredJPanel(flow);
                    jp.add(jc);
                    x = d.width + insets.right;
                }
            }
            else {
                jp.add(jc);
                x += d.width + insets.right;
            }
        }
        if (count > 0) {
            if (x > 0) {
                box.add(jp);
            }
            box.validate();
        }
    }

    private JPanel coloredJPanel(FlowLayout flow) {
        JPanel jp = new JPanel(flow);
        jp.setForeground(Color.black);
        jp.setBackground(Color.lightGray);
        return jp;
    }

    private JPanel coloredJPanel() {
    	// default is box
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
        jp.setForeground(Color.black);
        jp.setBackground(Color.lightGray);
        return jp;
    }
        
    class EventCode extends JButton implements ActionListener {
        /* Implements popup menu and buttons - triggers event */
        long    eventStart;
        String  eventKey;
        String  trans;
        String process;
        AProcess ap = null;
//        JMenuItem jmi;
        boolean running=false;  // don't allow nested events
        StringBuffer str;

        public EventCode(String eventKey, String trans, String process) {
            super();

            this.eventKey = eventKey.trim();
            this.process = process;
            this.setMargin(new Insets(0,1,0,1));
            setTranslation(trans);	
            
/*
		    try {
    		    jmi.setAccelerator(KeyStroke.getKeyStroke(eventKey));
		    } catch (NullPointerException e) { }
		    jmi.addActionListener(this);
*/
//		    popup.add(jmi);
		    this.addActionListener(this);

		    this.setBackground(Color.lightGray);
		    this.setForeground(Color.black);
		}

	    public void actionPerformed(ActionEvent event)
	    {
            start(System.currentTimeMillis());
	    }

	    public void start(long time) {
	        eventEnterBox.setText(eventKey);
	        eventEnterBox.selectAll();
	        
	        if (running) {
	        		stop(time);
	        		return;
	        }
	        
	        if (ap != null)
	        		ap.stopOthers(this,time);	// for mutually exclusive in process set
	        

	        str = new StringBuffer();

            eventStart = time;

            str.append("\"" + startTimeStr + "\"");

            Enumeration enum = dataSrc.elements();
            while(enum.hasMoreElements()) {
                str.append(",\"" + ((DataField) enum.nextElement()).getText() + "\"");
            }

			if (process != null)
				str.append(",\"" + process + "\"");
			else 
				str.append(",\"\"");
				
		    str.append(",\"" + eventKey + "\",\"" + trans + "\"," + (eventStart - startTime));

            running = true;

	        this.setBackground(Color.green);
	        this.repaint();
	    }

	    public void stop(long time) {
	        if (!running)
    	        return;

    	    str.append("," + (time - eventStart) + "\n");
//    	    events.addElement(new String(str));
            dataText.append(str.toString());

	        running = false;
	        this.setBackground(Color.lightGray);
	        this.repaint();
	    }
	    
	    public void setTranslation(String trans) {
            String msg;
            this.trans = trans.trim();

            msg = new String(this.eventKey + "=" + this.trans);
            this.setText(msg);
 //           jmi = new JMenuItem(msg);	    	
		}
	
		public void setProcess(AProcess ap) {
			this.ap = ap;
		}
   }


	class DataField extends JPanel implements FocusListener {
		JLabel myLabel = new JLabel();
		JTextField myText = new JTextField();

		public DataField(String label, String width) {
			super(new FlowLayout(FlowLayout.CENTER,2,2));

			int	columns = new Integer(width.trim()).intValue();
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
		    stopButton.setNextFocusableComponent(myText);
		}
	}
/*
	private void maybeShowPopup(MouseEvent e) {
	    if (e.isPopupTrigger()) {
	        popup.show(e.getComponent(),e.getX(), e.getY());
        }
	}
*/
	/* Code for MenuListener */
/*
	public void mouseReleased(MouseEvent event)
	{
        maybeShowPopup(event);
    }

	public void mousePressed(MouseEvent event)
	{
        maybeShowPopup(event);
	}

	public void mouseClicked(MouseEvent event)
	{
        maybeShowPopup(event);
	}

	public void mouseEntered(MouseEvent event) { }
	public void mouseExited(MouseEvent event) { }
*/

	/* Code for ActionListener */

	public void actionPerformed(ActionEvent event)
	{
		Object obj = event.getSource();
    	if (obj instanceof JButton) {
		    if (obj == startButton) {
			    startButton_actionPerformed(event);
		    } else if (obj == clearButton) {
			    clearButton_actionPerformed(event);
		   }  else if (obj == refreshButton) {
		   	refreshButton_actionPerformed(event);
		    } else if (obj == stopButton) {
			    stopButton_actionPerformed(event);
		    } else if (obj == submitAll) {
			    submitAll_actionPerformed(event);
		   }
    	}
    	else if (obj instanceof JMenuItem) {
        	if (obj == loadModel) {
        		String src = fileChooser.loadFile("Load Experiment Definition File");
        		parseCSVModel(src);
        	}
        	else if (obj == saveModel) {
        		String src = "[Identifiers]\n" +
        						modelText.getText() +
        						"\n[Events]\n" +
        						keymapText.getText();
        						
        		fileChooser.saveFile("Save Experiment Definition File", src);
        	}
        	else if (obj == saveData) {
        		fileChooser.saveFile("Save Data to File", dataText.getText());
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
				}        		
        	}
        	else if (obj == aboutItem) {
				try {
					// JDialog Create with owner and show as modal
						JDialog JDialog1 = new JDialog();
						JDialog1.setModal(false);
						JDialog1.show();
				} catch (Exception e) {
				}        		
        	}
      }
	}


	public boolean parseCSVModel(String src) {
		BufferedReader br = new BufferedReader(new StringReader(src));
		String s;
		StringBuffer ids = new StringBuffer();
		StringBuffer evs = new StringBuffer();
		StringBuffer comments = new StringBuffer();
		int section = COMMENTS;
				
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
			modelText.setText(ids.toString());
			keymapText.setText(evs.toString());
			return true;
		}
		catch (IOException e) { 
			System.out.println(e.getMessage());
			return false;
		}
		finally {
			try {
				br.close();
			}
			catch (Exception e) { }
		}
	}
	
	/* Code for EventListener */
	public void keyTyped(KeyEvent event)
	{
	    Object obj = event.getSource();
	    if (obj == eventEnterBox) {
			eventEnterBox_keyTyped(event);
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
