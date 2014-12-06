package com.cloud.dinein;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignInActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
	}
	
	public void signIn(View view)
	{
		Intent intent = new Intent(this, HomeActivity.class);
	    startActivity(intent);
	    
	}
	public void credentials(View view)
	{
		Intent intent = new Intent(this, SignUpActivity.class);
	    startActivity(intent);
	}
}
