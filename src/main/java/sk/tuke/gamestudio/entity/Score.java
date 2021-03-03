package sk.tuke.gamestudio.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery( name = "Score.getBestScores",
        query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points DESC")
public class Score implements Comparable < Score >, Serializable {

    @Id
    @GeneratedValue
    private Integer ident; //identifikator


    public Score() {
    }

    public Integer getIdent() {
        return ident;
    }


    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "Score{" +
                "ident=" + ident +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", points=" + points +
                ", playedon=" + playedOn +
                '}';
    }

    private String game;

    private String player;

    private int points;

    private Date playedOn;

    public Score ( String game , String player , int points , Date playedOn ) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
    }

    public String getGame () {
        return game;
    }

    public void setGame ( String game ) {
        this.game = game;
    }

    public String getPlayer () {
        return player;
    }

    public void setPlayer ( String player ) {
        this.player = player;
    }

    public int getPoints () {
        return points;
    }

    public void setPoints ( int points ) {
        this.points = points;
    }

    public Date getPlayedOn () {
        return playedOn;
    }

    public void setPlayedOn ( Date playedOn ) {
        this.playedOn = playedOn;
    }

    @Override
    public int compareTo ( Score o ) {
        if (o == null) return -1;
        return this.getPoints () - o.getPoints ();
    }
}