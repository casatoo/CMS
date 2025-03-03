package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.JoinDTO;

public interface JoinService {

    ApiResponse joinProcess(JoinDTO joinDTO);

}
