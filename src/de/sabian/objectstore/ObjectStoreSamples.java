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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import de.sabian.objectstore.AsyncStoreExecution.GetCallback;

public class ObjectStoreSamples {

	private static class TestObject {

		private String mStringValue;
		private int mIntValue;
		private double mDoubleValue;

		public TestObject(String stringValue, int intValue, double doubleValue) {
			this.mStringValue = stringValue;
			this.mIntValue = intValue;
			this.mDoubleValue = doubleValue;
		}

		public String getStringValue() {
			return mStringValue;
		}

		public void setStringValue(String mStringValue) {
			this.mStringValue = mStringValue;
		}

		public int getIntValue() {
			return mIntValue;
		}

		public void setIntValue(int mIntValue) {
			this.mIntValue = mIntValue;
		}

		public double getDoubleValue() {
			return mDoubleValue;
		}

		public void setDoubleValue(double mDoubleValue) {
			this.mDoubleValue = mDoubleValue;
		}

		public String toString() {
			return mStringValue + ", " + this.mIntValue + ", "
					+ this.mDoubleValue;
		}
	}

	public static void sample(Context context) {
		TestObject t1 = new TestObject("test1", 1, 1.1);
		TestObject t2 = new TestObject("test2", 2, 2.2);

		ObjectStore store = new ObjectStore(context, new SQLiteStorage(context,
				"sample"));

		store.store(t1.getStringValue(), t1);

		TestObject t1Back = store.get(t1.getStringValue(), t1.getClass());

		Log.d("Sample", t1.toString());
		Log.d("Sample", t1Back.toString());

		List<TestObject> objects = new ArrayList<TestObject>();
		objects.add(t1);
		objects.add(t2);
		store.storeList("testList", objects);

		StringBuilder out = new StringBuilder();
		for (TestObject o : objects) {
			out.append(o.toString());
			out.append(" | ");
		}

		Log.d("Sample", "testList: " + out.toString());

		List<TestObject> objectsBack = store.getList("testList",
				TestObject.class);
		Log.d("Sample", "Size of deserialized list: " + objectsBack.size());

		out = new StringBuilder();
		for (TestObject o : objectsBack) {
			out.append(o.toString());
			out.append(" | ");
		}

		Log.d("Sample", "testList deserialized: " + out.toString());

		Map<Integer, TestObject> testMap = new HashMap<Integer, TestObject>();
		testMap.put(100, t1);
		testMap.put(200, t2);
		store.storeMap("testMap", testMap);

		Map<Integer, TestObject> backMap = store.getMap("testMap",
				Integer.class, TestObject.class);

		Log.d("Sample", "size of map: " + testMap.size()
				+ ", size of deserialized map: " + backMap.size());

		store.async().getList("testList", TestObject.class,
				new GetCallback<List<TestObject>>() {
					@Override
					public void onGot(String identifier, List<TestObject> object) {
						Log.d("Sample", "GetCallback: onGot(" + identifier
								+ " with size = " + object.size());
					}

				});

	}
}
