package game.panel;
import game.MainFrame;
import script.UserCharacter;
import javax.swing.*;
import java.awt.*;

public class GameItemPanel extends JPanel {
    private final String background;
    private final MainFrame mainFrame;
    private final int stage;

    public GameItemPanel(MainFrame mainFrame, int stage) {
        this.mainFrame = mainFrame;
        this.stage = stage;
        this.setLayout(null);

        // 배경 설정
        background = (stage >= 2) ? "src/images/itemPanel.png" : "src/images/panel.png";
        initializeUI();
    }

    // UI 초기화 메서드
    private void initializeUI() {
        this.removeAll();

        if (stage >= 2) {
            UserCharacter character = mainFrame.getCurrentCharacter();

            // 포션 이미지와 텍스트
            JLabel potionLabel = new JLabel();
            potionLabel.setBounds(30, 30, 80, 80);
            mainFrame.labelImageResize(this, potionLabel, "src/images/Potion.png");

            JLabel potionCountLabel = new JLabel("[F1]포션 개수: " + character.getMoreHP());
            potionCountLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            potionCountLabel.setBounds(20, 120, 150, 30);
            potionCountLabel.setForeground(Color.WHITE);

            // 슬로우 아이템 이미지와 텍스트
            JLabel slowItemLabel = new JLabel();
            slowItemLabel.setBounds(195, 30, 80, 80);
            mainFrame.labelImageResize(this, slowItemLabel, "src/images/slowItem.png");

            JLabel slowItemStatusLabel = new JLabel("슬로우 활성화: " + (character.isSlowMonster() ? "ON" : "OFF"));
            slowItemStatusLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            slowItemStatusLabel.setBounds(165, 120, 200, 30);
            slowItemStatusLabel.setForeground(Color.WHITE);

            this.add(potionLabel);
            this.add(potionCountLabel);
            this.add(slowItemLabel);
            this.add(slowItemStatusLabel);
        } else {
            // STAGE2 미만인 경우
            JLabel lockLabel = new JLabel("STAGE2부터 활성화");
            lockLabel.setFont(new Font("메이플스토리", Font.BOLD, 32));
            lockLabel.setBounds(20, 50, 300, 50);
            this.add(lockLabel);
        }

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(background).getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
    }

    // 아이템 패널 업데이트 메서드
    public void updateItemPanel() {
        initializeUI(); // UI를 다시 초기화
    }
}
