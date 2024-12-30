package game.panel;
import game.MainFrame;
import script.User;
import script.UserCharacter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CharacterCreatePanel extends JPanel {
    private final ImageIcon createBackground = new ImageIcon("src/images/createBackground.png");
    private final Path userInfoPath = Path.of("src/user/userInfo");
    private final MainFrame mainFrame;
    public boolean isLoadCharacter = false; // 캐릭터 불러오기 중복 방지용 변수

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(createBackground.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public CharacterCreatePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(null);
        showCharacter();
        mainFrame.labelPrint(this, 700, 650, 250, 50);

        // 캐릭터 생성 버튼
        JButton createButton = new JButton();
        createButton.setBounds(710, 40, 245, 105);
        String CREATE_BUTTON_PATH = "src/images/CreateButton.png";
        String CREATE_BUTTON_ON_PATH = "src/images/CreateButtonOn.png";
        String CREATE_BUTTON_CLICK_PATH = "src/images/CreateButtonClicked.png";
        mainFrame.buttonImageResize(this, createButton, CREATE_BUTTON_PATH, CREATE_BUTTON_ON_PATH, CREATE_BUTTON_CLICK_PATH);

        // 캐릭터 삭제 버튼
        JButton deleteButton = new JButton();
        deleteButton.setBounds(710, 145, 245, 105);
        String DELETE_BUTTON_PATH = "src/images/DeleteButton.png";
        String DELETE_BUTTON_ON_PATH = "src/images/DeleteButtonOn.png";
        String DELETE_BUTTON_CLICK_PATH = "src/images/DeleteButtonClicked.png";
        mainFrame.buttonImageResize(this, deleteButton, DELETE_BUTTON_PATH, DELETE_BUTTON_ON_PATH, DELETE_BUTTON_CLICK_PATH);

        // 캐릭터 불러오기 버튼
        JButton LoadButton = new JButton();
        LoadButton.setBounds(710, 250, 245, 105);
        String LOAD_BUTTON_PATH = "src/images/LoadButton.png";
        String LOAD_BUTTON_ON_PATH = "src/images/LoadButtonOn.png";
        String LOAD_BUTTON_CLICK_PATH = "src/images/LoadButtonClicked.png";
        mainFrame.buttonImageResize(this, LoadButton, LOAD_BUTTON_PATH, LOAD_BUTTON_ON_PATH, LOAD_BUTTON_CLICK_PATH);

        // 게임 시작하기 버튼
        JButton StartButton = new JButton();
        StartButton.setBounds(710, 355, 245, 105);
        String START_BUTTON_PATH = "src/images/gameStartButton.png";
        String START_BUTTON_ON_PATH = "src/images/gameStartButtonOn.png";
        String START_BUTTON_CLICK_PATH = "src/images/gameStartButtonClicked.png";
        mainFrame.buttonImageResize(this, StartButton, START_BUTTON_PATH, START_BUTTON_ON_PATH, START_BUTTON_CLICK_PATH);

        // 뒤로가기 버튼 (재로그인)
        JButton BackButton = new JButton();
        BackButton.setBounds(710, 460, 245, 105);
        String BACK_BUTTON_PATH = "src/images/backButton.png";
        String BACK_BUTTON_ON_PATH = "src/images/backButtonOn.png";
        String BACK_BUTTON_CLICK_PATH = "src/images/backButtonClicked.png";
        mainFrame.buttonImageResize(this, BackButton, BACK_BUTTON_PATH, BACK_BUTTON_ON_PATH, BACK_BUTTON_CLICK_PATH);

        // 캐릭터 생성 버튼 이벤트 추가
        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createCharacter();
            }
        });

        // 캐릭터 삭제 버튼 이벤트 추가
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deleteCharacter();


            }
        });

        // 캐릭터 불러오기 버튼 이벤트 추가
        LoadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                User currentUser = mainFrame.getUser();
                Path userFilePath = userInfoPath.resolve(currentUser.getId() + ".txt");

                if (!Files.exists(userFilePath)) { // 파일이 존재하지 않는 경우
                    JOptionPane.showMessageDialog(
                            CharacterCreatePanel.this,
                            "캐릭터 파일이 존재하지 않습니다! 캐릭터를 먼저 생성해주세요.",
                            "오류",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                try {
                    // 데이터 읽어오기
                    String characterData = Files.readString(userFilePath).trim();
                    if (characterData.isBlank()) { // 캐릭터 파일이 공백인 경우
                        JOptionPane.showMessageDialog(
                                CharacterCreatePanel.this,
                                "생성된 캐릭터가 없습니다!",
                                "오류",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        if (!isLoadCharacter) {
                            showCharacter();
                            isLoadCharacter = true; // 중복 불러오기 방지
                        } else {
                            JOptionPane.showMessageDialog(
                                    CharacterCreatePanel.this,
                                    "이미 불러왔습니다!",
                                    "오류",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                            CharacterCreatePanel.this,
                            "파일 읽기 중 오류가 발생했습니다: " + ex.getMessage(),
                            "오류",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });


        // 시작하기 버튼 이벤트 추가
        StartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isLoadCharacter) { // 캐릭터가 불러와진 경우에만 시작
                    clearCharacterInfo();
                    // 게임 로비 패널 추가 및 이동
                    GameLobbyPanel gameLobbyPanel = new GameLobbyPanel(mainFrame);
                    mainFrame.getContentPane().add("game.panel.GameLobbyPanel", gameLobbyPanel);
                    mainFrame.changePanel("game.panel.GameLobbyPanel");
                }
                // 캐릭터가 불러오기가 실행된 경우에만 이동
                else JOptionPane.showMessageDialog(CharacterCreatePanel.this, "먼저 캐릭터를 불러와주세요!", "오류", JOptionPane.ERROR_MESSAGE);
            }

        });

        // 뒤로가기 버튼 이벤트 추가
        BackButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 로그아웃 문구 출력
                int response = JOptionPane.showConfirmDialog(
                        CharacterCreatePanel.this,
                        "로그아웃 됩니다. 계속 진행하시겠습니까?",
                        "로그아웃 확인",
                        JOptionPane.YES_NO_OPTION, // 버튼 타입
                        JOptionPane.QUESTION_MESSAGE // 아이콘 타입
                );

                // "예" 버튼을 눌렀을 경우 처리
                if (response == JOptionPane.YES_OPTION) {
                    // currentUser를 null로 설정
                    mainFrame.setUser(null);

                    // 캐릭터 정보 제거
                    clearCharacterInfo();

                    // 로그인 화면으로 전환
                    mainFrame.changePanel("game.panel.LoginPanel");
                }
            }
        });
    }

    // 캐릭터 이미지 및 정보를 출력하는 함수
    public void showCharacter() {
        User currentUser = mainFrame.getUser();
        if (currentUser == null) return;

        Path userFilePath = userInfoPath.resolve(currentUser.getId() + ".txt");

        try {
            if (!Files.exists(userFilePath)) return;

            // 경로 불러오기
            String characterData = Files.readString(userFilePath).trim();
            if (characterData.isBlank()) return;

            // 쉼표로 구분하여 데이터 저장
            String[] data = characterData.split(",");
            if (data.length < 5) return; // 파일의 값이 부족하면

            // 닉네임, 성별, 레벨, 돈 정보 저장
            String nickname = data[0];
            String gender = data[1];
            String strLevel = data[2];
            String strMoney = data[3];
            String stage = data[4];
            int level = Integer.parseInt(strLevel);
            int money = Integer.parseInt(strMoney);

            // 캐릭터 정보 표시
            showCharacterInfo(nickname, gender, level, money, stage);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "파일 읽기 중 오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 캐릭터 이미지를 출력하는 함수
    private void showCharacterInfo(String nickname, String gender, int level, int money, String stage) {
        // 캐릭터 정보 배경
        JLabel infoLabel = new JLabel();
        infoLabel.setBounds(100, 60, 300, 360);
        String INFO_PATH = "src/images/CharacterInfo.png";
        mainFrame.labelImageResize(this, infoLabel, INFO_PATH);

        // 레이블 생성
        addLabelToInfo(infoLabel, "닉네임: " + nickname, 50);
        addLabelToInfo(infoLabel, "성별: " + gender, 100);
        addLabelToInfo(infoLabel, "레벨: " + level, 150);
        addLabelToInfo(infoLabel, "돈: " + money, 200);
        addLabelToInfo(infoLabel, "클리어 스테이지: " + stage, 250);

        // 캐릭터 이미지
        JLabel characterLabel = new JLabel();
        characterLabel.setBounds(485, 513, 80, 75);
        String MAN_CHARACTER_PATH = "src/images/man.gif";
        mainFrame.labelImageResize(this, characterLabel, MAN_CHARACTER_PATH);
        //성별로 구분하여 캐릭터 이미지 출력
        if (gender.equals("남자")) {
            if (nickname.equals("조창연")) { // 히든 캐릭터
                JOptionPane.showMessageDialog(this, "히든 캐릭터!", "이스터에그", JOptionPane.INFORMATION_MESSAGE);
                String HIDDEN_CHARACTER_PATH = "src/images/조창연.gif";
                mainFrame.labelImageResize(this, characterLabel, HIDDEN_CHARACTER_PATH);
            } else  mainFrame.labelImageResize(this, characterLabel, MAN_CHARACTER_PATH);

        } else if (gender.equals("여자")) {
            String GIRL_CHARACTER_PATH = "src/images/girl.gif";
            mainFrame.labelImageResize(this, characterLabel, GIRL_CHARACTER_PATH);
            System.out.println("여자 캐릭터 출력");
        }

        this.add(infoLabel);
        this.revalidate();
        this.repaint();
    }

    // 캐릭터 정보 출력 레이블 전용 함수 (반복 작업 간소화 용도)
    private void addLabelToInfo(JLabel infoLabel, String text, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("메이플스토리", Font.BOLD, 18)); // 글꼴 설정
        label.setBounds(45, y, 200, 30);
        infoLabel.add(label);



    }

    // 캐릭터를 생성하는 함수
    private void createCharacter() {
        User currentUser = mainFrame.getUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(CharacterCreatePanel.this, "로그인된 유저가 없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 저장할 파일 경로 및 이름 설정
        Path userFilePath = userInfoPath.resolve(currentUser.getId() + ".txt");

        try {
            // 파일 존재 여부 확인
            if (Files.exists(userFilePath)) {
                String existingData = Files.readString(userFilePath).trim();
                if (!existingData.isBlank()) { // 파일이 존재하고 비어있지 않다면
                    JOptionPane.showMessageDialog(CharacterCreatePanel.this,
                            "이미 캐릭터가 생성되어있습니다! 캐릭터를 불러와주세요.",
                            "알림",
                            JOptionPane.INFORMATION_MESSAGE);
                    return; // 캐릭터 생성 중단
                }
            }

            // 닉네임 입력
            String nickname = JOptionPane.showInputDialog(CharacterCreatePanel.this, "캐릭터 닉네임을 입력하세요:");
            if (nickname == null || nickname.isBlank()) {
                JOptionPane.showMessageDialog(CharacterCreatePanel.this, "닉네임을 입력하세요!", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 성별 선택
            String[] genderOptions = {"남자", "여자"};
            String gender = (String) JOptionPane.showInputDialog(
                    CharacterCreatePanel.this,
                    "캐릭터 성별을 선택하세요:",
                    "성별 선택",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    genderOptions,
                    genderOptions[0]
            );

            if (gender == null || gender.isBlank()) {
                JOptionPane.showMessageDialog(CharacterCreatePanel.this, "성별을 선택하세요!", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 초기값 설정: 레벨, 돈, 클리어 스테이지
            int level = 1;
            int money = 0;
            int clearStage = 0;
            int moreHP = 0;
            boolean isSlowMonster = false;

            // 캐릭터 정보 저장
            String characterInfo = String.format("%s,%s,%d,%d,%d,%d,%b", nickname, gender, level, money, clearStage, moreHP, isSlowMonster);
            Files.writeString(userFilePath, characterInfo);

            // UserCharacter 객체 생성 및 업데이트
            UserCharacter newCharacter = new UserCharacter(nickname, gender, level, money, clearStage, moreHP, isSlowMonster);
            mainFrame.setCurrentCharacter(newCharacter);

            JOptionPane.showMessageDialog(CharacterCreatePanel.this, "캐릭터 생성이 완료되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(CharacterCreatePanel.this, "캐릭터 생성 중 오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }



    // 캐릭터 삭제 함수
    private void deleteCharacter() {
        User currentUser = mainFrame.getUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(CharacterCreatePanel.this, "로그인된 유저가 없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 삭제할 파일 경로 지정 및 이름 지정
         Path userFilePath = userInfoPath.resolve(currentUser.getId() + ".txt");

        try {
            String characterData = Files.readString(userFilePath).trim();
            if (characterData.isBlank()) {
                JOptionPane.showMessageDialog(CharacterCreatePanel.this, "삭제할 캐릭터 정보가 없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int response = JOptionPane.showConfirmDialog(
                    CharacterCreatePanel.this,
                    "캐릭터 정보를 삭제하시겠습니까?",
                    "캐릭터 삭제 확인",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                Files.writeString(userFilePath, ""); // 캐릭터 정보 제거
                //mainFrame.setUser(null); // 현재 캐릭터 정보를 초기화
                mainFrame.setCurrentCharacter(null);

                clearCharacterInfo(); // 패널 초기화

                JOptionPane.showMessageDialog(
                        CharacterCreatePanel.this,
                        "캐릭터 정보가 삭제되었습니다.",
                        "삭제 완료",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(CharacterCreatePanel.this, "캐릭터 파일이 존재하지 않습니다." , "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 캐릭터 생성창 레이블을 지우는 함수
    private void clearCharacterInfo() {
        Component[] components = this.getComponents();
        System.out.println("캐릭터 생성 패널 초기화");
        // JLabel만 제거, 버튼은 유지
        for (Component component : components) {
            if (component instanceof JLabel) {
                this.remove(component);
            }
        }
        // 학번 이름은 다시 출력되게 설정
        mainFrame.labelPrint(this, 700, 650, 250, 50);

        // isLoadCharacter 상태 초기화
        isLoadCharacter = false;

        // 패널 갱신
        this.revalidate();
        this.repaint();
    }
}
