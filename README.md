# Cursebyte Partai

**Cursebyte Partai** adalah plugin pendamping kustom yang dirancang untuk server Minecraft Roleplay berbasis **Paper (1.21+)**. Plugin ini menangani sistem buat, hapus dan edit dan Database Management dengan pendekatan _State Management_ yang efisien.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Platform](https://img.shields.io/badge/Platform-PaperMC-blue)
![Status](https://img.shields.io/badge/Status-Development-green)

## ✨ Fitur Utama

- **Manajemen Partai/Organisasi**: Pembuatan, penghapusan, dan edit data partai
- **Sistem Role/Jabatan**: Ketua, Wakil Ketua, Bendahara, Sekretaris, Anggota dengan slot terbatas
- **Ekonomi Partai**: Sistem saldo partai dengan setoran dan penarikan
- **Sistem Reputasi**: Tracking reputasi partai (0.0 - 1.0)
- **Relasi Antar Partai**: Sekutu dan musuh antar organisasi
- **Party Chat**: Sistem chat internal untuk anggota partai
- **Role Management**: Promosi, demosi, dan transfer kepemimpinan
- **Admin Tools**: Kontrol penuh untuk moderasi server

---

## 📋 Prasyarat (Requirements)

Sebelum menjalankan plugin ini, pastikan server memiliki:

1.  **Java 17** atau lebih baru.
2.  **Paper 1.21.4** (atau versi yang kompatibel).
3.  **[CursebyteCore](https://github.com/fzrilsh/cursebyte-rp-core)** (Wajib install untuk fitur Utamanya).

---

## ⚙️ Instalasi & Setup

1.  Download file `.jar` terbaru dari menu Releases.
2.  Masukkan ke folder `plugins/` di server Anda.
3.  Restart server.
4.  Lakukan setup in-game (lihat Command di bawah).

---

## ⌨️ Commands & Permissions

### Player Commands (`/partai` atau `/p` atau `/pt`)

| Command | Deskripsi | Permission |
|---------|-----------|------------|
| `/partai help` | Menampilkan daftar command yang tersedia | - |
| `/partai buat <nama_partai> <short_name>` | Membuat partai baru (maks 3-30 karakter, short_name 2-5 karakter) | - |
| `/partai daftar` | Melihat daftar semua partai yang ada dengan jumlah anggota | - |
| `/partai info [nama_partai]` | Melihat informasi detail partai (tanpa argumen: partai sendiri) | - |
| `/partai check <nama_player>` | Melihat partai dari player tertentu | - |
| `/partai stats` | Melihat top 5 partai terkaya dan paling terkenal | - |
| `/partai undang <nama_player>` | Mengundang player ke partai (hanya Ketua/Wakil) | - |
| `/partai terima` | Menerima undangan partai | - |
| `/partai tolak` | Menolak undangan partai | - |
| `/partai keluar` | Keluar dari partai (Ketua harus transfer dulu) | - |
| `/partai keluarkan <nama_player>` | Mengeluarkan anggota dari partai (hanya Ketua/Wakil) | - |
| `/partai promosi <nama_player> <jabatan>` | Promosi anggota ke jabatan lebih tinggi (cek slot) | - |
| `/partai demosi <nama_player> <jabatan>` | Demosi anggota ke jabatan lebih rendah | - |
| `/partai saldo` | Melihat saldo partai | - |
| `/partai setor <jumlah>` | Setor uang ke kas partai | - |
| `/partai tarik <jumlah>` | Tarik uang dari kas partai (hanya Ketua/Bendahara) | - |
| `/partai reputasi` | Melihat reputasi partai saat ini | - |
| `/partai sekutu <nama_partai>` | Mengajukan atau mengatur sekutu dengan partai lain | - |
| `/partai musuh <nama_partai>` | Mengatur status musuh dengan partai lain | - |
| `/partai status` | Melihat status keanggotaan dan jabatan Anda | - |
| `/partai chat <pesan>` | Kirim pesan ke party chat (hanya anggota partai) | - |
| `/partai pengumuman <pesan>` | Kirim pengumuman ke semua anggota online (hanya Ketua) | - |
| `/partai edit <field> <value>` | Edit data partai (name/short_name, hanya Ketua) | - |

# Slot maksimal untuk setiap jabatan
role:
  ketua:
    jumlah: 1
  wakil_ketua:
    jumlah: 1
  bendahara:
    jumlah: 1
  sekretaris:
    jumlah: 2
  anggota:
    jumlah: 44

# Total maksimal: 50 anggota per partai

---

## 🔧 Troubleshooting

### Error: "Player tidak ditemukan"
- Pastikan player **online** di server
- Nama player harus sesuai dengan citizen database (full_name)
- Gunakan tab completion untuk auto-complete nama

### Error: "Slot jabatan sudah penuh"
- Cek `config.yml` untuk jumlah slot per jabatan
- Demosi anggota lain terlebih dahulu untuk buat slot kosong
- Admin bisa paksa set dengan `/adminpartai set`

### Error: "Tidak bisa keluar, Anda adalah ketua"
- Transfer kepemimpinan dulu: `/adminpartai leader <partai> <nama_anda> <nama_baru>`
- Atau admin bubarkan partai: `/adminpartai bubarkan <partai>`

---

## 📄 License

Project ini menggunakan lisensi pribadi untuk **Cursebyte Network**. Tidak untuk distribusi komersial tanpa izin.

---

## 📞 Contact & Support

- **Discord**: [Cursebyte Community](#)
- **Issues**: [GitHub Issues](https://github.com/ChikoID/cursebyte-partai/issues)
- **Developer**: ChikoID

---

**Cursebyte Partai** - Sistem Organisasi untuk Minecraft Roleplay Server
### Admin Commands (`/adminpartai` atau `/ap` atau `/apt`)

| Command | Deskripsi | Permission |
|---------|-----------|------------|
| `/adminpartai reload` | Reload konfigurasi plugin | `partai.admin` |
| `/adminpartai list` | Melihat daftar lengkap semua partai | `partai.admin` |
| `/adminpartai set <partai> <player> <jabatan>` | Set jabatan player di partai | `partai.admin` |
| `/adminpartai balance <partai> <+/-jumlah>` | Tambah/kurangi saldo partai | `partai.admin` |
| `/adminpartai reputation <partai> <+/-nilai>` | Tambah/kurangi reputasi partai (auto clamp 0.0-1.0) | `partai.admin` |
| `/adminpartai kick <partai> <player>` | Kick player dari partai (tidak bisa kick Ketua) | `partai.admin` |
| `/adminpartai leader <partai> <playerA> <playerB>` | Transfer kepemimpinan dari A ke B | `partai.admin` |
| `/adminpartai bubarkan <partai> [alasan]` | Bubarkan partai secara paksa (hapus anggota + relasi) | `partai.admin` |
| `/adminpartai reset` | Reset semua data partai (konfirmasi 10 detik) | `partai.admin` |

---

## 📖 Detail Penjelasan

### Sistem Role/Jabatan

Plugin ini memiliki 5 jenis jabatan dengan slot terbatas berdasarkan `config.yml`:

- **Ketua** (1 slot) - Akses penuh: hapus partai, edit data, kelola semua anggota
- **Wakil Ketua** (1 slot) - Undang, keluarkan anggota, promosi/demosi
- **Bendahara** (1 slot) - Kelola keuangan: setor, tarik saldo
- **Sekretaris** (2 slot) - Tugas administratif khusus
- **Anggota** (44 slot) - Anggota biasa dengan akses terbatas

**Validasi Slot**: Saat promosi/set role, sistem otomatis cek apakah slot masih tersedia. Jika penuh, command ditolak.

### Sistem Ekonomi

- **Setor**: Semua anggota bisa setor uang ke kas partai menggunakan `/partai setor <jumlah>`
- **Tarik**: Hanya **Ketua** dan **Bendahara** yang bisa tarik dari kas menggunakan `/partai tarik <jumlah>`
- **Admin Balance**: Admin bisa adjust balance dengan format `+1000` (tambah) atau `-1000` (kurangi)
- Validasi: Tidak bisa tarik lebih dari saldo, tidak bisa membuat saldo negatif

### Sistem Reputasi

- Range: **0.0** (buruk) hingga **1.0** (sempurna)
- Reputasi mempengaruhi ranking di `/partai stats`
- Admin bisa adjust dengan `/adminpartai reputation <partai> +0.1` atau `-0.2`
- **Auto Clamping**: Nilai otomatis dibatasi antara 0.0 - 1.0 (tidak bisa overflow)

### Multi-Word Player Names

Plugin mendukung nama player dengan spasi (contoh: "Asep Musam Jaya"):
- Parser otomatis mencoba kombinasi nama terpanjang dulu
- Contoh: `/adminpartai kick Serikat Asep Musam Jaya` → Akan match player "Asep Musam Jaya" di partai "Serikat"

### Cascade Delete

Saat partai dibubarkan (`/adminpartai bubarkan` atau `/partai hapus`):
1. **Semua anggota** dihapus dari database
2. **Semua relasi** (sekutu/musuh) dengan partai lain dibersihkan
3. **Data partai** dihapus dari database dan cache

Ini mencegah data orphan dan menjaga integritas database.

### Integration dengan Core Plugin

Plugin ini terintegrasi dengan **cursebyte-rp-core** untuk:
- Lookup nama lengkap player (citizen data)
- Validasi player UUID
- Sistem ekonomi (EconomyService)

---

## 🎮 Contoh Penggunaan

```bash
# Buat partai baru
/partai buat "Serikat Rakyat" SR

# Undang anggota
/partai undang Asep Musam Jaya

# (Player lain) Terima undangan
/partai terima

# Promosi ke bendahara
/partai promosi Asep Musam Jaya bendahara

# Setor uang ke kas
/partai setor 10000

# Cek saldo partai
/partai saldo

# Lihat top partai
/partai stats

# [Admin] Set jabatan paksa
/adminpartai set "Serikat Rakyat" Budi Santoso sekretaris

# [Admin] Tambah saldo
/adminpartai balance "Serikat Rakyat" +50000

# [Admin] Transfer kepemimpinan
/adminpartai leader "Serikat Rakyat" Asep Musam Jaya Budi Santoso

# [Admin] Bubarkan partai
/adminpartai bubarkan "Serikat Rakyat" Pelanggaran aturan server
```

---

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
max-members: 50
invite-expiration: 15 # in minutes
min-reputation: 0.9 # Reputasi minimum untuk membuat partai
create-cost: 10000.0 # Biaya untuk membuat partai
min-reputation-to-join: 0.7 # Reputasi minimum untuk bergabung ke partai

role:
    ketua:
        nama: "Ketua"
        level: 4
        jumlah: 1 # Hanya 1 ketua per partai
    wakil_ketua:
        nama: "Wakil Ketua"
        level: 3
        jumlah: 1 # Hanya 1 wakil ketua per partai
    bendahara:
        nama: "Bendahara"
        level: 2
        jumlah: 1 # Hanya 1 bendahara per partai
    sekretaris:
        nama: "Sekretaris"
        level: 2
        jumlah: 2 # 2 sekretaris per partai
    anggota:
        nama: "Anggota"
        level: 1
        jumlah: 44 # Sisa anggota hingga total 50 Tidak boleh melewati batas dari max-members
```
