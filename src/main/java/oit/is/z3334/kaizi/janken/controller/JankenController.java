package oit.is.z3334.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; // 追加

import oit.is.z3334.kaizi.janken.model.User;
import oit.is.z3334.kaizi.janken.model.UserMapper;
import oit.is.z3334.kaizi.janken.model.Match;
import oit.is.z3334.kaizi.janken.model.MatchMapper;

@Controller
public class JankenController {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MatchMapper matchMapper;

  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    ArrayList<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);

    return "janken";
  }

  // 追加：/match?id=... のリクエストを受け取るメソッド
  @GetMapping("/match")
  public String match(@RequestParam("id") int id, Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    // クリックされた相手のユーザ情報を取得
    User opponent = userMapper.selectUserById(id);
    model.addAttribute("opponent", opponent);

    return "match";
  }
}
