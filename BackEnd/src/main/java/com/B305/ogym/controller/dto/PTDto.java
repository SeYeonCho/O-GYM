package com.B305.ogym.controller.dto;

import com.B305.ogym.domain.users.ptTeacher.Career;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
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
        public reservationRequest(String ptTeacherEmail, LocalDateTime reservationTime){
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
        private List<Map<String,String>> filter;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetTeacherReservationListResponse {
        private List<Reservation> teacherList;
        static class Reservation{
            private String username;
            private String nickname;
            private int gender;
            private String profile_url;
            private int price;
            private String sns_addrs;
            private String major;
            private float star_rating;
            private String description;
            private List<Career> careers ;
            private List<ReservationTime> alreadyReserveTime;

        }
        static class ReservationTime{
            private String reservationId;
            private LocalDateTime time;
        }
    }


}
