package com.example.testsyncprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class StubProvider extends ContentProvider
{

	public StubProvider()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onCreate()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri)
	{
		// TODO Auto-generated method stub
		return new String();
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}