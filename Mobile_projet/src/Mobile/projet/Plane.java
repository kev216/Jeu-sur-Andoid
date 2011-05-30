package Mobile.projet;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class Plane extends Activity {
    /** Called when the activity is first created. */
	
	boolean init=false;
	
	mainView mv; 
	
	private static final int STATE_RESTART=32;

	SensorManager mSensorManager = null;//SensorManager
	Sensor sensor=null;//Sensor
	Vibrator mVibrator;//Vibrator!
    SensorEventListener listener = new SensorEventListener(){
    	public void onSensorChanged(SensorEvent e){
    		if(!init)
    			return;
    		float x = e.values[SensorManager.DATA_X];//x direction 
    		float y = e.values[SensorManager.DATA_Y];//y direction
    		float z = e.values[SensorManager.DATA_Z];//z direction
    		
    		mv.moveTo(x,y,z);//call method moveTo() to change x,y,z values in "mv".
    		
    	}
    	
    	public void onAccuracyChanged(Sensor s, int accuracy){
    		//do nothing
    	}
    };
 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
		mv = new mainView(this,mVibrator);
		mv.get_thread().key_down|=STATE_RESTART;
		mv.setOnTouchListener(new OnTouchListener() {
		
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int eventaction = event.getAction();
				Log.i("&&&", "onTouchEvent : "+eventaction);
				
				float x = (float)event.getRawX();
				float y = (float)event.getRawY();
				
				float xBegin=0;
				float yBegin=0;
				
				float xdist = 0;
				float ydist = 0;
				
				switch(eventaction){
				case MotionEvent.ACTION_DOWN:{
					xBegin = (int) event.getX(); 
					yBegin = y - v.getTop(); 
					mv.addProof();//method addproof when touch screen
					break;
				}
				case MotionEvent.ACTION_MOVE:{
					xdist=x-xBegin;
					ydist=y-yBegin;
					mv.dragTo(xdist,ydist);//non realise cette methode
					break;
				}
				case MotionEvent.ACTION_UP:{
					break;
				}
				}
				return false;
			}
		});
		//get sensor server de type accelerometer
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//sensor type is Accelerometer!
		
		setContentView(mv);	
   }

//	public void onSensorChanged(int sensor, float[] values) {
//		if (Sensor.TYPE_ACCELEROMETER==sensor){
//			
//		}
//	}
	
    @Override
    /**
     * menu options...containts restart and return
     * pause and resume non realise
     */
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,0,1,"Return");
    	menu.add(0,1,0,"Restart");
//    	menu.add(0,2,0,"Pause");
//    	menu.add(0,3,0,"Resume");
    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    	//option "Quit"
        case 0:
        	finish();
        	android.os.Process.killProcess(android.os.Process.myPid());//杀死当前程序线程,如果只调用finish则是关闭当前activity，而数据则依旧被保存，若不调用finish，则缺少一个activity的过渡，ihm变丑陋
        	break;
        //option "Start"
        case 1:{
        	mv.get_thread().key_down|=STATE_RESTART;
        	mv.isStarted = true;
        	;//start music intent service
        }

    	}
    	
    		
    	return true;
    }
    
    
    
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		register();
	}

	@Override
    protected void onPause()
    {
    	super.onPause();

    	unregister();
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();

    	register();
    }
    
    @Override
    protected void onStop()
    {
    	super.onStop();
    	unregister();
    }
 
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		register();
	}

    /**
     * register a SensorListener onStart(),onResume(), onRestart() methods called
     */
	void register(){
    	mSensorManager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_GAME);
    }
	/**
	 * unregister a SensorListener onStop(),onPause() methods called
	 */
    void unregister(){
    	mSensorManager.unregisterListener(listener);
    }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && !init){
			init=true;
		}
	}
}
