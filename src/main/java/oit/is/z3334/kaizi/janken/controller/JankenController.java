package oit.is.z3334.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import oit.is.z3334.kaizi.janken.model.Janken;

@Controller
public class JankenController {

  // AC1: index.htmlから参加ボタンを押したとき（POST）
  @PostMapping("/janken")
  public String join(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
    return "janken";
  }

  // AC2: ブラウザで直接 /janken にアクセスしたとき（GET）
  @GetMapping("/janken")
  public String play(@RequestParam(required = false) String hand, ModelMap model) {
    // パラメータ hand が送られてきたら、じゃんけん対戦処理を行う
    if (hand != null) {
      Janken jankenGame = new Janken(hand);
      model.addAttribute("jankenGame", jankenGame);
    }
    return "janken";
  }
}
