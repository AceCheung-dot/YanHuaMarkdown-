package bob.eve.walle.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;

public class ParameterizedTypeImpl implements ParameterizedType {

    Class clazz;

    public ParameterizedTypeImpl(Class clz) {
        clazz = clz;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{clazz};
    }

    @Override
    public Type getRawType() {
        return LinkedList.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
