package com.dukez.best_travel.infrastructure.services;

import com.dukez.best_travel.infrastructure.abstract_service.IModifyUserService;
import com.dukez.best_travel.util.exceptions.UsernameNotFoundException;
import com.dukez.best_travel.domain.repositories.mongo.AppUserRepository;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AppUserService implements IModifyUserService {

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

}
