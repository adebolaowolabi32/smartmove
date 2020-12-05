package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.TransactionRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * @author adebola.owolabi
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private PageUtil pageUtil;

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Transaction save(Transaction transaction) {
        transaction.setTransactionId(UUID.randomUUID().toString());
        return transactionRepository.save(transaction);
    }

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Transaction save(Transaction transaction, String principal) {
        transaction.setTransactionId(UUID.randomUUID().toString());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public PageView<Transaction> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<Transaction> pages = transactionRepository.findAllByOwner(pageable, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            } else {
                Page<Transaction> pages = transactionRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<Transaction> pages = transactionRepository.findAllByOwner(pageable, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public Transaction findById(long id, String principal) {
        return transactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction does not exist"));
    }

    public List<Transaction> find(Enum.TransactionType type) {
        return transactionRepository.findAllByType(type);
    }

    public List<Transaction> findByOwner(User owner) {
        return transactionRepository.findAllByOwner(owner);
    }

    public Long countByOwner(String username) {
        User user = userService.findByUsername(username);
        return transactionRepository.countByOwner(user);
    }

    public Long countAll(){
        return transactionRepository.count();
    }

}
