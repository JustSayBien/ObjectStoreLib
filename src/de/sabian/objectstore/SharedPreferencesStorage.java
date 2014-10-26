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

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesStorage implements IJsonStorage {

	private SharedPreferences mPreferences;

	public SharedPreferencesStorage(Context context, String name) {
		mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	@Override
	public void storeJson(String identifier, String json) {
		mPreferences.edit().putString(identifier, json).commit();
	}

	@Override
	public String getJson(String identifier) {
		return mPreferences.getString(identifier, null);
	}

	@Override
	public boolean remove(String identifier) {
		boolean contained = contains(identifier);
		mPreferences.edit().remove(identifier).commit();
		return contained;
	}

	@Override
	public boolean contains(String identifier) {
		return mPreferences.contains(identifier);
	}

}
