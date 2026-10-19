package oit.is.z3334.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JankenController {

  // AC1: index.htmlから参加ボタンを押したとき（POST）
  @PostMapping("/janken")
  public String janken(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
    return "janken";
  }

  // AC2: ブラウザで直接 /janken にアクセスしたとき（GET）
  @GetMapping("/janken")
  public String janken(ModelMap model) {
    // 名前を渡さないので、Thymeleaf側で「Hi null」や空欄扱いになる
    return "janken";
  }
}
