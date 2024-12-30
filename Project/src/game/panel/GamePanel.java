package game.panel;
import javax.swing.*;
import java.awt.*;
import game.MainFrame;

// 게임 진행 패널들을 추가하기 위한 상위 패널
public class GamePanel extends JPanel {
    private GameStagePanel gameStagePanel;
    private GameUserPanel gameUserPanel;
    private GameTextPanel gameTextPanel;
    private GameItemPanel gameItemPanel;
    private GameTimerPanel gameTimerPanel;

    private final int stage;

    private final MainFrame mainFrame;
    private final GameLobbyPanel gameLobbyPanel;

    public GamePanel(MainFrame mainFrame, int stage) {
        this.mainFrame = mainFrame;
        this.gameLobbyPanel = new GameLobbyPanel(mainFrame);
        this.stage = stage;

        this.setLayout(new BorderLayout());
        splitPanel();
    }

    // 패널 분리, 상하, 상단은 다시 상하로 2개, 하단은 가로로 3개의 패널로 구성
    private void splitPanel() {
        //SplitPane 활용
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // 상하로 구분
        mainSplitPane.setDividerLocation((int) (mainFrame.getHeight() * 0.7));
        mainSplitPane.setEnabled(false);
        this.setLayout(new BorderLayout());
        this.add(mainSplitPane, BorderLayout.CENTER);

        JSplitPane topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // 다시 상하로 구분
        topSplitPane.setDividerLocation((int) (mainFrame.getHeight() * 0.7 * 0.93));
        topSplitPane.setEnabled(false);

        // 상단의 상단: 게임 화면
        gameStagePanel = new GameStagePanel(mainFrame, this, stage);
        topSplitPane.setTopComponent(gameStagePanel);

        // 상단의 하단: 텍스트 입력 화면
        gameTextPanel = new GameTextPanel(mainFrame, this, stage) ;
        topSplitPane.setBottomComponent(gameTextPanel);

        // 텍스트 패널 포커스 요청
        SwingUtilities.invokeLater(() -> gameTextPanel.focusTextField());

        // 상단 패널 설정
        mainSplitPane.setTopComponent(topSplitPane);

        // 하단 패널
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        bottomPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), (int) (mainFrame.getHeight() * 0.2)));


        // 하단의 좌측 : 유저 정보
        gameUserPanel = new GameUserPanel(mainFrame);
        bottomPanel.add(gameUserPanel);

        // 하단의 중앙 : 아이템
        gameItemPanel = new GameItemPanel(mainFrame, stage);
        bottomPanel.add(gameItemPanel);

        // 하단의 우측 : 타이머
        if (gameTimerPanel == null) { // 오류 방지
            gameTimerPanel = new GameTimerPanel(stage);
        }
        bottomPanel.add(gameTimerPanel);

        // 하단 패널 설정
        mainSplitPane.setBottomComponent(bottomPanel);
    }

    // 스테이지 패널 반환
    public GameStagePanel getGameStagePanel() {
        return gameStagePanel;
    }

    // 유저 패널 반환
    public GameUserPanel getGameUserPanel() {
        return gameUserPanel;
    }

    // 텍스트 입력 패널 반환
    public GameTextPanel getGameTextPanel() {
        return gameTextPanel;
    }

    // 아이템 패널 반환
    public GameItemPanel getGameItemPanel() {
        return gameItemPanel;
    }

    // 타이머 패널 반환
    public GameTimerPanel getGameTimerPanel() {
        if (gameTimerPanel == null) {
            gameTimerPanel = new GameTimerPanel(stage);
        }
        return gameTimerPanel;
    }
}
