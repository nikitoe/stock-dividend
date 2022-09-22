package com.nikitoe.stockdividend.model;

import com.nikitoe.stockdividend.persist.entity.MemberEntity;
import java.util.List;
import lombok.Builder;
import lombok.Data;

public class Auth {

    @Data
    public static class SignIn {

        private String username;
        private String password;

    }

    @Data
    @Builder
    public static class SignUp {

        private String username;
        private String password;
        private List<String> roles;

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                .username(this.username)
                .password(this.password)
                .roles(this.roles)
                .build();
        }
    }

    @Data
    @Builder
    public static class SignUpResult {

        private String username;
        private List<String> roles;

        public static SignUpResult of(MemberEntity member) {
            return SignUpResult.builder()
                .username(member.getUsername())
                .roles(member.getRoles())
                .build();
        }
    }

}
