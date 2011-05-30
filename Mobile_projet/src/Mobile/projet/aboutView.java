package Mobile.projet;

//Lingxiao
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class aboutView extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        // On initialyse le texte suivant si il doit etre en anglais ou en francais
        TextView t = (TextView) findViewById(R.id.text_about);
        //text messages display in view "About"
	    t.setText(
	         Html.fromHtml(
	            	" " +
	                "<br><br>" +
	                "This android game is developed by " +
	                "<br><br>" +
					"WANG Lingxiao" +
	                "<br><br>" +
	                "Date : Novermber 2010" +
	                "<br><br>" +
	                "Version : 1.0" +
	                "<br><br>" +
	                "Android : 1.5" +
	                " "));
        	this.setTitle("About");
        
        Button button_return = (Button)this.findViewById(R.id.button_return_about);

        button_return.setOnClickListener(new OnClickListener()
		{ 
			public void onClick(View v) {

				aboutView.this.finish();
			} 
		});
	}
}
