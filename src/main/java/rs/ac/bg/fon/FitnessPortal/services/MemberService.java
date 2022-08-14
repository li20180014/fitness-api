package rs.ac.bg.fon.FitnessPortal.services;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserPostDto;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public interface MemberService {

    UserGetDto create(UserPostDto userPostDto, List<ApplicationUserRole> roleTypes, String siteURL) throws MessagingException, UnsupportedEncodingException;
    void verify(String verificationCode);
}
