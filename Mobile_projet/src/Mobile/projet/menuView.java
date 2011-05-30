package Mobile.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class menuView extends Activity{
	static Button button_play;
	static Button button_scores;
	static Button button_param;
	static Button button_rules;
	static Button button_about;
	static Button button_exit;
	Intent intent = new Intent("com.angel.android.MUSIC");//Music Intent
	static boolean value;
	static final int SEND_M_REQUEST = 1;
	public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
     // On recupere le bouton "play" et on lui associe une action
        button_play = (Button)this.findViewById(R.id.Button_play);
        button_play.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				if (Parameters.isSoundActivate()) {
//					Parameters.playTinySound();
//					Parameters.pauseSoundBackground();
//				}
				if (value) {
					System.out.println("NO Music");
				} else {
					
					System.out.println("OOOOOOOOOOOOOOOOOOOOOO" + value);
					startService(intent);
				}
				Intent i = new Intent( menuView.this, Plane.class);	
				startActivity(i);
//				startService(intent);
				
				
				
				
				
				
				
			}
		});
        

        
        // On recupere le bouton "param" et on lui associe une action
        button_param = (Button)this.findViewById(R.id.Button_option);
        button_param.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			//	Parameters.playTinySound();
				Intent i = new Intent(menuView.this, optionView.class);
				startActivityForResult(i, SEND_M_REQUEST); 	
			}
		});
        
        // On recupere le bouton "rules" et on lui associe une action
        button_rules = (Button)this.findViewById(R.id.Button_rule);
        button_rules.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			//	Parameters.playTinySound();
				Intent i = new Intent( menuView.this, ruleView.class);				
				startActivity(i);
			}
		});
        
        // On recupere le bouton "about" et on lui associe une action
        button_about = (Button)this.findViewById(R.id.Button_about);
        button_about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			//	Parameters.playTinySound();
				Intent i = new Intent( menuView.this, aboutView.class);				
				startActivity(i);
			}
		});
         
        // On recupere le bouton "exit" et on lui associe une action
        button_exit = (Button)this.findViewById(R.id.Button_exit);
        button_exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				if (Parameters.isSoundActivate()) {
//					Parameters.playTinySound();
//					Parameters.stopSoundBackground();
//				}
				menuView.this.finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
        

	}
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) { 
	    if (requestCode == SEND_M_REQUEST) { 
	        if (resultCode == Activity.RESULT_OK) { 
	            Bundle extras = data.getExtras();
	            System.out.println(extras.getBoolean("keyName"));
	            if (extras != null) { 
	                value = extras.getBoolean("keyName"); 
	            } 
	        } 
	    } 
	}
}
