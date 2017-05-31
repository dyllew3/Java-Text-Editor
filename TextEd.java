import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import caching.Buffer;
import caching.Cache;

public class TextEd {
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;
	private JFrame window;
	private JTextArea text;
	private JScrollPane textWindow;
	private DocumentManager file;
	private Cache history;
	private Autosave a;
	
	
	//Autosave class uses multithreading to save the state of the document
	private class Autosave implements Runnable{
		
		
		public static final int DEFAULTSAVEFREQ = 1000;
		private JTextArea toSave;
		private int saveFreq;
		public String curState;
		
		
		public Autosave(JTextArea toSave){
			this(toSave,DEFAULTSAVEFREQ);
		}
		
		public Autosave(JTextArea toSave,int saveFreq) {
			// TODO Auto-generated constructor stub
			this.toSave = toSave;
			this.saveFreq = saveFreq;
			if(toSave.getText() != null)
				this.curState = new String(toSave.getText());
			else
				this.curState = "";
			
		}
		
		
		public void start(){
			Thread go = new Thread(this);
			go.start();
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				try{
					
					if(!curState.equals(toSave.getText())){
						curState = toSave.getText();
						history.saveStage(curState);
					}
					Thread.sleep(saveFreq);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				
			}

			
		}
		
	}
	
	
	public TextEd(String documentName, String path,boolean open){
		
		history = new Cache();
		text = new JTextArea();
		text.setLineWrap(true);
		text.setEditable(true);
		String title = "New";
		
		if(documentName != null && !documentName.equals("") && (path == null || path.equals(""))){
			file = new DocumentManager(documentName);
			
		}
		else if(path != null && documentName != null && !path.equals("") && !documentName.equals("")){
			file = new DocumentManager(documentName,path);
			
		}
		else{
			
			
			file = new DocumentManager();
			if(path != null && !path.equals(""))
				file.setDir(path);
			
		}

		if(open){
			file.open();
			title = file.getFilename();
			text.setText(file.getContent());
			history.saveStage(text.getText());
		}
		else{
			history.saveStage("");
		}
		a = new Autosave(text);
		a.start();
		textWindow = new JScrollPane(text);
		window  = new JFrame(title);
		window.setSize(WIDTH,HEIGHT);
		window.setJMenuBar(this.createMenuBar());
		window.add(textWindow);
		window.setVisible(true);
		
	}
	
	//default new text editor instance where it is a new file with no
	public TextEd(){
		this("","",false);
	}
	
	public TextEd(boolean open){
		
		this("","",open);
		
	}
	
	public TextEd(String documentName, String path){
		
		this(documentName,path,true);
		
		
	}
	
	 public JMenuBar createMenuBar(){
		 JMenuBar result = new JMenuBar();
		
		 
		 
		 
		 result.add(fileMenu());
		 result.add(editMenu());
		 return result;
				
		
	}
	 

	 public JMenu editMenu(){
		 JMenu editMenu = new JMenu("Edit");
		 JMenuItem cut = new JMenuItem(new AbstractAction("cut") {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Buffer.setBufferText(text.getSelectedText());
				text.replaceRange("", text.getSelectionStart(), text.getSelectionEnd());
				
			}
		});
		 JMenuItem copy = new JMenuItem(new AbstractAction("copy") {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					Buffer.setBufferText(text.getSelectedText());
					
				}
			});
		 JMenuItem paste = new JMenuItem(new AbstractAction("paste") {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					text.replaceSelection(Buffer.getBufferText());
					
				}
			});
		 JMenuItem selAll = new JMenuItem(new AbstractAction("select all") {
				
				/**
			 * 
			 */	@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					text.selectAll();
					
				}
			});
		 
		 JMenuItem undo = new JMenuItem(new AbstractAction("undo") {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					text.setText(history.undo());
					a.curState = text.getText();
				}
			});
		 
		 JMenuItem redo = new JMenuItem(new AbstractAction("redo") {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					 text.setText(history.redo(text.getText()));
					 a.curState = text.getText();
				}
			});
		 
		 
		 cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		 copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
		 paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
		 selAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		 undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		 redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
		 
		 
		 editMenu.add(cut);
		 editMenu.add(copy);
		 editMenu.add(paste);
		 editMenu.add(selAll);
		 editMenu.add(undo);
		 editMenu.add(redo);
		 
		 return editMenu;
	 }
	 
	 
	 
	 public JMenu fileMenu(){
		 JMenu fileMenu = new JMenu("File");
		 JMenuItem newFile = new JMenuItem(new AbstractAction("new") {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				new TextEd();
				
			}
		});
		 JMenuItem openFile = new JMenuItem(new AbstractAction("open") {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					new TextEd(true);
					
				}
			});
		 JMenuItem saveFile = new JMenuItem(new AbstractAction("save") {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(file.getDir() != null && file.getFilename() != null){
						file.save(file.getDir(),file.getFilename(),text.getText());
						//if new file change the title to reflect new name
						window.setTitle(file.getFilename());
					}
					else
						file.save(text.getText());
					
				}
			});
		 JMenuItem saveAsFile = new JMenuItem(new AbstractAction("save as") {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					file.save(text.getText());
					if(file.getFilename() != null)
						window.setTitle(file.getFilename());
					
				}
			});
		 
		 newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		 openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		 saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		 saveAsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.META_DOWN_MASK));
		 
		 
		 fileMenu.add(newFile);
		 fileMenu.add(openFile);
		 fileMenu.add(saveAsFile);
		 fileMenu.add(saveFile);
		 
		 return fileMenu;
	 }
	 
	 
	 
	 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TextEd demo = new TextEd("newFile.txt", "C:\\Users\\Dylan\\Documents");
	}

}
