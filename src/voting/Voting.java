package voting;

import Util.MensaBotUtil;

import java.util.HashMap;
import java.util.Map;

//by pbolliger
public class Voting {

    private Map<Long, String> votes = new HashMap<>();

    public String voteHandler(long userId, String call_data){
        MensaBotUtil util = new MensaBotUtil();
        String answer = "";

        if(votes.containsKey(userId) && votes.get(userId).equals(call_data)){
            votes.remove(userId);
        }else{
            votes.put(userId, call_data);
        }

        answer += "<b>------Abstimmung------</b>";
        answer += "\nMensa: " + "\t" + util.generateEmojiString(votes.values().stream().filter(vote -> vote.equals("Mensa")).count()) +
                "\nBistro: " + util.generateEmojiString(votes.values().stream().filter(vote -> vote.equals("Bistro")).count()) +
                "\nExtern: " + util.generateEmojiString(votes.values().stream().filter(vote -> vote.equals("Extern")).count()) +
                "\nSelber: " + util.generateEmojiString(votes.values().stream().filter(vote -> vote.equals("Selber")).count());
        return answer;
    }
}
