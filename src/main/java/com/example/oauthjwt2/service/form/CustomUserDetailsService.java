package com.example.oauthjwt2.service.form;

import com.example.oauthjwt2.dto.CustomUserDetails;
import com.example.oauthjwt2.entity.UserEntity;
import com.example.oauthjwt2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            log.info("userEntity: {}", userEntity.getName());
            return new CustomUserDetails(userEntity);
        }
        throw new UsernameNotFoundException("User not found");
    }
}