package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.dto.UserDTO;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.repository.UserRepository;
import io.github.mskim.comm.cms.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO findByLoginId(String loginId) {
        Optional<Users> userOptional = userRepository.findByLoginId(loginId);
        return userOptional.map(user -> modelMapper.map(user, UserDTO.class)).orElse(null);
    }
}
