package com.exaltit.dojo.batch;


import com.exaltit.dojo.model.Pass;
import com.exaltit.dojo.model.PassStatus;
import com.exaltit.dojo.repository.PassRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class PassItemWriter implements ItemWriter<Pass> {



    private static final String QR_CODE_PATH = "qrcodes";


    private final PassRepository passRepository;

    public PassItemWriter(PassRepository passRepository) {
        this.passRepository = passRepository;
    }

    @Override
    public void write(Chunk<? extends Pass> chunk) throws Exception {
        createQrCodeDirectory();

        for (Pass pass : chunk) {
            try {
                generateQrCode(pass);
                finalizePass(pass);
                passRepository.save(pass);
                log.info("Generated QR code for: {} {}", pass.getPrenom(), pass.getNom());
            } catch (Exception e) {
                handleError(pass, e);
            }
        }
    }

    private void createQrCodeDirectory() throws Exception {
        Path directory = Paths.get(QR_CODE_PATH);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    private void generateQrCode(Pass pass) throws Exception {
        String qrContent = createQrContent(pass);
        String fileName = UUID.randomUUID() + ".png";
        Path qrPath = Paths.get(QR_CODE_PATH, fileName);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        var bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrPath);

        pass.setQrCodePath(qrPath.toString());
    }

    private String createQrContent(Pass pass) {
        return String.format("%s_%s_%s_%s_%s",
                pass.getNom(),
                pass.getPrenom(),
                pass.getDateNaissance(),
                pass.isStatusVIP() ? "VIP" : "STANDARD",
                UUID.randomUUID()
        );
    }

    private void finalizePass(Pass pass) {
        pass.setDateHeureDeGeneration(LocalDateTime.now());
        pass.setStatus(PassStatus.COMPLETED);
    }

    private void handleError(Pass pass, Exception e) {
        log.error("Failed to generate QR code for: {} {}", pass.getPrenom(), pass.getNom(), e);
        pass.setStatus(PassStatus.FAILED);
        passRepository.save(pass);
    }
}