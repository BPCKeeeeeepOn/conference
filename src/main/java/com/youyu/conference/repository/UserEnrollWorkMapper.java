package com.youyu.conference.repository;

import com.youyu.conference.entity.UserEnrollWork;
import com.youyu.conference.entity.UserEnrollWorkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEnrollWorkMapper {
    long countByExample(UserEnrollWorkExample example);

    int deleteByExample(UserEnrollWorkExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserEnrollWork record);

    int insertSelective(UserEnrollWork record);

    List<UserEnrollWork> selectByExample(UserEnrollWorkExample example);

    UserEnrollWork selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserEnrollWork record, @Param("example") UserEnrollWorkExample example);

    int updateByExample(@Param("record") UserEnrollWork record, @Param("example") UserEnrollWorkExample example);

    int updateByPrimaryKeySelective(UserEnrollWork record);

    int updateByPrimaryKey(UserEnrollWork record);
}