/*package edu.columbia.dmi.TimeMotionTimer;*/

import javax.swing.*;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.Container;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Dimension;

/** Concrete Factor for building Java Swing components with same look and feel
**/
public class SwingFactory {
	private Color back = Color.lightGray;
	private Color fore = Color.black;
	private boolean opaque = true;
	private ActionListener al = null;
	private KeyListener kl = null;
	
	/** Initialize Factory with fore and background colors
	**/
	public SwingFactory(Color background, Color foreground, ActionListener al, KeyListener kl) {
		if (background != null)
			this.back = background;
			
		if (foreground != null)
			this.fore = foreground;
			
		this.al = al;
		this.kl = kl;
	}
	
	public JButton newJButton(String text) {
		JButton jc = new JButton();
		
		jc.setText(text);
		jc.setActionCommand(text);	// XXX What does ActionCommand do?
		jc.addActionListener(al);
		setDefaults(jc, true, true);
		return jc;
	}
	
	public JLabel newJLabel(String text, Integer alignment) {
		JLabel jc = new JLabel();
		
		jc.setText(text);
		
		if (alignment == null) {
			jc.setHorizontalAlignment(SwingConstants.CENTER);
		}
		else {
			try {
				jc.setHorizontalAlignment(alignment.intValue());
			}
			catch(Throwable t) { }
		}
		setDefaults(jc, true, false);
		return jc;
	}
	
	public JMenu newJMenu(String text) {
		JMenu jc = new JMenu();
		jc.setText(text);
		setDefaults(jc, true, false);
		return jc;
	}
	
	public JMenuBar newJMenuBar(String text) {
		JMenuBar jc = new JMenuBar();
		setDefaults(jc, true, false);
		return jc;
	}
	
	public JMenuItem newJMenuItem(String text) {
		JMenuItem jc = new JMenuItem();
		jc.setText(text);
		jc.addActionListener(al);		
		setDefaults(jc, true, false);
		return jc;
	}
		
	public JPanel newJPanel(LayoutManager layout, boolean visible) {
		JPanel jc = new JPanel();
		if (layout == null) {
			jc.setLayout(new BorderLayout(0,0));
		}
		else {
			jc.setLayout(layout);
		}
		
		setDefaults(jc, visible, false);
		return jc;
	}
	
	public JScrollPane newJScrollPane() {
		JScrollPane jc = new JScrollPane();
		setDefaults(jc, true, false);
		setDefaults(jc.getViewport(), true, false);
		return jc;
	}	
	
	public JSeparator newJSeparator() {
		JSeparator jc = new JSeparator();
		setDefaults(jc, true, false);
		return jc;
	}	
	
	public JTabbedPane newJTabbedPane() {
		JTabbedPane jc = new JTabbedPane();
		setDefaults(jc, true, false);
		return jc;
	}
	
	public JTextArea newJTextArea(int rows, int tabsize, boolean editable) {
		JTextArea jc = new JTextArea();
		jc.setRows(rows);
		jc.setTabSize(tabsize);
		jc.setEditable(editable);
		setDefaults(jc, true, true);
		return jc;
	}
	
	
	public JTextField newJTextField(int columns) {
		JTextField jc = new JTextField();
		jc.setColumns(columns);
		jc.addKeyListener(kl);
		setDefaults(jc, true, false);
		return jc;
	}
		
	private void setDefaults(JComponent jc, boolean visible, boolean focusable) {
		// Factory defaults
		try {
			jc.setOpaque(opaque);
			if (back != null)
				jc.setBackground(back);
			if (fore != null)
				jc.setForeground(fore);
			jc.setDoubleBuffered(false);
			
			jc.setRequestFocusEnabled(focusable);
			jc.setVisible(visible);
		}
		catch (Throwable t) { }
	}
	
	public void layoutFlowIntoBox(Container bounds, JComponent box, Vector components) {
	    /* Assumes that width of dst is same as master */
	    int count,x,maxWidth;
	    Enumeration enum = components.elements();
	    FlowLayout flow = new FlowLayout(FlowLayout.CENTER,2,2);
	    JPanel jp = newJPanel(flow,true);
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
	                    jp = newJPanel(new FlowLayout(FlowLayout.LEFT,2,2),true);
	                }
	                jp.add(jc); // make new panel for it, and fill it even though won't fit
	                box.add(jp);
	                jp = newJPanel(flow,true);
	                x = 0;
	            }
	            else {
	                if (count > 0)
	                    box.add(jp);
	                jp = newJPanel(flow,true);
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
}
