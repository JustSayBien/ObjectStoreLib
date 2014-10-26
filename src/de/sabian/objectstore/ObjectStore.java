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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.google.gson.JsonSyntaxException;

public class ObjectStore extends ObjectStoreRaw {

	public ObjectStore(Context context) {
		super(context);
	}

	public ObjectStore(Context context, IJsonStorage storage) {
		super(context, storage);
	}

	public boolean remove(String identifier) {
		try {
			return super.remove(identifier);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public <T> boolean store(String identifier, T object) {
		try {
			return super.store(identifier, object);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public <T> T get(String identifier, Class<T> clazz) {
		try {
			return super.get(identifier, clazz);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public <T> List<T> getList(String identifier, Class<T> classOfElements) {
		try {
			return super.getList(identifier, classOfElements);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public <T> boolean storeList(String identifier, List<T> objects) {
		try {
			return super.storeList(identifier, objects);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public <T> Set<T> getSet(String identifier, Class<T> classOfElements) {
		try {
			return super.getSet(identifier, classOfElements);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public <T> boolean storeSet(String identifier, Set<T> objects) {
		try {
			return super.storeSet(identifier, objects);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public <T, X> Map<T, X> getMap(String identifier, Class<T> classOfKeys,
			Class<X> classOfValues) {
		try {
			return super.getMap(identifier, classOfKeys, classOfValues);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public <T, X> boolean storeMap(String identifier, Map<T, X> mapEntries) {
		try {
			return super.storeMap(identifier, mapEntries);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public <T> boolean fillCollection(String identifier, Class<T> clazz,
			Collection<T> collection) {
		try {
			return super.fillCollection(identifier, clazz, collection);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public <T, X> boolean fillMap(String identifier, Class<T> classOfKeys,
			Class<X> classOfValues, Map<T, X> map) {
		try {
			return super.fillMap(identifier, classOfKeys, classOfValues, map);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

}
