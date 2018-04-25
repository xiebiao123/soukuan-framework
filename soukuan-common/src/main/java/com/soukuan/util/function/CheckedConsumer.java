package com.soukuan.util.function;

import java.util.Objects;

/**
 * Title
 * Author jirenhe@wanshifu.com
 * Time 2017/9/30.
 * Version v1.0
 */
@FunctionalInterface
public interface CheckedConsumer<T> {

    void accept(T t) throws Throwable;

    default CheckedConsumer<T> andThen(CheckedConsumer<? super T> after) throws Throwable {
        Objects.requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
