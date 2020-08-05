package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.view.TicketDetails;
import com.interswitch.smartmoveserver.repository.TicketRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

/*
 * Created by adebola.owolabi on 7/27/2020
 */
@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManifestService manifestService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private TripService tripService;

    @Autowired
    PageUtil pageUtil;

    public List<Route> getRoutes() {
        /*List<Route> routes = new ArrayList<>();
        Route route = new Route();
        route.setId(1);
        route.setName("Ojota - Oshodi");
        route.setStartTerminalId(2L);
        route.setStopTerminalId(1L);
        route.setPrice(200);
        routes.add(route);*/
        return routeService.getAll();
    }

    public List<Trip> getTrips() {
        /*List<Trip> trips = new ArrayList<>();
        Route route = new Route();
        route.setId(1);
        route.setName("Ojota - Oshodi");
        route.setStartTerminalId(2L);
        route.setStopTerminalId(1L);
        route.setPrice(200);

        Vehicle vehicle = new Vehicle();
        vehicle.setName("Vehicle One");

        Trip trip = new Trip();
        trip.setId(1);
        trip.setReferenceNo(UUID.randomUUID().toString());
        DateFormat format = new SimpleDateFormat("MMM dd yyyy HH:mm aa");
        Date dateobj = new Date();
        trip.setArrival(format.format(dateobj));
        trip.setDeparture(format.format(dateobj));
        trip.setDriver(new User());
        trip.setRoute(route);
        trip.setVehicle(vehicle);
        trip.setName(route.getName() + trip.getDeparture());
        trips.add(trip);*/
        return tripService.getAll();
    }

    public TicketDetails makeBooking(TicketDetails ticketDetails) {
        Route route = routeService.findById(Long.valueOf(ticketDetails.getRouteId()));
        ticketDetails.setRoute(route);
        Trip trip = tripService.findById(Long.valueOf(ticketDetails.getTripId()));
        ticketDetails.setTrip(trip);

        ticketDetails.setSeats(getAvailableSeats());
        ticketDetails.setCountries(getAllCountries());
        return ticketDetails;
    }


    public TicketDetails getTickets(TicketDetails ticketDetails) {
        //generate tickets from details
        String ref = UUID.randomUUID().toString().toUpperCase();
        ref = "TKT-" + ref.replace("-", "");
        ticketDetails.setReferenceNo(ref);
        ticketDetails.setTotalFare(ticketDetails.getRoute().getPrice());
        return ticketDetails;
    }

    public TicketDetails confirmTickets(String username, TicketDetails ticketDetails) {
        //confirm purchase, add details to manifest
        LocalDateTime bookingDate = LocalDateTime.now();
        ticketDetails.setBookingDate(bookingDate);
        User operator = new User();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) operator = user.get();
        Ticket ticket = new Ticket();
        ticket.setOperator(operator);
        ticket.setBookingDate(ticketDetails.getBookingDate());
        ticket.setPassengerName(ticketDetails.getName());
        ticket.setReferenceNo(ticketDetails.getReferenceNo());
        ticket.setSeatClass(ticketDetails.getSeatClass());
        ticket.setSeatNo(ticketDetails.getSeatNo());
        ticket.setTrip(ticketDetails.getTrip());
        ticket.setFare(ticketDetails.getTotalFare());

        ticketRepository.save(ticket);

        Manifest manifest = new Manifest();
        manifest.setName(ticketDetails.getName());
        manifest.setGender(ticketDetails.getGender());
        manifest.setContactEmail(ticketDetails.getContactEmail());
        manifest.setContactMobile(ticketDetails.getContactMobile());
        manifest.setIdCategory(ticketDetails.getIdCategory());
        manifest.setIdNumber(ticketDetails.getIdNumber());
        manifest.setNationality(ticketDetails.getNationality());
        manifest.setAddress(ticketDetails.getNationality());
        manifest.setNextOfKinMobile(ticketDetails.getNextOfKinMobile());
        manifest.setNextOfKinName(ticketDetails.getNextOfKinName());
        manifest.setTrip(ticketDetails.getTrip());
        manifest.setSeatNo(ticketDetails.getSeatNo());
        manifestService.save(manifest);
        return ticketDetails;
    }

    public List<String> getAvailableSeats() {
        List<String> seats = new ArrayList<>();
        for (int i = 3; i <= 18; i++) {
            seats.add(String.valueOf(i));
        }
        return seats;
    }

    private List<String> getAllCountries() {
        List<String> countries = new ArrayList<>();
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("en", countryCode);
            countries.add(obj.getDisplayCountry());
        }
        return countries;
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
