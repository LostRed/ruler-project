package com.ylzinfo.ruler.core;

import com.ylzinfo.ruler.constants.ValidType;
import com.ylzinfo.ruler.domain.ValidInfo;

import java.util.*;

/**
 * 校验配置
 *
 * @author dengluwei
 */
public final class ValidConfiguration {
    /**
     * 所有校验信息列表
     */
    private final List<ValidInfo> validInfos = new ArrayList<>();
    /**
     * 必填字段校验的信息列表
     */
    private final List<ValidInfo> requiredValidInfos = new ArrayList<>();
    /**
     * 字典字段校验的信息列表
     */
    private final List<ValidInfo> dictValidInfos = new ArrayList<>();
    /**
     * 数值范围字段校验的信息列表
     */
    private final List<ValidInfo> numberScopeValidInfos = new ArrayList<>();
    /**
     * 日期时间范围字段校验的信息列表
     */
    private final List<ValidInfo> datetimeScopeValidInfos = new ArrayList<>();
    /**
     * 字典字段规则所依赖的字典集合
     * <p>key值是字典类型，value是改类型字典下的所有字典码值</p>
     */
    private final Map<ValidInfo, Set<Object>> dict = new HashMap<>();

    public ValidConfiguration(Collection<ValidInfo> validInfos) {
        this.addValidInfo(validInfos);
    }

    /**
     * 添加校验信息
     *
     * @param validInfo 校验信息
     */
    public void addValidInfo(ValidInfo validInfo) {
        try {
            this.getClass().getClassLoader().loadClass(validInfo.getValidClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        if (ValidType.REQUIRED.equals(ValidType.valueOf(validInfo.getValidType().toUpperCase()))) {
            this.requiredValidInfos.add(validInfo);
        } else if (ValidType.DICT.equals(ValidType.valueOf(validInfo.getValidType().toUpperCase()))) {
            this.dictValidInfos.add(validInfo);
        } else if (ValidType.NUMBER_SCOPE.equals(ValidType.valueOf(validInfo.getValidType().toUpperCase()))) {
            this.numberScopeValidInfos.add(validInfo);
        } else if (ValidType.DATETIME_SCOPE.equals(ValidType.valueOf(validInfo.getValidType().toUpperCase()))) {
            this.datetimeScopeValidInfos.add(validInfo);
        }
        this.validInfos.add(validInfo);
    }

    /**
     * 添加校验信息
     *
     * @param validInfos 校验信息集合
     */
    public void addValidInfo(Collection<ValidInfo> validInfos) {
        if (validInfos == null) {
            return;
        }
        for (ValidInfo validInfo : validInfos) {
            this.addValidInfo(validInfo);
        }
    }

    /**
     * 移除校验信息
     *
     * @param validInfo 校验信息
     * @return 成功移除返回true，否则返回false
     */
    public boolean removeValidInfo(ValidInfo validInfo) {
        return this.validInfos.remove(validInfo)
                && (this.requiredValidInfos.remove(validInfo)
                || this.dictValidInfos.remove(validInfo)
                || this.numberScopeValidInfos.remove(validInfo)
                || this.datetimeScopeValidInfos.remove(validInfo));
    }

    /**
     * 添加字典
     *
     * @param dict 字典
     */
    public void addDict(Map<ValidInfo, Set<Object>> dict) {
        this.dict.putAll(dict);
    }

    /**
     * 移除字典
     *
     * @param validInfo 校验信息
     * @return 移除的字典
     */
    public Set<Object> removeDict(ValidInfo validInfo) {
        return this.dict.remove(validInfo);
    }

    public List<ValidInfo> getValidInfos() {
        return validInfos;
    }

    public List<ValidInfo> getRequiredValidInfos() {
        return requiredValidInfos;
    }

    public List<ValidInfo> getDictValidInfos() {
        return dictValidInfos;
    }

    public List<ValidInfo> getNumberScopeValidInfos() {
        return numberScopeValidInfos;
    }

    public List<ValidInfo> getDatetimeScopeValidInfos() {
        return datetimeScopeValidInfos;
    }

    public Map<ValidInfo, Set<Object>> getDict() {
        return dict;
    }
}
