package com.plato.recoserver.recoserver.util;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kevin
 * @date 2022-03-17
 */
public class RecUtils {

    public static long bytesToLong(byte[] bytes, int start) {
        int len = ArrayUtils.getLength(bytes);
        if (len < start + Long.BYTES) {
            throw new IllegalArgumentException("Invalid length for long value");
        }
        return Longs.fromBytes(bytes[start], bytes[start + 1], bytes[start + 2], bytes[start + 3],
                bytes[start + 4], bytes[start + 5], bytes[start + 6], bytes[start + 7]);
    }

    public static double bytesToDouble(byte[] bytes, int start) {
        long longBits = bytesToLong(bytes, start);
        return Double.longBitsToDouble(longBits);
    }

    public static int bytesToInt(byte[] bytes, int start) {
        int len = ArrayUtils.getLength(bytes);
        if (len < start + Integer.BYTES) {
            throw new IllegalArgumentException("Invalid length for integer value");
        }
        return Ints.fromBytes(bytes[start], bytes[start + 1], bytes[start + 2], bytes[start + 3]);
    }

    public static short bytesToShort(byte[] bytes, int start) {
        int len = ArrayUtils.getLength(bytes);
        if (len < start + Short.BYTES) {
            throw new IllegalArgumentException("Invalid length for integer value");
        }
        return Shorts.fromBytes(bytes[start], bytes[start + 1]);
    }
}
