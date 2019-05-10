package com.imooc.user.repository;

import com.imooc.user.dataobject.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/14 22:26
 */
public interface UserRepository extends JpaRepository<UserInfo, String> {

    List<UserInfo> findByUserRole(Integer userRole);

    UserInfo findByUserMail(String userMail);

    UserInfo findByUserName(String userName);

    UserInfo findByUserPhone(String userPhone);

    UserInfo findByUserId(String userId);
}
