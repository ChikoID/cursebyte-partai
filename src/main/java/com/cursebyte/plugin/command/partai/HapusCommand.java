package com.cursebyte.plugin.command.partai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.member.MemberData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class HapusCommand {
    private final PartaiCore plugin;
    private final Map<String, String> SaveCode = new HashMap<>();
    private final Random random = new Random();

    public HapusCommand(PartaiCore plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (MemberService.getPartaiUuid(playerUUID) == null) {
            Map<String, String> placeholders = new HashMap<>();
            MessageUtils.sendError(sender, "Anda tidak memiliki partai!" + placeholders);
            return;
        }

        String partaiName = PartaiService.getPartai(playerUUID).getName();
        String playerName = player.getName();

        String playerRank = MemberService.getRole(playerUUID);
        if (!"ketua".equals(playerRank)) {
            MessageUtils.sendError(sender, "Hanya ketua partai yang bisa menghapus partai!");
            return;
        }

        if (args.length == 1) {
            String code = String.format("%06d", random.nextInt(1000000));
            SaveCode.put(playerName, code);

            MessageUtils.sendWarning(sender, "⚠ PERINGATAN!");
            MessageUtils.sendWarning(sender, "Anda akan menghapus partai " + partaiName + "!");
            MessageUtils.sendWarning(sender, "Ketik: /partai hapus " + code + " untuk konfirmasi.");
            MessageUtils.sendWarning(sender, "Kode ini akan expired dalam 30 detik.");
            return;
        }

        String inputCode = args[1];
        String expectedCode = SaveCode.get(playerName);

        if (expectedCode == null || !expectedCode.equals(inputCode)) {
            MessageUtils.sendError(sender, "Kode konfirmasi salah atau sudah expired!");
            return;
        }

        PartaiService.deletePartai(MemberService.getPartaiUuid(playerUUID));
        SaveCode.remove(playerName);

        List<MemberData> members = MemberService.getMembersByPartaiUuid(MemberService.getPartaiUuid(playerUUID));
        for (MemberData member : members) {
            Player memberPlayer = plugin.getServer().getPlayer(member.getPlayerUuid());
            if (memberPlayer != null && memberPlayer.isOnline()) {
                MessageUtils.sendWarning(memberPlayer, "Partai " + partaiName + " telah dihapus oleh ketua!");
            }
        }
        
        MessageUtils.sendSuccess(sender, "Partai " + partaiName + " berhasil dihapus!");
    }
}
