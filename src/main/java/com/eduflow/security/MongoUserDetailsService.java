package com.eduflow.security;

import com.eduflow.people.repo.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsernameIgnoreCase(username)
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .disabled(!u.isEnabled())
                        .accountLocked(!u.isAccountNonLocked())
                        .authorities(u.getRoles().stream().map(Enum::name).toArray(String[]::new))
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
