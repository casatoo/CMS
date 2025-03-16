package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.entity.Users;

public interface UserService {

    Users findByLoginId(String loginId);

}
