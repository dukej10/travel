package com.dukez.best_travel.api.models.response;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse extends BaseErrorResponse {
    private String message;

    public Map<Object, Object> structureMsg(String status, Integer errorCode, Object message) {
        Map<Object, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("errorCode", errorCode);
        response.put("message", message);
        return response;

    }
}
