package com.viamhealth.android.adapters.cat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Kunal on 6/6/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelegateAdaptersAnnotation {
    Class<? extends DelegateAdapter>[] delegateAdapters();
}
