package Mobile.projet;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	private AudioManager mAudioManager;
	private Context mContext;

	public SoundManager() {

	}

	public void initSounds(Context theContext) {
		mContext = theContext;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
	}

	public void addSound(int Index, int SoundID) {
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, Index));

	}

	public void playSound(int index) {

		int streamNowVolume = mAudioManager
				.getStreamVolume((AudioManager.STREAM_MUSIC));
		int streamMaxVolume = mAudioManager
				.getStreamMaxVolume((AudioManager.STREAM_MUSIC));
		mSoundPool.play(mSoundPoolMap.get(index), streamNowVolume,
				streamMaxVolume, 1, 0, 1f);
	}

	public void playLoopedSound(int index, int uLoop) {

		int streamNowVolume = mAudioManager
				.getStreamVolume((AudioManager.STREAM_MUSIC));
		int streamMaxVolume = mAudioManager
				.getStreamMaxVolume((AudioManager.STREAM_MUSIC));
		mSoundPool.play(mSoundPoolMap.get(index), streamNowVolume,
				streamNowVolume, 1, uLoop, 1f);
	}

	public void stopMusic(int index) {
		mSoundPool.stop(index);

	}
}