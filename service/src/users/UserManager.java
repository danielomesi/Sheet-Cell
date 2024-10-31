package users;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Map<String, Boolean> user2Active;

    public UserManager() {
        user2Active = new HashMap<String, Boolean>();
    }

    public synchronized void addUser(String username) {
        user2Active.put(username,false);
    }

    public synchronized void connectUser(String username) {
        user2Active.put(username,true);
    }

    public synchronized void disconnectUser(String username) {
        user2Active.put(username,false);
    }

    public synchronized void removeUser(String username) {
        user2Active.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return user2Active.keySet();
    }

    public synchronized boolean isUserExists(String username) {
        return user2Active.containsKey(username);
    }

    public synchronized boolean isUserActive(String username) {
        return user2Active.getOrDefault(username,false);
    }
}
