package com.dpm.winwin.api.member.application;

import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.member.dto.request.MemberCreateRequest;
import com.dpm.winwin.api.member.dto.response.MemberCreateResponse;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.member.dto.request.MemberImageRequest;
import com.dpm.winwin.domain.repository.member.dto.request.MemberNicknameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dpm.winwin.api.common.error.enums.ErrorMessage.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class MemberCommandService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long updateMemberNickname(Long memberId,
                                     MemberNicknameRequest memberNicknameRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        member.updateNickname(memberNicknameRequest.nickname());
        return member.getId();
    }

    @Transactional
    public Long updateMemberImage(Long memberId,
                                  MemberImageRequest memberImageRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        member.updateImage(memberImageRequest.image());
        return member.getId();
    }

    @Transactional
    public MemberCreateResponse createMember(MemberCreateRequest memberCreateRequest){
        Member memberRequest = memberCreateRequest.toEntity();
        Member member = memberRepository.save(memberRequest);
        return MemberCreateResponse.from(member);

    }

}