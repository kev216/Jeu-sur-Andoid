package Mobile.projet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

//main View to display all movement of plane and bullets
// we draw them on a canvas with a new thread
public class mainView extends SurfaceView implements SurfaceHolder.Callback{
	
	private ManThread thread;//main thread to draw a canvas
	private Plane act;//father view;
	
	public int mMode;//game mode
    private int pMode;//plane mode
    
    //arrays to generate bullets
    private int[] data;
    private int[] randData;
    private int randEnd;
    private int randCurr;
    //sensor x y z directions modify values
    private float x=0;
    private float y=0;
    private float z=0;
    //bullets proof time and quantity
    private long proofTime;
    private int NBproof=0;
    private int usedProof=0;
    private boolean proof = false;
    
    public boolean isStarted = true;;
    
    private Context ctx;
    private Vibrator vbr;
    
    private SoundManager mSoundManager;
    
    


	public mainView(Context context, Vibrator vibrator) {
		super(context);
		/*
		 * surfaceView
		 */
		ctx = context;
		vbr = vibrator;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		//mSoundManager to play music
		mSoundManager = new SoundManager();
	    mSoundManager.initSounds(getContext());
	    mSoundManager.addSound(1, R.raw.bobm);
	    mSoundManager.addSound(2, R.raw.protect);
	    mSoundManager.addSound(3, R.raw.music2);
	    
		thread = new ManThread(holder,context,new Handler(),vbr);
		setFocusable(true); 
		
	}

	
	
	// Game main thread
	class ManThread extends Thread {
		private SurfaceHolder mSurfaceHolder;
		private Context mContext;
		private Handler mHandler;
		private Vibrator mVibrator;

		private Bitmap mBackgroundImage;
		private Bitmap pBitmap;//bitmap
		private Drawable p;// plane
		private Drawable bomb1, bomb2, bomb3, bomb4, bomb5, bomb6, bomb7,
				bomb8;//boom images
		private Drawable paodan;// 

		private int mWidth, mHeight;// 
	
	
		private int	mCanvasWidth=480;
		private int	mCanvasHeight=320;
		
		
		
		private Paint paint, paint2;//two different size paint
		private int px;// plane x position
		private int py;// plane y position

		//states
		public static final int STATE_MENU = 1;
		public static final int STATE_OVER = 2;
		public static final int STATE_LOAD = 3;
		public static final int STATE_RUNNING = 5;
		public static final int STATE_PAUSE = 6;

		// key pressed values
		public static final int DIRECTION_UP = 1;
		public static final int DIRECTION_DOWN = 2;
		public static final int DIRECTION_RIGHT = 4;
		public static final int DIRECTION_LEFT = 8;
		public static final int STATE_RESUME = 16;
		public static final int STATE_RESTART = 32;

		public static final int STEP = 10;// 
		public static final long time_fps = 1000 / 15;// 
		private long startTime;// game start time
		private long endTime;// game over time
		private long lastTime_fast;//
		private long lastTime_slow;// 
		private long pauseTime;// 

		public int key_down;//key pressed value

		private int p_hurt;//  p_hurt>0

		private int offset;// 

		private int k1, k2;//  mark in bullets array
		private int end1, head1, end2;//  mark in bullets array
		
		
		
		
		// ManThread
		public ManThread(SurfaceHolder sh, Context context, Handler ha,Vibrator vbr) {
			mSurfaceHolder = sh;
			mContext = context;
			mHandler = ha;
			mVibrator = vbr;

			
			mMode = STATE_LOAD;
			pMode = 0;

			p_hurt = 0;//  plane not shooted
			pauseTime = -1;
			
			

		}
		

