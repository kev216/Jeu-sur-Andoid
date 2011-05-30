package Mobile.projet;


import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ruleView extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);

        // On initialyse le texte suivant si il doit etre en anglais ou en francais
        TextView t = (TextView) findViewById(R.id.text_rule);
        // text messages display in "How to play"
	    t.setText(
	         Html.fromHtml(
	                "Welcome to play this game! " +
	                "The aim of this game is to move the plane to survive longer." +
	                "<br><br>" +
	                "HOW TO CONTROL THE PLANE'S POSITION ?" +
	                "<br><br>" +
	                "You can move the plane with the keyboard buttons :" +
	                "<br>" +
	                "\"Up\" / \"Down\" / \"Left\" / \"Right\"." +
	                "<br>" +
	                "You can also shake the phone." +
	                "<br><br>" +
	                "HOW TO ADD A BULLET PROOF ?" +
	                "<br><br>" +
	                "Every 10 seconds you have a chance to add a bullet proof on the plane" +
	                "It will last 3 seconds." +
	                "<br><br>" +
	                "You can add a bullet proof with the keyboard button \"Enter\"." +
	                "<br>" +
	                "You can also add a bullet proof by touching the screen." +
	                ""));
        	this.setTitle("How to play");
        
        // Bouton "Retour"
        Button button_return = (Button)this.findViewById(R.id.button_return_rule);

        button_return.setOnClickListener(new OnClickListener()
		{ 
			public void onClick(View v) {

				ruleView.this.finish();
			} 
		});
	}
}