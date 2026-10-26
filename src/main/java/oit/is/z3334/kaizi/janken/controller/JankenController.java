package oit.is.z3334.kaizi.janken.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import oit.is.z3334.kaizi.janken.model.Entry;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUser = prin.getName();

    // エントリーにユーザを追加
    this.entry.addUser(loginUser);

    // Thymeleafに渡すデータを登録
    model.addAttribute("entry", this.entry);
    model.addAttribute("loginUser", loginUser);

    return "janken";
  }
}
