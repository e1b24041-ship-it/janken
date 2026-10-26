package oit.is.z3334.kaizi.janken.model;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users")
    ArrayList<User> selectAllUsers();

    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectUserById(int id);

    // 追加：名前でユーザを1件取得する
    @Select("SELECT * FROM users WHERE name = #{name}")
    User selectUserByName(String name);
}
