package com.github.dekaulitz.mockyup.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.dekaulitz.mockyup.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.entities.UserEntities;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JwtManager {

    public static final String SECCRET = "qwERTyuIODfghjK@#$%^7888ghjkbnhjkxzxzxAJSDHJASDHJHJS";

    public static String generateToken(UserEntities userEntities) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(SECCRET);
        return JWT.create()
                .withClaim("id", userEntities.getId())
                .withClaim("username", userEntities.getUsername())
                .withClaim("roles", String.join(",", userEntities.getAccessList()))
                .withIssuedAt(new Date()).sign(algorithm);
    }

    public static AuthenticationProfileModel validateToken(String token) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(SECCRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        AuthenticationProfileModel jwtUserModel = new AuthenticationProfileModel();
        jwtUserModel.set_id(jwt.getClaim("id").asString());

        return jwtUserModel;
    }
}
