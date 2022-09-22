package com.nikitoe.stockdividend.service;

import com.nikitoe.stockdividend.exception.impl.AlreadyExistUserException;
import com.nikitoe.stockdividend.exception.impl.NoCorrectUserPassword;
import com.nikitoe.stockdividend.exception.impl.NoUserException;
import com.nikitoe.stockdividend.model.Auth;
import com.nikitoe.stockdividend.model.Auth.SignUpResult;
import com.nikitoe.stockdividend.persist.MemberRepository;
import com.nikitoe.stockdividend.persist.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.memberRepository.findByUsername(username)
            .orElseThrow(() -> new NoUserException());

    }

    public SignUpResult register(Auth.SignUp member) {

        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if (exists) {
            throw new AlreadyExistUserException();
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        MemberEntity memberEntity = this.memberRepository.save(member.toEntity());

        return SignUpResult.of(memberEntity);
    }

    public SignUpResult authenticate(Auth.SignIn member) {
        MemberEntity memberEntity = this.memberRepository.findByUsername(member.getUsername())
            .orElseThrow(() -> new NoUserException());

        if (!this.passwordEncoder.matches(member.getPassword(), memberEntity.getPassword())) {
            throw new NoCorrectUserPassword();
        }

        return SignUpResult.of(memberEntity);
    }
}
