public class Vote {
    private long chatId;
    private String location;
    private boolean set;

    public Vote(long chatId, String location){
        this.chatId = chatId;
        this.location = location;
    }

    public long getChatId() {
        return chatId;
    }

    public String getLocation() {
        return location;
    }

    public boolean isSet() {
        return set;
    }
}
