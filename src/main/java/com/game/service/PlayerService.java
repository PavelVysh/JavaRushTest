package com.game.service;

import com.game.entity.Player;

import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    public Query makeAQuery (String name,
                             String title, Integer minLevel,Integer maxLevel,
                             Integer pageNumber, Integer pageSize,Race race,
                             Integer minExperience, Integer maxExperience,Boolean banned) {

        StringBuilder sbUrl = new StringBuilder("Select p From Player p WHERE p.name LIKE :name " +
                "AND p.title LIKE :title " +
                "AND p.level BETWEEN :minLevel AND :maxLevel " +
                "AND p.experience BETWEEN :minExp AND :maxExp ");

        if(race != null)sbUrl.append("AND p.race = :race ");
        if(banned != null)sbUrl.append("AND p.banned = :banned ");

        Query query = entityManager.createQuery(sbUrl.toString());

        query.setParameter("name","%"+name+"%");
        query.setParameter("title", "%"+title+"%");
        query.setParameter("minLevel",minLevel);
        query.setParameter("maxLevel",maxLevel);
        query.setParameter("minExp", minExperience);
        query.setParameter("maxExp",maxExperience);

        if(race != null) query.setParameter("race", race);
        if(banned != null) query.setParameter("banned",banned);

        return query;
    }



    public List<Player> getPlayersList(String name,
                                       String title, Integer minLevel,Integer maxLevel,
                                        Integer pageNumber, Integer pageSize, Race race, Integer minExperience,
                                       Integer maxExperience, Boolean banned) {

        Query query = makeAQuery(name,title,minLevel,maxLevel,pageNumber,pageSize,race,minExperience,maxExperience,banned);

        return query.setFirstResult(pageSize*pageNumber).setMaxResults(pageSize).getResultList();
    }

    public Integer getPlayersCount(String name,
                                   String title, Integer minLevel,Integer maxLevel,
                                   Integer pageNumber, Integer pageSize, Race race, Integer minExperience,
                                   Integer maxExperience, Boolean banned) {

        Query query = makeAQuery(name,title,minLevel,maxLevel,pageNumber,pageSize,race,minExperience,maxExperience,banned);

        return query.getResultList().size();
    }

    public Player getPlayerById(Long id) {

        if(id <= 0 ) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid id: "+id);

        Optional<Player> player = playerRepository.findById(id);

        if (player.isPresent()) {
            return player.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No player with id: " + id);
        }
    }
}
