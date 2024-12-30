package game.panel;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.MainFrame;

// 인트로 패널
public class IntroPanel extends JPanel {
    private final MainFrame mainFrame;
    private final ImageIcon introBackground = new ImageIcon("src/images/introBackground.gif");

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(introBackground.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);


    }

    public IntroPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // 레이아웃 설정
        this.setLayout(null);
        mainFrame.labelPrint(this, 700, 650, 250, 50);

        // 인트로 화면 버튼 생성
        addButton();

        // 제목 출력
        addTitle();
        


    }
    
    private void addTitle() {
        JLabel titleLabel = new JLabel();
        titleLabel.setBounds(270, 50, 450, 120);
        String TITLE_PATH = "src/images/gameTitle.png";
        mainFrame.labelImageResize(this, titleLabel, TITLE_PATH);
    }
    private void addButton() {
            // 시작
            JButton startButton = new JButton();
            startButton.setBounds(400,350, 200,80);
        String START_BUTTON_PATH = "src/images/StartButton.png";
        String START_BUTTON_ON_PATH = "src/images/StartButtonOn.png";
        String START_BUTTON_CLICK_PATH = "src/images/StartButtonClicked.png";
        mainFrame.buttonImageResize(this, startButton, START_BUTTON_PATH, START_BUTTON_ON_PATH,
                START_BUTTON_CLICK_PATH);

            // 랭크
            JButton rankButton = new JButton();
            rankButton.setBounds(400,430, 200,80);
        String RANK_BUTTON_PATH = "src/images/RankButton.png";
        String RANK_BUTTON_ON_PATH = "src/images/RankButtonOn.png";
        String RANK_BUTTON_CLICK_PATH = "src/images/RankButtonClicked.png";
        mainFrame.buttonImageResize(this, rankButton, RANK_BUTTON_PATH, RANK_BUTTON_ON_PATH, RANK_BUTTON_CLICK_PATH);

            // 버튼 이벤트 설정
            // 기본
            startButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mainFrame.changePanel("game.panel.LoginPanel");
                }
            });

            // 랭크
            rankButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mainFrame.changePanel("game.panel.RankPanel");
                }
            });

            // 통계

            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                }
            });

            this.add(startButton);
            this.add(rankButton);


        }
    





}
