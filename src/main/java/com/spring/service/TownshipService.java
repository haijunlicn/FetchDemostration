package com.spring.service;

import com.spring.model.TownshipBean;
import com.spring.repository.TownshipRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TownshipService {

    private final TownshipRepository townshipRepo;

    public TownshipService() {
        this.townshipRepo = new TownshipRepository();
    }

    public List<TownshipBean> getAllTownships() {
        return townshipRepo.getAllTownship();
    }

    public TownshipBean getTownshipById(int id) {
        return townshipRepo.findById(id);
    }

    public TownshipBean saveTownship(TownshipBean township) {
    	townshipRepo.save(township);
        return township;  // Fix: Return the saved township
    }

    public void softDeleteTownship(int id) {
    	townshipRepo.softDelete(id);
    }

    public void hardDeleteTownship(int id) {
    	townshipRepo.hardDelete(id);
    }
}