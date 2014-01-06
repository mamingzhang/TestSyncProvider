package com.example.testsyncprovider;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Entity;
import android.util.Log;

public class SyncAdapter extends AbstractThreadedSyncAdapter
{
	private static final String TAG = "mmz";
	private Context mContext = null;

	private static String UsernameColumn = ContactsContract.RawContacts.SYNC1;
	private static String PhotoTimestampColumn = ContactsContract.RawContacts.SYNC2;

	private static ContentResolver mContentResolver = null;

	public SyncAdapter(Context context, boolean autoInitialize)
	{
		super(context, autoInitialize);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public SyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs)
	{
		super(context, autoInitialize, allowParallelSyncs);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "SyncAdapter onPerformSync");
		Log.v(TAG, "SyncAdapter account.name £º "+account.name);
		Log.v(TAG, "SyncAdapter account.type : "+account.type);
		
		HashMap<String, SyncEntry> localContacts = new HashMap<String, SyncEntry>();
		mContentResolver = mContext.getContentResolver();

		// Load the local contacts
		Uri rawContactUri = RawContacts.CONTENT_URI.buildUpon()
				.appendQueryParameter(RawContacts.ACCOUNT_NAME, account.name)
				.appendQueryParameter(RawContacts.ACCOUNT_TYPE, account.type)
				.build();
		Cursor c1 = mContentResolver.query(rawContactUri, new String[] {
				BaseColumns._ID, UsernameColumn, PhotoTimestampColumn }, null,
				null, null);
		
//		Cursor c1 = mContentResolver.query(RawContacts.CONTENT_URI, new String[] {
//				BaseColumns._ID, UsernameColumn, PhotoTimestampColumn }, null,
//				null, null);
		
