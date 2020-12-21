package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.AuditTrail;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.AuditTrailRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AuditTrailService {

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private PageUtil pageUtil;

    @Autowired
    private UserService userService;

    public List<AuditTrail> findAll(Long owner, String principal) {
        User user = userService.findByUsername(principal);

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                return auditTrailRepository.findAllByOwner(user);
            } else {
                return auditTrailRepository.findAll();
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                return auditTrailRepository.findAllByOwner(ownerUser);
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public PageView<AuditTrail> findAllPaginated(Long owner, int page, int size, String principal) {
            PageRequest pageable = pageUtil.buildPageRequest(page, size);
            User user = userService.findByUsername(principal);

            if (owner == 0) {
                if (securityUtil.isOwnedEntity(user.getRole())) {
                    Page<AuditTrail> pages = auditTrailRepository.findAllByOwner(pageable, user);
                    return new PageView<>(pages.getTotalElements(), pages.getContent());
                } else {
                    Page<AuditTrail> pages = auditTrailRepository.findAll(pageable);
                    return new PageView<>(pages.getTotalElements(), pages.getContent());
                }
            } else {
                if (securityUtil.isOwner(principal, owner)) {
                    User ownerUser = userService.findById(owner);
                    Page<AuditTrail> pages = auditTrailRepository.findAllByOwner(pageable, ownerUser);
                    return new PageView<>(pages.getTotalElements(), pages.getContent());
                }
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
            }
        }
}
