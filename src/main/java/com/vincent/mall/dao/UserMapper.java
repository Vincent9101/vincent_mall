package com.vincent.mall.dao;

import com.vincent.mall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(String username);

    int checkEmail(String email);

    User selectForLogin(@Param("username") String username, @Param("password") String password);

    String selectQuestionsByUsername(String username);

    int checkAns(@Param("username") String username,
                 @Param("ques") String ques,
                 @Param("ans") String ans);

    int updatePwdByUsername(@Param("username") String username, @Param("new_password") String newPwd);

    int checkPwd(@Param("password") String pwd, @Param("user_id") Integer userId);

    int checkEmailByUserId(@Param("email") String email, @Param("user_id") Integer userId);
}
