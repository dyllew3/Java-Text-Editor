import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;



public class DocumentManager  {
	
	private String dir;
	private String filename;
	private String text = "";
	private int wordCount;
	private int charCount;
	
	
	public DocumentManager(String filename,String dir){
		this.dir = dir;
		this.filename = filename;
	}
	
	public DocumentManager(String filename){
		this.filename = filename;
		this.dir = null;
		
	}
	public DocumentManager(){
		this.filename = null;
		this.dir = null;
		
	}
	
	
	
	
	public void open(){
		
		JFileChooser select = new JFileChooser();
		JFrame display = new JFrame("File select");
		display.setSize(400, 400);
		display.setVisible(false);
		File selFile;
		this.text = "";
		if(this.dir != null)select.setCurrentDirectory(new File(dir));
		if(this.filename != null){
			selFile = new File(select.getCurrentDirectory() + "\\" + filename );
			
		}
		else{
			display.setVisible(true);
			select.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(select.showOpenDialog(display) ==JFileChooser.APPROVE_OPTION){
				selFile = select.getSelectedFile();	
			
			}
			else{
				selFile = null;
			}
		}
		try {
			BufferedReader getData = new BufferedReader(new FileReader(selFile));
			String curLine;
			while((curLine = getData.readLine() ) != null){
				this.text += curLine ;
				this.text += " \n";
				
			}
			dir = select.getCurrentDirectory().getPath();
			filename = selFile.getName();
			
			this.setWordCount();
			this.setCharCount();
			getData.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		display.dispose();
		
	}
	
	public void printText(){
		
	}
	
	
	
	
	public void save(String content){
		
		try {
			JFileChooser select = new JFileChooser();
			JFrame display = new JFrame("Save");
			select.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(select.showSaveDialog(display) ==JFileChooser.APPROVE_OPTION){
				FileWriter writer = new FileWriter(select.getSelectedFile());
				dir = select.getCurrentDirectory().getPath();
				filename = select.getSelectedFile().getName();
				this.text = content;
				writer.write(content);
				
				writer.close();
			}
			
			
			
		} catch (Exception e) {
		   // do something
			System.out.println("Gets here!");
		}
	}
	
	
	public void save(String dir,String filename, String text){
		
		if(filename == null)return;
		try {
			
			FileWriter writer = new FileWriter(new File(dir+"/"+filename));
			writer.write(text);
			
			writer.close();
			
		} catch (Exception e) {
		   // do something
			System.out.println("Gets here!");
		}
		
	}
	
	
	
	public void setWordCount(){
		
		this.wordCount= this.text.split(" ").length;
	}
	
	//sets character count based on the current text string
	public void setCharCount(){
		String noSpace = this.text.replace(" ", "");
		this.charCount = noSpace.split("").length;
		
	}
	
	public int getCharCount(){
		return this.charCount;
	}
	
	public int getWordCount(){
		return this.wordCount;
	}
	
	public String getContent(){
		this.open();
		return this.text;
	}
	
	public String getDir(){
		return this.dir;
	}
	public String getFilename(){
		return this.filename;
	}
	
	public void setDir(String newDir){
		this.dir = newDir;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DocumentManager demo = new DocumentManager();
		demo.open();
	}

}
