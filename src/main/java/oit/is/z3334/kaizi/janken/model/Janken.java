package oit.is.z3334.kaizi.janken.model;

public class Janken {
  private String myHand;
  private String cpuHand = "グー"; // CPUの手は固定でOK
  private String result;

  // コンストラクタで自分の手を受け取り、勝敗を判定する
  public Janken(String myHand) {
    this.myHand = myHand;

    if (myHand.equals("グー")) {
      this.result = "Draw";
    } else if (myHand.equals("チョキ")) {
      this.result = "You Lose";
    } else if (myHand.equals("パー")) {
      this.result = "You Win!";
    }
  }

  // ThymeleafからアクセスできるようにGetterを用意する
  public String getMyHand() {
    return myHand;
  }

  public String getCpuHand() {
    return cpuHand;
  }

  public String getResult() {
    return result;
  }
}
