/*
 * Copyright (C) 2014 Andreas Sabitzer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.sabian.objectstore;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteStorage implements IJsonStorage {

	private class SQLiteStorageOpenHelper extends SQLiteOpenHelper {
		public static final int DATABASE_VERSION = 1;
		private static final String SQL_CREATE_TABLE = "CREATE TABLE key_value_store (key TEXT PRIMARY KEY, value TEXT)";

		public SQLiteStorageOpenHelper(Context context, String dbFilename) {
			super(context, dbFilename, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_TABLE);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	private Context mContext;
	private String mDbFilename;
	private SQLiteDatabase mDatabase;

	public SQLiteStorage(Context context, String dbFilename) {
		this.mContext = context;
		this.mDbFilename = dbFilename + ".objectstore.sqlite";
	}

	private SQLiteDatabase getDatabase() {
		if (mDatabase != null && mDatabase.isOpen())
			return mDatabase;
		else
			return new SQLiteStorageOpenHelper(mContext, mDbFilename)
					.getWritableDatabase();
	}

	public void closeDatabase() {
		if (mDatabase != null && mDatabase.isOpen())
			mDatabase.close();
	}

	@Override
	public boolean remove(String identifier) throws IOException {
		return getDatabase().delete("key_value_store", "key = ?",
				new String[] { identifier }) == 1;
	}

	@Override
	public boolean contains(String identifier) {
		Cursor c = getDatabase().query("key_value_store",
				new String[] { "key" }, "key = ?", new String[] { identifier },
				null, null, null, "1");
		boolean contained = c.getCount() == 1;
		c.close();
		return contained;
	}

	@Override
	public void storeJson(String identifier, String json) throws IOException {
		ContentValues values = new ContentValues();
		values.put("key", identifier);
		values.put("value", json);
		if (getDatabase().insertWithOnConflict("key_value_store", null, values,
				SQLiteDatabase.CONFLICT_REPLACE) == -1) {
			throw new IOException("Error while writing to database");
		}
	}

	@Override
	public String getJson(String identifier) throws IOException {
		Cursor c = getDatabase().query("key_value_store",
				new String[] { "value" }, "key = ?",
				new String[] { identifier }, null, null, null, "1");
		String value = null;
		if (c.moveToFirst()) {
			value = c.getString(0);
		}
		c.close();
		return value;
	}

}
