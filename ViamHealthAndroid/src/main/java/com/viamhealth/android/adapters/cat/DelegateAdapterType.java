package com.viamhealth.android.adapters.cat;

import com.viamhealth.android.model.enums.CatAdapterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by KUnal on 6/6/14.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DelegateAdapterType {
    CatAdapterType itemType();
}
