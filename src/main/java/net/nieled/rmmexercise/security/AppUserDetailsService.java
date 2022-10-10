package net.nieled.rmmexercise.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nieled.rmmexercise.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("userDetailsService")
@Slf4j
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.debug("Authenticating : {}", username);
        var lowercaseUsername = username.toLowerCase();
        return userRepository
                .findByEmail(lowercaseUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseUsername + " was not found."));
    }
}
