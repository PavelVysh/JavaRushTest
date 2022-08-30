package com.game.service;

import com.game.entity.Player;

import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class PlayerService {

    private int lastCount;

    private final PlayerRepository playerRepository;
    @PersistenceContext
    private EntityManager entityManager;


    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Query makeAQuery (String name,
                             String title, Integer minLevel,Integer maxLevel,
                             Integer pageNumber, Integer pageSize) {

        Query query = entityManager.createQuery( "Select p From Player p WHERE p.name LIKE :name " +
                                                        "AND p.title LIKE :title " +
                                                        "AND p.level BETWEEN :minLevel AND :maxLevel");

        query.setParameter("name","%"+name+"%");
        query.setParameter("title", "%"+title+"%");
        query.setParameter("minLevel",minLevel);
        query.setParameter("maxLevel",maxLevel);

        return query;
    }



    public List<Player> getPlayersList(String name,
                                       String title, Integer minLevel,Integer maxLevel,
                                        Integer pageNumber, Integer pageSize) {

        Query query = makeAQuery(name,title,minLevel,maxLevel,pageNumber,pageSize);

        Query queryForSize = makeAQuery(name,title,minLevel,maxLevel,pageNumber,pageSize);

        lastCount = queryForSize.getResultList().size();

        return query.setFirstResult(pageSize*(pageNumber)).setMaxResults(pageSize).getResultList();
    }

    public Integer getPlayersCount() {

        return lastCount;
    }

}
