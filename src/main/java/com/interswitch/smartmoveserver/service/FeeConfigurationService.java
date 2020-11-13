package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.FeeConfiguration;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.FeeConfigurationRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FeeConfigurationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeeConfigurationRepository feeConfigurationRepository;

    @Autowired
    PageUtil pageUtil;

    @Autowired
    SecurityUtil securityUtil;


    public List<FeeConfiguration> findAll() {
        return feeConfigurationRepository.findAll();
    }

    public PageView<FeeConfiguration> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal);
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole())) {
                Page<FeeConfiguration> pages = feeConfigurationRepository.findAllByOwner(pageable, user.get());
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            } else {
                Page<FeeConfiguration> pages = feeConfigurationRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                Optional<User> ownerUser = userRepository.findById(owner);
                if (!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                Page<FeeConfiguration> pages = feeConfigurationRepository.findAllByOwner(pageable, ownerUser.get());
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }


    /**
     * This implementation is done with the assumption that only an operator(as well as ISW admin for test purposes)
     * can create fee configuration
     * @param feeConfiguration
     * @param principal
     * @return
     */
    public FeeConfiguration save(FeeConfiguration feeConfiguration, String principal) {

        if (feeConfiguration.getValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fee value cannot be negative");
        }

        User systemUser = userRepository.findByUsername(principal).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));

        String transportOperatorUsername = (systemUser.getRole()==Enum.Role.OPERATOR || systemUser.getRole()==Enum.Role.ISW_ADMIN) ?
                systemUser.getUsername() : systemUser.getOwner()!=null ? systemUser.getOwner().getUsername() : "";

        boolean exists = feeConfigurationRepository.existsByFeeNameAndOperatorUsername(feeConfiguration.getFeeName(),transportOperatorUsername);

        if (exists)
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Fee type with the name %s already configured for %s.", feeConfiguration.getFeeName(), transportOperatorUsername));

        if (feeConfiguration.getOwner() == null) {
            feeConfiguration.setOwner(systemUser);
            feeConfiguration.setOperator(systemUser);
        }
        return feeConfigurationRepository.save(feeConfiguration);
    }

    public FeeConfiguration findById(long id, String principal) {

        FeeConfiguration feeConfig = feeConfigurationRepository.findById(id);
        if(feeConfig==null) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "fee does not exist");
        }
        return feeConfig;
    }


    public FeeConfiguration update(FeeConfiguration feeConfiguration, String principal) {

        log.info("Logging fee config update===>" + feeConfiguration);

        if (feeConfiguration.getValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fee value cannot be negative");
        }

        if (feeConfiguration != null) {

            if (feeConfiguration.getOwner() == null) {
                User owner = userRepository.findByUsername(principal).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
                feeConfiguration.setOwner(owner);
            }
            return feeConfigurationRepository.save(feeConfiguration);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fee Configuration does not exist");
    }

    public void delete(long id, String principal) {
        FeeConfiguration feeConfiguration = feeConfigurationRepository.findById(id);
        if (feeConfiguration!=null)
            feeConfigurationRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fee configuration does not exist");
        }
    }

    /**
     *
     * @param operatorUsername
     * @return
     */
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
