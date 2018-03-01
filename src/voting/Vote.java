package voting;

public class Vote {
    private long userId;
    private String location;
    private boolean voted;

    public Vote(long chatId, String location){
        this.userId = chatId;
        this.location = location;
        voted = false;
    }

    public long getUserId() {
        return userId;
    }

    public String getLocation() {
        return location;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted){
        this.voted = voted;
    }
}
