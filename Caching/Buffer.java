package Caching;

public class Buffer {
	
	private static String text;
	
	
	public static void setBufferText(String newText){
		text = new String(newText);
	}
	
	
	public static String getBufferText(){
		if(text == null){
			text = "";
			
		}
		return text;
	}
	
}
