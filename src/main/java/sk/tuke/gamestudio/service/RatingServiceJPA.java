package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;



@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        entityManager.createNamedQuery("Rating.deleteRating").setParameter("player", rating.getPlayer())
                .setParameter("game", rating.getGame()).executeUpdate();
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) {
        Double d = (Double) entityManager.createNamedQuery ( "Rating.getAverageRating" )
                .setParameter ( "game", game ).getSingleResult ();
        if(d != null) return (int) Math.round (d);
        /*List<Rating> list = entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getResultList();
        int rowCount = 0;
        int allRatings = 0;
        for (Rating rating : list) {
            allRatings = allRatings + rating.getRating();
            rowCount++;
        }
        if (rowCount != 0)
            return allRatings / rowCount;*/
        return 0;
    }

    @Override
    public int getRating(String game, String name) {
        return ((Rating) entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game).setParameter("player", name).getSingleResult()).getRating();
//        List<Rating> rates = entityManager.createNamedQuery("Rating.getRating")
//                .setParameter("game", game).setParameter("player", name).getResultList();
//
//        System.out.println(rates);
//        if (rates.size() == 1) {
//            return 447;
//        } else {
////            System.out.println("inside foreach");
//            Rating ratingToReturn = rates.get(0);
//            for (Rating rate :
//                    rates) {
////                rate.getRatedon()
//                if (ratingToReturn.getRatedon().before(rate.getRatedon())) {
//                    ratingToReturn = rate;
//                }
//
//            }
//            return ratingToReturn.getRating();
//        }


    }
}
