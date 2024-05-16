package com.dukez.best_travel.util.jsonStrings;

public class JsonStringResponse {
    public static final String ReservationResponseOk = "{\"id\": \"068992c2-e5fb-4a18-8736-c85fa09171b6\",\"dateTimeReservation\": \"2024-05-15 20:13\",\"dateStart\": \"2024-05-15\",\"dateEnd\": \"2024-05-25\",\"hotel\": {\"id\": 10,\"name\": \"Nordic\",\"address\": \"Icelandia 300\",\"rating\": 3,\"price\": 109.99}}";
    public static final String ReservationResponseInvalid = "{\"status\": \"BAD_REQUEST\",\"errorCode\": 400,\"message\": [\"The idClient must be between 18 and 20 characters\",\"The idHotel must be a positive number\"]}";
    public static final String ReservationResponseForbidden = "{\"status\": \"FORBIDDEN\",\"errorCode\": 403,\"message\": \"This customer is blocked\"}";
}
