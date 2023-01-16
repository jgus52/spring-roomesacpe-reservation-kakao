package kakao.domain;

import domain.Reservation;
import domain.ReservationValidator;
import kakao.dto.request.CreateReservationRequest;
import kakao.error.exception.DuplicatedReservationException;
import kakao.error.exception.IllegalCreateReservationRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class ReservationValidatorTest {
    @DisplayName("날짜보다 이전으로 request 요청을 보내면 InvalidRequest 예외를 보낸다")
    @Test
    void createInvalidDate() {
        CreateReservationRequest request = new CreateReservationRequest(
                LocalDate.of(2022, 10, 23),
                LocalTime.of(13, 00),
                "baker",
                1L
        );

        ReservationValidator validator = new ReservationValidator(new ArrayList<>());

        Assertions.assertThatExceptionOfType(IllegalCreateReservationRequestException.class)
                .isThrownBy(() -> validator.validateForCreate(request));
    }

    @DisplayName("같은 날짜의 예약이 존재하면 DuplicateReservation 예외를 발생한다.")
    @Test
    void duplicateCreate() {
        CreateReservationRequest request = new CreateReservationRequest(
                LocalDate.of(2023, 10, 23),
                LocalTime.of(13, 00),
                "baker",
                1L
        );

        ReservationValidator validator = new ReservationValidator(List.of(
                Reservation.builder()
                        .date(LocalDate.of(2023, 10, 23))
                        .time(LocalTime.of(13, 00)).build()));

        Assertions.assertThatExceptionOfType(DuplicatedReservationException.class)
                .isThrownBy(() -> validator.validateForCreate(request));
    }
}
