package utils;

import java.util.List;

import static model.Player.NO_VOTE;

public class VoteCounter {
    public int noVote = 0;
    public int spyVotes = 0;
    public int otherVotes = 0;

    public void addVote(InternalMessage message, String spyId) {
        if (message.getValue().equals(NO_VOTE)) {
            noVote++;
            System.out.println(noVote);
        } else if (message.getValue().equals(spyId)) {
            spyVotes++;
            System.out.println(spyVotes);
        } else {
            otherVotes++;
            System.out.println(otherVotes);
        }
    }

    public String countResult(List<String> ids) {
        int allVotes = noVote + spyVotes + otherVotes;
        if (allVotes == (ids.size()-1)) {
            if (noVote == ids.size() || otherVotes >= spyVotes) {
                return "Победил шпион";
            } else {
                return "Победили горожане";
            }
        } else {
            return "no";
        }
    }

}
