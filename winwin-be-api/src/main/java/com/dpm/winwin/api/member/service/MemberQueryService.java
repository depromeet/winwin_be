package com.dpm.winwin.api.member.service;

import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.dto.response.MemberRankResponse;
import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.dto.response.RanksResponse;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.member.MemberTalent;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import com.dpm.winwin.domain.entity.member.enums.TalentType;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dpm.winwin.domain.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dpm.winwin.api.common.error.enums.ErrorMessage.MEMBER_NOT_FOUND;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.GIVE;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.TAKE;
import static java.util.Collections.reverse;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberQueryService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public MemberRankReadResponse readMemberInfo(Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        Optional<Integer> likes = postRepository.getMemberLikeByMemberId(memberId);

        Integer memberLike = likes.stream().mapToInt(Integer::intValue).sum();

        member.updateRank(memberLike);

        MemberReadResponse memberReadResponse =  memberRepository.readMemberInfo(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        MemberRankResponse rank = MemberRankResponse.of(memberReadResponse.ranks().getName(),
                memberReadResponse.ranks().getImage(), memberLike);

        return new MemberRankReadResponse(
                memberReadResponse,
                rank,
                member.getProfileLinks().stream()
                        .map(LinkResponse::of)
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

    @Transactional(readOnly = true)
    public RanksListResponse getRankList() {
        List<RanksResponse> ranks = Arrays.stream(Ranks.values())
                .sorted(Collections.reverseOrder())
                .map(rank -> RanksResponse.of(rank.getName(), rank.getImage(), rank.getCondition()))
                .toList();

        return RanksListResponse.from(ranks);
    }

}
