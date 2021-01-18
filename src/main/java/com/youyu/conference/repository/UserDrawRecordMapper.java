package com.youyu.conference.repository;

import com.youyu.conference.entity.UserDrawRecord;
import com.youyu.conference.entity.UserDrawRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserDrawRecordMapper {
    long countByExample(UserDrawRecordExample example);

    int deleteByExample(UserDrawRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserDrawRecord record);

    int insertSelective(UserDrawRecord record);

    List<UserDrawRecord> selectByExample(UserDrawRecordExample example);

    UserDrawRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserDrawRecord record, @Param("example") UserDrawRecordExample example);

    int updateByExample(@Param("record") UserDrawRecord record, @Param("example") UserDrawRecordExample example);

    int updateByPrimaryKeySelective(UserDrawRecord record);

    int updateByPrimaryKey(UserDrawRecord record);
}