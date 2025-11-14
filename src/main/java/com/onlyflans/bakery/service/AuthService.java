package com.onlyflans.bakery.service;

import com.onlyflans.bakery.exception.InvalidTokenException;
import com.onlyflans.bakery.model.Token;
import com.onlyflans.bakery.model.User;
import com.onlyflans.bakery.model.dto.TokenDTOResponse;
import com.onlyflans.bakery.model.dto.request.LoginRequest;
import com.onlyflans.bakery.model.dto.request.UserCreateRequest;
import com.onlyflans.bakery.persistence.IUserPersistence;
import com.onlyflans.bakery.persistence.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenRepository tokenRepository;
    //private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IUserPersistence userEntityRepository;
    private final UserService userService;

    public TokenDTOResponse register(UserCreateRequest request){
        var user = userService.createUser(request);
        var savedUser = userEntityRepository.save(user); //? guarda el usuario???
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);

        saveUserToken(savedUser, jwtToken);
        return new TokenDTOResponse(jwtToken, refreshToken);
    }

    public TokenDTOResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = userEntityRepository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return new TokenDTOResponse(jwtToken, refreshToken);
    }

    public void saveUserToken(User user, String jwtToken){
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(final User user){
        final List<Token> validUserTokens = tokenRepository
                .findAllValidIsFalseOrRevokedIsFalseByUserRut(user.getRut()); //UserId
        if(!validUserTokens.isEmpty()){
            for(final Token token: validUserTokens){
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public TokenDTOResponse refreshToken(final String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Token no es de tipo Bearer o no ha sido proporcionado");
        }

        final String refreshToken = authHeader.substring(7);

        final String userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail == null){
            throw new IllegalArgumentException("Refresh token no contiene un usuario válido");
        }

        final User user = userEntityRepository.findByEmail(userEmail)
                .orElseThrow( () -> new UsernameNotFoundException(userEmail));

        if(!jwtService.isTokenValid(refreshToken, user)){
            throw new InvalidTokenException("Refresh token no válido");
        }

        // Generar nuevo token de acceso
        final String newAccessToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, newAccessToken);
        return new TokenDTOResponse(newAccessToken, refreshToken);
    }

    public void logout(final String authHeader) {
        String token = authHeader.substring(7);

        Token storedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token no válido"));

        if (storedToken.isExpired() || storedToken.isRevoked()) {
            throw new InvalidTokenException("Token ya se encuentra expirado o revocado");
        }

        storedToken.setExpired(true);
        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);
    }
}
