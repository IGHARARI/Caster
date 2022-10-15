package sts.caster.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import sts.caster.cards.CasterCard;
import sts.caster.core.MagicElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BattleHelper {
    public static ArrayList<AbstractMonster> getCurrentBattleMonstersSortedOnX(boolean aliveOnly) {
        ArrayList<AbstractMonster> orderedMonsters = new ArrayList<AbstractMonster>();
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (aliveOnly && m.isDeadOrEscaped()) continue;
                orderedMonsters.add(m);
            }
        }
        //order monsters on the X axis, Y axis pretty much only matters for enemies on the same vertical line.
        orderedMonsters.sort((m1, m2) -> Math.round((m1.drawX - m2.drawX) * 1000 + (m1.drawY - m2.drawY)));
        return orderedMonsters;
    }


    public static ArrayList<AbstractMonster> getCurrentBattleMonstersSortedDistanceTo(AbstractCreature measureFrom, boolean aliveOnly) {
        ArrayList<AbstractMonster> orderedMonsters = new ArrayList<AbstractMonster>();
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (aliveOnly && m.isDeadOrEscaped()) continue;
                orderedMonsters.add(m);
            }
        }
        orderedMonsters.sort((m1, m2) -> (int) Math.round(
                Math.hypot(Math.abs(m1.drawX - measureFrom.drawX), Math.abs(m1.drawY - measureFrom.drawY)) -
                        Math.hypot(Math.abs(m2.drawX - measureFrom.drawX), Math.abs(m2.drawY - measureFrom.drawY))
        ));
        return orderedMonsters;
    }

    public static int[] createSpellDamageMatrix(int baseDamage, MagicElement elem, boolean isPureDamage) {
        final int[] damages = new int[AbstractDungeon.getMonsters().monsters.size()];
        for (int i = 0; i < damages.length; ++i) {
            final DamageInfo info = new DamageInfo(AbstractDungeon.player, baseDamage);
            if (!isPureDamage) {
                CasterCard.customApplyEnemyPowersToSpellDamage(info, elem, AbstractDungeon.getMonsters().monsters.get(i));
            }
            damages[i] = info.output;
        }
        return damages;
    }

    /**
     * Combat Utils
     */
    // Fetches all alive monsters.
    public static ArrayList<AbstractMonster> getAliveMonsters() {
        return AbstractDungeon.getMonsters().monsters.stream().filter(m -> !m.isDeadOrEscaped()).collect(Collectors.toCollection(ArrayList::new));
    }

    // Fetches a random monster who is alive.
    public static AbstractMonster getRandomAliveMonster(MonsterGroup group, Predicate<AbstractMonster> isCandidate, Random rng) {
        return getRandomMonster(group, m -> (!m.halfDead && !m.isDying && !m.isEscaping && isCandidate.test(m)), rng);
    }

    // Fetches a random monster (No Predicate)
    public static AbstractMonster getRandomAliveMonster(MonsterGroup group, Random rng) {
        return getRandomMonster(group, m -> (!m.halfDead && !m.isDying && !m.isEscaping), rng);
    }

    // Fetches a random monster.
    public static AbstractMonster getRandomMonster(MonsterGroup group, Predicate<AbstractMonster> isCandidate, Random rng) {
        List<AbstractMonster> candidates = group.monsters.stream().filter(isCandidate).collect(Collectors.toList());
        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(rng.random(0, candidates.size() - 1));
    }

    public static int getCardEffectiveCost(AbstractCard card) {
        int cost = card.costForTurn;
        if (card.cost == -1) cost = card.energyOnUse;
        if (card.freeToPlayOnce || card.isInAutoplay) cost = 0;
        return cost;
    }

    /**
     * Intent Utilities
     */
    // Searches for Debuff Intents.
    public static boolean isDebuffIntent(Intent intent) {
        return
                intent == Intent.STRONG_DEBUFF ||
                        intent == Intent.ATTACK_DEBUFF ||
                        intent == Intent.DEBUFF ||
                        intent == Intent.DEFEND_DEBUFF;
    }

    // Searches for Attack Intents.
    public static boolean isAttackIntent(Intent intent) {
        return
                intent == Intent.ATTACK ||
                        intent == Intent.ATTACK_BUFF ||
                        intent == Intent.ATTACK_DEBUFF ||
                        intent == Intent.ATTACK_DEFEND;
    }

    public static boolean isBlockIntent(Intent intent) {
        return
                intent == Intent.DEFEND ||
                        intent == Intent.DEFEND_BUFF ||
                        intent == Intent.DEFEND_DEBUFF ||
                        intent == Intent.ATTACK_DEFEND;
    }

    // Searches for Buff Intents.
    public static boolean isBuffIntent(Intent intent) {
        return
                intent == Intent.BUFF ||
                        intent == Intent.ATTACK_BUFF ||
                        intent == Intent.DEFEND_BUFF;
    }

}
