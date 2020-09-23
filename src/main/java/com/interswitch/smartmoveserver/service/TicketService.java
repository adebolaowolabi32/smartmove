package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.view.Passenger;
import com.interswitch.smartmoveserver.model.view.ReassignTicket;
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
import java.time.LocalDate;
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

    public TicketDetails makeBooking(String scheduleId, int noOfPassengers) {
        TicketDetails ticketDetails = new TicketDetails();
        Schedule schedule = scheduleService.findById(Long.valueOf(scheduleId));
        ticketDetails.setSchedule(schedule);
        ticketDetails.setNoOfPassengers(noOfPassengers);
        ticketDetails.setSeats(this.getAvailableSeats());
        ticketDetails.setCountries(stateService.findAllCountries());
        ticketDetails.setPassengers(this.initializePassengerList(noOfPassengers));

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
        logger.info("Passengers:");
        logger.info(passengers);
        for (Passenger pass : passengers) {
            Ticket ticket = this.populateTicket(ticketDetails, pass);
            //ticket.setTrip(ticketDetails.getTrip());
            ticket.setSchedule(ticketDetails.getSchedule());
            ticket.setFare(ticketDetails.getSchedule().getFare());
            totalFare += ticket.getFare();
            tickets.add(ticket);
            if (ticketDetails.getReturnSchedule() != null) {
                Ticket returnTicket = this.populateTicket(ticketDetails, pass);
                //returnTicket.setTrip(ticketDetails.getTrip());
                returnTicket.setSchedule(ticketDetails.getReturnSchedule());
                returnTicket.setFare(ticketDetails.getReturnSchedule().getFare());
                totalFare += returnTicket.getFare();
                tickets.add(returnTicket);
            }
        }
        ticketDetails.setTickets(tickets);
        ticketDetails.setTotalFare(totalFare);

        return ticketDetails;
    }

    public TicketDetails confirmTickets(String username, TicketDetails ticketDetails) {
        this.saveAll(ticketDetails.getTickets());
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
        ticket.setReferenceNo(this.getTicketReference());
        ticket.setBookingDate(DateUtil.formatDate(LocalDateTime.now()));
        this.save(ticket);
        tickets.add(ticket);
        ticketDetails.setTickets(tickets);
        Manifest manifest = manifestService.findByScheduleIdAndName(fromSchedule.getId(), ticket.getPassengerName());
        manifest.setSchedule(toSchedule);
        manifestService.update(manifest);
        return ticketDetails;
    }

    private Ticket populateTicket(TicketDetails ticketDetails, Passenger pass) {
        Ticket ticket = new Ticket();
        ticket.setOperator(ticketDetails.getOperator());
        ticket.setBookingDate(ticketDetails.getBookingDate());
        ticket.setPassengerName(pass.getName());
        ticket.setReferenceNo(this.getTicketReference());
        ticket.setSeatClass(pass.getSeatClass());
        ticket.setSeatNo(pass.getSeatNo());
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

    private String getTicketReference() {
        //get operator prefix
        return "AKT-" + RandomUtil.getRandomNumber();
    }

    public Ticket save(Principal principal, Ticket ticket) {
        Optional<User> owner = userRepository.findByUsername(principal.getName());
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

    public Page<Ticket> findAllByOperator(Principal principal, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent())
            return ticketRepository.findAllByOperator(pageable, user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Owner not found");
    }
}
