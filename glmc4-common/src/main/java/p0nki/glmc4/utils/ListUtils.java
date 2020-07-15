package p0nki.glmc4.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class ListUtils {

    public static List<Float> floatList(float... values) {
        return List.of(ArrayUtils.toObject(values));
    }

}
