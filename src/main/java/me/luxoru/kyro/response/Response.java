package me.luxoru.kyro.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents an HTTP response in the context of the Kyro framework.
 * <p>
 * This class encapsulates the details of an HTTP response, including the response code.
 * It provides getters and setters for managing the response code.
 * </p>
 * <p>
 * The default response code is {@link ResponseCode#OK}.
 * </p>
 *
 * @see ResponseCode
 *
 * @author Luxoru
 */

@Getter
public class Response {

    /**
     * The HTTP response code to be sent with the response.
     * <p>
     * This field defaults to {@link ResponseCode#OK} (200 OK).
     * </p>
     */
    private ResponseCode responseCode = ResponseCode.OK;


    public void setResponseCode(@NonNull ResponseCode responseCode) {
        if(responseCode == null){
            throw new NullPointerException("responsecode marked as non-null but it null");
        }
        this.responseCode = responseCode;
    }

}
