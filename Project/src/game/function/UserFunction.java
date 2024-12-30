package game.function;

import script.User;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class UserFunction {

    // 유저 파일 경로
    private final Path userIdPath = Path.of("src/user/user.txt");

    // 유저의 정보가 담긴 파일 경로
    private final Path userInfoPath = Path.of("src/user/userInfo");

    // 유저 리스트
    private final List<User> users = new ArrayList<>();

    public UserFunction() {
        try {
            // user.txt가 존재하지 않으면 생성
            if (!Files.exists(userIdPath)) {
                Files.createDirectories(userIdPath.getParent()); // 경로가 없으면 디렉토리 생성
                Files.createFile(userIdPath); // 파일 생성
            }

            // user.txt 읽어서 유저 리스트 초기화
            List<String> lines = Files.readAllLines(userIdPath);
            for (String line : lines) {
                if (!line.isBlank()) {
                    users.add(User.fromString(line));
                }
            }
        } catch (IOException e) {
            System.out.println("유저 파일 초기화 중 오류 발생: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // 회원 가입한 유저 정보 저장
    public void addUser(String id, String password) {
        try {
            // 유저 리스트에 추가
            User newUser = new User(id, password);
            users.add(newUser);

            // user.txt에 유저 정보 저장
            String userLine = newUser.toString() + "\n";
            Files.writeString(userIdPath, userLine, StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.out.println("유저 정보 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // 로그인 검증
    public boolean userCheck(String id, String password) {
        boolean idExists = false;

        for (User user : users) {
            // 아이디가 존재하는지 확인
            if (user.getId().equals(id)) {
                idExists = true;
                // 아이디가 존재하면서 비밀번호가 일치하면 true 반환
                if (user.getPaseword().equals(password)) {
                    JOptionPane.showMessageDialog(null, "로그인 성공! 로그인 ID: " + user.getId(), "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    // 아이디는 존재하지만 비밀번호가 틀린 경우
                    JOptionPane.showMessageDialog(null, "비밀번호가 틀렸습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        // 아이디가 존재하지 않는 경우
        if (!idExists) {
            JOptionPane.showMessageDialog(null, "아이디가 존재하지 않습니다! 회원가입을 먼저 해주세요!", "로그인 실패", JOptionPane.WARNING_MESSAGE);
        }

        return false;
    }

    // 아이디가 이미 존재하는지 확인하는 코드
    public boolean checkExist(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return true; // 아이디가 이미 존재
            }
        }
        return false; // 아이디가 존재하지 않음
    }

}
