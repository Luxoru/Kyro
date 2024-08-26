package me.luxoru.kyro.event;

import me.luxoru.kyro.request.Request;
import me.luxoru.kyro.response.Response;

/**
 * Represents event which is handled whenever requests are fired
 *
 * @author Luxoru
 */
public interface KyroEvent {


    /**
     * Fired whenever event needs to be handled
     * @param request - http request from client
     * @param response - response which server handles
     */
    void handle(Request request, Response response);

}
