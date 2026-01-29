package com.cursebyte.plugin.command.partai;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberData;
import com.cursebyte.plugin.modules.member.MemberManager;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;
import net.kyori.adventure.text.format.NamedTextColor;

public class DaftarCommand {
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        PartaiData partai;
        if (args.length >= 2) {
            String partaiName = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            partai = PartaiService.getPartaiByName(partaiName);
            if (partai == null) {
                MessageUtils.sendError(sender, "Partai '" + partaiName + "' tidak ditemukan!");
                return true;
            }
        } else {
            UUID partaiUuid = MemberService.getPartaiUuid(playerUuid);
            if (partaiUuid == null) {
                MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
                return true;
            }
            partai = PartaiService.getPartai(partaiUuid);
            if (partai == null) {
                MessageUtils.sendError(sender, "Data partai tidak ditemukan!");
                return true;
            }
        }

        List<MemberData> members = MemberManager.getMembersByPartaiUuid(partai.getUuid());

        MessageUtils.sendHeader(sender, "Daftar Anggota " + partai.getName());
        MessageUtils.sendRaw(sender, "");

        if (members.isEmpty()) {
            MessageUtils.sendRaw(sender, "  Belum ada anggota", NamedTextColor.GRAY);
        } else {
            displayMembersByRole(sender, members, "ketua");
            displayMembersByRole(sender, members, "wakil_ketua");
            displayMembersByRole(sender, members, "bendahara");
            displayMembersByRole(sender, members, "sekretaris");
            displayMembersByRole(sender, members, "anggota");

            MessageUtils.sendRaw(sender, "");
            MessageUtils.sendRaw(sender, "  Total: " + members.size() + " anggota", NamedTextColor.YELLOW);
        }

        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendFooter(sender);

        return true;
    }

    private void displayMembersByRole(CommandSender sender, List<MemberData> members, String role) {
        List<MemberData> roleMembers = members.stream()
                .filter(m -> m.getRole() != null && m.getRole().equalsIgnoreCase(role))
                .toList();

        if (!roleMembers.isEmpty()) {
            String roleDisplay = getRoleDisplay(role);
            MessageUtils.sendRaw(sender, "  " + roleDisplay + ":", NamedTextColor.GOLD);

            for (MemberData member : roleMembers) {
                Player p = Bukkit.getPlayer(member.getPlayerUuid());
                String status = (p != null && p.isOnline()) ? "●" : "○";
                String name = (p != null) ? p.getName() : member.getPlayerUuid().toString();

                MessageUtils.sendRaw(sender, "    " + status + " " + name,
                        (p != null && p.isOnline()) ? NamedTextColor.GREEN : NamedTextColor.GRAY);
            }
        }
    }

    private String getRoleDisplay(String role) {
        return switch (role.toLowerCase()) {
            case "ketua" -> "Ketua";
            case "wakil_ketua" -> "Wakil Ketua";
            case "bendahara" -> "Bendahara";
            case "sekretaris" -> "Sekretaris";
            case "anggota" -> "Anggota";
            default -> role;
        };
    }
}
