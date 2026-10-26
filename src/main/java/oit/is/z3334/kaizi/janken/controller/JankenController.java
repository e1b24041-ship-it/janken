package oit.is.z3334.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

  @GetMapping("/match")
  public String match(@RequestParam("id") int id, Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    User opponent = userMapper.selectUserById(id);
    model.addAttribute("opponent", opponent);

    return "match";
  }

  // 追加：じゃんけんの勝負を行いDBに保存するメソッド
  @GetMapping("/fight")
  public String fight(@RequestParam("id") int opponentId, @RequestParam("hand") String myHand, Principal prin,
      ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    // ログインユーザの情報を取得して自分のIDを割り出す
    User loginUserInfo = userMapper.selectUserByName(loginUser);
    User opponent = userMapper.selectUserById(opponentId);

    // CPUの手を決定（今回は固定で "Gu" とします。ランダムでも可）
    String cpuHand = "Gu";

    // 勝敗判定ロジック（簡易版）
    String result = "Draw";
    if (myHand.equals("Gu") && cpuHand.equals("Choki") ||
        myHand.equals("Choki") && cpuHand.equals("Pa") ||
        myHand.equals("Pa") && cpuHand.equals("Gu")) {
      result = "You Win!";
    } else if (!myHand.equals(cpuHand)) {
      result = "You Lose...";
    }

    // Matchオブジェクトを作成してデータベースにINSERT
    Match newMatch = new Match();
    newMatch.setUser1(loginUserInfo.getId());
    newMatch.setUser2(opponent.getId());
    newMatch.setUser1Hand(myHand);
    newMatch.setUser2Hand(cpuHand);
    matchMapper.insertMatch(newMatch);

    // 画面に渡すデータを登録
    model.addAttribute("opponent", opponent);
    model.addAttribute("myHand", myHand);
    model.addAttribute("cpuHand", cpuHand);
    model.addAttribute("result", result);

    return "match";
  }
}
