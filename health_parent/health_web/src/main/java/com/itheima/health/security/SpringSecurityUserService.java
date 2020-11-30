package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

/**
 * '@author: Lea
 * '@version: RV01
 * '@date: 2020-11-30 09:50
 */

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过username定位该用户
        User user = userService.findByUsername(username);
        if (user != null) {
            //获取password
            String password = user.getPassword();
            //创建权限集合（遍历用户角色和权限添加)
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            //用户所拥有的角色。当使用hasRole用的角色做权限控制
            SimpleGrantedAuthority sai = null;  //GrantedAuthority是接口，不能直接new对象
            Set<Role> roles = user.getRoles();
            if (roles != null) {
                for (Role role : roles) {
                    //获取角色。当调用hasAuthority用的权限做控制
                    sai = new SimpleGrantedAuthority(role.getKeyword());
                    authorities.add(sai);
                    //获取角色下的权限
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions != null) {
                        for (Permission permission : permissions) {
                            sai = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sai);
                        }
                    }
                }
            }
            return new org.springframework.security.core.userdetails.User(username, password, authorities);
        }
        //没有该用户，限制访问
        return null;
    }
}
