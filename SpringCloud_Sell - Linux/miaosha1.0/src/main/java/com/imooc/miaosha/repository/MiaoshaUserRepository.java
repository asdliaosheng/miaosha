package com.imooc.miaosha.repository;

import com.imooc.miaosha.dataobject.MiaoshaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * create by ASheng
 * 2019/4/6 13:54
 */
public interface MiaoshaUserRepository extends JpaRepository<MiaoshaUser, String> {

    List<MiaoshaUser> findByUserRole(Integer userRole);

    MiaoshaUser findByUserMail(String userMail);

    MiaoshaUser findByUserName(String userName);

    MiaoshaUser findByUserId(String userId);
}