package game.panel;
import javax.swing.*;
import java.awt.*;

// 게임의 진행시간을 출력해주는 패널
public class GameTimerPanel extends JPanel {
    private String background;
    private int stage;

    // 시간 출력용 레이블
    private final JLabel timeLabel = new JLabel("00 : 00 : 000");

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (stage == 3 ) { background = "src/images/TimerPanel.png";}

        else { background = "src/images/panel.png";  }
        g.drawImage(new ImageIcon(background).getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public GameTimerPanel(int stage) {
        this.stage = stage;
        System.out.println(stage);

        if (stage == 3) { // 스테이지가 3인 경우에만 타이머 출력 및 작동
            this.setLayout(new BorderLayout());
            JLabel showTitleLabel = new JLabel("STAGE3 TIMER");
            showTitleLabel.setFont(new Font("메이플스토리", Font.BOLD, 32));
            showTitleLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            timeLabel.setFont(new Font("메이플스토리", Font.BOLD, 24));
            this.add(showTitleLabel, BorderLayout.NORTH);
            this.add(timeLabel, BorderLayout.CENTER);
        }
        else {
            this.setLayout(null); // 스테이지가 3이 아닌 경우 문구 출력
            JLabel lockLabel = new JLabel("STAGE3에서 활성화");
            lockLabel.setFont(new Font("메이플스토리", Font.BOLD, 32));
            lockLabel.setBounds(20, 50, 300, 50);
            this.add(lockLabel);
        }

    }

    // 시간 라벨 초기화
    public void resetTimeLabel() {
        timeLabel.setText("00 : 00 : 000");
    }

    // 시간 라벨 업데이트
    public void updateTimeLabel(String time) {
        timeLabel.setText(time);
    }
}
