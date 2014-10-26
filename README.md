ObjectStoreLib
==============

A small java/android library which encapsulates Google GSON in order to provide simple storing of objects, generic collections and maps.


Basic Usage
==============
```
//simple object store (uses InternalFileSystem)
ObjectStore objectStore = new ObjectStore(context);

//objects
objectStore.store("keyOfmyObject", myObject);
MyObject myObject = objectStore.get("keyOfMyObject", MyObject.class);

//lists (same for sets)
List<MyObject> list = new ArrayList<MyObject>();
list.add(myObject);
objectStore.storeList("keyList", list);
List<MyObject> listReloaded = objectStore.getList("keyList", MyObject.class);

//maps
Map<Integer, MyObject> map = new HashMap<Integer, MyObject>();
map.put(12, myObject);
objectStore.storeMap("keyMap", map);
Map<Integer, MyObject> mapReloaded = objectStore.getMap("keyMap", Integer.class, MyObject.class);

```

Asynchronous storing and loading:
```
store.async().store("myObject", myObject, new StoreCallback<MyObject>(){
		@Override
		public void onStored(String identifier, MyObject object,
				boolean success) {
			// object stored.
		}
});
		
		
store.async().get("myObject", TestObject.class, new GetCallback<TestObject>(){
		@Override
		public void onGot(String identifier, TestObject object) {
			// object loaded.
		}
});
