package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.PayoutDefinition;
import com.davidklhui.slotgame.repository.PayoutDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PayoutDefinitionServiceImpl implements IPayoutDefinitionService {

    private final PayoutDefinitionRepository payoutDefinitionRepository;

    @Autowired
    public PayoutDefinitionServiceImpl(final PayoutDefinitionRepository payoutDefinitionRepository) {
        this.payoutDefinitionRepository = payoutDefinitionRepository;
    }

    @Override
    public List<PayoutDefinition> listPayoutDefinitions() {
        return payoutDefinitionRepository.findAll();
    }
}
