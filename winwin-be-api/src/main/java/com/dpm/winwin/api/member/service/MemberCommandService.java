package com.dpm.winwin.api.member.service;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.api.post.dto.request.LinkRequest;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.link.Link;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.repository.category.SubCategoryRepository;
import com.dpm.winwin.domain.repository.link.LinkRepository;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.member.dto.request.MemberNicknameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static com.dpm.winwin.api.common.error.enums.ErrorMessage.MEMBER_NOT_FOUND;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.GIVE;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.TAKE;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final LinkRepository linkRepository;

    public Long updateMemberNickname(Long memberId,
                                     MemberNicknameRequest memberNicknameRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        member.updateNickname(memberNicknameRequest.nickname());
        return member.getId();
    }

    public MemberUpdateResponse updateMember(Long memberId,
                                         MemberUpdateRequest memberUpdateRequest) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        List<SubCategory> takenTalents = subCategoryRepository.findAllById(
                memberUpdateRequest.takenTalents());

        List<SubCategory> givenTalents = subCategoryRepository.findAllById(
                memberUpdateRequest.givenTalents());

        for (LinkRequest linkRequest : memberUpdateRequest.getExistentLinks()) {
            Link link = linkRepository.findById(linkRequest.id())
                    .orElseThrow(() -> new BusinessException(ErrorMessage.LINK_NOT_FOUND));
            link.setContent(linkRequest.content());
        }

        member.update(memberUpdateRequest.toDto(), givenTalents, takenTalents);

        return new MemberUpdateResponse(
                member.getNickname(),
                member.getImage(),
                member.getIntroduction(),
                member.getProfileLinks().stream()
                        .map(Link::getContent)
                        .toList(),
                member.getTalents().stream()
                        .filter(memberTalent -> memberTalent.getType().equals(GIVE))
                        .map(memberTalent -> memberTalent.getTalent().getName())
                        .toList(),
                member.getTalents().stream()
                        .filter(memberTalent -> memberTalent.getType().equals(TAKE))
                        .map(memberTalent -> memberTalent.getTalent().getName())
                        .toList()
        );
    }

}
