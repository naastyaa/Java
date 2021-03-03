package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingServiceJDBC implements RatingService {

    private static final String URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";


    private static final String INSERT_RATING =
            "INSERT INTO \"Rating\" (game, player, rating, ratedon) VALUES (?, ?, ?, ?)";

    private static final String SELECT_RATING =
            "SELECT game, player, rating, ratedon FROM \"Rating\" WHERE game = ? ORDER BY rating DESC LIMIT 50;";

    private static final String SELECT_RATING1 =
            "SELECT game, player, rating, ratedon FROM \"Rating\" WHERE game = ? AND player = ? ORDER BY rating DESC LIMIT 50;";

    private static final String GAME_NAME = "pexeso";


    public static void main ( String[] args ) throws Exception {
        BufferedReader br = new BufferedReader ( new InputStreamReader ( System.in ) );
//		RatingService ratingService = new RatingServiceJDBC();
//
////		Vypis players' ratings
//		for (Rating rating : getPlayersRatings(Game.GAME_NAME)) {
//			System.out.println(rating.getPlayer() + " " + rating.getRating());
//		}

        while (true) {
            System.out.println ( "name: " );
            String name = br.readLine ();
            System.out.println ( "rate: " );
            int ocenka = Integer.parseInt ( br.readLine () );
            Rating rating = new Rating ( GAME_NAME , name , ocenka , new java.util.Date () );
            RatingService ratingService = new RatingServiceJDBC ();
            ratingService.setRating ( rating );
//		System.out.println(ratingService.getRating(Game.GAME_NAME, "jaro"));
        }

    }

    //SELECT_RATING =
//			"SELECT game, player, rating, ratedon FROM rating WHERE game = ? ORDER BY rating DESC LIMIT 50;"
    public static List < Rating > getPlayersRatings ( String game ) throws RatingException {
        List < Rating > rates = new ArrayList <> ();
        try (Connection connection = DriverManager.getConnection ( URL , USER , PASSWORD )) {
            try (PreparedStatement ps = connection.prepareStatement ( SELECT_RATING )) {
                ps.setString ( 1 , game );
                try (ResultSet rs = ps.executeQuery ()) {
                    while (rs.next ()) {
                        Rating rating = new Rating (
                                rs.getString ( 1 ) ,
                                rs.getString ( 2 ) ,
                                rs.getInt ( 3 ) ,
                                rs.getTimestamp ( 4 )
                        );
                        rates.add ( rating );
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException ( "Error loading rating" , e );
        }
        return rates;
    }

    @Override
    public void setRating ( Rating rating ) throws RatingException {
        try (Connection connection = DriverManager.getConnection ( URL , USER , PASSWORD )) {
            try (PreparedStatement ps = connection.prepareStatement ( INSERT_RATING )) {
                ps.setString ( 1 , rating.getGame () );
                ps.setString ( 2 , rating.getPlayer () );
                ps.setInt ( 3 , rating.getRating () );
                ps.setDate ( 4 , new Date ( rating.getRatedon ().getTime () ) );

                ps.executeUpdate ();
            }
        } catch (SQLException e) {
            throw new RatingException ( "Error saving rating" , e );
        }
    }

    @Override
    public int getAverageRating ( String game ) throws RatingException {
        int rowsCount = 0;
        int sumRate = 0;
        try (Connection connection = DriverManager.getConnection ( URL , USER , PASSWORD )) {
            try (PreparedStatement ps = connection.prepareStatement ( SELECT_RATING )) {
                ps.setString ( 1 , game );
                try (ResultSet rs = ps.executeQuery ()) {

                    while (rs.next ()) {
                        Rating rating = new Rating (
                                rs.getString ( 1 ) ,
                                rs.getString ( 2 ) ,
                                rs.getInt ( 3 ) ,
                                rs.getTimestamp ( 4 )
                        );
                        rowsCount++;
                        sumRate += rating.getRating ();


                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException ( "Error loading rating" , e );
        }
        return (rowsCount > 0) ? (sumRate / rowsCount) : 0;

    }

    @Override
    public int getRating ( String game , String name ) throws RatingException {
        try (Connection connection = DriverManager.getConnection ( URL , USER , PASSWORD )) {
            try (PreparedStatement ps = connection.prepareStatement ( SELECT_RATING1 )) {
                ps.setString ( 2 , name );
                ps.setString ( 1 , game );
                try (ResultSet rs = ps.executeQuery ()) {
                    if (rs.next ()) {
                        Rating rating = new Rating (
                                rs.getString ( 1 ) ,
                                rs.getString ( 2 ) ,
                                rs.getInt ( 3 ) ,
                                rs.getTimestamp ( 4 )
                        );
                        return rating.getRating ();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException ( "Error loading rating" , e );
        }
        return 0;
    }

}