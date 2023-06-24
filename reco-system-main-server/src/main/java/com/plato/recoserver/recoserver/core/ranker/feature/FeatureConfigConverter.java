package com.plato.recoserver.recoserver.core.ranker.feature;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Kevin
 * @date 2022-04-20
 */
@Slf4j
@Component
@ConfigurationPropertiesBinding
public class FeatureConfigConverter implements Converter<String, List<FeatureProperty>> {
    @Override
    public List<FeatureProperty> convert(String source) {
        if (StringUtils.isBlank(source)) {
            log.error("blank feature extractor property found.");
            return Collections.emptyList();
        }
        try {
            return JSON.parseArray(source, FeatureProperty.class);
        } catch (Exception e) {
            log.error("Invalid feature extractor property:{}", source, e);
        }
        return Collections.emptyList();
    }
}
