package com.dukez.best_travel.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dukez.best_travel.infrastructure.abstract_service.IModifyUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import java.util.Map;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints for the user in the system.")
public class AppUserController {

    private final IModifyUserService modifyUserService;

    @Operation(summary = "Enable or disable a user in the system.")
    @PatchMapping(path = "enabled-or-disabled")
    public ResponseEntity<Map<String, Boolean>> enabledOrDisabled(@RequestParam String username) {
        return ResponseEntity.ok(this.modifyUserService.enabled(username));
    }

    @Operation(summary = "Add a role to a user in the system.")
    @PatchMapping(path = "add-role")
    public ResponseEntity<Map<String, List<String>>> addRole(@RequestParam String username, @RequestParam String role) {
        return ResponseEntity.ok(this.modifyUserService.addRole(username, role));
    }

    @Operation(summary = "Remove a role from a user in the system.")
    @PatchMapping(path = "remove-role")
    public ResponseEntity<Map<String, List<String>>> removeRole(@RequestParam String username,
            @RequestParam String role) {
        return ResponseEntity.ok(this.modifyUserService.removeRole(username, role));
    }

}
