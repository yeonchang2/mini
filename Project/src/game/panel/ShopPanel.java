package game.panel;
import game.MainFrame;
import script.UserCharacter;
import javax.swing.*;
import java.awt.*;

public class ShopPanel extends JPanel {
    private final ImageIcon shopBackground = new ImageIcon("src/images/shopBackground.png");
    private final MainFrame mainFrame;
    private JLabel moneyLabel; // 유저의 돈을 표시할 레이블
    private int userMoney;     // 현재 유저의 돈
    private final UserCharacter character; // UserCharacter 객체

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(shopBackground.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public ShopPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);

        // 현재 유저의 정보 가져오기
        this.character = mainFrame.getCurrentCharacter();
        if (character != null) {
            this.userMoney = character.getMoney();
        } else {
            this.userMoney = 0; // 예외 처리
        }

        // UI 요소 추가
        addGoLobbyButton();
        addMoneyLabel();
        addMoreHPButton();
        addSlowMonsterButton();
    }

    // 로비로 돌아가는 버튼
    private void addGoLobbyButton() {
        JButton goLobbyButton = new JButton();
        goLobbyButton.setBounds(400, 460, 200, 274);
        String LOBBY_BUTTON_PATH = "src/images/portal.gif";
        mainFrame.buttonImageResize(this, goLobbyButton, LOBBY_BUTTON_PATH, LOBBY_BUTTON_PATH, LOBBY_BUTTON_PATH);

        goLobbyButton.addActionListener(e -> {
            mainFrame.changePanel("game.panel.GameLobbyPanel");
        });

        this.add(goLobbyButton);
    }

    // 유저의 돈을 표시하는 레이블
    private void addMoneyLabel() {
        moneyLabel = new JLabel("보유 금액: " + userMoney + " Gold");
        moneyLabel.setFont(new Font("메이플스토리", Font.BOLD, 20));
        moneyLabel.setForeground(Color.WHITE);
        moneyLabel.setBounds(30, 20, 300, 40);
        this.add(moneyLabel);
    }

    // 아이템 구매 버튼 (moreHP 증가)
    private void addMoreHPButton() {
        JButton hpButton = new JButton();
        hpButton.setBounds(130, 85, 140, 140);
        mainFrame.buttonImageResize(this, hpButton, "src/images/Potion.png", "src/images/PotionOn.png", "src/images/PotionClicked.png");

        // 설명 레이블
        JLabel hpLabel = new JLabel("HP 증가 (+1) - 10 Gold");
        hpLabel.setFont(new Font("메이플스토리", Font.BOLD, 16));
        hpLabel.setForeground(Color.WHITE);
        hpLabel.setBounds(100, 220, 200, 30);

        hpButton.addActionListener(e -> {
            if (userMoney >= 10) {
                userMoney -= 10;
                character.setMoreHP(character.getMoreHP() + 1); // moreHP 증가
                character.setMoney(userMoney);
                moneyLabel.setText("보유금액: " + userMoney + " Gold");
                JOptionPane.showMessageDialog(this, "체력 증가 아이템을 구매했습니다!", "구매 완료", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "돈이 부족합니다!", "구매 실패", JOptionPane.ERROR_MESSAGE);
            }
        });
        hpButton.setToolTipText("감소된 체력을 1만큼 회복시켜줍니다."); // 툴팁

        this.add(hpButton);
        this.add(hpLabel);
    }

    // 아이템 구매 버튼 (slowMonster 활성화)
    private void addSlowMonsterButton() {
        JButton slowButton = new JButton();
        slowButton.setBounds(680, 90, 140, 140);
        mainFrame.buttonImageResize(this, slowButton, "src/images/slowItem.png", "src/images/slowItemOn.png", "src/images/slowItemClicked.png");

        // 설명 레이블
        JLabel slowLabel = new JLabel("몬스터 느려짐 - 30 Gold");
        slowLabel.setFont(new Font("메이플스토리", Font.BOLD, 16));
        slowLabel.setForeground(Color.WHITE);
        slowLabel.setBounds(650, 220, 250, 30);

        slowButton.addActionListener(e -> {
            if (character.isSlowMonster()) {
                JOptionPane.showMessageDialog(this, "이미 구매했습니다!", "구매 실패", JOptionPane.WARNING_MESSAGE);
            } else if (userMoney >= 30) {
                userMoney -= 30;
                character.setSlowMonster(true); // slowMonster 활성화
                character.setMoney(userMoney);
                moneyLabel.setText("보유금액: " + userMoney + " Gold");
                JOptionPane.showMessageDialog(this, "몬스터 느려짐 아이템을 구매했습니다!", "구매 완료", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "돈이 부족합니다!", "구매 실패", JOptionPane.ERROR_MESSAGE);
            }
        });
        slowButton.setToolTipText("모든 스테이지에서 몬스터의 이동속도가 느려집니다.(패시브)"); // 툴팁

        this.add(slowButton);
        this.add(slowLabel);
    }
}
