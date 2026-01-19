package com.eduflow.security.presentation.restController;

import com.eduflow.people.domain.UserAccount;
import com.eduflow.people.repo.UserAccountRepository;
import com.eduflow.security.presentation.restController.dto.AuthResponse;
import com.eduflow.security.presentation.restController.dto.LoginRequest;
import com.eduflow.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
        import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

        import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAccountRepository userRepo;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        var userDetails = (org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal();

        UserAccount ua = userRepo.findByUsernameIgnoreCase(userDetails.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String token = jwtService.generateAccessToken(
                userDetails,
                Map.of(
                        "personType", ua.getPersonType() == null ? null : ua.getPersonType().name(),
                        "personId", ua.getPersonId()
                )
        );

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(token)
                .roles(roles)
                .personType(ua.getPersonType() == null ? null : ua.getPersonType().name())
                .personId(ua.getPersonId())
                .username(ua.getUsername())
                .build());
    }
}
