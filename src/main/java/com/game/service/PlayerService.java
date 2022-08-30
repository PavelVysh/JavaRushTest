package com.game.service;

import com.game.entity.Player;

import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private int lastCount;

    private final PlayerRepository playerRepository;
    @PersistenceContext
    private EntityManager entityManager;


    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }



    public List<Player> getPlayersList(String name,
                                       String title, Integer minLevel,Integer maxLevel) {

        Query query = entityManager.createQuery("Select p From Player p WHERE p.name LIKE :name " +
                                                        "AND p.title LIKE :title AND p.level BETWEEN :minLevel " +
                                 "AND :maxLevel");

            query.setParameter("name","%"+name+"%");
            query.setParameter("title", "%"+title+"%");
            query.setParameter("minLevel",minLevel);
            query.setParameter("maxLevel",maxLevel);

            List<Player> players= query.getResultList();

            lastCount = players.size();

        return players;
    }

    public Integer getPlayersCount() {
        return lastCount;
    }

}
