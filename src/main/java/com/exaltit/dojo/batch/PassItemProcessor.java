package com.exaltit.dojo.batch;




import com.exaltit.dojo.model.Pass;
import com.exaltit.dojo.model.PassStatus;
import com.exaltit.dojo.repository.PassRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class PassItemProcessor implements ItemProcessor<Pass, Pass> {


    private final PassRepository passRepository;

    @Autowired
    public PassItemProcessor(PassRepository passRepository) {
        this.passRepository = passRepository;
    }

    @Override
    public Pass process(Pass pass) {
        log.info("Processing pass for: {} {} (VIP: {})",
                pass.getPrenom(), pass.getNom(), pass.isStatusVIP());
        pass.setStatus(PassStatus.IN_PROGRESS);
        pass.setDateHeureDeLaDemande(LocalDateTime.now());
        return passRepository.save(pass);
    }

}