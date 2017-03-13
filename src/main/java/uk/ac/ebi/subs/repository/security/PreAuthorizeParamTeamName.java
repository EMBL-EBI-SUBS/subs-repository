package uk.ac.ebi.subs.repository.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole(@authorizeUser.adminRole(),#teamName)")
public @interface PreAuthorizeParamTeamName {
}
