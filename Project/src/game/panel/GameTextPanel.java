package game.panel;
import game.MainFrame;
import script.UserCharacter;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// 텍스트를 입력하는 패널
public class GameTextPanel extends JPanel {
    private final JTextField textField;
    private int startMoney; // 돈 저장을 위한 변수

    public GameTextPanel(MainFrame mainFrame, GamePanel gamePanel,int stage) {
        UserCharacter character = mainFrame.getCurrentCharacter();
        this.startMoney = character.getMoney(); // 초기값 설정

        // 텍스트 입력받기
        textField = new JTextField(30);
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 입력한 문자열을 받아와서 저장
                String inputText = textField.getText();
                // 입력한 문자열 지우기
                textField.setText("");
                // 입력한 값으로 탐색하여 일치하면 몬스터 제거
                if (gamePanel.getGameStagePanel().removeMonster(inputText)) {
                    startMoney += 10; // 돈 추가
                    gamePanel.getGameUserPanel().updateMoney(startMoney); // UI 업데이트
                }

                // 성별로 구분하여 공격 모션 전환
                gamePanel.getGameStagePanel().changeCharacterAttack(character.getGender());
            }
        });

        // F1 키 입력 감지 로직
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1 && stage >= 2 ) { // F1 키 감지, 스테이지2 이상부터 사용 가능
                    useMoreHPItem(mainFrame, gamePanel);
                }
            }
        });

        add(textField);
    }

    // F1 키를 눌렀을 때 moreHP 아이템 사용
    private void useMoreHPItem(MainFrame mainFrame, GamePanel gamePanel) {
        UserCharacter character = mainFrame.getCurrentCharacter();

        if (character.getMoreHP() <= 0) { // 아이템이 1개라도 있는지 먼저 확인
            JOptionPane.showMessageDialog(this, "사용할 수 있는 체력 아이템이 없습니다!", "아이템 부족", JOptionPane.ERROR_MESSAGE);
            return; // 아이템이 없으므로 종료
        }

        // GameStagePanel에서 현재 체력이 최대인지 확인
        if (gamePanel.getGameStagePanel().isPlayerHealthMax()) {
            JOptionPane.showMessageDialog(this, "이미 최대 체력입니다!", "아이템 사용 불가", JOptionPane.WARNING_MESSAGE);
            return; // 최대 체력이므로 더 이상 체력 추가 불가
        }

        // 체력 아이템 개수 차감
        character.setMoreHP(character.getMoreHP() - 1);

        // GameStagePanel의 체력 증가 및 힐 이펙트 표시
        gamePanel.getGameStagePanel().increasePlayerHealth();
        gamePanel.getGameStagePanel().showHealEffect(); // heal.gif 표시

        // 아이템 패널 갱신
        gamePanel.getGameItemPanel().updateItemPanel();
    }

    // 텍스트 필드에 포커스를 요청하는 메서드, 게임 패널이 출력되자마자 바로 입력되기 위함
    public void focusTextField() {
        textField.requestFocusInWindow();
    }
}
