package com.cloud.dinein;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
	}
	
	public void signUp(View view)
	{
		Intent intent = new Intent(this, HomeActivity.class);
	    startActivity(intent);
	}

	
	
	
	
}
