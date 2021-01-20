package com.youyu.conference.repository;

import com.youyu.conference.entity.UserPrizeRecord;
import com.youyu.conference.entity.UserPrizeRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrizeRecordMapper {
    long countByExample(UserPrizeRecordExample example);

    int deleteByExample(UserPrizeRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserPrizeRecord record);

    int insertSelective(UserPrizeRecord record);

    List<UserPrizeRecord> selectByExample(UserPrizeRecordExample example);

    UserPrizeRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserPrizeRecord record, @Param("example") UserPrizeRecordExample example);

    int updateByExample(@Param("record") UserPrizeRecord record, @Param("example") UserPrizeRecordExample example);

    int updateByPrimaryKeySelective(UserPrizeRecord record);

    int updateByPrimaryKey(UserPrizeRecord record);
}