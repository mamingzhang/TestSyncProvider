package com.example.testsyncprovider;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity
{
	private static final String TAG = "mmz";
	
	public static final String AUTHORITY = "com.example.testsyncprovider.provider";

	public static final String ACCOUNT_TYPE = "com.example.mmz";

	private Account mAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final AccountManager accountManager = AccountManager.get(this);
		Account[] accounts = accountManager.getAccountsByType("com.example.mmz");
		if(accounts != null)
		{
			for (Account account : accounts)
			{
				Log.v(TAG, "account : "+account.name);
			}
		}
		findViewById(R.id.add_user).setOnClickListener(
				new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						accountManager.addAccount("com.example.mmz", null, null, null, MainActivity.this, null, null);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
