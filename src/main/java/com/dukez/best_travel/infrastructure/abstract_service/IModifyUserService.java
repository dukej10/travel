package com.dukez.best_travel.infrastructure.abstract_service;

import java.util.Map;
import java.util.List;

public interface IModifyUserService {
    Map<String, Boolean> enabled(String username);

    Map<String, List<String>> addRole(String username, String role);

    Map<String, List<String>> removeRole(String username, String role);
}
