package voting;

import Util.MensaBotUtil;

import java.util.HashMap;
import java.util.Map;

public class Voting {

    private Map<Long, Vote> votes = new HashMap<>();
    private Map<String, Integer> voteCounter = new HashMap<>();


    public void init(){
        voteCounter.put("Mensa", 0);
        voteCounter.put("Bistro", 0);
        voteCounter.put("Extern", 0);
        voteCounter.put("Selber", 0);
    }

    public String voteHandler(long userId, String call_data){
        MensaBotUtil util = new MensaBotUtil();
        String answer = "";
        Vote current_vote = new Vote(userId, call_data);
        this.init();

        if (!(votes.containsKey(userId))) {
            if (call_data.equals("Mensa")) {
                voteCounter.put("Mensa", voteCounter.get("Mensa") + 1);
            } else if (call_data.equals("Bistro")) {
                voteCounter.put("Bistro", voteCounter.get("Bistro") + 1);
            } else if (call_data.equals("Extern")) {
                voteCounter.put("Extern", voteCounter.get("Extern") + 1);
            } else if (call_data.equals("Selber")) {
                voteCounter.put("Selber", voteCounter.get("Selber") + 1);
            }
            votes.put(userId, current_vote);
            votes.get(userId).setVoted(true);
        } else if (!(votes.get(userId).getLocation().equals(call_data))) {
            if (voteCounter.get(votes.get(userId).getLocation()) > 0) {
                voteCounter.put(votes.get(userId).getLocation(), voteCounter.get(votes.get(userId).getLocation()) - 1);
            }
            votes.put(userId, current_vote);
            voteCounter.put(votes.get(userId).getLocation(), voteCounter.get(votes.get(userId).getLocation()) + 1);
            votes.get(userId).setVoted(true);
        } else if (votes.get(userId).getLocation().equals(call_data)) {
            if (votes.get(userId).isVoted()) {
                voteCounter.put(votes.get(userId).getLocation(), voteCounter.get(votes.get(userId).getLocation()) - 1);
                votes.get(userId).setVoted(false);
            } else {
                voteCounter.put(votes.get(userId).getLocation(), voteCounter.get(votes.get(userId).getLocation()) + 1);
                votes.get(userId).setVoted(true);
            }
        }
        System.out.println(voteCounter);

        answer += "<b>------Abstimmung------</b>";
        answer += "\nMensa: " + "\t" + util.generateEmojiString(voteCounter.get("Mensa")) +
                "\nBistro: " + util.generateEmojiString(voteCounter.get("Bistro")) +
                "\nExtern: " + util.generateEmojiString(voteCounter.get("Extern")) +
                "\nSelber: " + util.generateEmojiString(voteCounter.get("Selber"));
        return answer;
    }
}
