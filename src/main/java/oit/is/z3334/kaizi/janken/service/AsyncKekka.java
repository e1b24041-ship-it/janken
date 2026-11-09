package oit.is.z3334.kaizi.janken.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z3334.kaizi.janken.model.Match;
import oit.is.z3334.kaizi.janken.model.MatchMapper;

@Service
public class AsyncKekka {
  private boolean dbUpdated = true;
  private final Logger logger = LoggerFactory.getLogger(AsyncKekka.class);

  @Autowired
  private MatchMapper matchMapper;

  @Async
  public void asyncShowMatchResult(SseEmitter emitter) {
    dbUpdated = true;
    try {
      while (true) {
        if (!dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }

        // matchesテーブルでisActiveがtrueのものを検索
        ArrayList<Match> activeMatches = matchMapper.selectActiveMatches();
        if (activeMatches != null && !activeMatches.isEmpty()) {
          // 最新の試合結果を取得
          Match match = activeMatches.get(0);

          // クライアント(wait.html)へ非同期でデータを送信
          emitter.send(SseEmitter.event().name("matchResult").data(
              "Match: id " + match.getId() +
                  " user1: " + match.getUser1() +
                  " user2: " + match.getUser2() +
                  " user1Hand: " + match.getUser1Hand() +
                  " user2Hand: " + match.getUser2Hand()));

          // 同じ結果を何度も処理しないように更新フラグを折り、DBのisActiveをFALSEに更新する
          dbUpdated = false;
          matchMapper.updateMatchActive(match.getId(), false);
        } else {
          TimeUnit.MILLISECONDS.sleep(500);
        }
      }
    } catch (Exception e) {
      logger.warn("Exception: " + e.getMessage());
      emitter.completeWithError(e);
    }
  }
}
