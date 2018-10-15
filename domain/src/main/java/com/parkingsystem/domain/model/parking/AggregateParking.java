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
            ParkingError.PARKING_ADDRESS_IS_NOT_EXIST_1002.doThrow();

        if (!parkingLot.getIsEnabled())
            ParkingError.PARKING_LOT_IS_NOT_WORKING_1007.doThrow();

        final UserEntity user = userRepository.find(newSession.getLicensePlateNumber());
        if (user == null)
            ParkingError.LICENSE_NUMBER_NOT_EXIST_1003.doThrow();

        final SessionEntity openSession = sessionRepository.findNotClosedBy(newSession.getLicensePlateNumber());
        if (openSession != null) {
            log.error(MessageFormat.format("System contain not ended parking session for user=[{0}] for licensePlateNumber=[{1}]", user.getEmail(), newSession.getLicensePlateNumber()));
            ParkingError.LICENSE_NUMBER_HAVE_OPEN_SESSION_1004.doThrow();
        }

        final BigDecimal userBalance = user.getBalance();
        if (userBalance.compareTo(minimalAmount) <= 0) {
            if (sessionRepository.findAnyOneByUser(user) == null)
                ParkingError.USER_BALANCE_TOO_LOW_FOR_OPEN_SESSION_1005.doThrow();

            if (userBalance.compareTo(minimalAmountForCredit) <= 0)
                ParkingError.CREDIT_LIMIT_TO_BIG_FOR_OPEN_SESSION_1006.doThrow();
        }

        if (ApiVersion.V2.equals(apiVersion)) log.info("execute some api v2 logic");
        sessionRepository.save(new SessionEntity(user, roundInterval, tariff, userBalance, minimalAmount, minimalAmountForCredit, newSession.getLicensePlateNumber()));
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
            log.warn(MessageFormat.format("User try ride out with fake licPlate number=[{0}]", licencePlateNumber));
            ParkingError.LICENSE_NUMBER_NO_HAVE_OPEN_SESSION_1055.doThrow();
        }

        if (ApiVersion.V2.equals(apiVersion)) log.info("execute some api v2 logic");

        final BigDecimal totalCost = priceEngine.calc(session.getStartedAt(), now, session.getRoundInterval(), session.getTariff());
        UserEntity user = session.getUser();
        BigDecimal resultBalance = user.getBalance().subtract(totalCost);
        user.setBalance(resultBalance);
        userRepository.save(user);

        session.setEndedAt(now);
        session.setTotalCost(totalCost);
        sessionRepository.save(session);

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
        StringBuilder message = new StringBuilder();
        message.append("<h1>Dear ").append(session.getUser().getFirstName()).append(" ").append(session.getUser().getLastName()).append(" </h1>")
                .append("<h2>You are using TEST_PARKING:</h2>")
                .append("<p>session start time: ").append(session.getStartedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("</p>")
                .append("<p>stop time: ").append(session.getStartedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("</p>")
                .append("<p>vehicle number: ").append(session.getLicensePlateNumber()).append("</p>")
                .append("<p>total cost of the sessioncharged: ").append(session.getTotalCost()).append("</p>");
        return message.toString();
    }
}
