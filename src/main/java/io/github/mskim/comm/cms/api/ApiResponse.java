package io.github.mskim.comm.cms.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    @JsonProperty("status")
    private int status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("redirect")
    private String redirect;

    @JsonProperty("payload")
    private T payload;

    public ApiResponse(ApiStatus apiStatus, String message) {
        this.status = (apiStatus != null) ? apiStatus.getCode() : 500;
        this.message = message;
    }

    public ApiResponse(ApiStatus apiStatus, String message, T payload) {
        this.status = (apiStatus != null) ? apiStatus.getCode() : 500;
        this.message = message;
        this.payload = payload;
    }

    public ApiResponse(ApiStatus apiStatus, String message, String redirect, T payload) {
        this.status = (apiStatus != null) ? apiStatus.getCode() : 500;
        this.message = message;
        this.redirect = redirect;
        this.payload = payload;
    }

    public static ApiResponse<?> of(ApiStatus apiStatus, String message) {
        return new ApiResponse<>(apiStatus, message, null);
    }

    public static <T> ApiResponse<T> of(ApiStatus apiStatus, String message, T payload) {
        return new ApiResponse<>(apiStatus, message, payload);
    }

    public static <T> ApiResponse<T> of(ApiStatus apiStatus, String message, String redirect, T payload) {
        return new ApiResponse<>(apiStatus, message, redirect, payload);
    }
}
