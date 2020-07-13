package com.moments.auth.mapper;

import com.moments.auth.model.PO.SmsTemplatePo;
import com.moments.auth.model.example.SmsTemplatePoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsTemplatePoMapper {
    long countByExample(SmsTemplatePoExample example);

    int deleteByExample(SmsTemplatePoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsTemplatePo record);

    int insertSelective(SmsTemplatePo record);

    List<SmsTemplatePo> selectByExample(SmsTemplatePoExample example);

    SmsTemplatePo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmsTemplatePo record, @Param("example") SmsTemplatePoExample example);

    int updateByExample(@Param("record") SmsTemplatePo record, @Param("example") SmsTemplatePoExample example);

    int updateByPrimaryKeySelective(SmsTemplatePo record);

    int updateByPrimaryKey(SmsTemplatePo record);
}