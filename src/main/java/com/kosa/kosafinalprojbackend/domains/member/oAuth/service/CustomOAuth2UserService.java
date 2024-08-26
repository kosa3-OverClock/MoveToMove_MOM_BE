package com.kosa.kosafinalprojbackend.domains.member.oAuth.service;

import com.kosa.kosafinalprojbackend.domains.member.oAuth.dto.info.OAuth2UserInfo;
import com.kosa.kosafinalprojbackend.domains.member.oAuth.dto.info.OAuth2UserInfoFactory;
import com.kosa.kosafinalprojbackend.domains.member.oAuth.domain.ProviderType;
import com.kosa.kosafinalprojbackend.domains.member.oAuth.domain.Member;
import com.kosa.kosafinalprojbackend.domains.member.oAuth.dto.info.UserPrincipal;
import com.kosa.kosafinalprojbackend.global.error.exception.OAuthProviderMissMatchException;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberOAuthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.ERR_SOCIAL_PROVIDER_MISMATCH;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberOAuthMapper mapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try{
            return this.process(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());

        log.info("social Type = {}", providerType);
        log.info("email = {}", userInfo.getEmail());

        Member savedMember = mapper.findUserBySocialIdAndType(userInfo.getEmail(),providerType);
        log.info("savedMember = {}", savedMember);
        if (savedMember != null) {
            if (providerType != savedMember.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        ERR_SOCIAL_PROVIDER_MISMATCH
                );
            }
        } else {
            savedMember = creatUserEntity(userInfo, providerType);
//            mapper.insertMember(savedMember);
        }

        return new UserPrincipal(savedMember, user.getAttributes());
    }

    private Member creatUserEntity(OAuth2UserInfo userInfo, ProviderType providerType) {
        Member member = new Member();
        member.setProviderType(providerType);
        member.setEmail(userInfo.getEmail());
        member.setNickName(userInfo.getName());
        member.setProfileUrl(userInfo.getImageUrl());
        member.setCreatedAt(LocalDateTime.now());
        return member;
    }

}
