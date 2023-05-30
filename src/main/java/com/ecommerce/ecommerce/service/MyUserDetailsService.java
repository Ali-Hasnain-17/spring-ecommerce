package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.models.MyUserDetails;
import com.ecommerce.ecommerce.models.User;
import com.ecommerce.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("No user with given email");
        }
        return new MyUserDetails(
                user.get().getEmail(),
                user.get().getPassword(),
                user.get().getRole()
        );
    }
}
