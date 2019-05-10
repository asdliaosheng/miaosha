package com.imooc.user.repository;

import com.imooc.user.UserApplicationTests;
import com.imooc.user.dataobject.UserInfo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * create by ASheng
 * 2019/3/15 12:44
 */
public class UserRepositoryTest extends UserApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUserRole() throws Exception  {
        List<UserInfo> userInfoList = userRepository.findByUserRole(1);
        Assert.assertTrue(userInfoList.size() > 0);
    }
}