package com.moments.auth.mapper;

import com.moments.auth.model.PO.ThirdPartyAccessPo;
import com.moments.auth.model.example.ThirdPartyAccessPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ThirdPartyAccessPoMapper {
    long countByExample(ThirdPartyAccessPoExample example);

    int deleteByExample(ThirdPartyAccessPoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ThirdPartyAccessPo record);

    int insertSelective(ThirdPartyAccessPo record);

    List<ThirdPartyAccessPo> selectByExample(ThirdPartyAccessPoExample example);

    ThirdPartyAccessPo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ThirdPartyAccessPo record, @Param("example") ThirdPartyAccessPoExample example);

    int updateByExample(@Param("record") ThirdPartyAccessPo record, @Param("example") ThirdPartyAccessPoExample example);

    int updateByPrimaryKeySelective(ThirdPartyAccessPo record);

    int updateByPrimaryKey(ThirdPartyAccessPo record);
}