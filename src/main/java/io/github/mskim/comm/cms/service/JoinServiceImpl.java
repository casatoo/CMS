package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.config.Role;
import io.github.mskim.comm.cms.dto.JoinDTO;
import io.github.mskim.comm.cms.entity.UserEntity;
import io.github.mskim.comm.cms.repo.UserRepository;
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
    public void joinProcess(JoinDTO joinDTO){

        boolean isLoginId = userRepository.existsByLoginId(joinDTO.getLoginId());

        if(isLoginId) {
            return;
        }

        UserEntity data = new UserEntity();

        data.setLoginId(joinDTO.getLoginId());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setRole(Role.ROLE_USER.getCode());

        userRepository.save(data);

    }

}
