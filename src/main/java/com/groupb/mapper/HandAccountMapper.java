package com.groupb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupb.pojo.HandAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface HandAccountMapper extends BaseMapper<HandAccount> {

    @Insert("INSERT INTO hand_accounts (user_id, image_url, title, remark, created_at, updated_at, status) " +
            "VALUES (#{userId}, #{imageUrl}, #{title}, #{remark}, NOW(), NOW(), #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertHandAccount(HandAccount entity);
}
