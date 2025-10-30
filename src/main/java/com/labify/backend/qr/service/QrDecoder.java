package com.labify.backend.qr.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

@Component
public class QrDecoder {
    private final Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
    public QrDecoder() {
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
    }
    public String decode(MultipartFile file) throws IOException, NotFoundException {
        try (InputStream in = file.getInputStream()) {
            BufferedImage image = ImageIO.read(in);
            if (image == null) throw NotFoundException.getNotFoundInstance();
            LuminanceSource src = new BufferedImageLuminanceSource(image);
            BinaryBitmap bmp = new BinaryBitmap(new HybridBinarizer(src));
            Result r = new MultiFormatReader().decode(bmp, hints);
            return r.getText();
        }
    }
}
