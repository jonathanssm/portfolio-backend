package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.constants.ErrorConstants;
import com.jonathanssm.portfoliobackend.constants.TransactionConstants;
import com.jonathanssm.portfoliobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service especializado em autenticação de usuários
 * Responsabilidade única: Autenticação e validação de credenciais
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true, timeout = TransactionConstants.AUTH_TIMEOUT)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("🔐 Loading user by username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorConstants.USER_NOT_FOUND + username));
    }

}
