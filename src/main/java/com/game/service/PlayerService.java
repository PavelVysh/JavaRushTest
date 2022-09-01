package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;

import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.hibernate.Session;
import org.hibernate.cfg.QuerySecondPass;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    @PersistenceContext
    private EntityManager entityManager;


    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Query makeAQuery(String name,
                            String title, Integer minLevel, Integer maxLevel,
                            Integer pageNumber, Integer pageSize, Race race,
                            Integer minExperience, Integer maxExperience, Boolean banned,
                            Profession profession, Long after, Long before, PlayerOrder order) {

        StringBuilder sbUrl = new StringBuilder("Select p From Player p WHERE p.name LIKE :name " +
                "AND p.title LIKE :title " +
                "AND p.level BETWEEN :minLevel AND :maxLevel " +
                "AND p.experience BETWEEN :minExp AND :maxExp ");

        if (race != null) sbUrl.append("AND p.race = :race ");
        if (banned != null) sbUrl.append("AND p.banned = :banned ");
        if (profession != null) sbUrl.append("AND p.profession = :profession ");
        if (after != null) sbUrl.append("AND p.birthday > :after ");
        if (before != null) sbUrl.append("AND p.birthday < :before ");
        if (order != null) sbUrl.append("ORDER BY p.").append(order.getFieldName());

        Query query = entityManager.createQuery(sbUrl.toString());

        query.setParameter("name", "%" + name + "%");
        query.setParameter("title", "%" + title + "%");
        query.setParameter("minLevel", minLevel);
        query.setParameter("maxLevel", maxLevel);
        query.setParameter("minExp", minExperience);
        query.setParameter("maxExp", maxExperience);

        if (race != null) query.setParameter("race", race);
        if (banned != null) query.setParameter("banned", banned);
        if (profession != null) query.setParameter("profession", profession);
        if (after != null) query.setParameter("after", new Date(after));
        if (before != null) query.setParameter("before", new Date(before));

        return query;
    }


    public List<Player> getPlayersList(String name,
                                       String title, Integer minLevel, Integer maxLevel,
                                       Integer pageNumber, Integer pageSize, Race race, Integer minExperience,
                                       Integer maxExperience, Boolean banned, Profession profession,
                                       Long after, Long before, PlayerOrder order) {

        Query query = makeAQuery(name, title, minLevel, maxLevel,
                pageNumber, pageSize, race, minExperience, maxExperience, banned, profession, after, before, order);

        return query.setFirstResult(pageSize * pageNumber).setMaxResults(pageSize).getResultList();
    }

    public Integer getPlayersCount(String name,
                                   String title, Integer minLevel, Integer maxLevel,
                                   Integer pageNumber, Integer pageSize, Race race, Integer minExperience,
                                   Integer maxExperience, Boolean banned, Profession profession,
                                   Long after, Long before, PlayerOrder order) {

        Query query = makeAQuery(name, title, minLevel, maxLevel,
                pageNumber, pageSize, race, minExperience, maxExperience, banned, profession, after, before, order);

        return query.getResultList().size();
    }

    public Player getPlayerById(Long id) {

        if (id <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id: " + id);

        Optional<Player> player = playerRepository.findById(id);

        if (player.isPresent()) {
            return player.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No player with id: " + id);
        }

    }

    public Player createPlayer(Player player) {
        if (player.getName() == null ||
                player.getTitle() == null ||
                player.getRace() == null ||
                player.getProfession() == null ||
                player.getBirthday() == null ||
                player.getExperience() == null ||
                player.getName().length() > 12 ||
                player.getTitle().length() > 30 ||
                player.getName().equals("") ||
                player.getExperience() > 10000000 ||
                player.getExperience() < 0 ||
                player.getBirthday().before(new java.util.Date(946684800000L)) ||
                player.getBirthday().after(new java.util.Date(32503680000000L)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some of stuff is wrong");


        player.setLevel((int) ((Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100));
        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());


        playerRepository.save(player);

        return player;
    }

    public void deletePlayer(Long id) {

        if (id <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id: " + id);

        Optional<Player> player = playerRepository.findById(id);

        if (player.isPresent()) {
            playerRepository.delete(player.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No player with id: " + id);
        }
    }

/*
    public Player updatePlayer(Long id, Player player) {



        Query testQuert = entityManager.createQuery("update Player p set p.name = 'Demochange' where p.id = 42");
        testQuert.executeUpdate();


        System.out.println("Player received : " + player.getName());

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE Player p SET ");

        if (player.getName() != null) builder.append("p.name=:name, ");
        if (player.getTitle() != null) builder.append("p.title=:title, ");

        builder.setLength(builder.length() - 2);

        builder.append(" WHERE p.id=:id;");

        Query query = entityManager.createQuery(builder.toString());

        query.setParameter("name", player.getName());
        query.setParameter("title", player.getTitle());
        query.setParameter("id", id);


        query.executeUpdate();

        return getPlayerById(id);
    }
}
*/
    @Transactional
    public Player updatePlayer(Long id, Player player) {

        Session session = entityManager.unwrap(Session.class);
        Player playerFromDB = getPlayerById(id);


        if (player.getName() != null) playerFromDB.setName(player.getName());
        if (player.getTitle() != null) playerFromDB.setTitle(player.getTitle());
        if (player.getRace() != null) playerFromDB.setRace(player.getRace());
        if (player.getProfession() != null) playerFromDB.setProfession(player.getProfession());
        if (player.getBirthday() != null) {
            if (player.getBirthday().before(new Date(0L))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birthday time incorrect");
            } else {
                playerFromDB.setBirthday(player.getBirthday());
            }
        }
        if (player.getExperience() != null) {
            if (player.getExperience() < 0 || player.getExperience() > 1000000) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exp out of bounds" + player.getExperience());
                } else {
                    playerFromDB.setExperience(player.getExperience());
                    playerFromDB.setLevel((int) ((Math.sqrt(2500 + (200 * playerFromDB.getExperience())) - 50) / 100));
                    playerFromDB.setUntilNextLevel(50 * (playerFromDB.getLevel() + 1) * (playerFromDB.getLevel() + 2) - playerFromDB.getExperience());
                }
            }

            session.saveOrUpdate(playerFromDB);

            return playerFromDB;
        }
    }


