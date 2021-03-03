package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CommentServiceJDBC implements CommentService {

    private static final String URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private static final String INSERT_COMMENT =
            "INSERT INTO \"Comment\" (game, player, comment, commentedon) VALUES (?, ?, ?, ?)";

    private static final String SELECT_COMMENT =
            "SELECT game, player, comment, commentedon FROM \"Comment\" WHERE game = ? ORDER BY comment DESC LIMIT 100;";

    public static void main ( String[] args ) throws Exception {
//		Comment comment = new Comment(Game.GAME_NAME, "jaro", "nice", new java.util.Date());
        CommentService commentService = new CommentServiceJDBC ();
//		commentService.addComment(comment);
        int i = 0;
        for (Comment comment :
                commentService.getComments ( "pexeso" )) {
            System.out.println ( ++i + ". " + comment.getPlayer () + ": " + comment.getComment () );
        }
    }

    @Override
    public void addComment ( Comment comment ) throws CommentException {
        try (Connection connection = DriverManager.getConnection ( URL , USER , PASSWORD )) {
            try (PreparedStatement ps = connection.prepareStatement ( INSERT_COMMENT )) {
                ps.setString ( 1 , comment.getGame () );
                ps.setString ( 2 , comment.getPlayer () );
                ps.setString ( 3 , comment.getComment () );
                ps.setDate ( 4 , new Date ( comment.getCommentedOn ().getTime () ) );

                ps.executeUpdate ();
            }
        } catch (SQLException e) {
            throw new CommentException ( "Error saving comment" , e );
        }
    }

    @Override
    public List < Comment > getComments ( String game ) throws CommentException {
        List < Comment > commentsList = new ArrayList <> ();
        try (Connection connection = DriverManager.getConnection ( URL , USER , PASSWORD )) {
            try (PreparedStatement ps = connection.prepareStatement ( SELECT_COMMENT )) {
                ps.setString ( 1 , game );
                try (ResultSet rs = ps.executeQuery ()) {
                    while (rs.next ()) {
                        Comment comment = new Comment (
                                rs.getString ( 1 ) ,
                                rs.getString ( 2 ) ,
                                rs.getString ( 3 ) ,
                                rs.getTimestamp ( 4 )
                        );
                        commentsList.add ( comment );
                    }
                }
            }
        } catch (SQLException e) {
            throw new CommentException ( "Error loading comment" , e );
        }
        return commentsList;
    }
}