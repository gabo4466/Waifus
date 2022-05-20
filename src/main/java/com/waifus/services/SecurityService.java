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

public class SecurityService {

    private static final Algorithm algorithm = Algorithm.HMAC256("secret");

    private SecurityService(){
    }
    public static String createJWT(User user) throws JWTCreationException {
        return JWT.create()
                .withClaim("idUser", user.getIdUser())
                .withClaim("adult_content", user.isAdultContent())
                .withClaim("nickname", user.getNickname())
                .withClaim("admin", user.isAdmin())
                .withClaim("name", user.getName())
                .withClaim("email", user.getEmail())
                .withIssuer("gjm")
                .sign(algorithm);
    }

    public static DecodedJWT verifyJWT(HttpServletRequest req) throws JWTVerificationException {
        String jwt = req.getHeader("Authorization");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("gjm")
                .build();
        return verifier.verify(jwt);
    }


}
