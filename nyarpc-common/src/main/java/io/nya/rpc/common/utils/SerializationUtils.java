package io.nya.rpc.common.utils;

public class SerializationUtils {
    // 序列化类型，约定为 16 字节，不足 16 字节填充 0
    private static final String PADDING_STRING = "0";

    private static final int MAX_SERIALIZATION_TYPE_LEN = 16;

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
        StringBuilder strBuilder = new StringBuilder(s);
        for(int i = n; i <= MAX_SERIALIZATION_TYPE_LEN; i++) {
            strBuilder.append(PADDING_STRING);
        }
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
