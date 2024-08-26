package me.luxoru.kyro.user.event;

import me.luxoru.kyro.event.Cancellable;
import me.luxoru.kyro.event.KyroEvent;
import me.luxoru.kyro.request.Request;
import me.luxoru.kyro.response.Response;

public class UserCheckerEvent implements Cancellable, KyroEvent {
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void handle(Request request, Response response) {

    }
}
