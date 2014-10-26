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
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ObjectStoreRaw {

	private Gson mGson;
	private IJsonStorage mJsonStorage;
	private boolean mOverwrite;

	public ObjectStoreRaw(Context context) {
		this(context, new InternalFilesystemStorage(context));
	}

	public ObjectStoreRaw(Context context, IJsonStorage storage) {
		mGson = new Gson();
		mJsonStorage = storage;
		mOverwrite = true;
	}

	public void setOverwriteEnabled(boolean enabled) {
		mOverwrite = enabled;
	}

	public boolean contains(String identifier) {
		return mJsonStorage.contains(identifier);
	}

	public boolean remove(String identifier) throws IOException {
		if (contains(identifier)) {
			return mJsonStorage.remove(identifier);
		} else {
			return false;
		}
	}

	public <T> boolean store(String identifier, T object) throws IOException {
		return storeIntern(identifier, object, mOverwrite);
	}

	public <T> T get(String identifier, Class<T> clazz)
			throws JsonSyntaxException, IOException {
		if (contains(identifier)) {
			return mGson.fromJson(mJsonStorage.getJson(identifier), clazz);
		} else
			return null;
	}

	public <T> List<T> getList(String identifier, Class<T> classOfElements)
			throws IOException {
		List<T> list = new ArrayList<T>();
		fillCollection(identifier, classOfElements, list);
		return list;
	}

	public <T> boolean storeList(String identifier, List<T> objects)
			throws IOException {
		return storeCollectionIntern(identifier, objects, mOverwrite);
	}

	public <T> Set<T> getSet(String identifier, Class<T> classOfElements)
			throws IOException {
		Set<T> objects = new HashSet<T>();
		fillCollection(identifier, classOfElements, objects);
		return objects;
	}

	public <T> boolean storeSet(String identifier, Set<T> objects)
			throws IOException {
		return storeCollectionIntern(identifier, objects, mOverwrite);
	}

	public <T, X> Map<T, X> getMap(String identifier, Class<T> classOfKeys,
			Class<X> classOfValues) throws IOException {
		Map<T, X> map = new HashMap<T, X>();
		fillMap(identifier, classOfKeys, classOfValues, map);
		return map;
	}

	public <T, X> boolean storeMap(String identifier, Map<T, X> mapEntries)
			throws IOException {
		return storeMapIntern(identifier, mapEntries, mOverwrite);
	}

	public <T> boolean fillCollection(String identifier, Class<T> clazz,
			Collection<T> collection) throws IOException {
		if (contains(identifier)) {
			JsonReader jReader = new JsonReader(new StringReader(
					mJsonStorage.getJson(identifier)));
			jReader.beginArray();
			while (jReader.hasNext()) {
				T object = mGson.fromJson(jReader, clazz);
				collection.add(object);
			}
			jReader.endArray();
			jReader.close();
			return true;
		} else
			return false;
	}

	public <T, X> boolean fillMap(String identifier, Class<T> classOfKeys,
			Class<X> classOfValues, Map<T, X> map) throws IOException {
		if (contains(identifier)) {
			JsonReader jReader = new JsonReader(new StringReader(
					mJsonStorage.getJson(identifier)));
			jReader.beginArray();
			while (jReader.hasNext()) {
				jReader.beginObject();
				String name = jReader.nextName();
				if (!name.equals("key"))
					throw new IOException("expected name key but found: "
							+ name);
				T key = mGson.fromJson(jReader, classOfKeys);
				name = jReader.nextName();
				if (!name.equals("value"))
					throw new IOException("expected name value but found: "
							+ name);
				X value = mGson.fromJson(jReader, classOfValues);
				jReader.endObject();
				map.put(key, value);
			}
			jReader.endArray();
			jReader.close();
			return true;
		} else
			return false;
	}

	private <T> boolean storeIntern(String identifier, T object,
			boolean overwrite) throws IOException {
		if (overwrite || !contains(identifier)) {
			mJsonStorage.storeJson(identifier,
					mGson.toJson(object, object.getClass()));
			return true;
		} else {
			return false;
		}
	}

	private <T> boolean storeCollectionIntern(String identifier,
			Collection<T> objects, boolean overwrite) throws IOException {
		// currently the standard store method is used
		return storeIntern(identifier, objects, overwrite);
	}

	private <T, X> boolean storeMapIntern(String identifier,
			Map<T, X> mapEntries, boolean overwrite) throws IOException {
		if (overwrite || !contains(identifier)) {
			StringWriter sWriter = new StringWriter();
			JsonWriter jWriter = new JsonWriter(sWriter);
			jWriter.beginArray();
			for (Entry<T, X> entry : mapEntries.entrySet()) {
				jWriter.beginObject();
				jWriter.name("key");
				mGson.toJson(entry.getKey(), entry.getKey().getClass(), jWriter);
				jWriter.name("value");
				mGson.toJson(entry.getValue(), entry.getValue().getClass(),
						jWriter);
				jWriter.endObject();
			}
			jWriter.endArray();
			jWriter.flush();
			jWriter.close();
			mJsonStorage.storeJson(identifier, sWriter.getBuffer().toString());
			return true;
		} else
			return false;
	}

	public AsyncStoreExecution async() {
		return new AsyncStoreExecution(this);
	}

}
