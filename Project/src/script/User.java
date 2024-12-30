package script;

// 유저의 정보 저장
public class User {
    private final String id;
    private final String paseword;


    public User(String id, String paseword) {
        this.id = id;
        this.paseword = paseword;

    }

    public String getId() {
        return id;
    }

    public String getPaseword() {
        return paseword;
    }

    // "id,password" 형식의 문자열에서 User 객체를 생성
    public static User fromString(String userString) {
        String[] parts = userString.split(",");
        return new User(parts[0].trim(), parts[1].trim());
    }

    // User 객체를 "id,password,level,money" 형식의 문자열로 변환
    @Override
    public String toString() {
        return id + "," + paseword;
    }
}
