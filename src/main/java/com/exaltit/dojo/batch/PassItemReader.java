package com.exaltit.dojo.batch;


import com.exaltit.dojo.model.Pass;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PassItemReader extends FlatFileItemReader<Pass> {

    private final List<Pass> sortedPasses = new ArrayList<>();
    private int currentIndex = 0;

    public PassItemReader() {
        setResource(new ClassPathResource("passes.csv"));
        setLinesToSkip(1);
        setLineMapper(getPassLineMapper());
    }

    @Override
    public Pass read() throws Exception {
        if (sortedPasses.isEmpty()) {
            loadAndSortPasses();
        }

        if (currentIndex < sortedPasses.size()) {
            return sortedPasses.get(currentIndex++);
        }

        return null;
    }

    private void loadAndSortPasses() throws Exception {
        Pass item;
        while ((item = super.read()) != null) {
            sortedPasses.add(item);
        }

        sortedPasses.sort((p1, p2) -> {
            if (p1.isStatusVIP() && !p2.isStatusVIP()) {
                return -1;
            }
            if (!p1.isStatusVIP() && p2.isStatusVIP()) {
                return 1;
            }
            return 0;
        });
    }

    private DefaultLineMapper<Pass> getPassLineMapper() {
        DefaultLineMapper<Pass> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("nom", "prenom", "dateNaissance", "statusVIP");

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            Pass pass = new Pass();
            pass.setNom(fieldSet.readString("nom"));
            pass.setPrenom(fieldSet.readString("prenom"));
            pass.setDateNaissance(LocalDate.parse(fieldSet.readString("dateNaissance")));
            pass.setStatusVIP(fieldSet.readBoolean("statusVIP"));
            pass.setDateHeureDeLaDemande(LocalDateTime.now());
            return pass;
        });

        return lineMapper;
    }
}