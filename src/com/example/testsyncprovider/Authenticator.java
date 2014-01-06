package com.example.testsyncprovider;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Authenticator extends AbstractAccountAuthenticator
{
	private static final String TAG = "mmz";
	private Context mContext;
	
	public Authenticator(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options)
			throws NetworkErrorException
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "Authenticator addAccount");
		Bundle result = new Bundle();
		Intent i = new Intent(mContext, LoginActivity.class);
		i.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		result.putParcelable(AccountManager.KEY_INTENT, i);
		return result;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "Authenticator confirmCredentials");
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "Authenticator editProperties");
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "Authenticator getAuthToken");
		return null;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "Authenticator getAuthTokenLabel");
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "Authenticator hasFeatures");
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "Authenticator updateCredentials");
		return null;
	}

}
