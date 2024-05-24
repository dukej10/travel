package com.dukez.best_travel.infrastructure.services;

import com.dukez.best_travel.infrastructure.abstract_service.IModifyUserService;
import com.dukez.best_travel.util.exceptions.UsernameNotFoundException;
import com.dukez.best_travel.domain.entities.documents.AppUserDocument;
import com.dukez.best_travel.domain.repositories.mongo.AppUserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AppUserService implements IModifyUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final static List<String> AUTHORITIES_PERMIT = List.of("ROLE_USER", "ROLE_ADMIN");

    @Override
    public Map<String, Boolean> enabled(String username) {
        var user = this.appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(COLLECTION_NAME));
        user.setEnabled(!user.isEnabled());
        var userSaved = this.appUserRepository.save(user);
        log.info("User {} is enabled: {}", userSaved.getUsername(), userSaved.isEnabled());
        return Collections.singletonMap(userSaved.getUsername(), userSaved.isEnabled());
    }

    @Override
    public Map<String, List<String>> addRole(String username, String role) {
        var user = appUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(COLLECTION_NAME));
        // Validar que el rol no exista en la lista de roles del usuario y que sea un
        // rol permitido
        if (!user.getRole().getGrantedAuthorities().contains(role) && AUTHORITIES_PERMIT.contains(role)) {
            user.getRole().getGrantedAuthorities().add(role);

        }
        var userSaved = this.appUserRepository.save(user);
        var authorities = userSaved.getRole().getGrantedAuthorities();
        log.info("User {} has authorities: {}", userSaved.getUsername(), authorities);
        return Collections.singletonMap("authorities", authorities);
    }

    @Override
    public Map<String, List<String>> removeRole(String username, String role) {
        var user = appUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(COLLECTION_NAME));
        // Validar que el rol exista en la lista de roles del usuario
        if (user.getRole().getGrantedAuthorities().contains(role)) {
            user.getRole().getGrantedAuthorities().remove(role);
        }
        var userSaved = this.appUserRepository.save(user);
        var authorities = userSaved.getRole().getGrantedAuthorities();
        log.info("User {} has authorities: {}", userSaved.getUsername(), authorities);
        return Collections.singletonMap("authorities", authorities);
    }

    private static String COLLECTION_NAME = "app_users";

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = this.appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(COLLECTION_NAME));
        return mapUserToUserDetails(user);
    }

    private static UserDetails mapUserToUserDetails(AppUserDocument user) {
        Set<GrantedAuthority> authorities = user.getRole()
                .getGrantedAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        System.out.println("Authority from db" + authorities);
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities);
    }

}
