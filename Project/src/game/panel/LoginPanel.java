package game.panel;
import game.MainFrame;
import game.function.UserFunction;
import script.User;
import script.UserCharacter;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel { // 로그인 창을 띄우는 패널
    private final MainFrame mainFrame;
    private final UserFunction userFunction;
    private final ImageIcon loginBackground = new ImageIcon("src/images/LoginBackground.png");

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(loginBackground.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userFunction = new UserFunction();

        // 레이아웃 설정
        this.setLayout(null);

        String LOGIN_IMAGE_PATH = "src/images/LoginImage.png";
        JLabel loginLabel = labelPrint(this, LOGIN_IMAGE_PATH, 400, 250, 500, 250);

        // ID, PW 입력 필드
        JTextField idField = new JTextField();
        idField.setBounds(100, 60, 250, 30);
        loginLabel.add(idField);

        JPasswordField pwField = new JPasswordField();
        pwField.setBounds(100, 140, 250, 30);
        loginLabel.add(pwField);
        mainFrame.labelPrint(this, 40, 50, 250, 50);
        
        // 로그인 버튼
        JButton loginButton = new JButton();
        loginButton.setBounds(357, 53, 125, 50);
        String LOGIN_BUTTON_PATH = "src/images/LoginButton.png";
        String LOGIN_BUTTON_ON_PATH = "src/images/LoginButtonOn.png";
        String LOGIN_BUTTON_CLICK_PATH = "src/images/LoginButtonClicked.png";
        mainFrame.buttonImageResize(this, loginButton, LOGIN_BUTTON_PATH, LOGIN_BUTTON_ON_PATH, LOGIN_BUTTON_CLICK_PATH);
        loginLabel.add(loginButton);

        loginButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String pw = pwField.getText().trim();

            if (id.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userFunction.userCheck(id, pw)) {
                try {
                    // 로그인된 유저 객체 생성
                    User user = new User(id, pw);

                    // 현재 유저 설정 
                    mainFrame.setUser(user);

                    // 캐릭터 정보 확인
                    UserCharacter character = mainFrame.getCurrentCharacter();
                    if (character != null) {
                        System.out.println("캐릭터 정보: " + character);
                    } else {
                        System.out.println("캐릭터 정보가 존재하지 않습니다. 파일 경로 확인 필요.");
                    }

                    // 입력 필드 초기화
                    idField.setText("");
                    pwField.setText("");

                    // 캐릭터 창으로 전환
                    mainFrame.changePanel("game.panel.CharacterCreatePanel");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "유저 정보 처리 중 오류 발생: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 회원가입 버튼
        JButton registerButton = new JButton();
        registerButton.setBounds(357, 133, 125, 50);
        String REGISTER_BUTTON_PATH = "src/images/RegisterButton.png";
        String REGISTER_BUTTON_ON_PATH = "src/images/RegisterButtonOn.png";
        String REGISTER_BUTTON_CLICK_PATH = "src/images/RegisterButtonClicked.png";
        mainFrame.buttonImageResize(this, registerButton, REGISTER_BUTTON_PATH, REGISTER_BUTTON_ON_PATH, REGISTER_BUTTON_CLICK_PATH);
        loginLabel.add(registerButton);

        registerButton.addActionListener(e -> {
            String id = idField.getText().trim(); // 입력값 앞뒤 공백 제거
            String pw = pwField.getText().trim();

            if (id.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                // 입력 필드 초기화
                idField.setText("");
                pwField.setText("");
                return;
            }

            // 아이디 중복 확인
            if (userFunction.checkExist(id)) {
                JOptionPane.showMessageDialog(this, "이미 가입된 회원입니다!", "회원가입 오류", JOptionPane.WARNING_MESSAGE);
                // 입력 필드 초기화
                idField.setText("");
                pwField.setText("");
                return; // 중복 아이디일 경우 회원가입 중단
            }

            // 새 유저 추가
            userFunction.addUser(id, pw);
            JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);


        });

    }

    public JLabel labelPrint(JPanel panel, String path, int x, int y, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(path);
        Image resizedImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel label = new JLabel(resizedIcon);
        label.setBounds(x, y, width, height);
        panel.add(label);

        return label;
    }
}
