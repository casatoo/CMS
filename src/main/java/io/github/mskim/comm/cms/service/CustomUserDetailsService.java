package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.repo.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return userRepository.findByLoginId(loginId)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getLoginId(),
                        user.getPassword(),
                        AuthorityUtils.createAuthorityList(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginId));
    }
}

