package sts.caster.patches.relics;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import basemod.ReflectionHacks;
import sts.caster.relics.MagicBookRelic;
import sts.caster.ui.RememorizeCardOption;

@SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
public class MagicBookCampfireOptionPatch
{
    public static void Postfix(final Object meObj) {

		if (AbstractDungeon.player.hasRelic(MagicBookRelic.ID)) {
			final CampfireUI campfire = (CampfireUI)meObj;
			try {
				@SuppressWarnings("unchecked")
				final ArrayList<AbstractCampfireOption> campfireButtons = (ArrayList<AbstractCampfireOption>)ReflectionHacks.getPrivate((Object)campfire, CampfireUI.class, "buttons");
				final RememorizeCardOption button = new RememorizeCardOption((MagicBookRelic) AbstractDungeon.player.getRelic(MagicBookRelic.ID));	
				campfireButtons.add(button);
				float x = 950.f;
				float y = 990.0f - (270.0f * (float)((campfireButtons.size() + 1) / 2));
				if (campfireButtons.size() % 2 == 0) {
					x = 1110.0f;
					campfireButtons.get(campfireButtons.size() - 2).setPosition(800.0f * Settings.scale, y * Settings.scale);
				}
				campfireButtons.get(campfireButtons.size() - 1).setPosition(x * Settings.scale, y * Settings.scale);
			}
			catch (SecurityException | IllegalArgumentException ex2) {}
		}
    }
}