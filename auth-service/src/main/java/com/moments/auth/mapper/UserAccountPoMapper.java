package com.moments.auth.mapper;

import com.moments.auth.model.PO.UserAccountPo;
import com.moments.auth.model.example.UserAccountPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserAccountPoMapper {
    long countByExample(UserAccountPoExample example);

    int deleteByExample(UserAccountPoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserAccountPo record);

    int insertSelective(UserAccountPo record);

    List<UserAccountPo> selectByExample(UserAccountPoExample example);

    UserAccountPo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserAccountPo record, @Param("example") UserAccountPoExample example);

    int updateByExample(@Param("record") UserAccountPo record, @Param("example") UserAccountPoExample example);

    int updateByPrimaryKeySelective(UserAccountPo record);

    int updateByPrimaryKey(UserAccountPo record);
}