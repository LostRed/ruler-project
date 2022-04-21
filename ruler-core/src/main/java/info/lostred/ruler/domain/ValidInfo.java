package info.lostred.ruler.domain;

import info.lostred.ruler.constant.ValidType;

import java.util.Objects;

/**
 * 校验信息
 *
 * @author lostred
 */
public class ValidInfo {
    /**
     * id
     */
    private String id;
    /**
     * 业务类型
     */
    private String ruleCode;
    /**
     * 校验类型
     */
    private ValidType validType;
    /**
     * 规则约束的类
     */
    private Class<?> validClass;

    private String[] targetMethods;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValidInfo validInfo = (ValidInfo) o;
        return Objects.equals(id, validInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
