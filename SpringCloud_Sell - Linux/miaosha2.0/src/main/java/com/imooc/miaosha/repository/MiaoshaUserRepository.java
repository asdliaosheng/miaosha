package com.imooc.miaosha.repository;

import com.imooc.miaosha.dataobject.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * create by ASheng
 * 2019/4/6 13:54
 */
public interface MiaoshaUserRepository extends JpaRepository<UserInfo, String> {

    List<UserInfo> findByUserRole(Integer userRole);

    UserInfo findByUserMail(String userMail);

    UserInfo findByUserName(String userName);

    UserInfo findByUserId(String userId);
}