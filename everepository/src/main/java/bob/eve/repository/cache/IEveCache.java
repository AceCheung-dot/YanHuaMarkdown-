/*
 * Create by BobEve on 17-12-9 下午3:04
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 17-12-9 下午2:46
 */

package bob.eve.repository.cache;


import java.util.Set;

/**
 * Created by Bob on 17/12/9.
 */

public interface IEveCache<T, Y> {
	interface Factory {
		IEveCache build(IEveCacheType type);
	}


	Y get(T k);


	Y put(T k, Y v);

	Y remove(T k);

	int size();

	int getMaxSize();

	Set<T> keySet();

	boolean contains(T k);

	void evict();

	void clear();
}
