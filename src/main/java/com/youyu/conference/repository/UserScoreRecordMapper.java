package com.youyu.conference.repository;

import com.youyu.conference.entity.UserScoreRecord;
import com.youyu.conference.entity.UserScoreRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScoreRecordMapper {
    long countByExample(UserScoreRecordExample example);

    int deleteByExample(UserScoreRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserScoreRecord record);

    int insertSelective(UserScoreRecord record);

    List<UserScoreRecord> selectByExample(UserScoreRecordExample example);

    UserScoreRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserScoreRecord record, @Param("example") UserScoreRecordExample example);

    int updateByExample(@Param("record") UserScoreRecord record, @Param("example") UserScoreRecordExample example);

    int updateByPrimaryKeySelective(UserScoreRecord record);

    int updateByPrimaryKey(UserScoreRecord record);
}