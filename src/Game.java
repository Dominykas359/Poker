public class Game {

    private boolean playing = false;
    private boolean blinds = false;
    private boolean flop = false;
    private boolean turn = false;
    private boolean river = false;


    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isFlop() {
        return flop;
    }

    public void setFlop(boolean flop) {
        this.flop = flop;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isRiver() {
        return river;
    }

    public void setRiver(boolean river) {
        this.river = river;
    }

    public boolean isBlinds() {
        return blinds;
    }

    public void setBlinds(boolean blinds) {
        this.blinds = blinds;
    }
}
