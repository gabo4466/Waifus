package com.waifus.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.waifus.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JWTService {

    private static final Algorithm algorithm = Algorithm.HMAC256("secret");

    private JWTService(){
    }
    public static String createJWT(User user) throws JWTCreationException {
        long expireTime = (new Date().getTime()) + 86400000;
        Date expireDate = new Date(expireTime);
        return JWT.create()
                .withClaim("idUser", user.getIdUser())
                .withClaim("adult_content", user.isAdultContent())
                .withClaim("nickname", user.getNickname())
                .withClaim("admin", user.isAdmin())
                .withClaim("name", user.getName())
                .withClaim("email", user.getEmail())
                .withIssuer("gjm")
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    public static String createJWTOTP(User user, String otp) throws JWTCreationException {
        long expireTime = (new Date().getTime()) + 300000;
        Date expireDate = new Date(expireTime);
        return JWT.create()
                .withClaim("idUser", user.getIdUser())
                .withClaim("OTP", otp)
                .withIssuer("gjm")
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    public static DecodedJWT verifyJWT(String jwt) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("gjm")
                .build();
        return verifier.verify(jwt);
    }


}
