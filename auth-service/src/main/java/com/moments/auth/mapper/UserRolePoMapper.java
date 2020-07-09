package com.moments.auth.mapper;

import com.moments.auth.model.PO.UserRolePo;
import com.moments.auth.model.example.UserRolePoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserRolePoMapper {
    long countByExample(UserRolePoExample example);

    int deleteByExample(UserRolePoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserRolePo record);

    int insertSelective(UserRolePo record);

    List<UserRolePo> selectByExample(UserRolePoExample example);

    UserRolePo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserRolePo record, @Param("example") UserRolePoExample example);

    int updateByExample(@Param("record") UserRolePo record, @Param("example") UserRolePoExample example);

    int updateByPrimaryKeySelective(UserRolePo record);

    int updateByPrimaryKey(UserRolePo record);
}