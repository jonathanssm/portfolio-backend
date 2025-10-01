package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.constants.ErrorConstants;
import com.jonathanssm.portfoliobackend.model.Profile;
import com.jonathanssm.portfoliobackend.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public Profile findByName(String name) {
        return profileRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException(ErrorConstants.PROFILE_NOT_FOUND + name));
    }
}
