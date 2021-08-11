package com.B305.ogym.controller.dto;

import com.B305.ogym.domain.users.common.Gender;
import com.B305.ogym.domain.users.common.ProfilePicture;
import com.B305.ogym.domain.users.ptTeacher.Career;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import com.B305.ogym.domain.users.ptTeacher.Sns;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

public class PTDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class reservationRequest {

        @NotNull
        private String ptTeacherEmail;

        @FutureOrPresent(message = "현재/미래만 입력 가능합니다")
        private LocalDateTime reservationTime;

        @Builder
        public reservationRequest(String ptTeacherEmail, LocalDateTime reservationTime) {
            this.ptTeacherEmail = ptTeacherEmail;
            this.reservationTime = reservationTime;
        }

    }
//
//    @Getter
//    @NoArgsConstructor(access = AccessLevel.PROTECTED)
//    public static class CancelReservationRequest {
//        @NotNull
//        private String ptTeacherEmail;
//
//
//        private LocalDateTime reservationTime;
//    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetTeacherListRequest {

        @NotEmpty
        private List<Map<String, String>> filter;
    }


    /////////////////////////////////////////////////////
    // 모든 선생님의 리스트를 출력
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class AllTeacherListResponse {

        private List<PTTeacherDto> teacherList;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PTTeacherDto {

        // 이름
        private String username;

        // 성별
        private Gender gender;

        // 닉네임
        private String nickname;

        // 연락처
        private String tel;

        // 이메일
        private String email;

        // 프로필사진
        private ProfilePicture profilePicture;

        // 평점
        private float starRating;

        // 전문분야
        private String major;

        // 가격
        private int price;

        // 자기소개
        private String description;

        // sns 정보
        private List<SnsDto> snsList = new ArrayList<>();

        // 커리어 정보
        private List<CareerDto> careers = new ArrayList<>();

        // 예약된 시간정보
        private List<LocalDateTime> reservations = new ArrayList<>();

        // 자격증 정보
        private  List<CertificateDto> certificates = new ArrayList<>();


    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CareerDto {

        private String role;
        private LocalDate startDate;
        private LocalDate endDate;
        private String company;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CertificateDto {

        private String name;
        private String publisher;
        private LocalDate date;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SnsDto {

        private String url;
        private String platform;
    }

    /////////////////////////////////////////////////////////////


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetTeacherReservationListResponse {

        private List<Reservation> teacherList;

        static class Reservation {

            private String username;
            private String nickname;
            private int gender;
            private String profile_url;
            private int price;
            private String sns_addrs;
            private String major;
            private float star_rating;
            private String description;
            private List<Career> careers;
            private List<ReservationTime> alreadyReserveTime;

        }

        static class ReservationTime {

            private String reservationId;
            private LocalDateTime time;
        }
    }


}
