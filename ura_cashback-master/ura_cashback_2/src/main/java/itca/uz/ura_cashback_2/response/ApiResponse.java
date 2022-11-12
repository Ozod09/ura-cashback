package itca.uz.ura_cashback_2.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import itca.uz.ura_cashback_2.payload.ApiErrorResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ApiResponse<T> implements Serializable {
    private T body;
    private Integer status;
    private Integer total;
    private boolean success;
    private ApiErrorResponse error;

    public ApiResponse() {
    }

    public ApiResponse(Integer statusCode) {
        this.status = statusCode;
        this.success = true;
    }

    public ApiResponse(T body) {
        this(body, 200, null);
    }
    public ApiResponse(T body,Integer status) {
        this(body, status, null);
    }



    public ApiResponse(T body, @NonNull final HttpStatus status, Integer total) {
        this(body, status.value(), total);
    }

    public ApiResponse(T body, @NonNull final Integer status, Integer total) {
        this.body = body;
        this.success = true;
        this.status = status;
        this.total = total;
    }


    public ApiResponse(ApiErrorResponse error, @NonNull final HttpStatus status) {
        this(error, status.value());
    }

    public ApiResponse(ApiErrorResponse error, @NonNull final Integer status) {
        this.error = error;
        this.success = false;
        this.status = status;
    }


}
