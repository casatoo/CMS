package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.config.CustomModelMapper;
import io.github.mskim.comm.cms.dto.UserDTO;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.repository.UserRepository;
import io.github.mskim.comm.cms.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }
}
