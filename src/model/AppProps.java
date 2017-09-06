package model;

public class AppProps {
    public static final AppProps PROPS = new AppProps();

    private String DBUrl;
    private String userName;
    private String userPassword;
    private int fieldsCount;

    public String getDBUrl() {
        return DBUrl;
    }

    public void setDBUrl(String dataBaseUrl) {
        this.DBUrl = "jdbc:postgresql://"+dataBaseUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getFieldsCount() {
        return fieldsCount;
    }

    public void setFieldsCount(Integer fieldsCount) {
        this.fieldsCount = fieldsCount;
    }

    @Override
    public String toString() {
        return "AppProps{" +
                " DBUrl='" + DBUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", fieldsCount=" + fieldsCount +
                '}';
    }
}
