import java.io.*;
import java.lang.*;
import javax.swing.*;

public class FileIO extends JComponent {
   public static final int LOAD = 1;
   public static final int SAVE = 2;
	private JFileChooser jfc;
   
   public FileIO(String path) {
      jfc = new JFileChooser(path);
   }
   
   public FileIO() {
      jfc = new JFileChooser(".");
		jfc.setDialogTitle("Select the XML file");
   }
   
   
	/** Load a named file, and place contents within a JTextArea.
	@param file	the name of the file
	@param jt	the JTextArea component
	@return	whether successful
	*/
	public String loadFile(String message) {
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
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) { }
		}
		return sb.toString();
	}
	
	/** Saves the contents of a JTextArea to a file.
	@param toSave	the String from which to extract the contents
	*/
	public void saveFile(String message, String toSave) {
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
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) { }
		}
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
}

