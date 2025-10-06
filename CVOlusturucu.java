import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CVOlusturucu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Kişisel Bilgiler
        System.out.println("=== ÖZGEÇMİŞ OLUŞTURUCU ===\n");
        System.out.println("--- KİŞİSEL BİLGİLER ---");

        System.out.print("Ad: ");
        String ad = scanner.nextLine();

        System.out.print("Soyad: ");
        String soyad = scanner.nextLine();

        System.out.print("E-posta: ");
        String email = scanner.nextLine();

        System.out.print("Telefon: ");
        String telefon = scanner.nextLine();

        System.out.print("Adres: ");
        String adres = scanner.nextLine();

        // Fotoğraf Bilgileri
        System.out.println("\n--- FOTOĞRAF ---");
        System.out.print("Fotoğraf dosya yolu (boş bırakmak için Enter): ");
        String fotoYolu = scanner.nextLine();

        String fotoKonum = "Sol Üst";
        if (!fotoYolu.trim().isEmpty()) {
            System.out.println("Fotoğraf konumu seçin:");
            System.out.println("1 - Sol Üst");
            System.out.println("2 - Sağ Üst");
            System.out.println("3 - Orta Üst");
            System.out.print("Seçiminiz (1-3): ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "2": fotoKonum = "Sağ Üst"; break;
                case "3": fotoKonum = "Orta Üst"; break;
                default: fotoKonum = "Sol Üst"; break;
            }
        }

        // Eğitim Bilgileri
        System.out.println("\n--- EĞİTİM BİLGİLERİ ---");
        System.out.print("Okul adı: ");
        String okul = scanner.nextLine();

        System.out.print("Bölüm/Alan: ");
        String bolum = scanner.nextLine();

        System.out.print("Başlangıç yılı: ");
        String okulBaslangic = scanner.nextLine();

        System.out.print("Bitiş yılı (veya 'Devam Ediyor'): ");
        String okulBitis = scanner.nextLine();

        // İş Tecrübeleri
        System.out.println("\n--- İŞ TECRÜBELERİ ---");
        List<IsTecrubeData> isTecrubeleri = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            System.out.println("\n--- İş Tecrübesi " + i + " ---");
            System.out.print("Şirket adı (boş bırakmak için Enter): ");
            String sirket = scanner.nextLine();

            if (sirket.trim().isEmpty()) {
                break;
            }

            System.out.print("Pozisyon: ");
            String pozisyon = scanner.nextLine();

            System.out.print("Başlangıç tarihi (örn: Ocak 2020): ");
            String baslangic = scanner.nextLine();

            System.out.print("Bitiş tarihi (örn: Aralık 2022): ");
            String bitis = scanner.nextLine();

            System.out.print("İş açıklaması: ");
            String aciklama = scanner.nextLine();

            isTecrubeleri.add(new IsTecrubeData(sirket, pozisyon, baslangic, bitis, aciklama));
        }

        // PDF Dosya Yolu
        System.out.print("\nPDF dosya adı (örn: Ozgecmis.pdf): ");
        String pdfYolu = scanner.nextLine();

        if (!pdfYolu.toLowerCase().endsWith(".pdf")) {
            pdfYolu += ".pdf";
        }

        // PDF Oluştur
        try {
            pdfOlustur(ad, soyad, email, telefon, adres, fotoYolu, fotoKonum,
                    okul, bolum, okulBaslangic, okulBitis,
                    isTecrubeleri, pdfYolu);
            System.out.println("\n✓ PDF başarıyla oluşturuldu: " + pdfYolu);
        } catch (IOException e) {
            System.err.println("✗ PDF oluşturulurken hata: " + e.getMessage());
            e.printStackTrace();
        }

        scanner.close();
    }

    private static void pdfOlustur(String ad, String soyad, String email, String telefon,
                                   String adres, String fotoYolu, String fotoKonum,
                                   String okul, String bolum, String okulBaslangic, String okulBitis,
                                   List<IsTecrubeData> isTecrubeleri, String pdfYolu) throws IOException {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();

        // Font yükleme
        PDType0Font fontBold = null;
        PDType0Font fontRegular = null;

        try {
            fontBold = PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arialbd.ttf"));
            fontRegular = PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf"));
        } catch (IOException e) {
            // Windows fontları yoksa standart fontları kullan
            fontBold = PDType0Font.load(document, CVOlusturucu.class.getResourceAsStream("/org/apache/pdfbox/resources/ttf/LiberationSans-Bold.ttf"));
            fontRegular = PDType0Font.load(document, CVOlusturucu.class.getResourceAsStream("/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf"));
        }

        // Fotoğraf ekleme
        if (!fotoYolu.trim().isEmpty()) {
            try {
                PDImageXObject image = PDImageXObject.createFromFile(fotoYolu, document);

                float imageWidth = 80;
                float imageHeight = 100;
                float xPos = margin;

                if (fotoKonum.equals("Sağ Üst")) {
                    xPos = pageWidth - margin - imageWidth;
                } else if (fotoKonum.equals("Orta Üst")) {
                    xPos = (pageWidth - imageWidth) / 2;
                }

                contentStream.drawImage(image, xPos, yPosition - imageHeight,
                        imageWidth, imageHeight);

                yPosition -= imageHeight + 20;
            } catch (Exception e) {
                System.out.println("Uyarı: Fotoğraf yüklenemedi: " + e.getMessage());
            }
        }

        // Başlık - Ad Soyad
        contentStream.beginText();
        contentStream.setFont(fontBold, 24);
        contentStream.setNonStrokingColor(41f/255f, 128f/255f, 185f/255f);
        String adSoyad = ad + " " + soyad;
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(adSoyad);
        contentStream.endText();

        yPosition -= 40;

        // İletişim Bilgileri
        contentStream.setNonStrokingColor(0f, 0f, 0f);
        yPosition = drawText(contentStream, fontRegular, 11,
                "E-posta: " + email, margin, yPosition);
        yPosition = drawText(contentStream, fontRegular, 11,
                "Telefon: " + telefon, margin, yPosition - 20);
        yPosition = drawText(contentStream, fontRegular, 11,
                "Adres: " + adres, margin, yPosition - 20);

        yPosition -= 40;

        // EĞİTİM Bölümü
        if (!okul.trim().isEmpty()) {
            yPosition = drawSectionHeader(contentStream, fontBold, "EGITIM",
                    margin, yPosition, pageWidth);
            yPosition -= 30;

            contentStream.beginText();
            contentStream.setFont(fontBold, 12);
            contentStream.setNonStrokingColor(0f, 0f, 0f);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(okul);
            contentStream.endText();
            yPosition -= 18;

            contentStream.beginText();
            contentStream.setFont(fontRegular, 11);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(bolum + " | " + okulBaslangic + " - " + okulBitis);
            contentStream.endText();
            yPosition -= 30;
        }

        // İş Tecrübeleri Bölümü
        if (!isTecrubeleri.isEmpty()) {
            yPosition = drawSectionHeader(contentStream, fontBold, "IS TECRÜBELERI",
                    margin, yPosition, pageWidth);
            yPosition -= 30;

            for (IsTecrubeData tecrube : isTecrubeleri) {
                contentStream.beginText();
                contentStream.setFont(fontBold, 12);
                contentStream.setNonStrokingColor(0f, 0f, 0f);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(tecrube.pozisyon);
                contentStream.endText();
                yPosition -= 18;

                contentStream.beginText();
                contentStream.setFont(fontRegular, 11);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(tecrube.sirket + " | " + tecrube.baslangic +
                        " - " + tecrube.bitis);
                contentStream.endText();
                yPosition -= 20;

                List<String> satirlar = splitText(tecrube.aciklama, fontRegular, 10,
                        pageWidth - 2 * margin);

                for (String satir : satirlar) {
                    yPosition = drawText(contentStream, fontRegular, 10,
                            satir, margin + 10, yPosition);
                    yPosition -= 15;
                }

                yPosition -= 10;
            }
        }

        contentStream.close();
        document.save(pdfYolu);
        document.close();
    }

    private static float drawText(PDPageContentStream contentStream, PDType0Font font,
                                  float fontSize, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        return y;
    }

    private static float drawSectionHeader(PDPageContentStream contentStream,
                                           PDType0Font font, String text, float margin, float yPosition,
                                           float pageWidth) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, 16);
        contentStream.setNonStrokingColor(41f/255f, 128f/255f, 185f/255f);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(text);
        contentStream.endText();

        contentStream.setLineWidth(2);
        contentStream.setStrokingColor(41f/255f, 128f/255f, 185f/255f);
        contentStream.moveTo(margin, yPosition - 5);
        contentStream.lineTo(pageWidth - margin, yPosition - 5);
        contentStream.stroke();

        return yPosition;
    }

    private static List<String> splitText(String text, PDType0Font font, float fontSize,
                                          float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word :
                    currentLine + " " + word;
            float width = font.getStringWidth(testLine) / 1000 * fontSize;

            if (width > maxWidth && currentLine.length() > 0) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    static class IsTecrubeData {
        String sirket;
        String pozisyon;
        String baslangic;
        String bitis;
        String aciklama;

        public IsTecrubeData(String sirket, String pozisyon, String baslangic,
                             String bitis, String aciklama) {
            this.sirket = sirket;
            this.pozisyon = pozisyon;
            this.baslangic = baslangic;
            this.bitis = bitis;
            this.aciklama = aciklama;
        }
    }
}