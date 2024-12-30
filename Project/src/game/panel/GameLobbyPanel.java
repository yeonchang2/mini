package game.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.MainFrame;
import script.UserCharacter;

public class GameLobbyPanel extends JPanel {
    private final ImageIcon lobbyBackground = new ImageIcon("src/images/GameLobbyBackground.gif");

    private final MainFrame mainFrame;

    private UserCharacter preCharacterState; // 이전 상태 저장용 객체

    private JLabel nicknameLabel;
    private JLabel moneyLabel;
    private JLabel levelLabel;
    private JLabel clearStageLabel;
    private JLabel moreHPLabel;
    private JLabel slowMonsterLabel;
    private JLabel infoBackgroundLabel;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(lobbyBackground.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public GameLobbyPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(null);

        int stageButtonY = 320;
        // 스테이지 버튼 추가
        String LEVEL1_BUTTON_PATH = "src/images/Level1Button.png";
        String LEVEL1_BUTTON_ON_PATH = "src/images/Level1ButtonOn.png";
        String LEVEL1_BUTTON_CLICK_PATH = "src/images/Level1ButtonClicked.png";
        addStageButton(80, stageButtonY, LEVEL1_BUTTON_PATH, LEVEL1_BUTTON_ON_PATH, LEVEL1_BUTTON_CLICK_PATH, 1);
        String LEVEL2_BUTTON_PATH = "src/images/Level2Button.png";
        String LEVEL2_BUTTON_ON_PATH = "src/images/Level2ButtonOn.png";
        String LEVEL2_BUTTON_CLICK_PATH = "src/images/Level2ButtonClicked.png";
        addStageButton(380, stageButtonY, LEVEL2_BUTTON_PATH, LEVEL2_BUTTON_ON_PATH, LEVEL2_BUTTON_CLICK_PATH, 2);
        String LEVEL3_BUTTON_PATH = "src/images/Level3Button.png";
        String LEVEL3_BUTTON_ON_PATH = "src/images/Level3ButtonOn.png";
        String LEVEL3_BUTTON_CLICK_PATH = "src/images/Level3ButtonClicked.png";
        addStageButton(680, stageButtonY, LEVEL3_BUTTON_PATH, LEVEL3_BUTTON_ON_PATH, LEVEL3_BUTTON_CLICK_PATH, 3);

        // 상점 버튼 추가
        addShopButton();

        // 새로고침 버튼 추가
        JButton userUpdateButton = new JButton();
        userUpdateButton.setBounds(320, 215, 120, 30);
        userUpdateButton.addActionListener(e -> displayUserInfo());
        String UPDATE_BUTTON_ON_PATH = "src/images/userUpdateButtonOn.png";
        String UPDATE_BUTTON_PATH = "src/images/userUpdateButton.png";
        String UPDATE_BUTTON_CLICK_PATH = "src/images/userUpdateButtonClicked.png";
        mainFrame.buttonImageResize(this, userUpdateButton, UPDATE_BUTTON_PATH, UPDATE_BUTTON_ON_PATH, UPDATE_BUTTON_CLICK_PATH);

        // 캐릭터 이미지 표시
        UserCharacter character = mainFrame.getCurrentCharacter();
        showCharacter(character.getGender());

        // 유저 정보 표시
        displayUserInfo();

        // 학번 이름 출력
        mainFrame.labelPrint(this, 600, 690, 200, 40);



    }

