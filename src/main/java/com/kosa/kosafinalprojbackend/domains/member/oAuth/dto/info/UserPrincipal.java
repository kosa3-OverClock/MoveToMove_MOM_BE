package com.kosa.kosafinalprojbackend.domains.member.oAuth.dto.info;

import com.kosa.kosafinalprojbackend.domains.member.oAuth.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class UserPrincipal implements OAuth2User {
    // Security Context에 저장하고 이후 필요할 경우에 서비스에서 꺼내서 사용할 수 있다. 사용방법은 getMember()로 꺼내서 원하는 프로퍼티를 찾으면된다.
    private final Member member;
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return member.getNickName();
    }

}
