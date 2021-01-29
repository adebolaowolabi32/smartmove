package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.FeeConfiguration;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.FeeConfigurationRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class FeeConfigurationService {

    @Autowired
    PageUtil pageUtil;
    @Autowired
    SecurityUtil securityUtil;
    @Autowired
    private FeeConfigurationRepository feeConfigurationRepository;
    @Autowired
    private UserService userService;

    public List<FeeConfiguration> findAll() {
        return feeConfigurationRepository.findAll();
    }

    public List<FeeConfiguration> findAll(Long owner, String principal) {
        User user = userService.findByUsername(principal);

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                return feeConfigurationRepository.findAllByOwner(user);
            } else {
                return feeConfigurationRepository.findAll();
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                return feeConfigurationRepository.findAllByOwner(ownerUser);
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public PageView<FeeConfiguration> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<FeeConfiguration> pages = feeConfigurationRepository.findAllByOwner(pageable, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            } else {
                Page<FeeConfiguration> pages = feeConfigurationRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<FeeConfiguration> pages = feeConfigurationRepository.findAllByOwner(pageable, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    /**
     * This implementation is done with the assumption that only an operator(as well as ISW admin for test purposes)
     * can create fee configuration
     *
     * @param feeConfiguration
     * @param principal
     * @return
     */
    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public FeeConfiguration save(FeeConfiguration feeConfiguration, String principal) {

        User systemUser = userService.findByUsername(principal);
        String transportOperatorUsername = (systemUser.getRole() == Enum.Role.OPERATOR || systemUser.getRole() == Enum.Role.ISW_ADMIN) ?
                systemUser.getUsername() : systemUser.getOwner() != null ? systemUser.getOwner().getUsername() : "";

        boolean exists = feeConfigurationRepository.existsByFeeNameAndOperatorUsername(feeConfiguration.getFeeName(), transportOperatorUsername);

        if (exists)
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Fee type with the name %s already configured for %s.", feeConfiguration.getFeeName(), transportOperatorUsername));

        //TODO: this implementation to be adjusted with a drop-down of all operators(only to visible to isw admin)
        // from which one will select and create fee configs for transport operators/owners

        if (feeConfiguration.getOwner() == null) {
            feeConfiguration.setOwner(systemUser);
            feeConfiguration.setOperator(systemUser);
        }
        return feeConfigurationRepository.save(feeConfiguration);
    }

    public FeeConfiguration findById(long id, String principal) {

        FeeConfiguration feeConfig = feeConfigurationRepository.findById(id);
        if (feeConfig == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fee does not exist.");
        }
        return feeConfig;
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public FeeConfiguration update(FeeConfiguration feeConfiguration, String principal) {

        if (feeConfiguration != null) {

            if (feeConfiguration.getOwner() == null) {
                User owner = userService.findByUsername(principal);
                feeConfiguration.setOwner(owner);
                feeConfiguration.setOperator(owner);
            }
            return feeConfigurationRepository.save(feeConfiguration);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fee Configuration does not exist.");
    }

    public void delete(long id, String principal) {
        FeeConfiguration feeConfiguration = feeConfigurationRepository.findById(id);
        if (feeConfiguration != null)
            feeConfigurationRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fee configuration does not exist.");
        }
    }

    public List<FeeConfiguration> findEnabledFeeConfigByOperatorUsername(String operatorUsername) {
        return feeConfigurationRepository.findByEnabledAndOperatorUsername(true, operatorUsername);
    }

    public Long countByOwner(User user) {
        return feeConfigurationRepository.countByOwner(user);
    }

    public Long countAll() {
        return feeConfigurationRepository.count();
    }
}
