package com.waifus.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class SecurityService {

    private static final Algorithm algorithm = Algorithm.HMAC256("secret");

    private SecurityService(){
    }
    public String createJWT() throws JWTCreationException {
        return JWT.create()
                .withIssuer("gjm")
                .sign(algorithm);
    }

    public DecodedJWT verifyJWT(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build();
        return verifier.verify(token);
    }



}
