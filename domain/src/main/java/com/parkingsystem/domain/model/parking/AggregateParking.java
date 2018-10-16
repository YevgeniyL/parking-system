package com.parkingsystem.domain.model.parking;

import com.parkingsystem.domain.errors.ParkingError;
import com.parkingsystem.domain.model.management.ParkingLotEntity;
import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import com.parkingsystem.domain.repository.SessionRepository;
import com.parkingsystem.domain.repository.UserRepository;
import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.EmailService;
import com.parkingsystem.domain.sevice.PriceEngine;
import com.parkingsystem.domain.sevice.parking.ParkingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class AggregateParking implements ParkingService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Value("${price.minimalAmount}")
    private BigDecimal minimalAmount;

    @Value("${price.minimalAmountForCredit}")
    private BigDecimal minimalAmountForCredit;

    @Value("${price.tariff}")
    private BigDecimal tariff;

    @Value("${price.roundInterval}")
    private int roundInterval;

    @Autowired
    private PriceEngine priceEngine;

    @Autowired
    private EmailService emailService;

    @Override
    public void createSession(ApiVersion apiVersion, NewSession newSession, String parkingAddress) {
        if (StringUtils.isEmpty(newSession.getLicensePlateNumber()))
            ParkingError.IS_EMPTY_LICENSE_NUMBER_1001.doThrow();

        final ParkingLotEntity parkingLot = parkingLotRepository.findBy(parkingAddress);
        if (parkingLot == null)
            ParkingError.PARKING_ADDRESS_IS_NOT_EXIST_1002.doThrow("requested parkingAddress=" + parkingAddress);

        if (!parkingLot.getIsEnabled())
            ParkingError.PARKING_LOT_IS_NOT_WORKING_1007.doThrow();

        final UserEntity user = userRepository.find(newSession.getLicensePlateNumber());
        if (user == null)
            ParkingError.LICENSE_NUMBER_NOT_EXIST_1003.doThrow("requested licPlateNumber=" + newSession.getLicensePlateNumber());

        final SessionEntity openSession = sessionRepository.findNotClosedBy(newSession.getLicensePlateNumber());
        if (openSession != null)
            ParkingError.LICENSE_NUMBER_HAVE_OPEN_SESSION_1004
                    .doThrow(MessageFormat.format("requested licensePlateNumber=[{0}] but system contain not ended parking session with user=[{1}] and licensePlateNumber=[{1}]", newSession.getLicensePlateNumber(), user.getEmail(), openSession.getLicensePlateNumber()));

        final BigDecimal userBalance = user.getBalance();
        if (userBalance.compareTo(minimalAmount) <= 0) {
            if (sessionRepository.findAnyOneByUser(user) == null)
                ParkingError.USER_BALANCE_TOO_LOW_FOR_OPEN_SESSION_1005
                        .doThrow(MessageFormat.format("user have no one sessions on past and try to open new session with balance=[{0}], but system have minimal amount=[{1}]", user.getBalance(), minimalAmount));

            if (userBalance.compareTo(minimalAmountForCredit) <= 0)
                ParkingError.CREDIT_LIMIT_TO_BIG_FOR_OPEN_SESSION_1006
                        .doThrow(MessageFormat.format("user have sessions on past, but try to open new session with balance=[{0}], but system have minimal credit amount=[{1}]", user.getBalance(), minimalAmountForCredit));
        }

        if (ApiVersion.V2.equals(apiVersion)) log.info("execute some api v2 logic");
        sessionRepository.save(new SessionEntity(user, parkingLot, roundInterval, tariff, userBalance, minimalAmount, minimalAmountForCredit, newSession.getLicensePlateNumber()));
    }

    @Override
    public CloseSessionResponse closeSession(ApiVersion apiVersion, CloseSession closeSessionRequest, String parkingAddress, String licencePlateNumber, LocalDateTime now) {
        if (StringUtils.isEmpty(closeSessionRequest.getStatus()))
            ParkingError.IS_EMPTY_STATUS_1051.doThrow();
        if (!closeSessionRequest.getStatus().equals("stopped"))
            ParkingError.IS_NOT_STOPPED_STATUS_1053.doThrow();

        final ParkingLotEntity parkingLot = parkingLotRepository.findBy(parkingAddress);
        if (parkingLot == null)
            ParkingError.PARKING_ADDRESS_IS_NOT_EXIST_1054.doThrow();

        SessionEntity session = sessionRepository.findStartedAndNotClosedSessionBy(licencePlateNumber);
        if (session == null) {
            ParkingError.LICENSE_NUMBER_NO_HAVE_OPEN_SESSION_1055.doThrow(MessageFormat.format("User try ride out with fake licPlate number=[{0}]", licencePlateNumber));
        }

        if (ApiVersion.V2.equals(apiVersion)) log.info("execute some api v2 logic");

        UserEntity user = session.getUser();
        final BigDecimal totalCost = priceEngine.calc(session.getStartedAt(), now, session.getRoundInterval(), session.getTariff());
        final BigDecimal firstBalance = user.getBalance();
        final BigDecimal resultBalance = firstBalance.subtract(totalCost).setScale(2, RoundingMode.HALF_UP);
        user.setBalance(resultBalance);
        userRepository.save(user);

        session.setEndedAt(now);
        session.setTotalCost(totalCost);
        sessionRepository.save(session);
        log.info(String.format("User %s closed session with total cost =[%s]. Balance before parking =[%s], after close session =[%s]. Session id=[%s].", user.getEmail(), totalCost, firstBalance, resultBalance, session.getId()));

        new Thread(() -> sendEmail(new EmailMessage(buildCloseSessionText(session), user.getEmail(), "Invoice from test parking system", session.getId()))).run();
        return new CloseSessionResponse("stopped", totalCost, session.getStartedAt(), now);
    }

    private void sendEmail(EmailMessage emailMessage) {
        try {
            emailService.sendMessage(emailMessage);
        } catch (Exception e) {
            log.error(MessageFormat.format("Error on sending email after close parking session to [{0}]", emailMessage.getSendTo()));
        }
    }

    private String buildCloseSessionText(SessionEntity session) {
        return "<h1>Dear " + session.getUser().getFirstName() + " " + session.getUser().getLastName() + " </h1>" +
                "<h2>You are using TEST_PARKING:" + session.getParkingLot().getAddress() + "</h2>" +
                "<p>session start time: " + session.getStartedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "</p>" +
                "<p>stop time: " + session.getStartedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "</p>" +
                "<p>vehicle number: " + session.getLicensePlateNumber() + "</p>" +
                "<p>total cost of the sessioncharged: " + session.getTotalCost() + "</p>";
    }
}
