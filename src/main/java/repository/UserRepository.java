package repository;

import com.google.gson.Gson;
import models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.util.HashMap;
import java.util.logging.Logger;

public class UserRepository {
    final static Logger log = Logger.getAnonymousLogger();

    private static UserRepository userRepository;
    private static Gson gson = new Gson();
    private final String PATH_REPOSITORY_LOCATION = "users.db";

    public HashMap<String, User> userList = new HashMap<String, User>();

    public static UserRepository getInstance() {
        if(userRepository == null) {
            userRepository = new UserRepository();
        }

        return userRepository;
    }

    private UserRepository() {
        this.loadRecords();
    }

    private void loadRecords() {
        String dbAsString = Util.fileAsString(this.PATH_REPOSITORY_LOCATION);

        if(dbAsString == null) {
            JOptionPane.showMessageDialog(null, "The user db file is missing. Please refer to the README for assistance.", "Missing File", JOptionPane.ERROR_MESSAGE);
        } else {
            // File was found
            JSONArray userArray = new JSONArray(dbAsString);
            if(userArray.length() == 0) {
                JOptionPane.showMessageDialog(null, "The user database is empty or corrupt. Please refer to the README for assistance.", "User DB Error", JOptionPane.ERROR_MESSAGE);
            } else {
                for(int i = 0; i < userArray.length(); i++) {
                    JSONObject usrObject = userArray.getJSONObject(i);
                    User user = gson.fromJson(usrObject.toString(), User.class);
                    this.userList.put(user.username, user);
                }
            }
        }
    }

    public User userByName(String username) {
        return this.userList.getOrDefault(username, null);
    }
}
