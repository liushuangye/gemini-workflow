package com.gemini.workflow.service;

import com.gemini.workflow.entity.Role;
import com.gemini.workflow.entity.User;
import com.gemini.workflow.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在!");
        }
        /** 加载用户角色信息 */
        List<Role> roles = userMapper.getRolesByUserId(user.getUserId());
        user.setRoles(roles);
        return user;
    }
}
