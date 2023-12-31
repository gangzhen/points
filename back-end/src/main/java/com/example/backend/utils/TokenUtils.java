package com.example.backend.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {

    private static UserMapper staticUserMapper;

    @Resource
    UserMapper userMapper;

    @PostConstruct
    public void setUserService() {
        staticUserMapper = userMapper;
    }

    public static String generateToken(String userId, String sign) {
        return JWT.create().withAudience(userId)
                .withExpiresAt(DateUtil.offsetMinute(new Date(), 30))
                .sign(Algorithm.HMAC256(sign));
    }

    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserMapper.selectById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static long getExpirationTime(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().getTime();
    }

    public static boolean judgeRefreshToken(String token) {
        long expirationTime = getExpirationTime(token);
        long currentTime = new Date().getTime();
        long timeInterval = (expirationTime - currentTime) / 1000;
        return timeInterval < 900;
    }
}
