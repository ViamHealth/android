package com.viamhealth.android.ui.cache;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by naren on 27/10/13.
 */
public class MemoryCache {

    private Map<String, SoftReference<List<Byte>>> cache=Collections.synchronizedMap(new HashMap<String, SoftReference<List<Byte>>>());

    public List<Byte> get(String id){
        if(!cache.containsKey(id))
            return null;
        SoftReference<List<Byte>> ref=cache.get(id);
        return ref.get();
    }

    public void put(String id, List<Byte> byteArray){
        cache.put(id, new SoftReference<List<Byte>>(byteArray));
    }

    public void clear() {
        cache.clear();
    }
}
