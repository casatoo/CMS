package io.github.mskim.comm.cms.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumCode {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    HEADER("HEADER"),
    MANAGER("MANAGER"),
    REQUEST("REQUEST"),
    CONFIRM("CONFIRM"),
    REJECT("REJECT"),
    SUCCESS("SUCCESS"),
    FAIL("FAIL");

    private final String code;

}
