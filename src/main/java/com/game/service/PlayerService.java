package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public interface PlayerService {

    public Player createPlayer(Player player);

    public Page<Player> findAllPlayers(Specification<Player> specification, Pageable pageable);

    public List<Player> findAllPlayers(Specification<Player> specification);

    public Player updatePlayer(Long id, Player player);

    public void deletePlayer(Long id);

    public Player findPlayerByID(Long id);

    public int updateLevel(int experience);

    public int updateUntilLevelExperience(int experience, int level);

    public boolean isValidPlayer(Player player);

    public boolean isValidName(String name);

    public boolean isValidTitle(String title);

    public boolean isValidExperience(Integer experience);

    public boolean isValidDate(Date date);
}