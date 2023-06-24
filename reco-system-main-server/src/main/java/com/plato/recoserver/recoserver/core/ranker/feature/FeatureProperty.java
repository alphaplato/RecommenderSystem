package com.plato.recoserver.recoserver.core.ranker.feature;

import lombok.Setter;
import lombok.ToString;

/**
 * @author Kevin
 * @date 2022-04-01
 */
@Setter
@ToString
public class FeatureProperty {
    /**
     * The key of this feature in ali TF
     * The feature config in Apollo
     */
    private String group;

    private String separator;

    private Object default_val;

    private String input_type;

    private String input_name;

    private String expression;

    private String source_name;

    private Integer multi_length;

    public String getSeparator() {
        return separator;
    }

    public Object getDefaultVal() {
        return  default_val;
    }

    public String getInputType() {
        return input_type;
    }

    public String getInputName() {
        return input_name;
    }

    public String getGroup() {
        return group;
    }

    public String getExpression() {
        return expression;
    }

    public String getSourceName() { return source_name;}

    public Integer getMultiLength() { return multi_length;}
}
