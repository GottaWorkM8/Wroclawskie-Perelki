package com.example.cz1;

/*
	 *	This content is generated from the API File Info.
	 *	(Alt+Shift+Ctrl+I).
	 *
	 *	@desc 		
	 *	@file 		strona_g__wna
	 *	@date 		Sunday 19th of March 2023 11:05:16 PM
	 *	@title 		test
	 *	@author 	
	 *	@keywords 	
	 *	@generator 	Export Kit v1.3.figma
	 *
	 */
	



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.ImageButton;

public class MainPage extends Activity {

	

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_page);



		final ImageButton button;

		button = (ImageButton) findViewById(R.id.listButton);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				System.out.println("działa");
				openActivity2();
			}
		});

	}

		public void openActivity2(){
			Intent intent = new Intent(this, strona_zagadek.class);
			startActivity(intent);
		}
	}

	
	