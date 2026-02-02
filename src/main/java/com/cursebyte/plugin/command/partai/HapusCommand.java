package com.cursebyte.plugin.command.partai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.member.MemberData;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiManager;
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

        if (!PartaiManager.existsByUuid(playerUUID)) {
            MessageUtils.sendError(sender, "Anda tidak memiliki partai!");
            return;
        }

        var partaiData = PartaiService.getPartaiByPlayerUuid(playerUUID);
        if (partaiData == null) {
            MessageUtils.sendError(sender, "Data partai tidak ditemukan!");
            return;
        }

        String partaiName = partaiData.getName();
        String playerName = player.getName();

        String playerRank = MemberService.getRole(playerUUID);
        if (playerRank == null || !"ketua".equals(playerRank)) {
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

        
        UUID partaiUuid = MemberService.getPartaiUuid(playerUUID);
        
        List<MemberData> members = MemberService.getMembersByPartaiUuid(partaiUuid);
        
        MemberService.removeAllMembers(partaiUuid);
        PartaiService.deletePartai(partaiUuid);
        SaveCode.remove(playerName);

        for (MemberData member : members) {
            Player memberPlayer = plugin.getServer().getPlayer(member.getPlayerUuid());
            if (memberPlayer != null && memberPlayer.isOnline()) {
                MessageUtils.sendWarning(memberPlayer, "Partai " + partaiName + " telah dihapus oleh ketua!");
            }
        }
        
        MessageUtils.sendSuccess(sender, "Partai " + partaiName + " berhasil dihapus!");
    }
}
