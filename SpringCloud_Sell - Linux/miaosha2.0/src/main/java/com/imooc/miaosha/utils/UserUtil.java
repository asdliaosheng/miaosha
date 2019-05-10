package com.imooc.miaosha.utils;

import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.dataobject.UserInfo;
import com.imooc.miaosha.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import static com.imooc.miaosha.service.MiaoshaUserService.COOKIE_NAME_TOKEN;

/**
 * create by ASheng
 * 2019/4/7 16:53
 */
@Slf4j
public class UserUtil {

    //从cookie中判断该用户是否登录
    public static String checkSession(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            log.error("Session不存在或者已经失效");
            throw new GlobalException(CodeMsgVO.SESSION_ERROR);
        }
        String token = null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(COOKIE_NAME_TOKEN)){//取到token
                token = cookie.getValue();
            }
        }
        if(StringUtils.isEmpty(token)){
            log.error("Session不存在或者已经失效");
            throw new GlobalException(CodeMsgVO.SESSION_ERROR);
        }
        return token;
    }

    //创建许多用户对象
    private static void createUser(int count) throws Exception{
        List<UserInfo> users = new ArrayList<UserInfo>(count);
        //生成用户
        for(int i=0;i<count;i++) {
            UserInfo user = new UserInfo();
            String str = 18100000000L+i+"";
            user.setUserId(str);
            user.setUserName(str);
            user.setUserPhone(str);
            user.setSalt("1a2b3c4d");
            user.setUserPassword(MD5Util.inputPassToDBPass("123456",user.getSalt()));
            user.setUserRole(1);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            users.add(user);
        }
        System.out.println("create user");
		//插入数据库
		Connection conn = DBUtil.getConn();
		String sql = "insert into user_info(user_id, user_name, user_password, salt, user_phone, user_role, create_time, update_time)values(?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for(int i=0;i<users.size();i++) {
			UserInfo user = users.get(i);
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, user.getUserPassword());
			pstmt.setString(4, user.getSalt());
			pstmt.setString(5, user.getUserPhone());
			pstmt.setInt(6, user.getUserRole());
            pstmt.setTimestamp(7, new Timestamp(user.getCreateTime().getTime()));
            pstmt.setTimestamp(8, new Timestamp(user.getUpdateTime().getTime()));
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		pstmt.close();
		conn.close();
		System.out.println("insert to db");
        //登录，生成token
        //String urlString = "http://localhost:8085/login/do_login";
        String urlString = "http://47.101.175.31:8085/login/do_login";
        File file = new File("C:/Users/liaosheng/Desktop/GProject/tokens.txt");
        if(file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for(int i=0;i<users.size();i++) {
            UserInfo user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection)url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "userId="+user.getUserId()+"&userPassword="+"123456";
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0 ,len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println("create token : " + user.getUserId());

            String row = user.getUserId()+","+token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getUserId());
        }
        raf.close();

        System.out.println("over");
    }

    public static void main(String[] args)throws Exception {
        createUser(1000);
    }
}
