package top.yifan.template.util;

import java.util.UUID;

/**
 * uuid 生成工具
 *
 * @author Star.Y.Zheng
 */
public final class UUIDGenerateUtils {

    private UUIDGenerateUtils() {
    }

    /**
     * 随机生成code
     * @return
     */
    public static String generateCode(){
        String code = UUID.randomUUID().toString();

        return code;
    }

}
