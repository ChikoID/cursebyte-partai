package com.cursebyte.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.command.partai.BuatCommand;
import com.cursebyte.plugin.command.partai.ChatCommand;
import com.cursebyte.plugin.command.partai.CheckCommand;
import com.cursebyte.plugin.command.partai.DaftarCommand;
import com.cursebyte.plugin.command.partai.DemosiCommand;
import com.cursebyte.plugin.command.partai.EditCommand;
import com.cursebyte.plugin.command.partai.HapusCommand;
import com.cursebyte.plugin.command.partai.HelpCommand;
import com.cursebyte.plugin.command.partai.InfoCommand;
import com.cursebyte.plugin.command.partai.KeluarCommand;
import com.cursebyte.plugin.command.partai.KeluarkanCommand;
import com.cursebyte.plugin.command.partai.MusuhCommand;
import com.cursebyte.plugin.command.partai.PengumumanCommand;
import com.cursebyte.plugin.command.partai.PromosiCommand;
import com.cursebyte.plugin.command.partai.ReputasiCommand;
import com.cursebyte.plugin.command.partai.SaldoCommand;
import com.cursebyte.plugin.command.partai.SekutuCommand;
import com.cursebyte.plugin.command.partai.SetorCommand;
import com.cursebyte.plugin.command.partai.StatusCommand;
import com.cursebyte.plugin.command.partai.TarikCommand;
import com.cursebyte.plugin.command.partai.UndangCommand;
import com.cursebyte.plugin.utils.MessageUtils;

public class PartaiCommand implements CommandExecutor {
    // private final PartaiCore plugin;

    private final HelpCommand helpCommand;

    // Command modules
    private final BuatCommand buatCommand;
    private final HapusCommand hapusCommand;
    private final EditCommand editCommand;
    private final InfoCommand infoCommand;
    private final UndangCommand undangCommand;
    private final KeluarkanCommand keluarkanCommand;
    private final DaftarCommand daftarCommand;
    private final DemosiCommand demosiCommand;
    private final PromosiCommand promosiCommand;
    private final KeluarCommand keluarCommand;
    private final CheckCommand checkCommand;
    private final ReputasiCommand reputasiCommand;
    private final SetorCommand setorCommand;
    private final TarikCommand tarikCommand;
    private final SaldoCommand saldoCommand;
    private final PengumumanCommand pengumumanCommand;
    private final ChatCommand chatCommand;
    private final SekutuCommand sekutuCommand;
    private final MusuhCommand musuhCommand;
    private final StatusCommand statusCommand;

    public PartaiCommand(PartaiCore plugin) {
        // this.plugin = plugin;
        this.helpCommand = new HelpCommand();
        this.buatCommand = new BuatCommand(plugin);
        this.hapusCommand = new HapusCommand(plugin);
        this.editCommand = new EditCommand();
        this.infoCommand = new InfoCommand();
        this.undangCommand = new UndangCommand(plugin);
        this.keluarkanCommand = new KeluarkanCommand();
        this.daftarCommand = new DaftarCommand();
        this.demosiCommand = new DemosiCommand(plugin);
        this.promosiCommand = new PromosiCommand(plugin);
        this.keluarCommand = new KeluarCommand();
        this.checkCommand = new CheckCommand();
        this.reputasiCommand = new ReputasiCommand();
        this.setorCommand = new SetorCommand();
        this.tarikCommand = new TarikCommand();
        this.saldoCommand = new SaldoCommand();
        this.pengumumanCommand = new PengumumanCommand();
        this.chatCommand = new ChatCommand();
        this.sekutuCommand = new SekutuCommand();
        this.musuhCommand = new MusuhCommand();
        this.statusCommand = new StatusCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Perintah ini hanya bisa digunakan oleh player!");
            return true;
        }

        if (args.length == 0)
            return true;

        switch (args[0].toLowerCase()) {
            case "help" -> helpCommand.execute(sender, args);
            case "check" -> checkCommand.execute(sender, args);
            case "buat" -> buatCommand.execute(sender, args);
            case "hapus" -> hapusCommand.execute(sender, args);
            case "edit" -> editCommand.execute(sender, args);
            case "info" -> infoCommand.execute(sender, args);
            case "undang" -> undangCommand.execute(sender, args);
            case "keluarkan" -> keluarkanCommand.execute(sender, args);
            case "daftar" -> daftarCommand.execute(sender, args);
            case "demosi" -> demosiCommand.execute(sender, args);
            case "promosi" -> promosiCommand.execute(sender, args);
            case "keluar" -> keluarCommand.execute(sender, args);
            case "reputasi" -> reputasiCommand.execute(sender, args);
            case "setor" -> setorCommand.execute(sender, args);
            case "tarik" -> tarikCommand.execute(sender, args);
            case "saldo" -> saldoCommand.execute(sender, args);
            case "pengumuman" -> pengumumanCommand.execute(sender, args);
            case "chat" -> chatCommand.execute(sender, args);
            case "sekutu" -> sekutuCommand.execute(sender, args);
            case "musuh" -> musuhCommand.execute(sender, args);
            case "status" -> statusCommand.execute(sender, args);
            default -> MessageUtils.sendError(sender, "Subcommand tidak dikenal! Gunakan /partai help untuk bantuan.");
        }
        return true;
    }
}
