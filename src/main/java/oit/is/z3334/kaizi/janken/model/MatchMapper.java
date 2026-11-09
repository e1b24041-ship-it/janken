package oit.is.z3334.kaizi.janken.model;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchMapper {
    @Select("SELECT * FROM matches")
    ArrayList<Match> selectAllMatches();

    // 追加：試合結果のINSERT
    @Insert("INSERT INTO matches (user1, user2, user1Hand, user2Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{user2Hand}, #{isActive})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertMatch(Match match);

    // isActiveがTRUEの試合結果を取得する
@Select("SELECT * FROM matches WHERE isActive = true")
ArrayList<Match> selectActiveMatches();

// 試合結果のisActiveを更新する
@Update("UPDATE matches SET isActive = #{isActive} WHERE id = #{id}")
void updateMatchActive(int id, boolean isActive);
}
