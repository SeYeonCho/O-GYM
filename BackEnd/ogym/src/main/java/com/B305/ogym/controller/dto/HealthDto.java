package com.B305.ogym.controller.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HealthDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetMyHealthResponse {
        private List<Integer> heightList;
        private List<Integer> weightList;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetMyStudentsHealthListResponse {
        private List<StudentHealth> studentHealthList;
        static class StudentHealth{
            private String username;
            private String nickname;
            private int age ;
            private int gender ;
            private String profileUrl;
            private List<Integer> heightList;
            private List<Integer> weightList;
        }
    }
}
