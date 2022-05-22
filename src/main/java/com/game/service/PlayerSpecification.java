package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Objects;

@Component
public class PlayerSpecification {

    public Specification<Player> filterByName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(root.get("name"), "%" + name + "%");
    }

    public Specification<Player> filterByTitle(String title) {
        return (root, query, cb) -> title == null ? null : cb.like(root.get("title"), "%" + title + "%");
    }

    public Specification<Player> filterByRace(Race race) {
        return (root, query, cb) -> race == null ? null : cb.equal(root.get("race"), race);
    }

    public Specification<Player> filterByProfession(Profession profession) {
        return (root, query, cb) -> profession == null ? null : cb.equal(root.get("profession"), profession);
    }

    public Specification<Player> filterByDate(Long after, Long before) {
        return (root, query, cb) -> {
            if (after == null && before == null) return null;
            else if (after == null) return cb.lessThanOrEqualTo(root.get("birthday"), new Date(before));
            else if (before == null) return cb.greaterThanOrEqualTo(root.get("birthday"), new Date(after));
            else return cb.between(root.get("birthday"), new Date(after), new Date(before));
        };
    }

    public Specification<Player> filterByBanned(Boolean banned) {
        return (root, query, cb) -> banned == null ? null : banned ? cb.isTrue(root.get("banned")) : cb.isFalse(root.get("banned"));
    }

    public Specification<Player> filterByExperience(Integer minExperience, Integer maxExperience) {
        return (root, query, cb) -> {

            if (minExperience == null && maxExperience == null) return null;
            else if (minExperience == null) return cb.lessThanOrEqualTo(root.get("experience"), maxExperience);

            else if (maxExperience == null) return cb.greaterThanOrEqualTo(root.get("experience"), minExperience);
            else return cb.between(root.get("experience"), minExperience, maxExperience);
        };
    }

    public Specification<Player> filterByLevel(Integer minLevel, Integer maxLevel) {
        return (root, query, cb) -> {
            if (minLevel == null && maxLevel == null) return null;
            else if (minLevel == null) return cb.lessThanOrEqualTo(root.get("level"), maxLevel);
            else if (maxLevel == null) return cb.greaterThanOrEqualTo(root.get("level"), minLevel);
            else return cb.between(root.get("level"), minLevel, maxLevel);
        };
    }

    public Specification<Player> playersFilter(String name, String title, Race race, Profession profession
            , Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience
            , Integer minLevel, Integer maxLevel)
    {
        return Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Specification.where(filterByName(name))
                                                        .and(filterByTitle(title))
                                                        .and(filterByRace(race)))
                                                .and(filterByProfession(profession)))
                                        .and(filterByDate(after, before)))
                                .and(filterByBanned(banned)))
                        .and(filterByExperience(minExperience, maxExperience)))
                .and(filterByLevel(minLevel, maxLevel));
    }
}