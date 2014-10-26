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

import android.os.AsyncTask;

public final class AsyncStoreExecution {

	public static interface StoreCallback<T> {
		public void onStored(String identifier, T object, boolean success);
	}

	public static interface GetCallback<T> {
		public void onGot(String identifier, T object);
	}

	public static interface FillCallback<T> {
		public void onFilled(String identifier, T filledObject, boolean success);
	}

	public static interface RemoveCallback {
		public void onRemoved(String identifier, boolean removed);
	}

	private ObjectStoreRaw mObjectStore;

	protected AsyncStoreExecution(ObjectStoreRaw parentStore) {
		this.mObjectStore = parentStore;
	}

	public void remove(final String identifier, final RemoveCallback callback) {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					return mObjectStore.remove(identifier);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			protected void onPostExecute(Boolean result) {
				if (callback != null) {
					callback.onRemoved(identifier, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public <T> void store(final String identifier, final T object,
			final StoreCallback<T> callback) {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					return mObjectStore.store(identifier, object);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			protected void onPostExecute(Boolean result) {
				if (callback != null) {
					callback.onStored(identifier, object, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public <T> void get(final String identifier, final Class<T> clazz,
			final GetCallback<T> callback) {
		new AsyncTask<Void, Void, T>() {
			@Override
			protected T doInBackground(Void... params) {
				try {
					return mObjectStore.get(identifier, clazz);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			protected void onPostExecute(T result) {
				if (callback != null) {
					callback.onGot(identifier, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public <T> void getList(final String identifier,
			final Class<T> classOfElements, final GetCallback<List<T>> callback) {
		new AsyncTask<Void, Void, List<T>>() {
			@Override
			protected List<T> doInBackground(Void... params) {
				try {
					return mObjectStore.getList(identifier, classOfElements);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			protected void onPostExecute(List<T> result) {
				if (callback != null) {
					callback.onGot(identifier, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public <T> void storeList(final String identifier, final List<T> objects,
			final StoreCallback<List<T>> callback) {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					return mObjectStore.storeList(identifier, objects);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			protected void onPostExecute(Boolean result) {
				if (callback != null) {
					callback.onStored(identifier, objects, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public <T> void getSet(final String identifier,
			final Class<T> classOfElements, final GetCallback<Set<T>> callback) {
		new AsyncTask<Void, Void, Set<T>>() {
			@Override
			protected Set<T> doInBackground(Void... params) {
				try {
					return mObjectStore.getSet(identifier, classOfElements);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			protected void onPostExecute(Set<T> result) {
				if (callback != null) {
					callback.onGot(identifier, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public <T> void storeSet(final String identifier, final Set<T> objects,
			final StoreCallback<Set<T>> callback) {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					return mObjectStore.storeSet(identifier, objects);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			protected void onPostExecute(Boolean result) {
				if (callback != null) {
					callback.onStored(identifier, objects, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public <T, X> void getMap(final String identifier,
			final Class<T> classOfKeys, final Class<X> classOfValues,
			final GetCallback<Map<T, X>> callback) {
		new AsyncTask<Void, Void, Map<T, X>>() {
			@Override
			protected Map<T, X> doInBackground(Void... params) {
				try {
					return mObjectStore.getMap(identifier, classOfKeys,
							classOfValues);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			protected void onPostExecute(Map<T, X> result) {
				if (callback != null) {
					callback.onGot(identifier, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public <T, X> void storeMap(final String identifier,
			final Map<T, X> mapEntries, final StoreCallback<Map<T, X>> callback) {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					return mObjectStore.storeMap(identifier, mapEntries);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			protected void onPostExecute(Boolean result) {
				if (callback != null) {
					callback.onStored(identifier, mapEntries, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public <T> void fillCollection(final String identifier,
			final Class<T> clazz, final Collection<T> collection,
			final FillCallback<Collection<T>> callback) {

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					return mObjectStore.fillCollection(identifier, clazz,
							collection);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			protected void onPostExecute(Boolean result) {
				if (callback != null) {
					callback.onFilled(identifier, collection, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public <T, X> void fillMap(final String identifier,
			final Class<T> classOfKeys, final Class<X> classOfValues,
			final Map<T, X> map, final FillCallback<Map<T, X>> callback) {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					return mObjectStore.fillMap(identifier, classOfKeys,
							classOfValues, map);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}

			protected void onPostExecute(Boolean result) {
				if (callback != null) {
					callback.onFilled(identifier, map, result);
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

}
