package pers.yuews.bcu.core.common.enums;

/**
 * @author yuews
 * @create 2024/5/24 16:01
 * @describe
 */
public enum MessageTypeEnum {

    /**
     * 正常的状态
     */
    ROUTINE("正常"),
    /**
     * 错误状态
     */
    ERROR("错误");

    private String msg;

    MessageTypeEnum(String msg) {
       this.msg = msg;
   }

    public String getMsg() {
        return msg;
    }
}
