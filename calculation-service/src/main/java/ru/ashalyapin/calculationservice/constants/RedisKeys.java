package ru.ashalyapin.calculationservice.constants;

public final class RedisKeys {
    private RedisKeys() {}

    public static final String COEFFICIENTS = "coefficients";

     public static final String PROCESSED_EVENT_PREFIX = "processed:event:";

    public static final String MARKET = "market";
    public static final String BONUS_PERCENT = "bonus_percent";
    public static final String SALARY_JUNIOR = "salary_junior";
    public static final String SALARY_MIDDLE = "salary_middle";
    public static final String SALARY_SENIOR = "salary_senior";
}