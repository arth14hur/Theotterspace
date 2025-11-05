package com.T_jav_502.Theotterspace.units;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.T_jav_502.Theotterspace.teams.Team.Species.OTTER;
import static org.junit.jupiter.api.Assertions.*;

class BlasterTest {
    /**
     * This class serves as unit tests for the entire aUnit class
     *
     *
     */
    private Blaster blaster;
    private Blaster blaster2;

    @BeforeEach
    void setUp() {
        blaster = new Blaster(OTTER);
    }

    @Test
    void getHp() {
        assertEquals(20, blaster.getHp());
    }

    @Test
    void getAttack() {
        assertEquals(10, blaster.getAttack());
    }

    @Test
    void getDefense() {
        assertEquals(4, blaster.getDefense());
    }

    @Test
    void getMovement() {
        assertEquals(5, blaster.getMovement());
    }

    @Test
    void getRange() {
        assertEquals(2, blaster.getRange());
    }

    @Test
    void isMoved() {
        assertFalse(blaster.isMoved());
    }

    @Test
    void attack() {
        blaster2 = new Blaster(OTTER);
        assertEquals(20, blaster2.getHp());
        assertEquals(20, blaster.getHp());
        blaster.attack(blaster2);
        assertEquals((20 + blaster.getDefense() - blaster.getAttack()), blaster2.getHp());
        assertEquals(20, blaster.getHp());
    }

    @Test
    void receiveDamage() {
        assertEquals(20, blaster.getHp());
        blaster.receiveDamage(7);
        assertEquals((20 + blaster.getDefense() - 7), blaster.getHp());

    }

    @Test
    void getTeam() {
        assertEquals(OTTER, blaster.getTeam());
    }

    @Test
    void isAtRange() {
        blaster2 = new Blaster(OTTER);
        assertTrue(blaster.isAtRange(blaster2));
    }
}
