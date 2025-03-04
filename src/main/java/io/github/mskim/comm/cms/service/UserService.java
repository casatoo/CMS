package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.dto.UserDTO;

public interface UserService {

    UserDTO findByLoginId(String loginId);
}
