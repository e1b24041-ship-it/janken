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
import oit.is.z3334.kaizi.janken.model.MatchInfo;
import oit.is.z3334.kaizi.janken.model.MatchInfoMapper;
import oit.is.z3334.kaizi.janken.model.MatchMapper;
import oit.is.z3334.kaizi.janken.service.AsyncKekka;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class JankenController {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MatchMapper matchMapper;

  @Autowired
  private MatchInfoMapper matchInfoMapper;

  @Autowired
  private AsyncKekka asyncKekka;

  @GetMapping("/wait/result")
  public SseEmitter waitResult() {
    SseEmitter emitter = new SseEmitter();
    asyncKekka.asyncShowMatchResult(emitter);
    return emitter;
  }

  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    ArrayList<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);

    ArrayList<MatchInfo> activeMatches = matchInfoMapper.selectActiveMatches();
    model.addAttribute("activeMatches", activeMatches);

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

  @GetMapping("/wait")
  public String wait(@RequestParam("id") int opponentId, @RequestParam("hand") String myHand, Principal prin,
      ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    User myUser = userMapper.selectUserByName(loginUser);

    // 1. matchinfoテーブルの確認
    // 自分（myUser.getId()）をuser2として指定し、相手（opponentId）が待機しているアクティブなレコードを探す
    MatchInfo activeMatch = matchInfoMapper.selectActiveMatchInfo(opponentId, myUser.getId());

    if (activeMatch != null) {
      // 2. 存在する場合（相手が待機中 -> 試合成立）
      // matchesテーブルに対戦結果を保存（isActiveはTRUE）
      Match match = new Match();
      match.setUser1(activeMatch.getUser1());
      match.setUser2(myUser.getId());
      match.setUser1Hand(activeMatch.getUser1Hand());
      match.setUser2Hand(myHand);
      match.setActive(true);
      matchMapper.insertMatch(match); // Task1で作成したinsertメソッドを利用

      // matchinfoテーブルの該当レコードの待機状態を解除（FALSEに更新）
      matchInfoMapper.updateMatchInfoActive(activeMatch.getId(), false);

    } else {
      // 存在しない場合（自分が先手 -> 待機状態を登録）
      MatchInfo matchInfo = new MatchInfo();
      matchInfo.setUser1(myUser.getId());
      matchInfo.setUser2(opponentId);
      matchInfo.setUser1Hand(myHand);
      matchInfo.setActive(true);
      matchInfoMapper.insertMatchInfo(matchInfo);
    }

    return "wait";
  }
}
