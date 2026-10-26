package oit.is.z3334.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import oit.is.z3334.kaizi.janken.model.User;
import oit.is.z3334.kaizi.janken.model.UserMapper;

@Controller
public class JankenController {

  @Autowired
  private UserMapper userMapper;

  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    // DBからユーザ一覧を取得してThymeleafへ渡す
    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    return "janken";
  }
}
