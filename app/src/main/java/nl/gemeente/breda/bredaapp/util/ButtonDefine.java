package nl.gemeente.breda.bredaapp.util;

import android.content.DialogInterface;

public class ButtonDefine {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private String text;
	private DialogInterface.OnClickListener event;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public ButtonDefine(String text, DialogInterface.OnClickListener event) {
		this.text = text;
		this.event = event;
	}
	
	//================================================================================
	// Mutators
	//================================================================================
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	public DialogInterface.OnClickListener getEvent() {
		return event;
	}
	
	public void setEvent(DialogInterface.OnClickListener event) {
		this.event = event;
	}
}
