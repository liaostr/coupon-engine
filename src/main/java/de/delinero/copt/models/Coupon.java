package de.delinero.copt.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coupon {

    @JsonProperty("type")
    private String type;

    @JsonProperty("discount")
    private Integer discount;

    @JsonProperty(Scope.VALIDATION)
    private CouponRuleSet validationRules;

    @JsonProperty(Scope.APPLICATION)
    private CouponRuleSet applicationRules;

    public String getType() {
        return type;
    }

    public Integer getDiscount() {
        return discount;
    }

    public CouponRuleSet getValidationRules() {
        return validationRules;
    }

    public CouponRuleSet getApplicationRules() {
        return applicationRules;
    }
}
