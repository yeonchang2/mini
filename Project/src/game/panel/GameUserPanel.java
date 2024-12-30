package game.panel;

import game.MainFrame;
import script.UserCharacter;

import javax.swing.*;
import java.awt.*;

// 유저의 정보를 출력해주는 패널
public class GameUserPanel extends JPanel {

    private JLabel moneyLabel; // 돈 출력 레이블
    private final MainFrame mainFrame;

    public GameUserPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        // 유저 정보 출력 함수 호출
        displayUserInfo();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        UserCharacter character = mainFrame.getCurrentCharacter();

        if (character != null) {
            if ("남자".equals(character.getGender())) {
                if("조창연".equals(character.getNickname())) {
                    String HIDDEN_BACKGROUND = "src/images/hiddenUserBackground.png";
                    g.drawImage(new ImageIcon(HIDDEN_BACKGROUND).getImage(), 0, 0, this.getWidth(), this.getHeight(), this); }
                else {
                    String MAN_BACKGROUND = "src/images/manUserBackground.png";
                    g.drawImage(new ImageIcon(MAN_BACKGROUND).getImage(), 0, 0, this.getWidth(), this.getHeight(), this); }
            } else {
                String GIRL_BACKGROUND = "src/images/girlUserBackground.png";
                g.drawImage(new ImageIcon(GIRL_BACKGROUND).getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }
    }

    // 캐릭터 정보를 출력
    private void displayUserInfo() {
        UserCharacter character = mainFrame.getCurrentCharacter();

        if (character != null) {
            JLabel nicknameLabel = new JLabel(character.getNickname());
            nicknameLabel.setFont(new Font("메이플스토리", Font.BOLD, 32));
            nicknameLabel.setBounds(180, 15, 200, 40);
            add(nicknameLabel);

            JLabel levelLabel = new JLabel(String.valueOf(character.getLevel()));
            levelLabel.setFont(new Font("메이플스토리", Font.PLAIN, 16));
            levelLabel.setBounds(125, 82, 200, 30);
            add(levelLabel);

            // 돈 출력 레이블 초기화
            moneyLabel = new JLabel(String.valueOf(character.getMoney()));
            moneyLabel.setFont(new Font("메이플스토리", Font.PLAIN, 16));
            moneyLabel.setBounds(125, 110, 200, 30);
            add(moneyLabel);
        }
    }

    // 몬스터 처치시 업데이트를 위한 함수
    public void updateMoney(int money) {
        moneyLabel.setText(String.valueOf(money));
    }

}

