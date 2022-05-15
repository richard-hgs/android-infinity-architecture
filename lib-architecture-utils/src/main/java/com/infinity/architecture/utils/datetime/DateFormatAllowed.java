package com.infinity.architecture.utils.datetime;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
    DateFormats.PT_BR_1,
    DateFormats.PT_BR_2,
    DateFormats.PT_BR_3,
    DateFormats.PT_BR_4,
    DateFormats.PT_BR_5,
    DateFormats.PT_BR_6,
    DateFormats.PT_BR_7,
    DateFormats.PT_BR_8,
    DateFormats.PT_BR_9,
    DateFormats.PT_BR_10,
    DateFormats.PT_BR_11,
    DateFormats.PT_BR_12,
    DateFormats.PT_BR_13,
    DateFormats.PT_BR_14,
    DateFormats.PT_BR_15,
    DateFormats.PT_BR_16,
    DateFormats.PT_BR_17,
    DateFormats.PT_BR_18,
    DateFormats.PT_BR_19,
    DateFormats.PT_BR_20,
    DateFormats.PT_BR_21,
    DateFormats.PT_BR_22,
    DateFormats.PT_BR_23,

    DateFormats.EN_US_1,
    DateFormats.EN_US_2,
    DateFormats.EN_US_3,
    DateFormats.EN_US_4,
    DateFormats.EN_US_5,
    DateFormats.EN_US_6,
    DateFormats.EN_US_7,
    DateFormats.EN_US_8,
    DateFormats.EN_US_9,
    DateFormats.EN_US_10,
    DateFormats.EN_US_11,
    DateFormats.EN_US_12,
    DateFormats.EN_US_13,
    DateFormats.EN_US_14,
    DateFormats.EN_US_15,
    DateFormats.EN_US_16,
    DateFormats.EN_US_17,
    DateFormats.EN_US_18,
    DateFormats.EN_US_19,
    DateFormats.EN_US_20,
    DateFormats.EN_US_21,
    DateFormats.EN_US_22,
})
@Retention(RetentionPolicy.SOURCE)
public @interface DateFormatAllowed {
}
