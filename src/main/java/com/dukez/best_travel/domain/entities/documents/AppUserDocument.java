package com.dukez.best_travel.domain.entities.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "app_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserDocument implements Serializable {

    @Id
    private String id;

    private String dni;

    private String username;

    private boolean enabled;

    private String password;

    private Role role;

}
