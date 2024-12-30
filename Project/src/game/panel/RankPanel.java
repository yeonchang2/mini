package game.panel;
import game.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class RankPanel extends JPanel { // 랭크 확인 패널
    private final ImageIcon rankBackground = new ImageIcon("src/images/RankBackground.png");
    private final ImageIcon rankImage = new ImageIcon("src/images/Rank.png");

    private final Path rankFilePath = Paths.get("src/rank/3stageRank.txt");
    private final List<String[]> rankList = new ArrayList<>();
    private final MainFrame mainFrame;

    private JButton updateRankButton; // 버튼 추가
    private final JPanel rankDisplayPanel;  // 순위 표시 패널

    public RankPanel(MainFrame mainFrame) {
        this.setLayout(null); // 자유 배치
        this.mainFrame = mainFrame;

        // 업데이트 버튼 생성
        UpdateRankButton();

        // 순위 패널 생성
        rankDisplayPanel = new JPanel();
        rankDisplayPanel.setLayout(new BoxLayout(rankDisplayPanel, BoxLayout.Y_AXIS));
        rankDisplayPanel.setOpaque(false);

        // 데이터를 불러오고 랭크 표시
        loadRankData();
        displayRank();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 이미지 출력
        g.drawImage(rankBackground.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);

        // Rank 이미지 배경 출력
        int rankImageX = 270; // X 좌표
        int rankImageY = 150; // Y 좌표
        g.drawImage(rankImage.getImage(), rankImageX, rankImageY, 450, 450, this);
    }

    // updateRank 버튼 생성 및 기능 추가
    private void UpdateRankButton() {
        updateRankButton = new JButton("Update Rank");
        updateRankButton.setBounds(380, 600, 200, 60); // 버튼의 위치와 크기 설정

        updateRankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshRankData(); // 버튼 클릭 시 랭크 데이터를 새로고침
            }
        });
        String UPDATE_BUTTON_PATH = "src/images/UpdateButton.png";
        String UPDATE_BUTTON_ON_PATH = "src/images/UpdateButtonOn.png";
        String UPDATE_BUTTON_CLICK_PATH = "src/images/UpdateButtonClicked.png";
        mainFrame.buttonImageResize(this, updateRankButton, UPDATE_BUTTON_PATH, UPDATE_BUTTON_ON_PATH, UPDATE_BUTTON_CLICK_PATH);
    }

    private void refreshRankData() {
        rankList.clear();     // 기존 데이터 제거
        loadRankData();       // 새 데이터 로드
        removeAll();          // 기존 컴포넌트 제거
        add(updateRankButton); // 버튼 다시 추가
        displayRank();        // 새 데이터로 랭크 표시
        revalidate();         // 패널 재검증
        repaint();            // 패널 다시 그리기
    }

    private void loadRankData() {
        try {
            // 파일이 존재하지 않으면 오류 출력
            if (!Files.exists(rankFilePath)) {
                JOptionPane.showMessageDialog(this, "랭크 파일이 존재하지 않습니다!", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String> lines = Files.readAllLines(rankFilePath);

            // 파일이 공백인 경우
            if (lines.isEmpty()) {
                rankList.clear();
                return;
            }

            // 파일 내용 처리
            for (String line : lines) {
                if (!line.isBlank()) {
                    String[] data = line.split(",");
                    if (data.length == 4) { // 데이터가 정상적으로 입력되었는지 확인
                        rankList.add(data);
                    }
                }
            }

            // 클리어 시간 누적 초를 기준으로 정렬
            rankList.sort(Comparator.comparingInt(o -> Integer.parseInt(o[2].trim())));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "랭크 파일을 불러오는 중 오류 발생: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayRank() {
        int rankImageX = 270;
        int rankImageY = 150;
        int textX = rankImageX + 90; // 텍스트 X 좌표
        int textY = rankImageY + 135; // 텍스트 Y 시작 좌표
        int lineHeight = 40;         // 줄 간격

        JLabel titleLabel = new JLabel("3Stage 클리어 순위");
        titleLabel.setFont(new Font("메이플스토리", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setBounds(417, 177, 300, 30);
        this.add(titleLabel);

        if (rankList.isEmpty()) {
            JLabel noRankLabel = new JLabel("3stage를 클리어한 유저가 없습니다!");
            noRankLabel.setFont(new Font("메이플스토리", Font.BOLD, 16));
            noRankLabel.setForeground(Color.RED);
            noRankLabel.setBounds(textX + 16, textY + 80, 400, 30);
            this.add(noRankLabel);
        } else {
            // rankList의 실제 크기와 5 중 작은 값을 기준으로 반복, 최대 5명까지만 출력되도록 설정
            for (int i = 0; i < Math.min(5, rankList.size()); i++) {
                String[] data = rankList.get(i);
                String rankText = (i + 1) + "위: " + data[0].trim() + " - " + data[1].trim() + " - " + data[3].trim();
                JLabel rankTextLabel = new JLabel(rankText);
                rankTextLabel.setFont(new Font("메이플스토리", Font.PLAIN, 16));
                rankTextLabel.setForeground(Color.BLACK);
                rankTextLabel.setBounds(textX, textY + (i * lineHeight), 400, 30);
                this.add(rankTextLabel);
            }
        }
    }

}
