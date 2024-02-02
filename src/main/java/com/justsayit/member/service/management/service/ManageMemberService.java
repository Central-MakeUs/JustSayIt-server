package com.justsayit.member.service.management.service;

import com.justsayit.core.security.auth.AuthServiceHelper;
import com.justsayit.member.domain.BlockList;
import com.justsayit.member.domain.Member;
import com.justsayit.member.repository.MemberRepository;
import com.justsayit.member.service.MemberServiceHelper;
import com.justsayit.member.service.management.command.BlockMemberCommand;
import com.justsayit.member.service.management.repository.BlockListRepository;
import com.justsayit.member.service.management.usecase.ManageMemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ManageMemberService implements ManageMemberUseCase {

    private final MemberRepository memberRepository;
    private final BlockListRepository blockListRepository;

    @Override
    public void blockMember(BlockMemberCommand cmd) {
        Long memberId = AuthServiceHelper.getMemberId();
        Member blockerMember = MemberServiceHelper.findExistingMember(memberRepository, memberId);
        Member blockedMember = MemberServiceHelper.findExistingMember(memberRepository, cmd.getBlockedId());
        BlockList blockList = new BlockList(blockerMember, blockedMember);
        blockListRepository.save(blockList);
    }
}
