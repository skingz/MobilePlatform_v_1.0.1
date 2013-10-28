package com.leading.baselibrary.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leading.baselibrary.R;

public class DialogModal extends Dialog {

	public Button btnOK,btnCancal;
	public DialogModal(Context context) {
		super(context,R.style.ModalDialog);
		this.setContentView(R.layout.modal_dialog);
		btnOK=(Button)this.findViewById(R.id.btn_OK_Dailog);
		btnCancal=(Button)this.findViewById(R.id.btn_cancel_Dailog);
	}
	public void showMessage(String msgContent){
		TextView tv_Msg=(TextView)this.findViewById(R.id.tv_Msg_Dailog);
		tv_Msg.setText(msgContent);
		btnCancal.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				DialogModal.this.dismiss();
			}});
		super.show();
	}

}
