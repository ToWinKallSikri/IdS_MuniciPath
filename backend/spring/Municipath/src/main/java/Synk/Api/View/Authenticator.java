package Synk.Api.View;


import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class Authenticator {

    private String secret = "passwordsegretissima";

    public String createJwt(String username) {
        JWTCreator.Builder builder = JWT.create().withSubject(username);
        final DateTime now = DateTime.now();
        builder.withIssuedAt(now.toDate()).withExpiresAt(now.plusDays(1).toDate());
        return builder.sign(Algorithm.HMAC256(this.secret));
    }

    public String getUsername(String jwt) {
    	try {
    		DecodedJWT token = JWT.require(Algorithm.HMAC256(this.secret)).build().verify(jwt);
    		return token.getSubject();
    	} catch(Exception e) {
    		return null;
    	}
    }
}