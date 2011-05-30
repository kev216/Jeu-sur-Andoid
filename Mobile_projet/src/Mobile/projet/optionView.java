package Mobile.projet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class optionView extends Activity {

	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option);
		
		
		final CheckBox checkBox = (CheckBox) findViewById(R.id.CheckBox01);
		checkBox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {				
					Intent ent = new Intent(); 
					Bundle bundle = new Bundle(); 
					bundle.putBoolean("keyName", true); 
					ent.putExtras(bundle); 
					setResult(RESULT_OK, ent ); 
					finish();
				}
			}
		});

		

	}

}