/*package edu.columbia.dmi.TimeMotionTimer;*/

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import javax.swing.*;

public class EventCode extends JButton implements ActionListener {
    /* Implements button - triggers event */
    private long    eventStart;
    private String  eventKey;
    private String  trans;
    private String process;
    private AProcess ap = null;
    private boolean running=false;  // don't allow nested events
    private StringBuffer str;
    private JTextField eventEnterBox = null;

    public EventCode(String eventKey, String trans, String process, JTextField eventEnterBox) {
        super();

        this.eventKey = eventKey.trim();
        this.process = process;
        this.eventEnterBox = eventEnterBox;
        
        this.setMargin(new Insets(0,1,0,1));
        setTranslation(trans);	
        
	    this.addActionListener(this);

	    this.setBackground(Color.lightGray);
	    this.setForeground(Color.black);
	}

    public void actionPerformed(ActionEvent event)
    {
        start(System.currentTimeMillis());
    }
    
/*    
    public void startDataEntry() {
        str = new StringBuffer();
        str.append("\"" + startTimeStr + "\"");	// XXX startTimeStr

        Enumeration enum = dataSrc.elements();	// XXX dataSrc
        while(enum.hasMoreElements()) {
            str.append(",\"" + ((DataField) enum.nextElement()).getText() + "\"");
        }

		if (process != null)
			str.append(",\"" + process + "\"");
		else 
			str.append(",\"\"");
			
	    str.append(",\"" + eventKey + "\",\"" + trans + "\"," + (eventStart - startTime));	// XXX startTime
	}

    public void finishDataEntry() {
	    str.append("," + (time - eventStart) + "\n");
        dataText.append(str.toString());	// XXX dataText
    }
*/    

    public void start(long time) {
    	if (eventEnterBox != null) {
		    eventEnterBox.setText(eventKey);	
		    eventEnterBox.selectAll();
		}
        
        if (running) {
    		stop(time);
    		return;
        }
        
        eventStart = time;

        if (ap != null)
        	ap.stopOthers(this,time);	// for mutually exclusive in process set
        
        running = true;

        this.setBackground(Color.green);
        this.repaint();
    }

    public void stop(long time) {
        if (!running)
	        return;

        running = false;
        this.setBackground(Color.lightGray);
        this.repaint();
    }
    
    public void setTranslation(String trans) {
        String msg;
        this.trans = trans.trim();

        msg = new String(this.eventKey + "=" + this.trans);
        this.setText(msg);
	}

	public void setProcess(AProcess ap) {
		this.ap = ap;
	}
}
