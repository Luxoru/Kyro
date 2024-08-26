package me.luxoru.kyro.user.route;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.luxoru.kyro.request.Request;
import me.luxoru.kyro.request.RequestMethod;
import me.luxoru.kyro.request.RestPath;
import me.luxoru.kyro.request.Route;
import me.luxoru.kyro.response.Response;
import me.luxoru.kyro.user.data.User;
import me.luxoru.kyro.user.data.UserContainer;

import java.util.Set;

@Route(path = "/v1")
@AllArgsConstructor
@Slf4j(topic = "userfetch")
public class UserFetchRoute {

    private final UserContainer container;


    @RestPath(path = "/user", method = RequestMethod.GET)
    public User fetchUser(Request request, Response response) {

        String name = request.getParameter("name");

        log.info("Fetching user found with name {}", name);

        return container.getUser(name);
    }

    @RestPath(path = "/users", method = RequestMethod.GET)
    public Set<User> getUsers(Request request, Response response){

        return container.getUsers();

    }

    @RestPath(path = "/glester", method = RequestMethod.GET)
    public User get(Request request, Response response){

        return new User("deglester", 32);

    }

    @RestPath(path = "/put", method = RequestMethod.POST)
    public User insert(Request request, Response response){
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        User user = new User(name, age);
        container.addUser(user);
        return user;
    }




}
