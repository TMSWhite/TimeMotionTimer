/*package edu.columbia.dmi.TimeMotionTimer;*/

import java.io.*;
import java.lang.*;
import javax.swing.*;

public class FileIO extends JComponent {
   public static final int LOAD = 1;
   public static final int SAVE = 2;
	private JFileChooser jfc=null;
   
   public FileIO(String path) {
   		try {
      		jfc = new JFileChooser(path);
      	}
      	catch (Throwable t) {
      		System.err.println(t.getMessage());
      	}
   }
   
   public FileIO() {
   		try {
      		jfc = new JFileChooser(".");
			jfc.setDialogTitle("Select the XML file");
		}
		catch (Throwable t) {
      		System.err.println(t.getMessage());
		}
   }
   
   
	/** Load a named file, and place contents within a JTextArea.
	@param file	the name of the file
	@param jt	the JTextArea component
	@return	whether successful
	*/
	public String loadFile(String message) {
		if (jfc == null) {
			System.out.println("File IO not supported on this platform/browser");
			return null;
		}
		
		jfc.setDialogTitle(message);
		
		BufferedReader br = null;
	   File file = getFile(LOAD);
		StringBuffer sb = new StringBuffer();
	   if (file == null)
	      return null;;
	   try {
			String s;
			br = new BufferedReader(new FileReader(file));
			while ((s = br.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}
		} catch (FileNotFoundException e) {
			System.out.println("unable to find file '" + file + "'");
			return null;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
		try {
			if (br != null)
				br.close();
		} catch (IOException e) { }
		return sb.toString();
	}
	
	/** Saves the contents of a JTextArea to a file.
	@param toSave	the String from which to extract the contents
	*/
	public void saveFile(String message, String toSave) {
		if (jfc == null) {
			System.out.println("File IO not supported on this platform/browser");
			return;
		}
				
		jfc.setDialogTitle(message);

		BufferedWriter bw = null;
		try {
		   File file = getFile(SAVE);
		   if (file == null)
		      return;
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(toSave.toCharArray());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} 
		try {
			if (bw != null)
				bw.close();
		} catch (IOException e) { }
	}
	
	public File getFile(int option) {
	   if (option == LOAD) {
		   if (jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
		      return null;
	   }
	   else {
		   if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
		      return null;
	   }
	   File f = jfc.getSelectedFile();
	   if (option == LOAD && !f.isFile())
	      return null;
	   
	   return f;
	}
	
	public boolean isAvailable() {
		return (jfc != null);
	}
}

