package com.example.albaease.auth.service;
import com.example.albaease.auth.CustomUserDetails;
import com.example.albaease.auth.dto.*;
import com.example.albaease.auth.exception.*;
import com.example.albaease.user.entity.SocialType;
import com.example.albaease.user.entity.User;
import com.example.albaease.user.repository.UserRepository;
import com.example.albaease.auth.jwt.JwtUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //회원가입 메서드
    public void signup(SignupRequest request) {
//        Boolean isIdChecked = (Boolean) session.getAttribute("isIdChecked");
//        Boolean isPhoneVerified = (Boolean) session.getAttribute("isPhoneVerified");
//
//        //세션에서 전화번호 인증했는지 확인
//        if (isPhoneVerified == null || !(Boolean) session.getAttribute("isPhoneVerified")) {
//            System.out.println("isPhoneVerified확인" + isPhoneVerified);
//            throw new PhoneVerificationRequiredException("전화번호 인증을 먼저 진행해주세요.");
//        }
//
//        // 세션에서 아이디 중복검사 했는지 확인
//        if (isIdChecked == null || !isIdChecked) {
//            System.out.println("isIdChecked확인" + isIdChecked);
//            throw new IdDuplicationCheckRequiredException("아이디 중복 검사를 먼저 진행해주세요.");
//        }

        // 비밀번호 확인 추가 (비밀번호와 비밀번호 확인 필드가 있다고 가정)
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        // 소셜 로그인을 위한 socialType 설정
        SocialType socialType = "kakao".equals(request.getSocialType()) ? SocialType.KAKAO : SocialType.NONE;

        // 비밀번호 암호화 후 저장
        User user = new User(
                request.getLastName(),
                request.getFirstName(),
                request.getId(),
                passwordEncoder.encode(request.getPassword()),  // BCrypt 암호화
                request.getPhoneNumber(),
                socialType,
                request.getRole(),
                null,  // store는 일단 null
                null   // businessNumber도 null
        );
        // 사용자 정보를 DB에 저장
        userRepository.save(user);
    }
    //로그인 메서드
    public String login(LoginRequest request) {
        //로그인아이디로 사용자 조회
        User user = userRepository.findByLoginId(request.getId())
                .orElseThrow(() -> new InvalidCredentialsException("유저를 찾을 수 없습니다."));

        // 비밀번호 검증 -> 입력받은 비번이랑 저장된 비번이랑 같은지 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 발급 -> 비밀번호가 일치하면 JWT 토큰을 발급하여 반환
        String token =  jwtUtil.generateToken(user.getUserId().toString(), user.getRole().toString());

        // CustomUserDetails 객체 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        System.out.println("로그인에서 제대로 객체생성하는지 확인" + customUserDetails);
        // Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());

        System.out.println("로그인에서 제대로 객체생성하는지 확인2" + authentication);

        // SecurityContext에 Authentication 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // JWT 콘솔 출력 확인(나중에 지울거)
        System.out.println("Generated JWT: " + token);
        return token;
    }

    //로그인 아이디 중복 검사
    public void checkIdDuplicate(IdCheckRequest request) {
        // ID 중복 검사
        if (userRepository.existsByLoginId(request.getId())) {
            throw new IDAlreadyExistsException("이미 존재하는 ID입니다.");
        }
    }
    //현재 비밀번호 확인
    public void verifyCurrentPassword(VerifyPasswordRequest request, String token , HttpSession session) {
        //토큰을 통해 userId 가져옴
        Long userId = Long.valueOf(jwtUtil.extractUserId(token));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("유저를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

//        session.setAttribute("isPasswordChecked", true);
    }

    //비밀번호 변경
    public void changePassword(PasswordChangeRequest request, String token, HttpSession session) {
        Boolean isPasswordChecked = (Boolean) session.getAttribute("isPasswordChecked");
        // 세션에서 비밀번호 확인 했는지 확인
        if (isPasswordChecked == null || !isPasswordChecked) {
            throw new IdDuplicationCheckRequiredException("비밀번호 체크를 먼저 진행해주세요.");
        }
        // 토큰을 통해 userId 가져옴

        Long userId = Long.valueOf(jwtUtil.extractUserId(token));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("유저를 찾을 수 없습니다."));

        if(!request.getNewPassword().equals(request.getConfirmNewPassword())){
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }
        user.changePassword(request.getNewPassword(), passwordEncoder);
        userRepository.save(user); //변경된 비밀번호 저장
    }
}

