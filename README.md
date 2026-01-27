# Cursebyte Partai

**Cursebyte Partai** adalah plugin pendamping kustom yang dirancang untuk server Minecraft Roleplay berbasis **Paper (1.21+)**. Plugin ini menangani sistem buat, hapus dan edit dan Database Management dengan pendekatan _State Management_ yang efisien.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Platform](https://img.shields.io/badge/Platform-PaperMC-blue)
![Status](https://img.shields.io/badge/Status-Development-green)

## ✨ Fitur Utama

###

---

## 📋 Prasyarat (Requirements)

Sebelum menjalankan plugin ini, pastikan server memiliki:

1.  **Java 17** atau lebih baru.
2.  **Paper 1.21.4** (atau versi yang kompatibel).

---

## ⚙️ Instalasi & Setup

1.  Download file `.jar` terbaru dari menu Releases.
2.  Masukkan ke folder `plugins/` di server Anda.
3.  Restart server.
4.  Lakukan setup in-game (lihat Command di bawah).

---

## ⌨️ Commands & Permissions

## 🏗️ Cara Build (Untuk Developer)

Project ini menggunakan **Maven**. Namun, karena project ini mengakses NMS (Net Minecraft Server) secara langsung, Anda perlu menyediakan file server secara manual.

1.  Clone repository ini.
2.  Dapatkan file `paper-1.21.4-mojang-mapped.jar` (Generate dari server Paper).
3.  Edit `pom.xml`, sesuaikan path pada bagian `systemPath`:

```xml
<dependency>
    <groupId>io.papermc.paper</groupId>
    <artifactId>paper-server-manual</artifactId>
    <version>1.21.4</version>
    <scope>system</scope>
    <systemPath>C:/Path/To/Your/paper-mojang-mapped.jar</systemPath>
    </dependency>
```

4.  Jalankan perintah build:

```bash
mvn clean package
```

## 📂 Konfigurasi (`config.yml`)

```yaml

```
