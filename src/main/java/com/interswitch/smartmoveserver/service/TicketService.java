package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.view.Passenger;
import com.interswitch.smartmoveserver.model.view.ScheduleBooking;
import com.interswitch.smartmoveserver.model.view.TicketDetails;
import com.interswitch.smartmoveserver.repository.TicketRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.DateUtil;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.RandomUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Created by adebola.owolabi on 7/27/2020
 */
@Service
public class TicketService {
    private final Log logger = LogFactory.getLog(getClass());

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
    PageUtil pageUtil;

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
        List<Schedule> schedules = scheduleService.findAll();
        List<Schedule> scheduleResults = schedules.stream()
                .filter(s -> s.getStartTerminal().getName().equals(scheduleBooking.getStartTerminal()) && s.getStopTerminal().getName()
                        .equals(scheduleBooking.getStopTerminal()) && s.getDepartureDate().equals(scheduleBooking.getDeparture())).collect(Collectors.toList());
        scheduleBooking.setSchedules(scheduleResults);
        return scheduleBooking;
    }

    public TicketDetails makeBooking(String scheduleId, int noOfPassengers) {
        TicketDetails ticketDetails = new TicketDetails();
        Schedule schedule = scheduleService.findById(Long.valueOf(scheduleId));
        ticketDetails.setSchedule(schedule);
        ticketDetails.setNoOfPassengers(noOfPassengers);
        ticketDetails.setSeats(getAvailableSeats());
        ticketDetails.setCountries(stateService.findAllCountries());
        ticketDetails.setPassengers(initializePassengerList(noOfPassengers));
        return ticketDetails;
    }

    public TicketDetails getTickets(String username, TicketDetails ticketDetails) {
        //generate tickets from details
        double fare = ticketDetails.getSchedule().getFare();
        String bookingDate = DateUtil.formatDate(LocalDateTime.now());
        ticketDetails.setBookingDate(bookingDate);

        User operator = new User();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) //and if user is operator
            operator = user.get();
        List<Ticket> tickets = new ArrayList<>();
        /*Passenger passenger = new Passenger();
        passenger.setGender(ticketDetails.getGender());
        passenger.setIdCategory(ticketDetails.getIdCategory());
        passenger.setIdNumber(ticketDetails.getIdNumber());
        passenger.setName(ticketDetails.getName());
        passenger.setNationality(ticketDetails.getNationality());
        passenger.setSeatClass(ticketDetails.getSeatClass());
        passenger.setSeatNo(ticketDetails.getSeatNo());
        ticketDetails.setPassenger(passenger);*/
        List<Passenger> passengers = ticketDetails.getPassengers();
        logger.info("Passengers:");
        logger.info(passengers);
        for (Passenger pass : passengers) {
            Ticket ticket = new Ticket();
            ticket.setOperator(operator);
            ticket.setBookingDate(bookingDate);
            ticket.setPassengerName(pass.getName());
            ticket.setReferenceNo(getTicketReference());
            ticket.setSeatClass(pass.getSeatClass());
            ticket.setSeatNo(pass.getSeatNo());
            //ticket.setTrip(ticketDetails.getTrip());
            ticket.setSchedule(ticketDetails.getSchedule());
            ticket.setFare(fare);
            tickets.add(ticket);
        }
        ticketDetails.setTickets(tickets);
        ticketDetails.setTotalFare(fare * ticketDetails.getNoOfPassengers());

        return ticketDetails;
    }

    public TicketDetails confirmTickets(String username, TicketDetails ticketDetails) {
        ticketRepository.saveAll(ticketDetails.getTickets());
        List<Passenger> passengers = ticketDetails.getPassengers();
        for (Passenger passenger : passengers) {
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
            manifest.setTrip(ticketDetails.getTrip());
            manifest.setSchedule(ticketDetails.getSchedule());
            manifestService.save(manifest);
        }

        Transaction transaction = new Transaction();
        transaction.setVehicleId(ticketDetails.getSchedule().getVehicle().getName());
        transaction.setTerminalId(String.valueOf(ticketDetails.getSchedule().getId()));
        transaction.setOperatorId(username);
        transaction.setType(Enum.TransactionType.TICKET_SALE);
        transaction.setMode(ticketDetails.getSchedule().getMode());
        transaction.setAmount(ticketDetails.getTotalFare());
        transaction.setTransactionDateTime(LocalDateTime.now());
        transactionService.saveTransaction(transaction);
        return ticketDetails;
    }

    public List<Passenger> initializePassengerList(int noOfPassengers) {
        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < noOfPassengers; i++) {
            passengers.add(new Passenger());
        }
        return passengers;
    }

    public List<String> getAvailableSeats() {
        List<String> seats = new ArrayList<>();
        for (int i = 3; i <= 18; i++) {
            seats.add(String.valueOf(i));
        }
        return seats;
    }

    public String getTicketReference() {
        //get operator prefix
        return "AKT-" + RandomUtil.getRandomNumber(6);
    }

    public Ticket save(Principal principal, Ticket ticket) {
        Optional<User> owner = userRepository.findByUsername(principal.getName());
        if (owner.isPresent()) ticket.setOperator(owner.get());
        return ticketRepository.save(ticket);
    }

    public Ticket findById(long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket does not exist"));
    }

    public Page<Ticket> findAllByOperator(Principal principal, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent())
            return ticketRepository.findAllByOperator(pageable, user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }
}
