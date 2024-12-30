package script;

public class UserCharacter {
    private String nickname;
    private String gender;
    private int level;
    private int money;
    private int clearStage;

    private int moreHP;            // 추가 체력
    private boolean slowMonster;   // 몬스터 느리게 하기

    public UserCharacter(String nickname, String gender, int level, int money, int clearStage, int moreHP, boolean slowMonster) {
        this.nickname = nickname;
        this.gender = gender;
        this.level = level;
        this.money = money;
        this.clearStage = clearStage;
        this.moreHP = moreHP;
        this.slowMonster = slowMonster;
    }

    // Getter and Setter methods
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }

    public int getClearStage() { return clearStage; }
    public void setClearStage(int clearStage) { this.clearStage = clearStage; }

    public int getMoreHP() { return moreHP; }
    public void setMoreHP(int moreHP) { this.moreHP = moreHP; }

    public boolean isSlowMonster() { return slowMonster; }
    public void setSlowMonster(boolean slowMonster) { this.slowMonster = slowMonster; }

    // 객체를 문자열로 변환
    @Override
    public String toString() {
        return nickname + "," + gender + "," + level + "," + money + "," + clearStage + "," + moreHP + "," + slowMonster;
    }

}
