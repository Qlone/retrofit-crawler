package com.qlone.craw;

import com.sun.istack.internal.Nullable;

import java.io.IOException;

/**
 * @author heweinan
 * @date 2020-10-27 10:04
 */
abstract class ParameterHandler<T> {
    abstract void apply(ConnectionBuilder builder, @Nullable T value) throws IOException;
}
