package com.example.testsyncprovider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SyncService extends Service
{
	private static final String TAG = "mmz";
	
	private static SyncAdapter sSyncAdapter = null;
	private static final Object sSyncAdapterLock = new Object();
	
	public SyncService()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		
		synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "SyncService onBind");
		return sSyncAdapter.getSyncAdapterBinder();
	}

}
