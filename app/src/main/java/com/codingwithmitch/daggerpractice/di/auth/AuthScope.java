package com.codingwithmitch.daggerpractice.di.auth;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

/**
 * Scope is nothing more than a naming convention for the lifespan of a component. The Component owns the scope
 * once it's annotated with it.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface AuthScope {
}
