package com.moments.auth.mapper;

import com.moments.auth.model.PO.UserRoleRelPo;
import com.moments.auth.model.example.UserRoleRelPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserRoleRelPoMapper {
    long countByExample(UserRoleRelPoExample example);

    int deleteByExample(UserRoleRelPoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserRoleRelPo record);

    int insertSelective(UserRoleRelPo record);

    List<UserRoleRelPo> selectByExample(UserRoleRelPoExample example);

    UserRoleRelPo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserRoleRelPo record, @Param("example") UserRoleRelPoExample example);

    int updateByExample(@Param("record") UserRoleRelPo record, @Param("example") UserRoleRelPoExample example);

    int updateByPrimaryKeySelective(UserRoleRelPo record);

    int updateByPrimaryKey(UserRoleRelPo record);
}