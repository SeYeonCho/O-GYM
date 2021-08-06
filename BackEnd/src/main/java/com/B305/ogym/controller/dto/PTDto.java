package com.B305.ogym.controller.dto;

import com.B305.ogym.domain.users.ptTeacher.Career;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

public class PTDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SaveReservationRequest {

        @NotNull
        private String ptTeacherEmail;

        @FutureOrPresent(message = "잘못된 입력입니다")
        private LocalDateTime reservationTime;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CancelReservationRequest {
        @NotNull
        private int reservationId;
    }

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
