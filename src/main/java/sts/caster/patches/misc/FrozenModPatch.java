package sts.caster.patches.misc;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import sts.caster.actions.ShowCardVeryBrieflyAction;
import sts.caster.cards.mods.FrozenCardMod;

import java.util.ArrayList;


public class FrozenModPatch {

	//	START OF TURN patch
	@SpirePatch(clz=GameActionManager.class, method="getNextAction")
	public static class onStartOfTurnTriggerPatch {
		
		@SpireInsertPatch(locator=Locator.class, localvars={})
		public static void Insert(GameActionManager __instance) {
			AbstractPlayer p = AbstractDungeon.player;
			for (final AbstractCard card : p.hand.group) {
				if (CardModifierManager.hasModifier(card, FrozenCardMod.ID)) {
					AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
						@Override
						public void update() {
							card.superFlash(Color.BLUE.cpy());
							isDone = true;
						}
					});
					AbstractDungeon.actionManager.addToTop(new GainBlockAction(p, FrozenCardMod.ON_DRAW_BLOCK_AMOUNT, true));
					AbstractDungeon.actionManager.addToTop(new ShowCardVeryBrieflyAction(card));
				}
			}
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.characters.AbstractPlayer", "applyStartOfTurnPostDrawPowers");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
}
