package sts.caster.delayedCards;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RewindingCodexManager {
	public static AbstractCard lastSpellUsed;
	public static Map<UUID,ArrayList<AbstractCard>> spellsRecastedThisCombat = new HashMap<>();
	public static void resetOnStartOfCombat() {
		lastSpellUsed = null;
		spellsRecastedThisCombat = new HashMap<>();
	}
}
