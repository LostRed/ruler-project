package info.lostred.ruler.core;

import info.lostred.ruler.domain.ValidInfo;
import info.lostred.ruler.rule.SingleFieldRule;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 校验配置
 * <p>主要是单字段校验规则依赖的配置</p>
 *
 * @author lostred
 * @see SingleFieldRule
 */
public final class ValidConfiguration {
    /**
     * 所有校验信息
     * <p>key为校验类型，value为校验信息</p>
     */
    private final Map<String, Map<String, ValidInfo>> validInfoMap = new ConcurrentHashMap<>();
    /**
     * 是否启用框架默认的通用规则
     */
    private final boolean enableCommonRules;

    public ValidConfiguration(Collection<ValidInfo> validInfoMap, boolean enableCommonRules) {
        this.addValidInfo(validInfoMap);
        this.enableCommonRules = enableCommonRules;
    }

    /**
     * 添加校验信息
     *
     * @param validInfo 校验信息
     */
    public void addValidInfo(ValidInfo validInfo) {
        if (validInfo.getValidClass() == null) {
            try {
                Class<?> validClass = this.getClass().getClassLoader().loadClass(validInfo.getValidClassName());
                validInfo.setValidClass(validClass);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        }
        String validType = validInfo.getValidType();
        Map<String, ValidInfo> validInfos = this.validInfoMap.getOrDefault(validType, new HashMap<>());
        validInfos.put(validInfo.getId(), validInfo);
        this.validInfoMap.putIfAbsent(validType, validInfos);
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
     * @return 移除的校验信息
     */
    public ValidInfo removeValidInfo(ValidInfo validInfo) {
        String validType = validInfo.getValidType();
        Map<String, ValidInfo> validInfos = this.validInfoMap.getOrDefault(validType, new HashMap<>());
        return validInfos.remove(validInfo.getId());
    }

    /**
     * 根据校验类型获取校验信息集合
     *
     * @param validType 校验类型
     * @return 校验信息集合
     */
    public Collection<ValidInfo> getValidInfos(String validType) {
        return this.validInfoMap.getOrDefault(validType, new HashMap<>(0)).values();
    }

    /**
     * 根据校验类型和校验信息id获取校验信息
     *
     * @param id 校验信息id
     * @return 校验信息
     */
    public ValidInfo getValidInfo(String id) {
        for (Map<String, ValidInfo> map : this.validInfoMap.values()) {
            ValidInfo validInfo = map.get(id);
            if (validInfo != null) {
                return validInfo;
            }
        }
        return null;
    }

    /**
     * 给指定id的校验信息设置字典
     *
     * @param id   校验信息id
     * @param dict 字典
     */
    public void setDict(String id, Set<Object> dict) {
        ValidInfo validInfo = this.getValidInfo(id);
        if (validInfo != null) {
            validInfo.setDict(dict);
        }
    }

    public boolean isEnableCommonRules() {
        return enableCommonRules;
    }
}
