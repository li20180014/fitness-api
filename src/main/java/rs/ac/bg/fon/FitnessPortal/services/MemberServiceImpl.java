package rs.ac.bg.fon.FitnessPortal.services;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserPostDto;
import rs.ac.bg.fon.FitnessPortal.entities.Member;
import rs.ac.bg.fon.FitnessPortal.exception_handling.AccountVerificationFailedException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.EmailExistsException;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.UserMapper;
import rs.ac.bg.fon.FitnessPortal.repositories.MemberRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.UserRepository;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;
import rs.ac.bg.fon.FitnessPortal.services.utility.UserConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Represents a service layer class responsible for implementing all Member related API methods.
 * Available API method implementations: POST
 *
 * @author Lana
 * @version 1.0
 */
@Service
public class MemberServiceImpl implements MemberService{

    /**
     * Instance of User repository class, responsible for interacting with User database table.
     */
    private UserRepository userRepository;
    /**
     * Instance of Mapstruct Mapper class, responsible for mapping from DTO to DAO and vice versa.
     */
    private UserMapper userMapper;
    /**
     * Instance of Member repository class, responsible for interacting with Member database table.
     */
    private MemberRepository memberRepository;
    /**
     * Instance of User Configurer class, responsible for handling user configurations such as encoding passwords and setting application roles.
     */
    private UserConfigurer userConfigurer;
    /**
     * Instance of JavaMailSender class, responsible for sending emails from specific email address.
     */
    private JavaMailSender javaMailSender;


    /**
     * Adds new application member to database. Returns instance of saved member from database.
     *
     * @param userPostDto instance of UserPostDto class with new member data for inserting.
     * @param roleTypes list of ApplicationUserRoles.
     * @param siteURL string value of application site url.
     * @return UserGetDto instance of saved member in database.
     * @throws MessagingException The base class for all exceptions thrown by the Messaging classes.
     * @throws UnsupportedEncodingException The Character Encoding is not supported.
     * @throws EmailExistsException if member with provided email already exists in database.
     */
    @Override
    @Transactional
    public UserGetDto create(UserPostDto userPostDto, List<ApplicationUserRole> roleTypes, String siteURL) throws MessagingException, UnsupportedEncodingException {
        if(userRepository.existsByEmail(userPostDto.getEmail())) throw new EmailExistsException(userPostDto.getEmail());

        Member member = userMapper.userPostDtoToMember(userPostDto);
        userConfigurer.addRoles(member, roleTypes);
        userConfigurer.encodePassword(member);
        
        String randomCode = RandomString.make(64);
        member.setVerificationCode(randomCode);
        member.setEnabled(false);

        memberRepository.save(member);
        
        sendVerificationEmail(member, siteURL);
        
        return userMapper.userToUserGetDto(member);
    }

    /**
     * Method responsible for sending verification emails to new users from server.
     *
     * @param user instance of User class to whom the mail should be sent to.
     * @param siteURL string value of application site url.
     * @throws MessagingException The base class for all exceptions thrown by the Messaging classes.
     * @throws UnsupportedEncodingException The Character Encoding is not supported.
     */
    private void sendVerificationEmail(Member user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "lana.ilic99@gmail.com";
        String senderName = "Fitness Portal";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Synergy Fitness";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
        String verifyURL = siteURL + "/api/v1/members/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        javaMailSender.send(message);
    }

    /**
     * Method responsible for verifying a new User account.
     *
     * @param verificationCode string value of a User's verification code.
     */
    public void verify(String verificationCode){
        Member member = memberRepository.findByVerificationCode(verificationCode);

        if(member == null || member.getEnabled() == true) throw new AccountVerificationFailedException();

        member.setVerificationCode(null);
        member.setEnabled(true);
        memberRepository.save(member);
    }

    /**
     * Sets user repository to provided instance of UserRepository class.
     *
     * @param userRepository new Object instance of UserRepository class.
     */
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Sets user mapper to provided instance of UserMapper class.
     *
     * @param userMapper new Object instance of mapstruct UserMapper class.
     */
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Sets  member repository to provided instance of MemberRepository class.
     *
     * @param memberRepository new Object instance of MemberRepository class.
     */
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Sets user configurer to provided instance of UserConfigurer class.
     *
     * @param userConfigurer new Object instance of UserConfigurer class.
     */
    @Autowired
    public void setUserConfigurer(UserConfigurer userConfigurer) {
        this.userConfigurer = userConfigurer;
    }

    /**
     * Sets java mail sender to provided instance of JavaMailSender class.
     *
     * @param javaMailSender new Object instance of JavaMailSender class.
     */
    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
}
