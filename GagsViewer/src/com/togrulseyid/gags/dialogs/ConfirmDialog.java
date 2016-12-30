package com.togrulseyid.gags.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.togrulseyid.gags.interfaces.DialogConfirmCustomView;
import com.togrulseyid.gags.interfaces.DialogConfirmOnCancelListener;
import com.togrulseyid.gags.operations.Utility;
import com.togrulseyid.gags.viewer.R;


/**
 * ConfirmDialog is Mobchannel's custom dialog that extended to Dialog @see
 * android.app.Dialog and implemented DialogConfirmOnCancelListener @see
 * com.mobchannel.interfaces.DialogConfirmOnCancelListener and
 * DialogConfirmCustomView @see
 * com.mobchannel.interfaces.DialogConfirmCustomView This class has 3
 * constructors :
 * <ul>
 * <li>ConfirmDialog(Context context, String title, String message)
 * <li>ConfirmDialog(Context context, String title, View view)
 * <li>ConfirmDialog(Context context, String title, View view, int positiveBtn,
 * int negativeBtn)
 * </ul>
 * <p>
 * ConfirmDialog gives ability to change center of dialog with a custom view and
 * listen to UI changes.
 * 
 * @author Toghrul Seyidov
 * @version 1.9, 3 Mar 2015
 * @since 1.2
 */
public class ConfirmDialog extends Dialog implements
		DialogConfirmOnCancelListener, DialogConfirmCustomView {

	private String title;
	private String message;
	private TextView textViewMesage;
	private LinearLayout fragmentBody;
	private TextView textViewMessageTitle;
	private Button buttonOK;
	private Button buttonCancel;
	private Context context;
	private View view;
	private int positiveBtn;
	private int negativeBtn;
	
	/** 
	 * This Constructs ConfirmDialog dialog with parameters context, title, message.
	 *
	 * @param context Context
	 * @param title title of Dialog
	 * @param message body of Dialog
	 * */
	public ConfirmDialog(Context context, String title, String message) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.title = title;
		this.message = message;
		this.context = context;
	}

	/**
	 * This Constructs ConfirmDialog dialog with parameters context, title, view.
	 *
	 * @param context Context
	 * @param title the title of Dialog
	 * @param view the view of Dialog content
	 * */
 	public ConfirmDialog(Context context, String title, View view) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.title = title;
		this.view = view;
		this.context = context;
	}
	
	/**
	 * This Constructs ConfirmDialog dialog with parameters context, title, view, positiveBtn, negativeBtn.
	 *
	 * @param context Context
	 * @param title the title of Dialog
	 * @param view the view of Dialog content
	 * @param positiveBtn reference of positive button text
	 * @param negativeBtn reference of negative button text
	 */
	public ConfirmDialog(Context context, String title, View view, int positiveBtn, int negativeBtn) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.title = title;
		this.view = view;
		this.context = context;
		this.positiveBtn = positiveBtn;
		this.negativeBtn = negativeBtn;
	}

	
	/**
	 * this method is for dismissing dialog
	 */
	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setGravity(Gravity.CENTER);
		
		setContentView(R.layout.dialog_confirm);
		
		
		// android.R.style.Theme_Translucent_NoTitleBar
		textViewMesage = (TextView) findViewById(R.id.textViewDialogConfirmMessageBody);
		fragmentBody = (LinearLayout) findViewById(R.id.linearLayoutDialogConfirmMessageBody);
		textViewMessageTitle = (TextView) findViewById(R.id.textViewDialogConfirmMessageTitle);
		buttonOK = (Button) findViewById(R.id.buttonDialogConfirmOK);
		buttonCancel = (Button) findViewById(R.id.buttonDialogConfirmCancel);
		
		if(positiveBtn!=0) {
			buttonOK.setText(positiveBtn);
			buttonCancel.setText(negativeBtn);
		} 

		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
			
		});

		if (!Utility.isEmptyOrNull(message) && !Utility.isEmptyOrNull(title)) {
			
			textViewMesage.setVisibility(View.VISIBLE);
			textViewMesage.setText(message);
			textViewMessageTitle.setText(title);
			
		} else {
			
			textViewMessageTitle.setText(title);
			fragmentBody.setVisibility(View.VISIBLE);
			fragmentBody.addView(view);

		}

	}

	/**
	 * Set View.OnClickListener for catching onClickListener of positive button
	 *
	 * @param onClickListener View.OnClickListener
	 */
	public void setOnClickListener(View.OnClickListener onClickListener) {
		buttonOK.setOnClickListener(onClickListener);
	}

	@Override
	public void onCancelled() {
	}

	@Override
	public void onApproved() {
	}
}
