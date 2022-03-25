package com.ylzinfo.ruler.core;

import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.constants.ValidType;

import java.util.*;

/**
 * 校验配置
 *
 * @author dengluwei
 */
public final class ValidConfiguration {
    /**
     * 必填字段校验的信息列表
     */
    private List<ValidInfo> requiredValidInfos = new ArrayList<>();
    /**
     * 字典字段校验的信息列表
     */
    private List<ValidInfo> dictValidInfos = new ArrayList<>();
    /**
     * 数值范围字段校验的信息列表
     */
    private List<ValidInfo> numberScopeValidInfos = new ArrayList<>();
    /**
     * 日期时间范围字段校验的信息列表
     */
    private List<ValidInfo> datetimeScopeValidInfos = new ArrayList<>();
    /**
     * 字典字段规则所依赖的字典集合
     * <p>key值是字典类型，value是改类型字典下的所有字典码值</p>
     */
    private Map<String, Set<Object>> dictType = new HashMap<>();

    public ValidConfiguration() {
    }

    public void addValidInfo(ValidInfo validInfo) {
        if (ValidType.REQUIRED.equals(ValidType.valueOf(validInfo.getValidType().toUpperCase()))) {
            this.requiredValidInfos.add(validInfo);
        } else if (ValidType.DICT.equals(ValidType.valueOf(validInfo.getValidType().toUpperCase()))) {
            this.dictValidInfos.add(validInfo);
        } else if (ValidType.NUMBER_SCOPE.equals(ValidType.valueOf(validInfo.getValidType().toUpperCase()))) {
            this.numberScopeValidInfos.add(validInfo);
        } else if (ValidType.DATETIME_SCOPE.equals(ValidType.valueOf(validInfo.getValidType().toUpperCase()))) {
            this.datetimeScopeValidInfos.add(validInfo);
        }
    }

    public void addValidInfo(Collection<ValidInfo> validInfos) {
        for (ValidInfo validInfo : validInfos) {
            this.addValidInfo(validInfo);
        }
    }

    public boolean removeValidInfo(ValidInfo validInfo) {
        return this.requiredValidInfos.remove(validInfo) || this.dictValidInfos.remove(validInfo);
    }

    public void addDictType(Map<String, Set<Object>> dictType) {
        this.dictType.putAll(dictType);
    }

    public Set<Object> removeDictType(String dictType) {
        return this.dictType.remove(dictType);
    }

    public List<ValidInfo> getRequiredValidInfos() {
        return requiredValidInfos;
    }

    public void setRequiredValidInfos(List<ValidInfo> requiredValidInfos) {
        this.requiredValidInfos = requiredValidInfos;
    }

    public List<ValidInfo> getDictValidInfos() {
        return dictValidInfos;
    }

    public void setDictValidInfos(List<ValidInfo> dictValidInfos) {
        this.dictValidInfos = dictValidInfos;
    }

    public List<ValidInfo> getNumberScopeValidInfos() {
        return numberScopeValidInfos;
    }

    public void setNumberScopeValidInfos(List<ValidInfo> numberScopeValidInfos) {
        this.numberScopeValidInfos = numberScopeValidInfos;
    }

    public List<ValidInfo> getDatetimeScopeValidInfos() {
        return datetimeScopeValidInfos;
    }

    public void setDatetimeScopeValidInfos(List<ValidInfo> datetimeScopeValidInfos) {
        this.datetimeScopeValidInfos = datetimeScopeValidInfos;
    }

    public Map<String, Set<Object>> getDictType() {
        return dictType;
    }

    public void setDictType(Map<String, Set<Object>> dictType) {
        this.dictType = dictType;
    }
}
