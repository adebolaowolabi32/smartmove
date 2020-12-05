package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.dto.ManifestDto;
import com.interswitch.smartmoveserver.repository.ManifestRepository;
import com.interswitch.smartmoveserver.util.DateUtil;
import com.interswitch.smartmoveserver.util.FileParser;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
public class ManifestService {

    @Autowired
    ManifestRepository manifestRepository;

    @Autowired
    TripService tripService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    SeatService seatService;

    @Autowired
    PageUtil pageUtil;

    public List<Manifest> findAll() {
        return manifestRepository.findAll();
    }

    public PageView<Manifest> findAllPaginated(int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Page<Manifest> pages = manifestRepository.findAll(pageable);
        return new PageView<>(pages.getTotalElements(), pages.getContent());
    }

    public PageView<Manifest> findPaginatedManifestByTripId(int page, int size, long tripId) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Page<Manifest> pages = manifestRepository.findByTripId(pageable, tripId);
        return new PageView<>(pages.getTotalElements(), pages.getContent());
    }

    public PageView<Manifest> findPaginatedManifestByScheduleId(int page, int size, long scheduleId) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Page<Manifest> pages = manifestRepository.findByScheduleId(pageable, scheduleId);
        return new PageView<>(pages.getTotalElements(), pages.getContent());
    }

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Manifest save(Manifest manifest) {
        return manifestRepository.save(manifest);
    }

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Manifest save(Manifest manifest, String principal) {
        return manifestRepository.save(buildManifest(manifest));
    }

    public Manifest findById(long id, String principal) {
        return manifestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manifest does not exist"));
    }

    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Manifest update(Manifest manifest) {
        Optional<Manifest> existing = manifestRepository.findById(manifest.getId());
        if (existing.isPresent())
            return manifestRepository.save(buildManifest(manifest));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manifest does not exist");
    }

    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Manifest update(Manifest manifest, String principal) {
        Optional<Manifest> existing = manifestRepository.findById(manifest.getId());
        if (existing.isPresent())
            return manifestRepository.save(buildManifest(manifest));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manifest does not exist");
    }

    public void delete(long id) {
        Optional<Manifest> existing = manifestRepository.findById(id);
        if (existing.isPresent())
            manifestRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manifest does not exist");
        }
    }
    public void delete(long id, String principal) {
        Optional<Manifest> existing = manifestRepository.findById(id);
        if (existing.isPresent())
            manifestRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manifest does not exist");
        }
    }

    public Manifest findByTripIdAndName(long tripId, String name) {
        return manifestRepository.findByTripIdAndName(tripId, name);
    }

    public Manifest findByScheduleIdAndName(long scheduleId, String name) {
        return manifestRepository.findByScheduleIdAndName(scheduleId, name);
    }

    public List<Manifest> findByScheduleId(long scheduleId) {
        return manifestRepository.findAllByScheduleId(scheduleId);
    }

    public Iterable<Manifest> saveAll(List<Manifest> manifests) {
        return manifestRepository.saveAll(manifests);
    }

    public Long countAll() {
        return manifestRepository.count();
    }

    public void deleteAll() {
        manifestRepository.deleteAll();
    }

    private Manifest buildManifest(Manifest manifest) {
        Trip trip = manifest.getTrip();
        if(trip != null)
            manifest.setTrip(tripService.findById(trip.getId()));

        Schedule schedule = manifest.getSchedule();
        if(schedule != null)
            manifest.setSchedule(scheduleService.findById(schedule.getId()));

        return manifest;
    }

    public List<Manifest> upload(MultipartFile file, Trip trip, Schedule schedule) throws IOException {

        FileParser<ManifestDto> fileParser = new FileParser<>();
        List<Manifest> savedManifests = new ArrayList<>();
        List<ManifestDto> manifestDtoList = fileParser.parseFileToEntity(file, ManifestDto.class);
        manifestDtoList.forEach(manifestDto->{

            Manifest manifest = mapToManifest(manifestDto);
            if(trip!=null){
                manifest.setTrip(trip);
            }
            else{
                manifest.setSchedule(schedule);
            }
            savedManifests.add(manifestRepository.save(manifest));
        });

        return savedManifests;
    }

    private Manifest mapToManifest(ManifestDto dto){
        return Manifest.builder()
                .address(dto.getAddress())
                .boarded(dto.getBoarded().startsWith("T") || dto.getBoarded().startsWith("t"))
                .bvn(dto.getBvn())
                .completed(dto.getCompleted().startsWith("T") || dto.getCompleted().startsWith("t"))
                .contactEmail(dto.getContactEmail())
                .gender(dto.getGender())
                .id(0)
                .idNumber(dto.getIdNumber())
                .idCategory(convertToCategoryEnum(dto.getIdCategory()))
                .name(dto.getName())
                .nationality(dto.getNationality())
                .nextOfKinMobile(dto.getNextOfKinMobile())
                .nextOfKinName(dto.getNextOfKinName())
                .seatNo(dto.getSeatNo())
                .seat(null)
                .timeofBoarding(DateUtil.textToLocalDateTime(dto.getTimeOfBoarding()))
                .timeofCompletion(DateUtil.textToLocalDateTime(dto.getTimeOfBoarding()))
                .build();
    }


    private Enum.IdCategory convertToCategoryEnum(String name){
        // NATIONAL_ID, DRIVERS_LICENSE, INTERNATIONAL_PASSPORT, VOTERS_CARD, SCHOOL_ID, OTHER
        if(name.startsWith("NAT")){
            return Enum.IdCategory.NATIONAL_ID;
        }

        if(name.startsWith("DRIVERS_LICENSE")){
            return Enum.IdCategory.DRIVERS_LICENSE;
        }

        if(name.startsWith("INTERNATIONAL_PASSPORT")){
            return Enum.IdCategory.INTERNATIONAL_PASSPORT;
        }

        return Enum.IdCategory.OTHER;
    }
}
