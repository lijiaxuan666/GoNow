package com.balls.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RelationMapper {
    //查找用户关注的所有用户
    @Select("SELECT ouid FROM relationship WHERE uid = #{uid}")
    List<String> getfollow(String uid);

    //关注
    @Insert("INSERT INTO relationship (uid,ouid) VALUES (#{uid},#{ouid})")
    boolean follow(String uid,String ouid);

    //取消关注
    @Delete("DELETE FROM relationship WHERE uid = #{uid} AND ouid = #{ouid}")
    boolean cancelFollow(String uid,String ouid);

    //统计关注数量
    @Select("SELECT COUNT(id) FROM relationship WHERE uid = #{uid}")
    int  countRelation(String uid);

    //取消关注
    @Select("SELECT COUNT(id) FROM relationship WHERE uid = #{uid} AND ouid = #{ouid}")
    int isFollow(String uid,String ouid);
}
