package com.justsayit.member.service.auth;

import com.justsayit.core.jwt.JwtTokenProvider;
import com.justsayit.core.jwt.dto.JwtToken;
import com.justsayit.member.domain.Member;
import com.justsayit.member.domain.ProfileInfo;
import com.justsayit.member.repository.MemberRepository;
import com.justsayit.member.service.auth.command.LoginCommand;
import com.justsayit.member.service.auth.dto.LoginRes;
import com.justsayit.member.service.auth.usecase.LoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService implements LoginUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;


    @Override
    public LoginRes login(LoginCommand cmd) {
        Member member = createMember(cmd);
        memberRepository.save(member);
        JwtToken accessToken = jwtTokenProvider.createToken(member.getId());
        return LoginRes.builder()
                .memberId(member.getId())
                .accessToken(accessToken.getAccessToken())
                .build();
    }

    private Member createMember(LoginCommand cmd) {
        return Member.builder()
                .token(cmd.getToken())
                .loginType(cmd.getLoginType())
                .profileInfo(ProfileInfo.builder()
                        .nickname(cmd.getNickname())
                        .profileImg(cmd.getProfileImg())
                        .build())
                .build();
    }
}
