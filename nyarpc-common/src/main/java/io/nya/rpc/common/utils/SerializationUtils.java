package io.nya.rpc.common.utils;

import java.util.stream.IntStream;

public class SerializationUtils {
    // 序列化类型，约定为 16 字节，不足 16 字节填充 0
    public static final String PADDING_STRING = "0";

    public static final int MAX_SERIALIZATION_TYPE_LEN = 16;

    /**
     * 不足补0
     * @param s 序列化类型
     * @return 填入header中的序列化类型
     */
    public static String paddingString(String s) {
        s = transferNullEmpty(s);
        int n = s.length();
        if(n >= MAX_SERIALIZATION_TYPE_LEN) {
            return s;
        }
        int paddingCount = MAX_SERIALIZATION_TYPE_LEN - n;
        StringBuilder strBuilder = new StringBuilder(s);
        IntStream.range(0, paddingCount).forEach((i)->{
            strBuilder.append(PADDING_STRING);
        });
        s = strBuilder.toString();
        return s;
    }

    /**
     * 获得原始字符串
     * @param s header中的序列化类型
     * @return 原始序列化类型
     */
    public static String subString(String s) {
        s = transferNullEmpty(s);
        return s.replace(PADDING_STRING, "");
    }
    private static String transferNullEmpty(String s) {
        return  s == null ? "" : s;
    }
}
