package com.example.testsyncprovider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AuthenticatorService extends Service
{
	private static final String TAG = "mmz";
	
	private Authenticator sAuthenticator = null;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "AuthenticatorService onBind");
		IBinder binder = null;
		if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT))
			binder = getAuthenticator().getIBinder();
		return binder;
	}
	
	private Authenticator getAuthenticator() {
		if (sAuthenticator == null)
			sAuthenticator = new Authenticator(this);
		return sAuthenticator;
	}
}
