package in.phish.stream;

import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    public static final String ACTION_PLAY  = "PLAY";
    public static final String ACTION_PAUSE = "PAUSE";
    public static final String ACTION_STOP  = "STOP";
    public static final String ACTION_NEXT  = "NEXT";
    public static final String ACTION_PREV  = "PREV";

    MediaPlayer mediaPlayer = null;
    WifiLock wifiLock = null;

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder binder = new LocalBinder();
    
    public MusicPlayerService() {}
    
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    
    public void initMediaPlayer() {
        if (mediaPlayer == null) {    
        	mediaPlayer = new MediaPlayer();
        }
        else if (mediaPlayer.isPlaying()) { 
        	mediaPlayer.stop();
        	mediaPlayer.reset();
        }
        
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        wifiLock = ((WifiManager)getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

        return;
    }   

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
        	if (mediaPlayer.isPlaying())
        		mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (wifiLock != null && wifiLock.isHeld()) 
            wifiLock.release();
        mediaPlayer = null;
        return;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {      
        
        Log.e("PLAYER", "LOADING: " + intent.getStringExtra("URL"));
        if (intent.getAction().equals(ACTION_STOP)) {
        	if (mediaPlayer.isPlaying())
        		mediaPlayer.stop();
        	mediaPlayer.release();
        	mediaPlayer = null;
        	if (wifiLock != null && wifiLock.isHeld())
        		wifiLock.release();
        	stopSelf();
        	return START_STICKY;
        }
        if (intent.getAction().equals(ACTION_PLAY)) {
        	initMediaPlayer();
            try {
                mediaPlayer.setDataSource(intent.getStringExtra("URL"));
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            wifiLock.acquire();
            mediaPlayer.prepareAsync(); // prepare async to not block main thread
        }
        return START_STICKY;
    }
    
    @Override 
    public void onCompletion(MediaPlayer mp) {
        // TODO Next track?
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        wifiLock.release();
        return;
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        wifiLock.release();
        return true;
    }

    /** Called when MediaPlayer is ready */
    @Override
    public void onPrepared(MediaPlayer player) {
        mediaPlayer.start();
        return;
    }


}
