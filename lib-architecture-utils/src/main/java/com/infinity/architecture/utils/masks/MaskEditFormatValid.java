package com.infinity.architecture.utils.masks;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@StringDef({
    MaskEditUtil.FORMAT_CPF,
    MaskEditUtil.FORMAT_CELLFONE_1,
    MaskEditUtil.FORMAT_PHONE_1,
    MaskEditUtil.FORMAT_CEP,
    MaskEditUtil.FORMAT_DATE_1,
    MaskEditUtil.FORMAT_HOUR_1,
    MaskEditUtil.FORMAT_CNPJ,
    MaskEditUtil.FORMAT_RG,
    MaskEditUtil.FORMAT_BANK_AGENCY_1,
    MaskEditUtil.FORMAT_BANK_ACCOUNT_1,
    MaskEditUtil.FORMAT_MONETARY,
})
public @interface MaskEditFormatValid {
}
