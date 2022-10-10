package net.nieled.rmmexercise.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nieled.rmmexercise.controller.viewmodel.AuthRequest;
import net.nieled.rmmexercise.controller.viewmodel.AuthResponse;
import net.nieled.rmmexercise.domain.User;
import net.nieled.rmmexercise.security.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class JWTController {

    private final AuthenticationManager authManager;
    private final TokenProvider tokenProvider;

    /**
     * {@code POST  /login} : Login request endpoint.
     * @param authRequest The user credentials.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accessToken. Or the corresponding error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authorize(@RequestBody @Valid AuthRequest authRequest) {
        log.debug("REST request to authenticate user : {}", authRequest.getEmail());
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(), authRequest.getPassword())
            );
            User user = (User) authentication.getPrincipal();
            String accessToken = tokenProvider.generateAccessToken(user);
            AuthResponse response = new AuthResponse(user.getEmail(), accessToken);

            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }
}
