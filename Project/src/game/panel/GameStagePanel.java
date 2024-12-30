package game.panel;

import game.MainFrame;
import script.Monster;
import game.function.TimerFunction;
import script.UserCharacter;

import java.time.LocalDate;  // 날짜 저장용
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameStagePanel extends JPanel {
    private final ImageIcon Level1Background = new ImageIcon("src/images/Level1Background.png");
    private final ImageIcon Level2Background = new ImageIcon("src/images/Level2Background.png");
    private final ImageIcon Level3Background = new ImageIcon("src/images/Level3Background.png");
    private final String MAN_CHARACTER_PATH = "src/images/man.gif";
    public final String MAN_ATTACK_PATH = "src/images/manAttack.gif";
    private final String GIRL_CHARACTER_PATH = "src/images/girl.gif";
    private final String GIRL_ATTACK_PATH = "src/images/girlAttack.gif";
    private final List<JLabel> lifeLabels = new ArrayList<>(); // 체력 레이블 표시용 리스트
    private final GamePanel gamePanel;
    private GameTimerThread timerThread;
    private final MainFrame mainFrame;
    private final List<Monster> monsters = new CopyOnWriteArrayList<>(); // 몬스터 객체 저장을 위한 리스트
    private final Random random = new Random();
    private final int stage; // 현재 단계
    private int monsterCount = 0; // 현재 처치한 몬스터
    private int playerHealth;
    // 몬스터 이미지 경로 배열
    private final String[][] MONSTER_IMAGE_PATHS = {
            {"src/images/level1Monster1.gif", "src/images/level1Monster2.gif"}, // 스테이지 1
            {"src/images/level2Monster1.gif", "src/images/level2Monster2.gif"}, // 스테이지 2
            {"src/images/level3Monster1.gif", "src/images/level3Monster2.gif"}  // 스테이지 3
    };

    private final int GOAL_MONSTER_COUNT = 1;

    private final int PLAYER_X = 50; // 플레이어 x좌표
    private final int PLAYER_Y = 200; // 플레이어 y좌표
    private final int PLAYER_HEALTH_POWER = 6; // 플레이어 체력, 수정 용도
    private int initialPlayerHealth; // 초기 체력 저장용 변수

    private JLabel characterLabel; // 캐릭터 이미지 레이블 선언
    private final JLabel goalLabel; // 목표 레이블 이미지 선언
    private final String currentCharacterImage; // 현재 캐릭터 이미지 경로저장을 위한 변수(이미지 변환할때 기존 경로 저장하는 용도)
    private volatile boolean backToLobby = false; // 뒤로가기 플래그 추가
    private boolean levelUpCheck = false;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 이미지 출력
        switch (stage) {
            case 1 -> g.drawImage(Level1Background.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            case 2 -> g.drawImage(Level2Background.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            case 3 -> g.drawImage(Level3Background.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
        }

        // 몬스터 출력
        for (Monster monster : monsters) {
            g.drawImage(monster.getImage(), monster.getX(), monster.getY(), 120, 120, this);

            int textX = monster.getX() + 10;
            int textY = monster.getY() + 20;

            // 텍스트 높이 알아내기
            FontMetrics metrics = g.getFontMetrics(new Font("메이플스토리", Font.BOLD, 16));
            int textWidth = metrics.stringWidth(monster.getWord());
            int textHeight = metrics.getHeight();

            // 몬스터가 표시할 단어 출력
            g.setColor(Color.WHITE);
            g.fillRect(textX - 5, textY - textHeight, textWidth + 10, textHeight);

            g.setColor(Color.BLACK);
            g.setFont(new Font("메이플스토리", Font.BOLD, 16));
            g.drawString(monster.getWord(), textX, textY);
        }

        // 플레이어 체력 출력
        g.setColor(Color.BLACK);
        g.setFont(new Font("메이플스토리", Font.BOLD, 18));
        g.drawString("체력: " + playerHealth, 20, 30);
    }

    public GameStagePanel(MainFrame mainFrame, GamePanel gamePanel, int stage) {
        this.stage = stage;
        this.mainFrame = mainFrame;
        this.gamePanel = gamePanel;
        this.setLayout(null);

        mainFrame.labelPrint(this, 700, 650, 250, 50);

        // 플레이어 체력 설정
        UserCharacter character = mainFrame.getCurrentCharacter();
        if (character != null) {
            playerHealth = PLAYER_HEALTH_POWER + character.getLevel();
            initialPlayerHealth = playerHealth; // 시작 시 체력 저장
        }

        // 학번 이름 출력
        mainFrame.labelPrint(this, 40, 420, 200, 40);

        // 체력 레이블 초기화
        initializeLifeLabels();

        // GOAL 라벨 초기화
        goalLabel = new JLabel();
        goalLabel.setFont(new Font("메이플스토리", Font.BOLD, 18));
        goalLabel.setForeground(Color.BLUE); // 글씨 색상
        goalLabel.setBackground(Color.WHITE); // 배경 색상
        goalLabel.setOpaque(true);
        goalLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 중앙 정렬
        goalLabel.setBounds(400, 20, 120, 40); // 위치와 크기 설정
        updateGoalLabel(); // 초기 출력 텍스트 설정
        this.add(goalLabel);

        // 캐릭터 이미지 출력
        currentCharacterImage = "남자".equals(character.getGender()) ? MAN_CHARACTER_PATH : GIRL_CHARACTER_PATH;
        showCharacter(character.getGender());

        if (stage == 3) {
            startTimerThread();
        }

        new GameThread().start(); // 게임 스레드 시작

        // 퇴장 버튼 추가
        addExitButton();
    }


    // 체력 레이블 초기화
    private void initializeLifeLabels() {
        for (JLabel label : lifeLabels) {
            this.remove(label); // 기존 레이블 제거
        }
        lifeLabels.clear();

        int startX = 20; // 체력 아이콘 시작 위치 X
        int startY = 45; // 체력 아이콘 위치 Y
        int spacing = 30; // 체력 아이콘 간격

        // 현재 남아있는 체력만큼 이미지 레이블 생성
        for (int i = 0; i < playerHealth; i++) {
            JLabel lifeLabel = new JLabel();
            lifeLabel.setBounds(startX + (i * spacing), startY, 20, 20); // X, Y, width, height
            lifeLabels.add(lifeLabel);
            // 체력 이미지
            String LIFE_IMAGE_PATH = "src/images/Life.png";
            mainFrame.labelImageResize(this, lifeLabel, LIFE_IMAGE_PATH);
        }
        this.revalidate();
        this.repaint();
    }

    // 체력 감소 시 레이블 갱신
    private void updateLifeLabels() {
        SwingUtilities.invokeLater(() -> {
            initializeLifeLabels();
        });
    }

    // Back 버튼 생성 및 기능 추가
    private void addExitButton() {
        JButton exitButton = new JButton();
        exitButton.setBounds(800, 20, 150, 40); // 버튼 위치와 크기 설정
        exitButton.setFocusPainted(false);

        exitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "게임을 종료하고 로비로 돌아갑니다. 진행 중인 게임은 저장되지 않습니다.",
                    "게임 종료", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                // 스레드 종료 플래그 설정
                backToLobby = true;
                stopAllThreads();

                // 로비 화면으로 전환
                mainFrame.changePanel("game.panel.GameLobbyPanel");
            }
        });

        String EXIT_BUTTON_PATH = "src/images/exitButton.png";
        String EXIT_BUTTON_ON_PATH = "src/images/exitButtonOn.png";
        String EXIT_BUTTON_CLICK_PATH = "src/images/exitButtonClicked.png";
        mainFrame.buttonImageResize(this, exitButton, EXIT_BUTTON_PATH, EXIT_BUTTON_ON_PATH, EXIT_BUTTON_CLICK_PATH);
    }

    // 모든 스레드 종료 메서드
    private void stopAllThreads() {
        // 타이머 스레드 종료
        if (timerThread != null && timerThread.isRunning()) {
            timerThread.stopTimer();
        }
    }



    // 타이머 스레드 시작 메서드
    private void startTimerThread() {
        if (timerThread != null && timerThread.isRunning()) {
            timerThread.stopTimer(); // 기존 스레드 종료
        }

        timerThread = new GameTimerThread(gamePanel.getGameTimerPanel());
        timerThread.start();
    }


    // 캐릭터 이미지를 출력하는 함수
    private void showCharacter(String gender) {
        characterLabel = new JLabel();
        characterLabel.setBounds(PLAYER_X, PLAYER_Y, 80, 90);

        // 현재 로그인된 유저 정보 가져오기
        UserCharacter character = mainFrame.getCurrentCharacter();

        if ("남자".equals(gender)) {
            if (character != null && "조창연".equals(character.getNickname())) {
                // 닉네임이 "조창연"인 경우 특별 이미지 사용
                mainFrame.labelImageResize(this, characterLabel, "src/images/조창연.gif");
            } else {
                // 일반 남자 캐릭터 이미지 사용
                mainFrame.labelImageResize(this, characterLabel, MAN_CHARACTER_PATH);
            }
        } else {
            // 여자 캐릭터 이미지 사용
            mainFrame.labelImageResize(this, characterLabel, GIRL_CHARACTER_PATH);
        }
    }

    // 캐릭터 충돌 출력 함수 (변경된 로직 추가)
    private void changeCharacterImageTemporarily(String newImagePath) {
        SwingUtilities.invokeLater(() -> {
            UserCharacter character = mainFrame.getCurrentCharacter();
            if (playerHealth > 0) {
                if ("남자".equals(character.getGender()) && "조창연".equals(character.getNickname())) {
                    mainFrame.labelImageResize(this, characterLabel, "src/images/조창연Oops.gif");
                } else {
                    mainFrame.labelImageResize(this, characterLabel, newImagePath);
                }
                this.repaint();

                // 일정 시간 후 원래 이미지로 복구
                Timer timer = new Timer(2000, e -> {
                    if ("남자".equals(character.getGender()) && "조창연".equals(character.getNickname())) {
                        mainFrame.labelImageResize(this, characterLabel, "src/images/조창연.gif");
                    } else {
                        mainFrame.labelImageResize(this, characterLabel, currentCharacterImage);
                    }
                    this.repaint();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
    }

    // 캐릭터 공격 모션 함수
    public void changeCharacterAttack(String gender) {
        SwingUtilities.invokeLater(() -> { // 스레드 안정성 확보를 위해 invokeLater로 작성
            UserCharacter character = mainFrame.getCurrentCharacter();
            if ("남자".equals(gender)) {
                if (character != null && "조창연".equals(character.getNickname())) {
                    mainFrame.labelImageResize(this, characterLabel, "src/images/조창연Attack.gif");
                } else {
                    mainFrame.labelImageResize(this, characterLabel, MAN_ATTACK_PATH);
                }
            } else {
                mainFrame.labelImageResize(this, characterLabel, GIRL_ATTACK_PATH);
            }
            this.repaint();

            // 2초 후 원래 이미지로 복구
            Timer timer = new Timer(2000, e -> {
                if ("남자".equals(gender)) {
                    if (character != null && "조창연".equals(character.getNickname())) {
                        mainFrame.labelImageResize(this, characterLabel, "src/images/조창연.gif");
                    } else {
                        mainFrame.labelImageResize(this, characterLabel, MAN_CHARACTER_PATH);
                    }
                } else {
                    mainFrame.labelImageResize(this, characterLabel, GIRL_CHARACTER_PATH);
                }
                this.repaint();
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    // 몬스터 추가 함수
    private void addMonster() {
        try {
            String WORDS_FILE_PATH = "src/monster/words.txt";
            List<String> words = Files.readAllLines(Paths.get(WORDS_FILE_PATH));

            String word = words.get(random.nextInt(words.size()));

            int x = mainFrame.getWidth(); // 오른쪽 가장자리에서 생성
            int y = random.nextInt(Math.max(this.getHeight() - 40, 1));
            y = Math.max(y, 10);

            // 스테이지에 따른 랜덤 이미지 선택 (1/2 확률)
            String[] stageImages = MONSTER_IMAGE_PATHS[Math.min(stage - 1, MONSTER_IMAGE_PATHS.length - 1)];
            String selectedImagePath = stageImages[random.nextInt(stageImages.length)];

            // 몬스터 추가
            Monster monster = new Monster(x, y, word, new ImageIcon(selectedImagePath).getImage());
            monsters.add(monster);

        } catch (IOException e) {
            System.err.println("단어 파일 읽기 실패: " + e.getMessage());
        }
    }

    // 몬스터 이동
    private void moveMonsters(int stage) {
        UserCharacter character = mainFrame.getCurrentCharacter();
        boolean slowMonster = character != null && character.isSlowMonster(); // slowMonster 여부 확인

        // 몬스터 객체 이동
        for (Monster monster : monsters) {
            if (monster.getX() > PLAYER_X + 50) {
                if (slowMonster) {
                    // slow아이템이 활성화 된 경우
                    monster.setX(monster.getX() - (8 * stage - 5));
                } else {
                    // slow아이템이 활성화 되지 않은 경우
                    monster.setX(monster.getX() - 8 * stage);
                }
            }

            if (monster.getY() != PLAYER_Y) {
                monster.setY(monster.getY() + (monster.getY() > PLAYER_Y ? -1 : 1));
            }

            // 목표 지점 도달 시 처리
            if (monster.getX() <= PLAYER_X + 50) {
                monsters.remove(monster); // 몬스터 제거
                playerHealth--; // 체력 감소
                updateLifeLabels();

                // 캐릭터 이미지 변경 (충돌 시각화)
                String MAN_OOPS_PATH = "src/images/manOops.gif";
                String GIRL_OOPS_PATH = "src/images/girlOops.gif";
                String oopsImage = currentCharacterImage.equals(MAN_CHARACTER_PATH) ? MAN_OOPS_PATH : GIRL_OOPS_PATH;
                changeCharacterImageTemporarily(oopsImage);
            }
        }
    }

    // 몬스터를 지우는 함수
    public boolean removeMonster(String word) {
        for (Monster monster : monsters) {
            if (monster.getWord().equals(word)) {
                monsters.remove(monster);
                monsterCount += 1;
                System.out.println("현재 처치한 몬스터: " + monsterCount);

                updateGoalLabel(); // GOAL 라벨 업데이트
                checkCount(); // 몬스터 처치한 마릿수 확인

                this.repaint();
                return true;
            }
        }
        return false;
    }


    // 패널 상단 목표 최신화
    private void updateGoalLabel() {
        goalLabel.setText("GOAL: " + monsterCount + " / " + (GOAL_MONSTER_COUNT * stage));
    }


    // 현재 체력이 최대인지 확인
    public boolean isPlayerHealthMax() {
        return playerHealth >= initialPlayerHealth;
    }

    // 체력 증가 메서드
    public void increasePlayerHealth() {
        if (playerHealth < initialPlayerHealth) {
            playerHealth++; // 체력 1 증가
            updateLifeLabels(); // 체력 레이블 갱신
        }
    }

    // heal.gif 이미지 표시 로직
    public void showHealEffect() {
        JLabel healLabel = new JLabel(new ImageIcon("src/images/heal.gif"));
        healLabel.setBounds(PLAYER_X, PLAYER_Y - 50, 180, 180); // 캐릭터 위치 위쪽에 표시

        this.add(healLabel);
        this.revalidate();
        this.repaint();

        // 2초 후에 healLabel 제거
        Timer timer = new Timer(2000, e -> {
            this.remove(healLabel);
            this.revalidate();
            this.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    // monsterCount를 검사하여 목표치에 도달하면 스레드를 종료하고 돈 저장
    public void checkCount() {
        if (monsterCount >= GOAL_MONSTER_COUNT * stage) {
            // 현재 유저 정보 가져오기
            UserCharacter character = mainFrame.getCurrentCharacter();
            if (character == null) {
                System.err.println("유저 정보가 없습니다.");
                return;
            }

            // 기존 clearStage와 현재 stage를 비교
            int currentClearStage = character.getClearStage();
            int updatedClearStage = Math.max(currentClearStage, stage); // 더 큰 값 선택
            int saveMoney = 20 * monsterCount; // 몬스터 처치 보상

            // 유저 정보 저장
            saveUser(saveMoney, updatedClearStage);
        }
    }

    // 게임 종료 함수
    public void endGame(String endType, boolean levelUpCheck) {
        // 현재 실행 중인 GameThread 종료
        if (Thread.currentThread() instanceof GameThread gameThread) {
            gameThread.interrupt(); // 스레드 종료
        }

        // 타이머 스레드 종료
        if (timerThread != null) {
            timerThread.stopTimer();
        }

        SwingUtilities.invokeLater(() -> {
            if ("count".equals(endType)) {
                if (levelUpCheck) {JOptionPane.showMessageDialog(this, "축하합니다! 몬스터를 모두 처치하고 레벨도 1올랐습니다!", "게임 완료", JOptionPane.INFORMATION_MESSAGE); }
                else JOptionPane.showMessageDialog(this, "축하합니다! 몬스터를 모두 처치했습니다.", "게임 완료", JOptionPane.INFORMATION_MESSAGE);

            } else if ("death".equals(endType)) {
                JOptionPane.showMessageDialog(this, "게임 오버! 체력이 모두 소진되었습니다.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            } else if ("exit".equals(endType)) {
                JOptionPane.showMessageDialog(this, "게임에서 나갑니다.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }

            // 게임 로비로 전환
            mainFrame.changePanel("game.panel.GameLobbyPanel");
        });
    }


    // 스테이지 클리어시 기록 저장 및 레벨 업 확인
    public void saveUser(int saveMoney, int clearStage) {
        System.out.println("저장기능 활성화");
        UserCharacter character = mainFrame.getCurrentCharacter();
        String userId = mainFrame.getUser().getId(); // User 객체의 id 가져오기

        if (character != null && userId != null) {
            // UserCharacter 업데이트
            character.setMoney(character.getMoney() + saveMoney);
            character.setClearStage(clearStage);

            // 레벨 업 조건 확인: 남은 체력이 초기 체력과 동일한 경우
            if (playerHealth == initialPlayerHealth) {
                int newLevel = character.getLevel() + 1;
                character.setLevel(newLevel); // 레벨 +1
                System.out.println("레벨 업! 새로운 레벨: " + newLevel);
                levelUpCheck = true;
            }

            // 3스테이지 클리어 시 기록 저장
            if (stage == 3) {
                long elapsedMilliseconds = 0;
                String formattedTime = "";
                long totalSeconds = 0;

                // 타이머 종료 시간 계산
                if (timerThread != null) {
                    Instant startTime = timerThread.getStartTime();
                    Instant endTime = timerThread.getEndTime();
                    elapsedMilliseconds = Duration.between(startTime, endTime).toMillis();

                    // 포맷팅된 시간 (분, 초)
                    long minutes = elapsedMilliseconds / (1000 * 60);
                    long seconds = (elapsedMilliseconds / 1000) % 60;
                    formattedTime = minutes + "분 " + seconds + "초 ";

                    // 총 초 계산
                    totalSeconds = elapsedMilliseconds / 1000;
                }

                // 현재 날짜 가져오기
                LocalDate currentDate = LocalDate.now();
                String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

                // 3stageRank.txt 파일 저장
                Path rankFilePath = Paths.get("src/rank/3stageRank.txt");
                try {
                    Files.createDirectories(rankFilePath.getParent());

                    try (BufferedWriter writer = Files.newBufferedWriter(rankFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                        writer.write(character.getNickname() + "," + formattedTime + "," + totalSeconds + "," + formattedDate);
                        writer.newLine();
                    }
                    System.out.println("랭킹 저장 완료: " + rankFilePath);
                } catch (IOException e) {
                    System.err.println("랭킹 저장 실패: " + e.getMessage());
                }
            }

            // 유저 정보 저장
            Path userInfoPath = Paths.get("src/user/userInfo", userId + ".txt");
            try (BufferedWriter writer = Files.newBufferedWriter(userInfoPath)) {
                writer.write(character.getNickname() + "," +
                        character.getGender() + "," +
                        character.getLevel() + "," +
                        character.getMoney() + "," +
                        character.getClearStage() + "," +
                        character.getMoreHP() + "," +
                        character.isSlowMonster());
                System.out.println("유저 정보 저장 완료: " + userInfoPath);
            } catch (IOException e) {
                System.err.println("유저 정보 저장 실패: " + e.getMessage());
            }
        } else {
            System.err.println("유저 정보나 ID가 없습니다.");
        }
    }

    // 게임을 관리하는 스레드
    class GameThread extends Thread {
        @Override
        public void run() {
            try {
                if (stage == 3) {
                    startTimerThread(); // 타이머 스레드 시작
                }

                while (!Thread.currentThread().isInterrupted() && playerHealth > 0 && !backToLobby) { // 스레드 종료 조건 확인
                    addMonster(); // 몬스터 추가

                    for (int i = 0; i < 30; i++) { // 3초 간격으로 몬스터 호출되도록 설정
                        if (backToLobby) {
                            return; // 뒤로가기 버튼을 누를 경우 스레드 종료
                        }

                        moveMonsters(stage);
                        repaint();


                        if (monsterCount >= GOAL_MONSTER_COUNT * stage) { // 몬스터 목표치를 전부 잡았을 경우
                            endGame("count", levelUpCheck);
                            return;
                        }


                        Thread.sleep(100); // 0.1초 대기
                    }
                }

                if (!backToLobby) { // 뒤로가기 상태가 아닌 경우에만 실행
                    endGame("death", levelUpCheck); // 체력 소진 시 종료
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // 타이머 스레드 종료
                if (timerThread != null) {
                    timerThread.stopTimer();
                }
            }
        }
    }

    // 타이머 스레드
    class GameTimerThread extends Thread {
        private final GameTimerPanel timerPanel;
        private Instant startTime;
        private Instant endTime; // 종료 시간 추가
        private volatile boolean running = true; // 종료 플래그

        public GameTimerThread(GameTimerPanel timerPanel) {
            if (timerPanel == null) {
                throw new IllegalArgumentException("GameTimerPanel cannot be null");
            }
            this.timerPanel = timerPanel;
        }

        @Override
        public void run() {
            startTime = Instant.now(); // 시작 시간 측정
            try {
                while (running) { // 실행중엔 시간 측정
                    Duration elapsedTime = Duration.between(startTime, Instant.now());
                    String formattedTime = TimerFunction.durationToStringFormat(elapsedTime);

                    // 시간 레이블 업데이트
                    SwingUtilities.invokeLater(() -> timerPanel.updateTimeLabel(formattedTime));

                    Thread.sleep(30); // 50ms마다 업데이트
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                endTime = Instant.now(); // 종료 시간 설정
            }
        }

        public void stopTimer() {
            running = false; // 종료 플래그 설정
        }

        public boolean isRunning() {
            return running; // 현재 실행 상태 반환
        }

        public Instant getStartTime() {
            return startTime; // 시작 시간 반환
        }

        public Instant getEndTime() {
            return endTime != null ? endTime : Instant.now(); // 종료 시간 반환
        }
    }
}








