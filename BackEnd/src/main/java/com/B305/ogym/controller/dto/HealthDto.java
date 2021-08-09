package com.B305.ogym.controller.dto;

import com.B305.ogym.domain.users.common.Gender;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HealthDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyHealthResponse {

        private List<Integer> heightList;
        private List<Integer> weightList;

        @Builder
        public MyHealthResponse(List<Integer> heightList, List<Integer> weightList) {
            this.heightList = heightList;
            this.weightList = weightList;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Data
    public static class MyStudentsHealthListResponse {

        private List<StudentHealth> studentHealthList;


    }

    @NoArgsConstructor
    @Data
    public static class StudentHealth {

        private String username;
        private String nickname;
        private int age;
        private Gender gender;
        private String profileUrl;
        private List<Integer> heightList;
        private List<Integer> weightList;

        public void addHeight(int height) {
            if (heightList == null) {
                heightList = new ArrayList<>();
            }
            heightList.add(height);
        }

        public void addWeight(int weight) {
            if (weightList == null) {
                weightList = new ArrayList<>();
            }
            weightList.add(weight);
        }
    }

}
