//@ian
package data;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import model.Member;

public class MemberDataManager {
    private static final String FILE_PATH = "data/members.txt";

    // ─────────────── Add Member ───────────────
    public static boolean addMember(Member member) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();

            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String existingEmail = parts[2];
                    String existingPhone = parts[3];
                    if (existingEmail.equalsIgnoreCase(member.getEmail()) ||
                        existingPhone.equals(member.getPhone())) {
                        return false; // duplicate
                    }
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(member.toCsv());
                bw.newLine();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ─────────────── Load All Members ───────────────
    public static List<Member> loadMembers() {
        List<Member> members = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return members;

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    members.add(new Member(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return members;
    }

    // ─────────────── Update Member ───────────────
    public static boolean updateMember(Member updatedMember) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try {
            List<Member> members = loadMembers();
            boolean updated = false;

            for (int i = 0; i < members.size(); i++) {
                if (members.get(i).getId().equals(updatedMember.getId())) {
                    members.set(i, updatedMember);
                    updated = true;
                    break;
                }
            }

            if (updated) {
                saveAll(members);
            }

            return updated;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ─────────────── Delete Member ───────────────
    public static boolean deleteMember(String id) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try {
            List<Member> members = loadMembers();
            boolean removed = members.removeIf(m -> m.getId().equals(id));

            if (removed) {
                saveAll(members);
            }

            return removed;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ─────────────── Save All Members ───────────────
    private static void saveAll(List<Member> members) throws IOException {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (Member m : members) {
                bw.write(m.toCsv());
                bw.newLine();
            }
        }
    }
}
