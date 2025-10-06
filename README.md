📄 Java CV Generator: Profesyonel Özgeçmiş Otomasyonu

📝 Proje Özeti

Bu uygulama, Apache PDFBox kütüphanesini kullanarak, konsol üzerinden alınan yapısal veriyi otomatik olarak estetik ve düzenli bir PDF özgeçmişine (CV) dönüştüren bir Java çözümüdür. Projenin temel amacı, PDF formatının zorunlu kıldığı mutlak koordinat sistemini yöneterek, sayfa düzenini (layout) dinamik olarak oluşturmaktır.



✨ Ana Özellikler ve Teknik Çözümler

1\. Belge ve Yapı Yönetimi

PDF Çekirdeği: PDFBox ile PDDocument ve PDPage nesneleri kullanılarak sıfırdan vektörel bir çıktı üretilir. Tüm çizimler PDPageContentStream üzerinden yapılır.



Veri Modelleme: Tekrarlayan iş tecrübesi kayıtları, IsTecrubeData adında statik bir sınıf ile kapsüllenmiştir. Bu, verilerin temiz ve nesne tabanlı bir yapıda tutulmasını sağlar.



Layout Kontrolü: İçeriğin sayfa akışı, sürekli güncellenen yPosition değişkeni ile yönetilir. Bu değişken, her öğenin (başlık, metin) birbirine karışmadan, tutarlı bir boşlukla yerleştirilmesini garantiler.



2\. Tipografi ve Sağlamlık (Robustness)

Çift Aşamalı Font Stratejisi: Uygulama, öncelikle sistemde yüklü olan standart fontları (örn. Arial) yüklemeyi dener. Başarısız olursa, PDFBox'ın içindeki açık kaynaklı yedek fontlara (Failover) geçiş yapar. Bu, farklı işletim sistemlerinde tipografik tutarlılığı sağlar.



Akıllı Metin Sarma (splitText): PDFBox'ın yerleşik kelime sarma (word wrap) desteği olmaması nedeniyle, uzun metinler için özel bir algoritma geliştirilmiştir. Bu algoritma, font.getStringWidth() metoduyla her kelime eklendiğinde satırın piksel genişliğini hesaplar ve marj aşımında otomatik olarak yeni satıra geçer. Bu, profesyonel bir metin hizalaması sağlar.



3\. Görsel Tasarım ve Hiyerarşi

Bölüm Ayırıcıları: drawSectionHeader metodu, başlığı özel bir renk tonunda yazdıktan sonra altına aynı renkte kalın bir çizgi çizer. Bu, belgedeki ana bölümler arasında net ve güçlü bir görsel hiyerarşi oluşturur.



Marginal Tutarlılık: Tüm içerik için sabit bir marj (50) kullanılması, çıktının temiz ve profesyonel bir görünüme sahip olmasını destekler.



🛠️ Kurulum ve Çalıştırma

Gereksinimler

Java Development Kit (JDK) 8+



Apache PDFBox Kütüphanesi (Harici bağımlılık)



Komutlar

Derleme (Compilation):



Bash



javac -cp "path/to/pdfbox-app-x.x.x.jar" CVOlusuturucu.java

Çalıştırma (Execution):



Bash



java -cp ".;path/to/pdfbox-app-x.x.x.jar" CVOlusuturucu

(Not: Uygulama çalıştıktan sonra konsol üzerinden gerekli bilgileri adım adım girmeniz gerekmektedir.)

