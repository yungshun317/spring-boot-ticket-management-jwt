package yungshun.chang.springbootticketmanagementjwt.service;

public interface SecurityService {

    String createToken(String subject, long ttlMillis);

    String getSubject(String token);
}
