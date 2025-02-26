package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.dto.CustomUserDetails;
import io.github.mskim.comm.cms.entity.UserEntity;
import io.github.mskim.comm.cms.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginId);
        // 사용자 정보가 존재하지 않을 경우 예외 발생
        UserEntity userEntity = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with loginId: " + loginId));

        return new CustomUserDetails(userEntity); // CustomUserDetails 반환
    }
}

