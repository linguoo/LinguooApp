package com.mkiisoft.linguoo;

import com.mkiisoft.linguoo.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegisterUserActivity extends Activity {
	
	private static final String TAG = "Register User Activity";
	private TextView tv_error;
	private EditText et_usu;
	private RelativeLayout rl_reg_usu;
	private RelativeLayout rl_reg_nomape;
	private EditText et_name;
	private EditText et_ape;
	private RelativeLayout rl_reg_email;
	private EditText et_email;
	private RelativeLayout rl_reg_pass;
	private EditText et_pass;
	private EditText et_pass2;
	private DatePicker dp_fecnac;
	private RelativeLayout rl_reg_bday;
	private RelativeLayout rl_reg_gen;
	private RadioGroup rg_gender;
	private Button btn_cancel;
	private Button btn_conf;
	private Button btn_next;
	private Button btn_back;
	private ProgressBar pb_reg;
	private int complete=0,actualpos=0;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"Linguoo Main");
        super.onCreate(savedInstanceState);
        
        setLinguooView();
		setListeners();
		setWatchers();
		initView();
    }


	private void setWatchers() {
		et_usu.addTextChangedListener(new GenericTextWatcher(et_usu));
		et_name.addTextChangedListener(new GenericTextWatcher(et_name));
		et_ape.addTextChangedListener(new GenericTextWatcher(et_ape));
		et_email.addTextChangedListener(new GenericTextWatcher(et_email));
		et_pass.addTextChangedListener(new GenericTextWatcher(et_pass));
		et_pass2.addTextChangedListener(new GenericTextWatcher(et_pass2));


	}


	private void setListeners() {
		btn_cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra("UsuLog", -1);
				setResult(RESULT_CANCELED,i);
				finish();
			}
			
		});
		
		btn_next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0){
				switch(actualpos){
				
				case 0:
					show(rl_reg_nomape);
					if ((et_name.getText().toString().equals("")==false)&&
							(et_ape.getText().toString().equals("")==false)){
						btn_next.setClickable(true);
					}else{
						btn_next.setClickable(false);
					}
					btn_back.setVisibility(View.VISIBLE);
					actualpos++;
					break;
				case 1:
					show(rl_reg_email);
					if (validateEmail(et_email.getText().toString())==false){
						btn_next.setClickable(false);
					}else{
						btn_next.setClickable(true);
					}
					actualpos++;
					break;
				case 2:
					show(rl_reg_pass);
					if(et_pass2.getText().toString().equals(et_pass.getText().toString())
							&&(et_pass2.getText().toString().equals("")==false)){
						btn_next.setClickable(true);
					}else{
						btn_next.setClickable(false);
					}
					actualpos++;
					break;
				case 3:
					show(rl_reg_bday);
					actualpos++;
					break;
				case 4:
					show(rl_reg_gen);
					btn_next.setText("Registrarse");
					actualpos++;
					break;
				case 5:
					//llamar al asynctask para completar el registro
					String registro=Constants.WSREUSR;
					registro+=","+et_name.getText().toString();
					registro+=","+et_ape.getText().toString();
					registro+=","+et_email.getText().toString();
					registro+=","+et_usu.getText().toString();
					registro+=","+et_pass.getText().toString();
					registro+=","+dp_fecnac.getDayOfMonth()+"/"+dp_fecnac.getMonth()+"/"+
					dp_fecnac.getYear();
					RadioButton radioSexButton = (RadioButton) findViewById(rg_gender.getCheckedRadioButtonId());
					registro+=","+radioSexButton.getText().toString().charAt(0)+",L";
					Log.d(TAG,registro);
					break;
				}
				
			}
			
		});
		
		btn_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				switch(actualpos){
				case 1:
					show(rl_reg_usu);
					if(et_usu.getText().toString().equals("")==false){
						btn_next.setClickable(true);
					}else{
						btn_next.setClickable(false);
					}
					btn_back.setVisibility(View.GONE);
					break;
				case 2:
					show(rl_reg_nomape);
					if ((et_name.getText().toString().equals("")==false)&&
							(et_ape.getText().toString().equals("")==false)){
						btn_next.setClickable(true);
					}else{
						btn_next.setClickable(false);
					}
					break;
				case 3:
					show(rl_reg_email);
					if (validateEmail(et_email.getText().toString())==false){
						btn_next.setClickable(false);
					}else{
						btn_next.setClickable(true);
					}
					break;
				case 4:
					show(rl_reg_pass);
					if(et_pass2.getText().toString().equals(et_pass.getText().toString())){
						btn_next.setClickable(true);
					}else{
						btn_next.setClickable(false);
					}
					break;
				case 5:
					show(rl_reg_bday);
					btn_next.setText("Siguiente");
					break;
				}
				actualpos--;
			}
		});
		
	}

	protected void show(RelativeLayout rl) {
		goneAllLayouts();
		rl.setVisibility(View.VISIBLE);
	}


	private void setLinguooView() {
		setContentView(R.layout.regist_layout);
		//Menu widgets
		btn_cancel = (Button)findViewById(R.id.btn_cancel);
		btn_conf = (Button)findViewById(R.id.btn_conf);
		
		//Error message
		tv_error = (TextView)findViewById(R.id.tv_error);
		
		//username
		rl_reg_usu = (RelativeLayout)findViewById(R.id.rl_reg_usu);
		et_usu = (EditText)findViewById(R.id.et_usu);
		
		//name and lastname
		rl_reg_nomape = (RelativeLayout)findViewById(R.id.rl_reg_nomape);
		et_name = (EditText)findViewById(R.id.et_name);
		et_ape = (EditText)findViewById(R.id.et_ape);
		
		//email
		rl_reg_email = (RelativeLayout)findViewById(R.id.rl_reg_email);
		et_email = (EditText)findViewById(R.id.et_email);
		
		//Password
		rl_reg_pass = (RelativeLayout)findViewById(R.id.rl_reg_pass);
		et_pass = (EditText)findViewById(R.id.et_pass);
		et_pass2 = (EditText)findViewById(R.id.et_pass2);
		
		//Datepicker
		rl_reg_bday = (RelativeLayout)findViewById(R.id.rl_reg_bday);
		dp_fecnac = (DatePicker)findViewById(R.id.dp_fecnac);
		
		//Genero y registro
		rl_reg_gen = (RelativeLayout)findViewById(R.id.rl_reg_gen);
		rg_gender = (RadioGroup)findViewById(R.id.rg_gender);
		
		//Control Form Buttons
		btn_back = (Button)findViewById(R.id.btn_back);
		btn_next = (Button)findViewById(R.id.btn_next);
		
		//Progress Bar
		pb_reg = (ProgressBar)findViewById(R.id.pb_reg);
	}

	private void initView() {
		tv_error.setText("");
		goneAllLayouts();
		rl_reg_usu.setVisibility(View.VISIBLE);
		btn_back.setVisibility(View.GONE);
		btn_next.setClickable(false);
	}


	private void goneAllLayouts() {
		rl_reg_usu.setVisibility(View.GONE);
		rl_reg_nomape.setVisibility(View.GONE);
		rl_reg_email.setVisibility(View.GONE);
		rl_reg_pass.setVisibility(View.GONE);
		rl_reg_bday.setVisibility(View.GONE);
		rl_reg_gen.setVisibility(View.GONE);
	}

	private boolean validateEmail(String text) {
		boolean result = false;
		String domain;
		if ((text.equals("")==false)&&(text.contains("@")==true)){
			domain = text.substring(text.indexOf("@")+1, text.length());
			//Log.d(TAG,domain);
			if ((domain.contains("@")==false)&&(domain.contains(".")==true)&&
					(domain.substring(domain.indexOf(".")).length()>1)){
				result = true;
			}
		}
		
		return result;
	}
	
	private class GenericTextWatcher implements TextWatcher{

	    private View view;
	    private GenericTextWatcher(View view) {
	        this.view = view;
	    }
	    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
	    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
	    public void afterTextChanged(Editable editable) {
	    	String text = editable.toString();
	    	switch(view.getId()){
	    	case R.id.et_usu:
	    		if (text.equals("")==true){
	    			complete = complete & (~ 0x01);
	    		}else{
	    			complete = complete | 0x01;
	    		}
	    		break;
	    	case R.id.et_name:
	    		if (text.equals("")==true){
	    			complete = complete & (~ 0x02);
	    		}else{
	    			complete = complete | 0x02;
	    		}
	    		break;
	    	case R.id.et_ape:
	    		if (text.equals("")==true){
	    			complete = complete & (~ 0x04);
	    		}else{
	    			complete = complete | 0x04;
	    		}
	    		break;
	    	case R.id.et_email:
	    		if (validateEmail(text)==false){
					complete = complete & (~ 0x08);
        		}else{
					complete = complete | 0x08;
        		}
            	break;
	    	case R.id.et_pass:
	    		if (text.equals("")==true){
	    			complete = complete & (~ 0x10);
	    		}else{
	    			complete = complete | 0x10;
	    		}
	    		break;
	    	case R.id.et_pass2:
	    		if ((text.equals("")==true)||(text.equals(et_pass.getText().toString())==false)){
	    			complete = complete & (~ 0x20);
	    		}else{
	    			complete = complete | 0x20;
	    		}
	    		break;
	    	}
			
	    	switch(actualpos){
			case 0:
				if ((complete & 0x01)>0){
					btn_next.setClickable(true);
				}
				break;
			case 1:
				if (((complete & 0x02)>0)&&((complete & 0x04)>0)){
					btn_next.setClickable(true);
				}
				break;
			case 2:
				if ((complete & 0x08)>0){
					btn_next.setClickable(true);
				}
				break;
			case 3:
				if (((complete & 0x10)>0)&&((complete & 0x20)>0)){
					btn_next.setClickable(true);
				}
				break;
			}
			Log.d(TAG,"complete value: "+complete);

	    	
	    }
	}

}