		@Override
		public void run() {
			long t1, t2;
		
			t1 = System.currentTimeMillis();
			while (mMode != STATE_OVER)
			{
				t2 = System.currentTimeMillis(); 
				if(t2-proofTime>3000){
					
					proof = false;
					p = ctx.getResources().getDrawable(R.drawable.plane);
				}
				
				NBproof = (int) (((t2-startTime)/10000)-usedProof);
				
				if (t2 - t1 < time_fps) {
					try {
						sleep(time_fps + t1 - t2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					t1 += time_fps;

					State_Change();

					if (pauseTime != -1)
						continue;

					Update_Fps();

					Canvas c = null;
					try {
						c = mSurfaceHolder.lockCanvas(null);
						synchronized (mSurfaceHolder) {
							doDraw(c);// surface
						}
					} finally {
						if (c != null) {
							mSurfaceHolder.unlockCanvasAndPost(c);
						}
					}

				}
			}


		}
	    // drawing method, it depends different states
private void doDraw(Canvas c) {
			
	    	int i;
			switch (mMode)
			{
				case STATE_RUNNING://  game running state
				{
					c.drawBitmap(mBackgroundImage,0,0,null);//draw background
					
					for (i=0;i<=end1;i+=3)
					{
						//draw bullets
						paodan.setBounds(data[k1+i]/10, data[k1+i+1]/10, data[k1+i]/10+8, data[k1+i+1]/10+8);
						paodan.draw(c);
					}
					
					if (pMode<1)
					{
						//draw plane
						p.setBounds(px, py, px+mWidth, py+mHeight);
						p.draw(c);
					}
					
					//draw bomb images
					else if (pMode==1)
					{
						 bomb1.setBounds(px-20, py-10, px+60, py+70);
						 bomb1.draw(c);
					}
					else if (pMode==2)
					{
						 bomb2.setBounds(px-20, py-10, px+60, py+70);
						 bomb2.draw(c);
					}
					else if (pMode==3)
					{
						 bomb3.setBounds(px-20, py-10, px+60, py+70);
						 bomb3.draw(c);
					}
					else if (pMode==4)
					{
						 bomb4.setBounds(px-20, py-10, px+60, py+70);
						 bomb4.draw(c);
					}
					else if (pMode==5)
					{
						 bomb5.setBounds(px-20, py-10, px+60, py+70);
						 bomb5.draw(c);
					}
					else if (pMode==6)
					{
						 bomb6.setBounds(px-20, py-10, px+60, py+70);
						 bomb6.draw(c);
					}
					else if (pMode==7)
					{
						 bomb7.setBounds(px-20, py-10, px+60, py+70);
						 bomb7.draw(c);
					}
					else if (pMode==1)
					{
						 bomb8.setBounds(px-20, py-10, px+60, py+70);
						 bomb8.draw(c);
					}
					
				
					c.drawText("Life Time"+((endTime-startTime)/1000)+"."+(((endTime-startTime)%1000)/100), 5, 20, paint2);
					//draw bullet proof number
					c.drawText("BulletProof : "+NBproof, 5, 40, paint2);
//					c.drawText("UsedProof : "+usedProof, 5, 80, paint2);
					break;
				}
				case STATE_MENU:
				{
					c.drawBitmap(mBackgroundImage,0,0,null);
					if (pMode==1)
					{
						// we don't detail this part in out project
						//
						// 
//						c.drawText("welcome", 50, 100, paint);
						}
					else
					{
						
						//draw survive time and final score
		            	c.drawText("Game Over!", mCanvasWidth/3, mCanvasHeight/3, paint2);
		            	i=(int ) ((endTime-startTime)/1000);
		            	c.drawText("Life survive time : "+((endTime-startTime)/1000)+"."+(((endTime-startTime)%1000)/10), mCanvasWidth/3, mCanvasHeight/3+30, paint2);
		            	if (i<10)
		            	{
		            		c.drawText("Work Harder!",mCanvasWidth/3, mCanvasHeight/3+60,paint2);
		            	}
		            	else if (i<20)
		            	{
		            		c.drawText("Just Beginner!",mCanvasWidth/3, mCanvasHeight/3+60,paint2);
		            	}
		            	else if (i<40)
		            	{
		            		c.drawText("Not Bad!",mCanvasWidth/3, mCanvasHeight/3+60,paint2);
		            	}
		            	else if (i<60)
		            	{
		            		c.drawText("Nice Play!",mCanvasWidth/3, mCanvasHeight/3+60,paint2);
		            	}
		            	else if (i<100)
		            	{
		            		c.drawText("Great!",mCanvasWidth/3, mCanvasHeight/3+60,paint2);
		            	}
		            	else 
		            	{
		            		c.drawText("Amazing",mCanvasWidth/3, mCanvasHeight/3+60,paint2);
		            	}
					}
					break;
				}
				case STATE_PAUSE:
				{
					// game pause non realise
					c.drawText("pause", mCanvasWidth/3, mCanvasHeight/3+30, paint);
					break;
				}
			}
		}

		// update mMode
		private void Update_Fps() {

			switch (mMode) {
			case STATE_LOAD: {
				if (pMode == 1) {
					load_game();
				} else if (pMode == 2) {
					load_gate();
				}
				break;
			}
			case STATE_MENU: {
				// do nothing in out project
				break;
			}
			case STATE_RUNNING: {
				pauseTime = -1;
				new_paodan();
				move();
				is_pengzhuang();
				break;
			}
			case STATE_PAUSE: {
				thead_pause();
				pauseTime = System.currentTimeMillis();//
				break;
			}
			}

		}

		private void thead_pause() {
			mMode=STATE_PAUSE;
		}

		// check whether bullet hit plane
		// algo exist
		private void is_pengzhuang() {
			int i,x,y,k;
			if(!proof){
				for (i=0;i<=end1;)
				{
					x=data[k1+i]/10+4;
					i++;
					y=data[k1+i]/10+4;
					i++;
					i++;
					
					if (x<px+mWidth+10&&x>px-10&&y<py+mHeight+10&&y>py-10)
					{
						x-=px-10;
						y-=py-10;
						k=y*(mWidth+20)+x;
						if (data[k+offset]<4)
						{
							//set p_hurt=1 and vibrating
							p_hurt=1;
							mSoundManager.playSound(1);//play dead music
							isStarted = false;// set isStarted flag
							mVibrator =  (Vibrator)mContext.getSystemService(mContext.VIBRATOR_SERVICE);
							long[] pattern = {100,10,100,100};
							mVibrator.vibrate(pattern,-1);
							break;
						}
					}
				}
			}
		}
		
		
		// move the plane and bullets
		private void move() {
			
			/*
			 * 
			 */
			if ((key_down & DIRECTION_UP)>0) py-=STEP;
			if ((key_down & DIRECTION_DOWN)>0) py+=STEP;
			if ((key_down & DIRECTION_RIGHT)>0) px+=STEP;
			if ((key_down & DIRECTION_LEFT)>0) px-=STEP;
			
			px+=y/6*STEP;//sensor y direction control plane direction left and right
			py+=x/6*STEP;//sensor x direction control plane direction right and left
			
			if (px>mCanvasWidth-mWidth) px=mCanvasWidth-mWidth;
			if (px<0) px=0;
			if (py>mCanvasHeight-mHeight) py=mCanvasHeight-mHeight*2;
			if (py<0) py=0;
			
			
			/*
			 * 
			 */
			int i,k,x,y;
			double f;
			for (i=0;i<=end1;)
			{
				x=data[k1+i];//
				i++;
				y=data[k1+i];//
				i++;
				f=(double)(data[k1+i]);
				f=f*Math.PI/180;//
				if ((data[k1+i]&(1<<10))>0)
					k=(int) (4+(endTime-startTime)/2000);
				else k=(int) (2+(endTime-startTime)/3000);
				k*=10;//
				i++;
				
				x+=k*Math.sin(f);
				y-=k*Math.cos(f);
				if (x>-100&&x<mCanvasWidth*10&&y>-100&&y<mCanvasHeight*10)
				{
					//
					end2++;
					data[k2+end2]=x;
					end2++;
					data[k2+end2]=y;
					end2++;
					data[k2+end2]=data[k1+i-1];
				}
			}
			
			/*
			 * 
			 */
			end1=end2;
			end2=-1;
			k1^=(1<<12)+(1<<13);
			k2^=(1<<12)+(1<<13);
		}
		//Generate 2 types bullets each 2 seconds and 5 seconds from 4 directions
		private void new_paodan() {
			long t;
			t=System.currentTimeMillis();
			endTime=t;
			if (t-lastTime_slow>=2000)
			{
				lastTime_slow+=2000;//
				int i,k,j;
				k=(int) (2+(t-startTime)/2000);//
				if(k>10)
					k=10;
				for (i=0;i<k;i++)
				{
		
					j=getrand();
					if (j<250)//  
					{
						//
						j=getrand()%320;
						end1++;
						data[k1+end1]=j;
						data[k1+end1]*=10;
						end1++;
						data[k1+end1]=0-8;
						data[k1+end1]*=10;
						j=getrand()%180+90;
						end1++;
						data[k1+end1]=j;
					}
					else if (j<500)//
					{
						//
						j=getrand()%320;
						end1++;
						data[k1+end1]=j;
						data[k1+end1]*=10;
						end1++;
						data[k1+end1]=mCanvasHeight;
						data[k1+end1]*=10;
						j=getrand()%180+270;
						end1++;
						data[k1+end1]=j;
					}
					else if (j<750)
					{
						//
						j=getrand()%480;
						end1++;
						data[k1+end1]=0-8;
						data[k1+end1]*=10;
						end1++;
						data[k1+end1]=j;
						data[k1+end1]*=10;
						j=getrand()%180+0;
						end1++;
						data[k1+end1]=j;
					}
					else if (j<1000) 
					{
						
						j=getrand()%480;
						end1++;
						data[k1+end1]=mCanvasWidth;
						data[k1+end1]*=10;
						end1++;
						data[k1+end1]=j;
						data[k1+end1]*=10;
						j=getrand()%180+180;
						end1++;
						data[k1+end1]=j;
					}
				}
			}
			if (t-lastTime_fast>=5000)
			{
				lastTime_fast+=5000;
				int i,k,j;
				k=(int) (5+(t-startTime)/1500);
				if(k>10)
					k=10;
				for (i=0;i<k;i++)
				{
		
					j=getrand();
					if (j<250)
					{
						
						j=getrand()%320;
						end1++;
						data[k1+end1]=j;
						data[k1+end1]*=10;
						end1++;
						data[k1+end1]=0-mHeight;
						data[k1+end1]*=10;
						j=getrand()%180+90;
						j|=(1<<10);
						end1++;
						data[k1+end1]=j;
					}
					else if (j<500)
					{
						
						j=getrand()%320;
						end1++;
						data[k1+end1]=j;
						data[k1+end1]*=10;
						end1++;
						data[k1+end1]=mCanvasHeight;
						data[k1+end1]*=10;
						j=getrand()%180+270;
						j|=(1<<10);
						end1++;
						data[k1+end1]=j;
					}
					else if (j<750)
					{
						
						j=getrand()%480;
						end1++;
						data[k1+end1]=0-mWidth;
						data[k1+end1]*=10;
						end1++;
						data[k1+end1]=j;
						data[k1+end1]*=10;
						j=getrand()%180+0;
						j|=(1<<10);
						end1++;
						data[k1+end1]=j;
					}
					else
					{
						
						j=getrand()%480;
						end1++;
						data[k1+end1]=mCanvasWidth;
						data[k1+end1]*=10;
						end1++;
						data[k1+end1]=j;
						data[k1+end1]*=10;
						j=getrand()%180+180;
						j|=(1<<10);
						end1++;
						data[k1+end1]=j;
					}
				}
			}
		}
		// Initial some parameters values
		private void load_gate() {
			
			
			px=(mCanvasWidth-mWidth)/2;
			py=(mCanvasHeight-mHeight)/2;
			startTime=System.currentTimeMillis();
			lastTime_fast=startTime-5000;
			lastTime_slow=startTime-2000;
			
			NBproof=0;
			usedProof=0;
			isStarted = true;
			/*
			 * 
			 */
			k1=k2=0;
			k2|=(1<<12)+(1<<13);
			end1=head1=-1;
			
			p_hurt=0;
			pauseTime=-1;
		}
		private void load_game() {
			
			 
			data=new int[30000];
			
			
			Resources res = mContext.getResources();
            p = res.getDrawable(R.drawable.plane);  
            bomb1=res.getDrawable(Mobile.projet.R.drawable.bomb01);
            bomb2=res.getDrawable(Mobile.projet.R.drawable.bomb02);
            bomb3=res.getDrawable(Mobile.projet.R.drawable.bomb03);
            bomb4=res.getDrawable(Mobile.projet.R.drawable.bomb04);
            bomb5=res.getDrawable(Mobile.projet.R.drawable.bomb05);
            bomb6=res.getDrawable(Mobile.projet.R.drawable.bomb06);
            bomb7=res.getDrawable(Mobile.projet.R.drawable.bomb07);
            bomb8=res.getDrawable(Mobile.projet.R.drawable.bomb08);
            paodan=res.getDrawable(Mobile.projet.R.drawable.dd);
            
            mBackgroundImage = BitmapFactory.decodeResource(res, R.drawable.bg_game);
            pBitmap=BitmapFactory.decodeResource(res, R.drawable.plane); 
            
            
            mWidth = p.getIntrinsicWidth();
            mHeight = p.getIntrinsicHeight();
            
            /*
             * 
             */
            paint = new Paint();
            paint.setAntiAlias(true); 
            paint.setARGB(255, 255, 0, 0);
            paint.setTextSize(10);
            
            paint2 = new Paint();
            paint2.setAntiAlias(true);
            paint2.setARGB(255,255, 0, 0); 
            paint2.setTextSize(18);
            
            /*
             * 
             */
            offset=0;
            pBitmap.getPixels(data, offset, mWidth, 0, 0, mWidth, mHeight);
            pauseTime=-1;
            
            /*
             * 
             */
            int i,u,k,j,end1,end2,cur;      
            offset=25000;
            end2=mWidth*mHeight;
            end1=end2+1;
            for (i=0;i<mWidth+20;i++) for (u=0;u<mHeight+20;u++)
            {
            	k=u*(20+mWidth)+i;
            	data[offset+k]=-1;
            }
            
            /*
             * 
             */
            for (i=0;i<=mWidth;i++) for (u=0;u<=mHeight;u++)
            {
            	k=(u+10)*(mWidth+20)+i+10;
            	if (data[u*mWidth+i]!=0)
            	{
            		data[offset+k]=0;
            		end2++;
            		data[end2]=k;
            	}
            }
            
            /*
             * 
             */
            j=0;cur=end1;
            while (end2>end1)
            {
            	j++;
            	end1=end2;
            	while (cur<=end1)
            	{
            		k=data[cur]+1;
            		if (k<(mWidth+20)*(mHeight+20)&&data[offset+k]<0)
            		{
            			end2++;
            			data[end2]=k;
            			data[offset+k]=j;
            		}
            		k=data[cur]+mWidth+20;
            		if (k<(mWidth+20)*(mHeight+20)&&data[offset+k]<0)
            		{
            			end2++;
            			data[end2]=k;
            			data[offset+k]=j;
            		}
            		k=data[cur]-1;
            		if (k>=0&&data[offset+k]<0)
            		{
            			end2++;
            			data[end2]=k;
            			data[offset+k]=j;
            		}
            		k=data[cur]-mWidth-20;
            		if (k>=0&&data[offset+k]<0)
            		{
            			end2++;
            			data[end2]=k;
            			data[offset+k]=j;
            		}
            		cur++;
            	}
            }
           
            /*
             * 
             */
            randData=new int[1000];
            randEnd=-1; 
            k=(int) (Math.random()*1000);
            while (randEnd<1000-1)
            {
            	randEnd++;
            	k*=691;
            	k+=863;
            	k%=997;
            	randData[randEnd]=k;
            }
            randCurr=0;
            
		}
		
		int getrand()
		{
			randCurr++;
			if (randCurr>=randEnd) randCurr=0;
			return randData[randCurr];
		}

		void State_Change() {
			switch (mMode) {
			case STATE_MENU: {
				if ((key_down & STATE_RESTART) > 0) {
					
					key_down -= STATE_RESTART;

					mMode = STATE_LOAD;
					pMode = 2;
				}
				break;
			}
			case STATE_PAUSE: {
				if ((key_down & STATE_RESUME) > 0) {
					
					key_down -= STATE_RESUME;
					mMode = STATE_RUNNING;
					
					long k = System.currentTimeMillis() - pauseTime;
					startTime += k;
					endTime += k;
					lastTime_fast += k;
					lastTime_slow += k;
				}
				break;
			}
			case STATE_LOAD: {
				if (pMode == 0) {
					
					pMode = 1;
				} else if (pMode == 1) {
					
					mMode = STATE_MENU;
					pMode = 1;
				} else if (pMode == 2) {
					
					mMode = STATE_RUNNING;
					pMode = 0;
				}
				break;
			}
			case STATE_RUNNING: {
				if ((key_down & STATE_RESUME) > 0) {
					// 
					key_down &= 63 - STATE_RESUME;
					mMode = STATE_PAUSE;
				} 
				else 
					if (pMode > 0)
						pMode++;
					else {
					if (p_hurt > 0)
							pMode = 1;
					}
				if (pMode > 8) {
					
					mMode = STATE_MENU;
					pMode = 2;
				}
				break;
			}
			}
		}
			
		
		boolean doKeyDown(int key,KeyEvent msg)
		{
			//key_down=0;
			if (key == KeyEvent.KEYCODE_DPAD_UP) key_down|=DIRECTION_UP;
			if (key == KeyEvent.KEYCODE_DPAD_DOWN) key_down|=DIRECTION_DOWN;
			if (key == KeyEvent.KEYCODE_DPAD_RIGHT) key_down|=DIRECTION_RIGHT;
			if (key == KeyEvent.KEYCODE_DPAD_LEFT) key_down|=DIRECTION_LEFT;
			if (key == KeyEvent.KEYCODE_ENTER) {
				// addProof
				System.out.println("enter typed");
				proofTime = System.currentTimeMillis();
				if(isStarted){
					if(NBproof>0){
						System.out.println("NBproof>0");
						proof=true;
						NBproof--;
						usedProof++;
						thread.p = ctx.getResources().getDrawable(R.drawable.plane_proof);
						mSoundManager.playSound(2);// proof music
					}
				}
				
			}
				return true;
		}
			
		boolean doKeyup(int key,KeyEvent msg)
		{
			if (key == KeyEvent.KEYCODE_DPAD_UP) key_down&=63-DIRECTION_UP;
			if (key == KeyEvent.KEYCODE_DPAD_DOWN) key_down&=63-DIRECTION_DOWN;
			if (key == KeyEvent.KEYCODE_DPAD_RIGHT) key_down&=63-DIRECTION_RIGHT;
			if (key == KeyEvent.KEYCODE_DPAD_LEFT) key_down&=63-DIRECTION_LEFT;
//			if (key == KeyEvent.KEYCODE_DPAD_CENTER) key_down&=63-STATE_RESUME;
			return true;
		}
	}
			

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	public void surfaceCreated(SurfaceHolder holder) {

		{
			thread.start();
		}
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
		try
		{
			thread.join();
		}
		catch (InterruptedException e) {
        }
	}
	
	/*
	 * 
	 */
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return thread.doKeyDown(keyCode, msg);
    }

    @Override
    public boolean onKeyUp(int keyCode,KeyEvent msg)
    {
    	return thread.doKeyup(keyCode,msg);
    }
    
    ManThread get_thread()
    {
    	return this.thread;
    }

    //Get x,y,z direction changes from Sensor
	public void moveTo(float x2, float y2, float z2) {
		// TODO Auto-generated method stub
		this.x = x2;
		this.y = y2;
		this.z = z2;
		
	}
	/**
	 * method not finished
	 * @param x2
	 * @param y2
	 */
	public void dragTo(float x2, float y2) {
		// TODO Auto-generated method stub
		this.x = x2;
		this.y = y2;	
	}
	/**
	 * add proof on plane
	 */
	public void addProof() {
		// TODO Auto-generated method stub
		proofTime = System.currentTimeMillis();
		if(isStarted){
		if(NBproof>0){
			proof=true;
			NBproof--;
			usedProof++;
			thread.p = ctx.getResources().getDrawable(R.drawable.plane_proof);
			mSoundManager.playSound(2);// proof music
		}
		}
	}
}

