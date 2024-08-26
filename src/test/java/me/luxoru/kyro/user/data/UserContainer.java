package me.luxoru.kyro.user.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserContainer {

    private Set<User> users = Collections.synchronizedSet(new HashSet<>());

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public User getUser(String name) {
        for (User user : users) {
            if(user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public Set<User> getUsers() {
        return Set.copyOf(users);
    }


}
