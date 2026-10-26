package oit.is.z3334.kaizi.janken.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Entry {
  private List<String> users = new ArrayList<>();

  public void addUser(String name) {
    if (!this.users.contains(name)) {
      this.users.add(name);
    }
  }

  public List<String> getUsers() {
    return this.users;
  }
}
