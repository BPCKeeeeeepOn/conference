package com.youyu.conference.repository;

import com.youyu.conference.entity.UserEnrollWorkEvent;
import com.youyu.conference.entity.UserEnrollWorkEventExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserEnrollWorkEventMapper {
    long countByExample(UserEnrollWorkEventExample example);

    int deleteByExample(UserEnrollWorkEventExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserEnrollWorkEvent record);

    int insertSelective(UserEnrollWorkEvent record);

    List<UserEnrollWorkEvent> selectByExample(UserEnrollWorkEventExample example);

    UserEnrollWorkEvent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserEnrollWorkEvent record, @Param("example") UserEnrollWorkEventExample example);

    int updateByExample(@Param("record") UserEnrollWorkEvent record, @Param("example") UserEnrollWorkEventExample example);

    int updateByPrimaryKeySelective(UserEnrollWorkEvent record);

    int updateByPrimaryKey(UserEnrollWorkEvent record);
}