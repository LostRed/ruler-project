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
     * 范围字段校验的信息列表
     */
    private Map<String, ValidInfo> scopeValidInfos = new HashMap<>();
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
        } else if (ValidType.SCOPE.equals(ValidType.valueOf(validInfo.getValidType().toUpperCase()))) {
            this.scopeValidInfos.put(validInfo.getFieldName(), validInfo);
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

    public Map<String, ValidInfo> getScopeValidInfos() {
        return scopeValidInfos;
    }

    public void setScopeValidInfos(Map<String, ValidInfo> scopeValidInfos) {
        this.scopeValidInfos = scopeValidInfos;
    }

    public Map<String, Set<Object>> getDictType() {
        return dictType;
    }

    public void setDictType(Map<String, Set<Object>> dictType) {
        this.dictType = dictType;
    }
}