		Log.v(TAG, "SyncAdapter c1 : "+c1.getCount());
		if (c1 != null)
		{
			while (c1.moveToNext())
			{
				SyncEntry entry = new SyncEntry();
				entry.raw_id = c1.getLong(c1.getColumnIndex(BaseColumns._ID));
				entry.photo_timestamp = c1.getLong(c1
						.getColumnIndex(PhotoTimestampColumn));
				localContacts.put(c1.getString(1), entry);
				
				Log.v(TAG, "SyncAdapter find raw_id : "+entry.raw_id);
				Log.v(TAG, "SyncAdapter find username : "+c1.getString(1));
			}

			c1.close();
			c1 = null;
		}

		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
		try
		{
			// If we don't have any contacts, create one. Otherwise, set a
			// status message
			if (localContacts.get("mmz") == null)
			{
				Log.v(TAG, "SyncAdapter add account");
				addContact(account, "Ma Mingzhang", "mmz");
			} else
			{
				Log.v(TAG, "SyncAdapter update account");
				if (localContacts.get("mmz").photo_timestamp == null
						|| System.currentTimeMillis() > (localContacts
								.get("mmz").photo_timestamp + 604800000L))
				{
					// You would probably download an image file and just pass
					// the bytes, but this sample doesn't use network so we'll
					// decode and re-compress the icon resource to get the bytes
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					Bitmap icon = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.ic_launcher);
					icon.compress(CompressFormat.PNG, 0, stream);
					updateContactPhoto(operationList,
							localContacts.get("mmz").raw_id,
							stream.toByteArray());
				}
				updateContactStatus(operationList,
						localContacts.get("mmz").raw_id, "hunting wabbits");
			}
			if (operationList.size() > 0)
				mContentResolver.applyBatch(ContactsContract.AUTHORITY,
						operationList);
		} catch (Exception e1)
		{
			// TODO Auto-generated catch block
			Log.v(TAG, "SyncAdapter exception : "+e1);
			e1.printStackTrace();
		}
	}

	private static void addContact(Account account, String name, String username)
	{
		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

		ContentProviderOperation.Builder builder = ContentProviderOperation
				.newInsert(RawContacts.CONTENT_URI);
		builder.withValue(RawContacts.ACCOUNT_NAME, account.name);
		builder.withValue(RawContacts.ACCOUNT_TYPE, account.type);
		builder.withValue(RawContacts.SYNC1, username);
		operationList.add(builder.build());

		builder = ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(
				ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID,
				0);
		builder.withValue(
				ContactsContract.Data.MIMETYPE,
				ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
		builder.withValue(
				ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
				name);
		operationList.add(builder.build());

		builder = ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
		builder.withValue(ContactsContract.Data.MIMETYPE,
				"vnd.android.cursor.item/vnd.org.TestSyncProvider.profile");
		builder.withValue(ContactsContract.Data.DATA1, username);
		builder.withValue(ContactsContract.Data.DATA2,
				"TestSyncProvider Profile");
		builder.withValue(ContactsContract.Data.DATA3, "View profile");
		operationList.add(builder.build());

		try
		{
			mContentResolver.applyBatch(ContactsContract.AUTHORITY,
					operationList);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			Log.v(TAG, "SyncAdapter addContact : "+e);
			e.printStackTrace();
		}
	}

	private static void updateContactPhoto(
			ArrayList<ContentProviderOperation> operationList,
			long rawContactId, byte[] photo)
	{
		ContentProviderOperation.Builder builder = ContentProviderOperation
				.newDelete(ContactsContract.Data.CONTENT_URI);
		builder.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = '"
				+ rawContactId + "' AND " + ContactsContract.Data.MIMETYPE
				+ " = '"
				+ ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
				+ "'", null);
		operationList.add(builder.build());

		try
		{
			if (photo != null)
			{
				builder = ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI);
				builder.withValue(
						ContactsContract.CommonDataKinds.Photo.RAW_CONTACT_ID,
						rawContactId);
				builder.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
				builder.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO,
						photo);
				operationList.add(builder.build());

				builder = ContentProviderOperation
						.newUpdate(ContactsContract.RawContacts.CONTENT_URI);
				builder.withSelection(ContactsContract.RawContacts.CONTACT_ID
						+ " = '" + rawContactId + "'", null);
				builder.withValue(PhotoTimestampColumn,
						String.valueOf(System.currentTimeMillis()));
				operationList.add(builder.build());
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void updateContactStatus(
			ArrayList<ContentProviderOperation> operationList,
			long rawContactId, String status)
	{
		Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI,
				rawContactId);
		Uri entityUri = Uri.withAppendedPath(rawContactUri,
				Entity.CONTENT_DIRECTORY);
		Cursor c = mContentResolver.query(entityUri, new String[] {
				RawContacts.SOURCE_ID, Entity.DATA_ID, Entity.MIMETYPE,
				Entity.DATA1 }, null, null, null);
		try
		{
			while (c.moveToNext())
			{
				if (!c.isNull(1))
				{
					String mimeType = c.getString(2);

					if (mimeType
							.equals("vnd.android.cursor.item/vnd.org.TestSyncProvider.profile"))
					{
						ContentProviderOperation.Builder builder = ContentProviderOperation
								.newInsert(ContactsContract.StatusUpdates.CONTENT_URI);
						builder.withValue(
								ContactsContract.StatusUpdates.DATA_ID,
								c.getLong(1));
						builder.withValue(
								ContactsContract.StatusUpdates.STATUS, status);
						builder.withValue(
								ContactsContract.StatusUpdates.STATUS_RES_PACKAGE,
								"mmz.TestSyncProvider");
						builder.withValue(
								ContactsContract.StatusUpdates.STATUS_LABEL,
								R.string.app_name);
						builder.withValue(
								ContactsContract.StatusUpdates.STATUS_ICON,
								R.drawable.ic_launcher);
						builder.withValue(
								ContactsContract.StatusUpdates.STATUS_TIMESTAMP,
								System.currentTimeMillis());
						operationList.add(builder.build());

						// Only change the text of our custom entry to the
						// status message pre-Honeycomb, as the newer contacts
						// app shows
						// statuses elsewhere
						if (Build.VERSION.SDK_INT < 11)
						{
							builder = ContentProviderOperation
									.newUpdate(ContactsContract.Data.CONTENT_URI);
							builder.withSelection(
									BaseColumns._ID + " = '" + c.getLong(1)
											+ "'", null);
							builder.withValue(ContactsContract.Data.DATA3,
									status);
							operationList.add(builder.build());
						}
					}
				}
			}
		} finally
		{
			c.close();
		}
	}

	private static class SyncEntry
	{
		public Long raw_id = 0L;
		public Long photo_timestamp = null;
	}
}
