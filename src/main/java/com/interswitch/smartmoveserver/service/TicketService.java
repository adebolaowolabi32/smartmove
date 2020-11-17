package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.view.*;
import com.interswitch.smartmoveserver.repository.TicketRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.DateUtil;
import com.interswitch.smartmoveserver.util.NumberFormatter;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Created by adebola.owolabi on 7/27/2020
 */
@Slf4j
@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManifestService manifestService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private TripService tripService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StateService stateService;

    @Autowired
    private TicketTillService ticketTillService;

    private static String PREFIX_SEPARATOR = "|";

    @Autowired
    private TicketReferenceService ticketReferenceService;
    @Autowired
    PageUtil pageUtil;
    @Autowired
    private FeeConfigurationService feeConfigurationService;

    public List<Terminal> getTerminals() {
        return terminalService.findAll();
    }

    public List<Schedule> getSchedules() {
        return scheduleService.findAll();
    }

    public ScheduleBooking findBooking(String username, ScheduleBooking scheduleBooking) {
        User operator = new User();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) //and if user is operator
            operator = user.get();
        //List<Schedule> schedules = scheduleService.findByOwner);
        LocalDate returnDate = scheduleBooking.getReturnDate();
        if (returnDate != null) scheduleBooking.setRoundTrip(true);
        //make sure to search by operator
        List<Schedule> schedules = scheduleService.findAll();
        List<Schedule> scheduleResults = schedules.stream()
                .filter(s -> s.getStartTerminal().getName().equals(scheduleBooking.getStartTerminal()) && s.getStopTerminal().getName()
                        .equals(scheduleBooking.getStopTerminal()) && s.getDepartureDate().equals(scheduleBooking.getDeparture())).collect(Collectors.toList());
        List<Schedule> returnScheduleResults = schedules.stream()
                .filter(s -> s.getStartTerminal().getName().equals(scheduleBooking.getStopTerminal()) && s.getStopTerminal().getName()
                        .equals(scheduleBooking.getStartTerminal()) && s.getDepartureDate().equals(scheduleBooking.getReturnDate())).collect(Collectors.toList());
        scheduleBooking.setSchedules(scheduleResults);
        scheduleBooking.setReturnSchedules(returnScheduleResults);
        return scheduleBooking;
    }

    public TicketDetails makeBooking(String username, String scheduleId, int noOfPassengers) {

        log.info("username===>" + username);
        User systemUser = new User();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) //and if user is operator
            systemUser = user.get();

        TicketDetails ticketDetails = new TicketDetails();
        Schedule schedule = scheduleService.findById(Long.valueOf(scheduleId));
        ticketDetails.setSchedule(schedule);
        ticketDetails.setNoOfPassengers(noOfPassengers);
        ticketDetails.setSeats(this.getAvailableSeats());
        ticketDetails.setCountries(stateService.findAllCountries());
        ticketDetails.setPassengers(this.initializePassengerList(noOfPassengers));

        String transportOperatorUsername = (systemUser.getRole() == Enum.Role.OPERATOR || systemUser.getRole() == Enum.Role.ISW_ADMIN) ?
                systemUser.getUsername() : systemUser.getOwner() != null ? systemUser.getOwner().getUsername() : "";

        log.info("Transport Operator username===>" + transportOperatorUsername);

        List<FeeConfiguration> feeConfigurationList = feeConfigurationService.findEnabledFeeConfigByOperatorUsername(transportOperatorUsername);
        log.info("feeConfigurationList===>" + feeConfigurationList);
        //add FeeConfiguration list to the ticketDetails
        ticketDetails.setFees(feeConfigurationList);
        return ticketDetails;
    }

    public TicketDetails makeReturnBooking(TicketDetails ticketDetails, String scheduleId) {
        Schedule schedule = scheduleService.findById(Long.valueOf(scheduleId));
        ticketDetails.setReturnSchedule(schedule);
        return ticketDetails;
    }

    public TicketDetails getTickets(String username, TicketDetails ticketDetails) {
        //generate tickets from details
        String bookingDate = DateUtil.formatDate(LocalDateTime.now());
        ticketDetails.setBookingDate(bookingDate);
        User operator = new User();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) //and if user is operator
            operator = user.get();
        ticketDetails.setOperator(operator);
        double totalFare = 0;
        List<Ticket> tickets = new ArrayList<>();
        List<Passenger> passengers = ticketDetails.getPassengers();

        for (Passenger pass : passengers) {
            Schedule schedule = ticketDetails.getSchedule();
            Ticket ticket = this.populateTicket(ticketDetails, schedule, pass, username);

            totalFare += this.applyConfiguredFees(ticketDetails,ticket,pass);
            tickets.add(ticket);

            Schedule returnSchedule = ticketDetails.getReturnSchedule();
            if (ticketDetails.getReturnSchedule() != null) {
                Ticket returnTicket = this.populateTicket(ticketDetails, returnSchedule, pass, username);

                totalFare += this.applyConfiguredFees(ticketDetails,returnTicket,pass);
                tickets.add(returnTicket);

            }

        }
        log.info("AppliedFeeDetails==>" + ticketDetails.getAppliedFees());
        ticketDetails.setTickets(tickets);
        ticketDetails.setTotalFare(totalFare);
        return ticketDetails;
    }

    public TicketDetails confirmTickets(String username, TicketDetails ticketDetails) {
        Iterable<Ticket> savedTicketsIterable = ticketRepository.saveAll(ticketDetails.getTickets());
        //this is an asynchronous event here running on another thread.
        ticketTillService.pushDataToTicketTill(savedTicketsIterable);
        List<Manifest> manifests = new ArrayList<>();
        List<Passenger> passengers = ticketDetails.getPassengers();
        for (Passenger passenger : passengers) {
            Manifest manifest = this.populateManifest(ticketDetails, passenger);
            //manifest.setTrip(ticketDetails.getTrip());
            manifest.setSchedule(ticketDetails.getSchedule());
            manifests.add(manifest);
            if (ticketDetails.getReturnSchedule() != null) {
                Manifest manifest1 = this.populateManifest(ticketDetails, passenger);
                //manifest1.setTrip(ticketDetails.getTrip());
                manifest1.setSchedule(ticketDetails.getReturnSchedule());
                manifests.add(manifest1);
            }
        }

        manifestService.saveAll(manifests);
        Transaction transaction = new Transaction();
        transaction.setVehicleId(ticketDetails.getSchedule().getVehicle().getName());
        transaction.setTerminalId(String.valueOf(ticketDetails.getSchedule().getId()));
        transaction.setOperatorId(username);
        transaction.setType(Enum.TransactionType.TICKET_SALE);
        transaction.setMode(ticketDetails.getSchedule().getMode());
        transaction.setAmount(ticketDetails.getTotalFare());
        transaction.setTransactionDateTime(LocalDateTime.now());
        //fields below are require to be non null,hence added empty strings
        transaction.setCardId("");
        transaction.setDeviceId("");
        transactionService.save(transaction);
        return ticketDetails;
    }

    public ScheduleBooking reassignTicket(String username, ReassignTicket reassignTicket) {
        User operator = new User();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) //and if user is operator
            operator = user.get();
        ScheduleBooking scheduleBooking = new ScheduleBooking();
        Ticket ticket = findByReferenceNo(reassignTicket.getReferenceNo().trim());
        if (ticket != null) {
            Schedule schedule = ticket.getSchedule();
            reassignTicket.setTicket(ticket);
            //List<Schedule> schedules = scheduleService.findByOwner);
            List<Schedule> schedules = scheduleService.findAll();
            List<Schedule> scheduleResults = schedules.stream()
                    .filter(s -> s.getStartTerminal().getName().equals(schedule.getStartTerminal().getName()) && s.getStopTerminal().getName()
                            .equals(schedule.getStopTerminal().getName()) && s.getDepartureDate().equals(schedule.getDepartureDate())).collect(Collectors.toList());
            scheduleBooking.setSchedules(scheduleResults);
        }
        else scheduleBooking.setInvalid(true);
        return scheduleBooking;
    }

    public TicketDetails confirmReassignment(String username, ReassignTicket reassignTicket, TicketDetails ticketDetails) {
        User operator = new User();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) //and if user is operator
            operator = user.get();
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = reassignTicket.getTicket();
        Schedule fromSchedule = ticket.getSchedule();
        Schedule toSchedule = ticketDetails.getSchedule();
        ticket.setSchedule(toSchedule);
        ticket.setReferenceNo(this.getTicketReference(toSchedule, username));
        ticket.setBookingDate(DateUtil.formatDate(LocalDateTime.now()));
        this.save(ticket);
        tickets.add(ticket);
        ticketDetails.setTickets(tickets);
        Manifest manifest = manifestService.findByScheduleIdAndName(fromSchedule.getId(), ticket.getPassengerName());
        manifest.setSchedule(toSchedule);
        manifestService.update(manifest);
        return ticketDetails;
    }

    private Ticket populateTicket(TicketDetails ticketDetails, Schedule schedule, Passenger pass, String principal) {
        Ticket ticket = new Ticket();
        ticket.setOperator(ticketDetails.getOperator());
        ticket.setBookingDate(ticketDetails.getBookingDate());
        ticket.setPassengerName(pass.getName());
        ticket.setReferenceNo(this.getTicketReference(schedule, principal));
        ticket.setSeatClass(pass.getSeatClass());
        ticket.setSeatNo(pass.getSeatNo());
        //ticket.setTrip(ticketDetails.getTrip());
        ticket.setSchedule(schedule);
        ticket.setFare(schedule.getFare());
        return ticket;
    }

    private Manifest populateManifest(TicketDetails ticketDetails, Passenger passenger) {
        Manifest manifest = new Manifest();
        manifest.setName(passenger.getName());
        manifest.setGender(passenger.getGender());
        manifest.setContactEmail(ticketDetails.getContactEmail());
        manifest.setContactMobile(ticketDetails.getContactMobile());
        manifest.setIdCategory(passenger.getIdCategory());
        manifest.setIdNumber(passenger.getIdNumber());
        manifest.setNationality(passenger.getNationality());
        //add address to data capture
        manifest.setAddress(passenger.getNationality());
        manifest.setSeatNo(passenger.getSeatNo());
        manifest.setNextOfKinMobile(ticketDetails.getNextOfKinMobile());
        manifest.setNextOfKinName(ticketDetails.getNextOfKinName());
        manifest.setBvn(ticketDetails.getBvn());
        return manifest;
    }

    private List<Passenger> initializePassengerList(int noOfPassengers) {
        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < noOfPassengers; i++) {
            passengers.add(new Passenger());
        }
        return passengers;
    }

    private List<String> getAvailableSeats() {
        List<String> seats = new ArrayList<>();
        for (int i = 3; i <= 18; i++) {
            seats.add(String.valueOf(i));
        }
        return seats;
    }

    private String getTicketReference(Schedule schedule, String principal) {
        //get operator prefix
        TicketReference ticketReference = ticketReferenceService.findByOwner(principal);
        String prefix = "";
        String startTerminal = "";
        String stopTerminal = "";
        if (ticketReference!=null && ticketReference.isEnabled()) {
            prefix = ticketReference.getPrefix() + PREFIX_SEPARATOR;
            if (ticketReference.isStartTerminalEnabled())
                startTerminal = schedule.getStartTerminal().getCode() + PREFIX_SEPARATOR;
            if (ticketReference.isStopTerminalEnabled())
                stopTerminal = schedule.getStopTerminal().getCode() + PREFIX_SEPARATOR;
        }
        return prefix + startTerminal + stopTerminal + RandomUtil.getRandomNumber(6);
    }


    public Ticket save(String principal, Ticket ticket) {
        Optional<User> owner = userRepository.findByUsername(principal);
        if (owner.isPresent()) ticket.setOperator(owner.get());
        return ticketRepository.save(ticket);
    }

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Iterable<Ticket> saveAll(List<Ticket> tickets) {
        return ticketRepository.saveAll(tickets);
    }

    public Ticket findById(long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket does not exist"));
    }

    public Ticket findByReferenceNo(String ref) {
        return ticketRepository.findByReferenceNo(ref);
    }

    public PageView<Ticket> findAllByOperator(int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal);
        if (user.isPresent()) {
            Page<Ticket> pages = ticketRepository.findAllByOperator(pageable, user.get());
            return new PageView<>(pages.getTotalElements(), pages.getContent());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Owner not found");
    }


    private double applyConfiguredFees(TicketDetails ticketDetails, Ticket ticket, Passenger passenger) {
        List<FeeConfiguration> feeConfigurationList = ticketDetails.getFees();
        List<FeeDetails> feeDetailList = new ArrayList<>();
        double totalFare = ticket.getFare();

        if (feeConfigurationList == null || feeConfigurationList.isEmpty()) {
            return totalFare;
        }

        for (FeeConfiguration feeConfiguration : feeConfigurationList) {
            if (feeConfiguration.getFeeName() == Enum.FeeName.ID_CARD_FEE && passenger.getIdCategory() != Enum.IdCategory.NO_ID) {
                //do not apply ID-CARD-FEE
                continue;
            } else {
                log.info("feeConfiguration getFeeName ===>" + feeConfiguration.getFeeName().name() + "CustomName::" + feeConfiguration.getFeeName().getCustomName());
                if (feeConfiguration.getRatingMetricType() == Enum.RatingMetricType.FLAT) {
                    feeDetailList.add(new FeeDetails(feeConfiguration.getFeeName().getCustomName(), NumberFormatter.getFormatted(feeConfiguration.getValue(),2)));
                    totalFare += feeConfiguration.getValue();
                } else if (feeConfiguration.getRatingMetricType() == Enum.RatingMetricType.PERCENT) {
                    double feeAmount = (feeConfiguration.getValue() / 100) * ticket.getFare();
                    feeDetailList.add(new FeeDetails(feeConfiguration.getFeeName().getCustomName(),  NumberFormatter.getFormatted(feeAmount,2)));
                    totalFare += feeAmount;
                }
            }
        }

        ticketDetails.setAppliedFees(feeDetailList);
        return totalFare;
    }


}