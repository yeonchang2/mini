package game;
import game.panel.*;
import script.User;
import script.UserCharacter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class MainFrame extends JFrame {
    private final String LABEL_PATH = "src/images/Label.png";
    private final CardLayout cardLayout = new CardLayout();
    private User currentUser; // 현재 로그인한 유저
    private UserCharacter currentCharacter; // 현재 로그인된 유저의 캐릭터 정보
    private final CharacterCreatePanel characterCreatePanel;
    private GameStagePanel gameStagePanel;
    private final Path userInfoPath = Path.of("src/user/userInfo");
    private String currentPanel;

    public MainFrame() {
        setTitle("textGame");
        characterCreatePanel = new CharacterCreatePanel(this);

        // 메뉴 생성
        makeMenu();

        // 중앙으로 설정
        setSize(1000, 800);
        //setLocationRelativeTo(null);
        //setResizable(false);

        // 카드 레이아웃
        getContentPane().setLayout(cardLayout);

        // 시작 패널
        IntroPanel introPanel = new IntroPanel(this);
        getContentPane().add("game.panel.IntroPanel", introPanel);

        // 랭크 패널
        RankPanel rankPanel = new RankPanel(this);
        getContentPane().add("game.panel.RankPanel", rankPanel);

        // 로그인 패널
        LoginPanel loginPanel = new LoginPanel(this);
        getContentPane().add("game.panel.LoginPanel", loginPanel);

        // 캐릭터 선택 패널
        CharacterCreatePanel characterCreatePanel = new CharacterCreatePanel(this);
        getContentPane().add("game.panel.CharacterCreatePanel", characterCreatePanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);

    }

    private void makeMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        // 메뉴 생성
        JMenu gameMenu = new JMenu("메뉴");

        // 메인 메뉴로 돌아가기
        JMenuItem returnMain = gameMenu.add(new JMenuItem("메인 메뉴로 돌아가기"));
        returnMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean goHome = true;
                switch (currentPanel) {
                    case "game.panel.CharacterCreatePanel":
                        JOptionPane.showMessageDialog(MainFrame.this, "이곳에선 불가능합니다!", "오류", JOptionPane.ERROR_MESSAGE);
                        goHome = false;
                        break;
                    case "game.panel.GamePanel":
                        JOptionPane.showMessageDialog(MainFrame.this, "게임중엔 불가능합니다!", "오류", JOptionPane.ERROR_MESSAGE);
                        goHome = false;
                        break;
                }

                if (goHome) {
                    if (getUser() != null) {
                        int response = JOptionPane.showConfirmDialog(
                                MainFrame.this,
                                "로그아웃 됩니다. 계속 진행하시겠습니까?",
                                "로그아웃 확인",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if (response == JOptionPane.NO_OPTION) return;
                    }
                    setUser(null);
                    changePanel("game.panel.IntroPanel");
                }
            }
        });

        // 단어 추가 기능
        JMenuItem addWordItem = gameMenu.add(new JMenuItem("단어 추가"));
        addWordItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewWord(); // 단어 추가 기능 실행
            }
        });

        menuBar.add(gameMenu);
    }

    // 단어 추가 함수
    private void addNewWord() {
        String newWord = JOptionPane.showInputDialog(
                MainFrame.this,
                "추가할 단어를 입력하세요:",
                "단어 추가",
                JOptionPane.PLAIN_MESSAGE
        );

        if (newWord != null && !newWord.trim().isEmpty()) {
            Path wordsFilePath = Path.of("src/monster/words.txt");
            try {
                // 파일에 단어 추가
                Files.writeString(wordsFilePath, newWord.trim() + System.lineSeparator(),
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND);

                JOptionPane.showMessageDialog(
                        MainFrame.this,
                        "단어가 성공적으로 추가되었습니다: " + newWord,
                        "성공",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        MainFrame.this,
                        "단어 추가 중 오류가 발생했습니다: " + ex.getMessage(),
                        "오류",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    MainFrame.this,
                    "입력된 단어가 없습니다!",
                    "오류",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }



    // 패널을 전환하는 함수
    public void changePanel(String panel) {

        cardLayout.show(getContentPane(), panel);
        currentPanel = panel;

        // 디버깅용 문구
        System.out.println("Current panel: " + currentPanel);
    }

    // 유저 관련 함수
    // 로그인된 유저 반환
    public User getUser() {
        return currentUser;
    }

    // 로그인된 유저의 캐릭터 설정
    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            // user가 null이 아닐 때만 캐릭터 정보를 불러온다.
            loadCharacterInfo(user.getId());
        } else {
            // user가 null일 경우 currentCharacter를 null로 설정
            this.currentCharacter = null;
        }
    }

    public void setCurrentCharacter(UserCharacter currentCharacter) { this.currentCharacter = currentCharacter;};

    // 로그인된 유저 저장
    public UserCharacter getCurrentCharacter() {
        return currentCharacter;
    }

    private void loadCharacterInfo(String userId) {
        Path filePath = userInfoPath.resolve(userId + ".txt");
        System.out.println("불러올 파일 경로: " + filePath); // 디버깅

        if (!Files.exists(filePath)) {
            System.out.println("파일이 존재하지 않습니다: " + filePath);
            currentCharacter = null;
            return;
        }

        try {
            String content = Files.readString(filePath).trim();
            System.out.println("불러온 파일 내용: " + content); // 디버깅

            if (content.isEmpty()) {
                System.out.println("파일 내용이 비어 있습니다.");
                currentCharacter = null;
                return;
            }

            String[] data = content.split(",");
            if (data.length >= 7) {
                currentCharacter = new UserCharacter(
                        data[0], data[1],
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3]),
                        Integer.parseInt(data[4]),
                        Integer.parseInt(data[5]),
                        Boolean.parseBoolean(data[6])
                );
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading character info: " + e.getMessage());
            currentCharacter = null;
        }
    }

    // 이미지를 불러와서 버튼 크기에 맞게 사이즈를 조절해서 넣고 패널에 추가한다.
    public void buttonImageResize(JPanel panel, JButton button, String normalPath, String hoverPath,
                                  String clickedPath) {
        // 이미지 불러오기
        // 기본
        ImageIcon originalIcon = new ImageIcon(normalPath);

        // 마우스를 갖다댔을 경우
        ImageIcon hoverIcon = new ImageIcon(hoverPath);

        // 마우스로 클릭한 경우
        ImageIcon clickedIcon = new ImageIcon(clickedPath);

        // 파일 확장자 확인
        boolean isGif = normalPath.toLowerCase().endsWith(".gif");

        if (!isGif) { // gif가 아닌 경우 (png 등)
            // 버튼 크기 불러오기
            int buttonWidth = button.getWidth();
            int buttonHeight = button.getHeight();

            // 이미지 크기 조절
            // 기본
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
            originalIcon = new ImageIcon(resizedImage);

            // hover
            Image hoverImage = hoverIcon.getImage();
            Image resizedHoverImage = hoverImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
            hoverIcon = new ImageIcon(resizedHoverImage);

            // clicked
            Image clickedImage = clickedIcon.getImage();
            Image resizedClickedImage = clickedImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
            clickedIcon = new ImageIcon(resizedClickedImage);
        }

        // 버튼에 이미지 넣기
        button.setIcon(originalIcon); // 기본
        button.setRolloverIcon(hoverIcon); // hover
        button.setPressedIcon(clickedIcon);

        // 버튼 테두리 및 배경 제거
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        // 패널에 버튼 추가
        panel.add(button);
    }

    // 이미지를 불러와서 레이블 크기에 맞게 조정 후 패널에 추가한다.
    public void labelImageResize(JPanel panel, JLabel label, String path) {
        // 이미지 불러오기
        ImageIcon originalIcon = new ImageIcon(path);

        // GIF 처리
        if (path.toLowerCase().endsWith(".gif")) { // GIF는 파일 훼손 방지를 위해 크기 조정을 하지 않고 원본 아이콘을 직접 적용
            label.setIcon(originalIcon);
        } else {
            // PNG 이미지 형식은 크기 조정
            int labelWidth = label.getWidth();
            int labelHeight = label.getHeight();

            // 이미지 크기 조절
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            // 레이블 이미지 넣기
            label.setIcon(resizedIcon);
        }

        // 패널에 레이블 추가
        panel.add(label);
    }

    // 학번이름을 출력하는 함수
    public void labelPrint(JPanel panel, int x, int y, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(LABEL_PATH);
        Image resizedImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // 레이블 생성
        JLabel label = new JLabel(resizedIcon);
        label.setBounds(x, y, width, height);

        // 패널에 추가
        panel.add(label);

    }

}
