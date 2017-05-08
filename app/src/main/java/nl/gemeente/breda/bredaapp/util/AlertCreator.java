package nl.gemeente.breda.bredaapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;

import static android.R.style.Theme_DeviceDefault_Dialog_Alert;

public class AlertCreator {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private AlertDialog.Builder dialog;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public AlertCreator(Context context) {
		dialog = new AlertDialog.Builder(context, Theme_DeviceDefault_Dialog_Alert);
	}
	
	public AlertCreator(Context context, String title, @DrawableRes int iconID, String message) {
		dialog = new AlertDialog.Builder(context, Theme_DeviceDefault_Dialog_Alert);
		setTitle(title);
		setIcon(iconID);
		setMessage(message);
	}
	
	//================================================================================
	// Mutators
	//================================================================================
	
	public void setTitle(String title) {
		dialog.setTitle(title);
	}
	
	public void setIcon(@DrawableRes int iconID) {
		dialog.setIcon(iconID);
	}
	
	public void setMessage(String message) {
		dialog.setMessage(message);
	}
	
	public void setPositiveButton(String text, DialogInterface.OnClickListener event) {
		dialog.setPositiveButton(text, event);
	}
	
	public void setPositiveButton(ButtonDefine button) {
		dialog.setPositiveButton(button.getText(), button.getEvent());
	}
	
	public void setNegativeButton(String text, DialogInterface.OnClickListener event) {
		dialog.setNegativeButton(text, event);
	}
	
	public void setNegativeButton(ButtonDefine button) {
		dialog.setNegativeButton(button.getText(), button.getEvent());
	}
	
	public AlertDialog show() {
		return dialog.show();
	}
}