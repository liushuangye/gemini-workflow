package com.gemini.workflow.mapper;


import com.gemini.workflow.entity.Role;
import com.gemini.workflow.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select t_user.*,t_staff.staff_name from t_user left join t_staff on t_user.staff_id = t_staff.staff_id where t_user.user_id = #{userId}")
    public User loadUserByUsername(String userId);//ctms的userId作为Security的userName

    @Select("select t_role.* from t_role inner join t_user_role on t_user_role.role_id = t_role.role_id where t_user_role.user_id = #{userId}")
    public List<Role> getRolesByUserId(String userId);

}
