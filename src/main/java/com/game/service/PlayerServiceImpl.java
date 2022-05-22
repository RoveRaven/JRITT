package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Date;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository repository;

    @Autowired
    public void createRepository(PlayerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Player createPlayer(Player player) {
        if (!isValidPlayer(player))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Some fields are not filled  or filled  incorrectly");

        player.setBanned(player.getBanned() != null && player.getBanned());
        player.setLevel(updateLevel(player.getExperience()));
        player.setUntilNextLevel(updateUntilLevelExperience(player.getExperience(), player.getLevel()));
        return repository.save(player);
    }

    @Override
    public Page<Player> findAllPlayers (Specification < Player > specification, Pageable pageable){
        return repository.findAll(specification, pageable);
    }

    @Override
    public List<Player> findAllPlayers (Specification < Player > specification) {
        return repository.findAll(specification);
    }

    @Override
    public Player updatePlayer (Long id, Player player){
        Player updatedPlayer = findPlayerByID(id);

        if (player.getName() != null) updatedPlayer.setName(player.getName());
        if (player.getTitle() != null) updatedPlayer.setTitle(player.getTitle());
        if (player.getRace() != null) updatedPlayer.setRace(player.getRace());
        if (player.getProfession() != null) updatedPlayer.setProfession(player.getProfession());
        if (player.getBanned() != null) updatedPlayer.setBanned(player.getBanned());
        if (player.getBirthday() != null) {
            if (!isValidDate(player.getBirthday()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong birthdate");
            updatedPlayer.setBirthday(player.getBirthday());
        }

        if (player.getExperience() != null) {
            if (!isValidExperience(player.getExperience()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Experience exceeds acceptable values");
            updatedPlayer.setExperience(player.getExperience());
            updatedPlayer.setLevel(updateLevel(updatedPlayer.getExperience()));
            updatedPlayer.setUntilNextLevel(updateUntilLevelExperience(updatedPlayer.getExperience(), updatedPlayer.getLevel()));
        }
        return repository.save(updatedPlayer);
    }

    @Override
    public void deletePlayer (Long id){
        findPlayerByID(id);
        repository.deleteById(id);
    }

    @Override
    public Player findPlayerByID (Long id){
        if (id <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong id");
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
    }

    @Override
    public int updateLevel ( int experience){
        return (int) ((Math.sqrt(2500 + 200 * experience) - 50) / 100);
    }

    @Override
    public int updateUntilLevelExperience(int experience, int level){
        return 50 * (level + 1) * (level + 2) - experience;
    }

    @Override
    public boolean isValidPlayer (Player player){
        return player != null && player.getRace() != null && player.getProfession() != null &&
                isValidName(player.getName()) && isValidTitle(player.getTitle()) &&
                isValidExperience(player.getExperience()) && isValidDate(player.getBirthday());
    }

    @Override
    public boolean isValidName (String name){
        return name != null && name.length() <= 12 && !name.isEmpty();
    }

    @Override
    public boolean isValidTitle (String title){
        return title != null && title.length() <= 30;
    }

    @Override
    public boolean isValidExperience (Integer experience){
        return experience != null && experience > 0 && experience <= 10_000_000;
    }

    @Override
    public boolean isValidDate (Date date){
        return date != null && date.getTime() >= 0 &&
                date.getTime() >= 946674000482L && date.getTime() <= 32535205199494L;
    }
}