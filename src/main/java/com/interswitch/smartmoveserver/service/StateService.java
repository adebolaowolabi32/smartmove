package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.State;
import com.interswitch.smartmoveserver.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepo;

    public List<State> findAll() {
       return stateRepo.findAll();
    }

}
