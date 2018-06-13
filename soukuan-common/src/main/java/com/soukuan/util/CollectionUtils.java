package com.soukuan.util;

import java.util.Collection;

/**
 * Title
 * Time 2017/5/17.
 * Version v1.0
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

    public CollectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> T getFirstSafety(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        } else {
            return collection.iterator().next();
        }
    }
}