    // 유저 정보를 출력하는 메서드
    private void displayUserInfo() {
        UserCharacter character = mainFrame.getCurrentCharacter();

        // 이전 상태와 현재 상태 비교
        if (preCharacterState != null && character != null &&
                preCharacterState.getMoney() == character.getMoney() &&
                preCharacterState.getLevel() == character.getLevel() &&
                preCharacterState.getClearStage() == character.getClearStage() &&
                preCharacterState.getMoreHP() == character.getMoreHP() &&
                preCharacterState.isSlowMonster() == character.isSlowMonster()) {
            JOptionPane.showMessageDialog(this, "최신 상태입니다!", "알림", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 기존 레이블 제거
        removeExistingLabels();

        if (character != null) {
            nicknameLabel = new JLabel("닉네임: " + character.getNickname());
            moneyLabel = new JLabel("골드: " + character.getMoney());
            levelLabel = new JLabel("레벨: " + character.getLevel());
            clearStageLabel = new JLabel("클리어 스테이지: " + character.getClearStage());
            moreHPLabel = new JLabel("체력 증가: " + character.getMoreHP());
            slowMonsterLabel = new JLabel("몬스터 느려지게: " +
                    (character.isSlowMonster() ? "활성화" : "비활성화"));

            // 공통 설정
            Font infoFont = new Font("메이플스토리", Font.BOLD, 20);
            Color textColor = Color.GREEN;

            JLabel[] labels = {nicknameLabel, moneyLabel, levelLabel, clearStageLabel, moreHPLabel, slowMonsterLabel};
            int yPosition = 40;

            // 레이블 추가
            for (JLabel label : labels) {
                label.setFont(infoFont);
                label.setForeground(textColor);
                label.setBounds(100, yPosition, 400, 30);
                yPosition += 35;
                this.add(label);
            }

            // 정보를 출력하는 레이블들의 배경
            if (infoBackgroundLabel == null) {
                infoBackgroundLabel = new JLabel();
                infoBackgroundLabel.setBounds(0, 0, 500, 320);
                mainFrame.labelImageResize(this, infoBackgroundLabel, "src/images/lobbyInfoBackground.png");
            }
            this.add(infoBackgroundLabel);

            // 이전 상태를 현재 상태로 저장
            preCharacterState = new UserCharacter(
                    character.getNickname(),
                    character.getGender(),
                    character.getLevel(),
                    character.getMoney(),
                    character.getClearStage(),
                    character.getMoreHP(),
                    character.isSlowMonster()
            );
        }

        revalidate();
        repaint();
    }

    // 기존 레이블 제거 메서드
    private void removeExistingLabels() {
        if (nicknameLabel != null) remove(nicknameLabel);
        if (moneyLabel != null) remove(moneyLabel);
        if (levelLabel != null) remove(levelLabel);
        if (clearStageLabel != null) remove(clearStageLabel);
        if (moreHPLabel != null) remove(moreHPLabel);
        if (slowMonsterLabel != null) remove(slowMonsterLabel);
        if (infoBackgroundLabel != null) remove(infoBackgroundLabel);
        infoBackgroundLabel = null; // 필드를 null로 초기화
    }


    // 스테이지 버튼 추가 메서드
    private void addStageButton(int x, int y, String normalPath, String hoverPath, String clickedPath, int stage) {
        JButton stageButton = new JButton();
        stageButton.setBounds(x, y, 250, 250);
        mainFrame.buttonImageResize(this, stageButton, normalPath, hoverPath, clickedPath);

        stageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                UserCharacter character = mainFrame.getCurrentCharacter(); // 유저 정보 불러오기
                int stageCheck = character.getClearStage();
                if (stageCheck >= stage - 1) {
                    GamePanel gamePanel = new GamePanel(mainFrame, stage);
                    mainFrame.getContentPane().add("game.panel.GamePanel", gamePanel);
                    mainFrame.changePanel("game.panel.GamePanel");
                } else {
                    JOptionPane.showMessageDialog(GameLobbyPanel.this,
                            stage - 1 + "단계를 먼저 클리어하세요!", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        this.add(stageButton);
    }

    // 상점 버튼 추가
    private void addShopButton() {
        JButton shopButton = new JButton();
        shopButton.setBounds(430, 580, 150, 150);
        String SHOP_BUTTON_PATH = "src/images/shopButton.png";
        String SHOP_BUTTON_ON_PATH = "src/images/shopButtonOn.png";
        String SHOP_BUTTON_CLICK_PATH = "src/images/shopButtonClicked.png";
        mainFrame.buttonImageResize(this, shopButton, SHOP_BUTTON_PATH, SHOP_BUTTON_ON_PATH, SHOP_BUTTON_CLICK_PATH);

        shopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ShopPanel shopPanel = new ShopPanel(mainFrame); // Shop 패널 추가 및 이동 기능 (레이블 초기화를 위함)
                mainFrame.getContentPane().add("game.panel.ShopPanel", shopPanel);
                mainFrame.changePanel("game.panel.ShopPanel");
            }
        });
        this.add(shopButton);
    }

    // 캐릭터 이미지 출력
    private void showCharacter(String gender) {
        JLabel lobbyCharacterLabel = new JLabel();
        lobbyCharacterLabel.setBounds(23, 30, 80, 90);

        // 현재 로그인된 유저 정보 가져오기
        UserCharacter character = mainFrame.getCurrentCharacter();

        if ("남자".equals(gender)) {
            if (character != null && "조창연".equals(character.getNickname())) {
                // 닉네임이 "조창연"인 경우 특별 이미지 사용
                mainFrame.labelImageResize(this, lobbyCharacterLabel, "src/images/조창연.gif");
            } else {
                // 일반 남자 캐릭터 이미지 사용
                mainFrame.labelImageResize(this, lobbyCharacterLabel, "src/images/man.gif");
            }
        } else {
            // 여자 캐릭터 이미지 사용
            mainFrame.labelImageResize(this, lobbyCharacterLabel, "src/images/girl.gif");
        }
    }

}
