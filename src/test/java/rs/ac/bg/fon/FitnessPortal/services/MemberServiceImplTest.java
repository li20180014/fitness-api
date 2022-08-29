package rs.ac.bg.fon.FitnessPortal.services;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserPostDto;
import rs.ac.bg.fon.FitnessPortal.entities.Member;
import rs.ac.bg.fon.FitnessPortal.entities.Role;
import rs.ac.bg.fon.FitnessPortal.entities.User;
import rs.ac.bg.fon.FitnessPortal.exception_handling.AccountVerificationFailedException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.EmailExistsException;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.UserMapper;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.UserMapperImpl;
import rs.ac.bg.fon.FitnessPortal.repositories.MemberRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.UserRepository;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;
import rs.ac.bg.fon.FitnessPortal.services.utility.UserConfigurer;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapperImpl userMapper;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private UserConfigurer userConfigurer;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MemberServiceImpl memberService;

    private MimeMessage mimeMessage;

    private RandomString randomString;

    @BeforeEach
    void setUp() {
        mimeMessage = new MimeMessage((Session)null);
        randomString = mock(RandomString.class);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void createShouldThrowEmailExists() {

        UserPostDto userPostDto = new UserPostDto(1, "Lana", "Ilic", "lana@gmail.com", "jfnsf");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        Assertions.assertThrows(EmailExistsException.class, () -> {
            memberService.create(userPostDto, null, null);
        });
    }

    @Test
    void createMemberShouldAddMember() throws MessagingException, UnsupportedEncodingException {
        UserPostDto userPostDto = new UserPostDto(1, "Lana", "Ilic", "lana.ilic99@gmail.com", "jfnsf");

        List<ApplicationUserRole> roleTypes = List.of(ApplicationUserRole.USER);

        doAnswer(invocation -> {
            User user = (User) invocation.getArgument(0);
            List<ApplicationUserRole> roleTypes1 =(List<ApplicationUserRole>) invocation.getArgument(1);

            Set<Role> roles = createRoles(roleTypes1);

            user.setRoles(roles);

            return null;
        }).when(userConfigurer).addRoles(any(User.class), any(List.class));

        doAnswer(invocation -> {
            User user = (User) invocation.getArgument(0);
            user.setPassword("123");
            return null;
        }).when(userConfigurer).encodePassword(any(User.class));

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);


        try (MockedStatic<RandomString> mockedStatic = Mockito.mockStatic(RandomString.class)) {
            mockedStatic.when(() -> RandomString.make(64)).thenReturn("someRandomString");
            memberService.create(userPostDto, roleTypes, "siteUrl");

            ArgumentCaptor<Member> memberArgumentCaptor = ArgumentCaptor.forClass(Member.class);
            verify(memberRepository).save(memberArgumentCaptor.capture());

            Member capturedMember = memberArgumentCaptor.getValue();

            Member member = userMapper.userPostDtoToMember(userPostDto);
            member.addRole(new Role(ApplicationUserRole.USER));
            member.setPassword("123");

            member.setVerificationCode("someRandomString");
            member.setEnabled(false);

            assertThat(capturedMember).usingRecursiveComparison().isEqualTo(member);
        }


    }

    private Set<Role> createRoles(List<ApplicationUserRole> roleTypes1) {
        Set<Role> roles = new HashSet<>();

        for (ApplicationUserRole rt: roleTypes1) {
            roles.add(new Role(rt));
        }
        return roles;
    }

    @Test
    void verifyShouldThrowException() {

        when(memberRepository.findByVerificationCode("neki kod")).thenReturn(null);

        Assertions.assertThrows(AccountVerificationFailedException.class, () -> {
            memberService.verify("neki kod");
        });
    }

    @Test
    void verifyShouldVerifyMember() {

        Member member = new Member(1, "Lana", "Ilic" ,"lana.ilic99@gmail.com", "123");

        when(memberRepository.findByVerificationCode("nekikod")).thenReturn(member);

        memberService.verify("nekikod");
        ArgumentCaptor<Member> memberArgumentCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberArgumentCaptor.capture());

        Member capturedMember = memberArgumentCaptor.getValue();
        member.setEnabled(true);
        member.setVerificationCode(null);

        assertThat(capturedMember).usingRecursiveComparison().isEqualTo(member);

    }

}