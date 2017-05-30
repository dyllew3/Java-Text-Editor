package caching;

import java.util.Stack;

public class Cache {
	
	private static final int DEFAULTSIZE = 100;
	private int stackSize;
	
	private int stageSize;
	
	
	private Stack<String> stages;
	private Stack<String> reverseStages;
 	
	
	public Cache(){
		this(DEFAULTSIZE);
	}
	
	
	public Cache(int size){
		
		stages = new Stack<String>();
		reverseStages = new Stack<String>();
		stackSize = size;
		stageSize = 0;
		
	}
	
	
	public void saveStage(String text){
		if(text == null)return;
		
		if(stageSize == stackSize){
			stages.remove(--stageSize); 
			
		}
		
		if(clearRedo(text)){
			stages.push(text);
			stageSize++;
		}
		
	}
	
	
	
	public boolean clearRedo(String currentText){
		
		if(stages.isEmpty() || !currentText.equals(stages.peek())){
			reverseStages.removeAllElements();
			return true;
		}
		return false;
	}
	
	public String undo(){
		if(stages.size() == 1){
			return stages.peek();
		}
		reverseStages.push(stages.peek());
		stageSize--;
		return stages.pop();
	}
	
	public String redo(String curText){
		if(reverseStages.size() == 1){
			return reverseStages.peek();
		}
		else if(reverseStages.isEmpty()){
			return curText;
		}
		stages.push(reverseStages.peek());
		
		return reverseStages.pop();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
