package nl.sogyo.ttt_app.domain;

import java.util.Set;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import nl.sogyo.ttt_app.api.IStorable;

@Entity(name = "player")
@Table(name = "players")
public class Player implements IStorable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_ID")
    private int player_ID;
    @Column(name = "player_rating")
    private int rating;
    @Column(name = "player_name")
    private String name;
    @Column(name = "player_adress")
    private String adress;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "player_tournament_join",
        joinColumns = { @JoinColumn(name = "player_ID")},
        inverseJoinColumns = { @JoinColumn(name = "tournament_ID")}
    )
    private Set<Tournament> tournaments = new HashSet<Tournament>();

    @ManyToMany(mappedBy = "players", fetch = FetchType.LAZY)
    private Set<Match> matches = new HashSet<Match>();

    public Player(){

    }

    public Player(String name){
        this.name = name;
    }

    public int getID(){
        return player_ID;
    }
    public void setID(int ID){
        this.player_ID = ID;
    }
    public int getRating(){
        return rating;
    }
    public void setRating(int rating){
       this.rating = rating;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getAdress(){
        return adress;
    }
    public void setAdress(String adress){
        this.adress = adress;
    }
    public Set<Tournament> getTournaments(){
        return tournaments;
    }
    public Set<Match> getMatches() {
        return matches;
    }
    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    public void signUpForTournament(Tournament tournament){
        this.tournaments.add(tournament);
        tournament.playerSignUp(this);
    }
}