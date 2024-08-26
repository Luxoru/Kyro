package me.luxoru.kyro;

import me.luxoru.kyro.user.data.User;
import me.luxoru.kyro.user.data.UserContainer;
import me.luxoru.kyro.user.route.UserFetchRoute;
import org.junit.jupiter.api.Test;
import me.luxoru.kyro.Kyro.KyroBuilder;


public class KyroTest {


    private static final long MAX_ALIVE_TIME = 100000L;

    @Test
    public void testKyro() {

        UserContainer container = new UserContainer();

        container.addUser(new User("Des", 32));
        container.addUser(new User("Maria", 21));
        container.addUser(new User("Preston", 23));


        Kyro kyro = new KyroBuilder(8080)
                .addRoute(new UserFetchRoute(container))
                .build();


        kyro.start();


        Runtime.getRuntime().addShutdownHook(new Thread(kyro::cleanup));
        // We keep the test running for a predefined amount of time so the test doesn't
        // finish instantly
        long started = System.currentTimeMillis();
        while ((System.currentTimeMillis() - started) < MAX_ALIVE_TIME) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

}
