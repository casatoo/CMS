package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.config.EnumCode;
import io.github.mskim.comm.cms.dto.JoinDTO;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.repo.UserRepository;
import io.github.mskim.comm.cms.service.JoinService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinServiceImpl implements JoinService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ApiResponse joinProcess(JoinDTO joinDTO){

        boolean isLoginId = userRepository.existsByLoginId(joinDTO.getLoginId());

        if(isLoginId) {
            return ApiResponse.of(ApiStatus.CONFLICT, "중복된 아이디 입니다.");
        }

        Users data = new Users();

        data.setLoginId(joinDTO.getLoginId());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setRole(EnumCode.ROLE_USER.getCode());
        data.setRank(EnumCode.MANAGER.getCode());
        data.setName(joinDTO.getName());

        userRepository.save(data);

        return ApiResponse.of(ApiStatus.OK, "회원가입 성공");

    }

}
